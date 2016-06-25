package donut;

import donut.spril.Instruction;
import donut.spril.Operator;
import donut.spril.Program;
import donut.spril.Reg;
import donut.spril.instructions.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.List;

/**
 * Created by Ron on 23-6-2016.
 *
 * Responsible for creating a single thread that executes the given statements
 */
public class ThreadGenerator extends GeneratorIII {

    /** Distance in shared memory between a variable and its lock */
    private static final int LOCKDISTANCE = (SHAREDMEMSIZE - THREADS)/2;
    /** Reference to ID register */
    public static final Reg SPRID = new Reg("(regSprID)");

    /** Result from the checker phase */
    private CheckerResultII result;
    /** Used for making fresh registers */
    private int regCount;
    /** Count nr of lines in the program (used for jumps) */
    private int lineCount;
    /** Map values to registers */
    private ParseTreeProperty<Reg> registers;
    /** Thread program that will be written to */
    private Program program;
    /** Thread ID - Index in shared memory where main can communicate with the thread */
    private final int id;

    /** Create a new ThreadGenerator with thread ID */
    public ThreadGenerator(int id)    {
        this.id = id;
    }

    /**
     * Generate a new thread in the given program
     * @param statements -- Statements to be executed by the thread
     * @param result     -- Checker result
     * @param thread     -- Program that will contain the thread
     * @return           -- The program appended with a newly generated thread executing the giving statements
     */
    public Program generate(List<ParseTree> statements, CheckerResultII result, Program thread)   {
        this.program = thread;
        this.result = result;
        this.registers = new ParseTreeProperty<>();
        this.regCount = 1;                       // Register 0 cannot be used
        this.lineCount = thread.size();          // Continue writing at the end of the program

        Reg reg = new Reg("reg1");               // Temporary use of register 1 to indicate when the thread can start
        emit(new LoadI(id, SPRID));
        emit(new Read(SPRID));                   // Read from reserved location in shared memory
        emit(new Receive(reg));                  // Receive value from memory
        emit(new BranchI(reg, 2, false));        // If it is 1 -> thread can start
        emit(new JumpI(-3, false));

        for (ParseTree tree : statements)  {     // Generate code to execute all statements
            tree.accept(this);
        }

        emit(new Write(ZEROREG, SPRID));         // Write 0 to reserved location in shared memory to indicate that the thread is done
        emit(new Read(SPRID));
        emit(new Receive(reg));
        emit(new BranchI(reg, -2, false));

        return program;
    }

    /*
        Block
     */

    /** Visit block - Return the first line number */
    @Override
    public Integer visitBlock(DonutParser.BlockContext ctx) {
        int begin = lineCount;
        this.visitChildren(ctx);
        return begin;
    }

    /** Visit lock block - Return the first line number */
    @Override
    public Integer visitLockBlock(DonutParser.LockBlockContext ctx) {
        int begin = lineCount;
        this.visitChildren(ctx);
        return begin;
    }

    /*
        Stat
     */

    /** Visit lock statement - Before visiting the children, generates code that checks if the variable can be locked.
     *  Afterwards generates code that unlocks the variable */
    @Override
    public Integer visitLockStat(DonutParser.LockStatContext ctx) {
        int begin = lineCount;
        int offset = offset(ctx.ID());
        int lockOffset = offset - LOCKDISTANCE;     // Determine the offset of the lock (distance between lock and variable in memory is constant)

        emit(new TestAndSetAI(lockOffset));         // Test if the lock can be obtained. If not, keep trying
        emit(new Receive(reg(ctx)));
        emit(new BranchI(reg(ctx), 2, false));      // Lock obtained -> continue by branching over the jump
        emit(new JumpI(-3, false));                 // Lock denied   -> Try again

        visitChildren(ctx);

        emit(new WriteAI(ZEROREG, lockOffset));     // Write 0 to lock to indicate the lock has been removed
        emit(new ReadAI(lockOffset));               // Ensure the lock has been removed before continuing
        emit(new Receive(reg(ctx)));
        emit(new BranchI(reg(ctx), -2, false));     // Lock still active -> read again

        return begin;
    }

    /*
        Help functions
     */

    private Instruction emit(Instruction instr) {
        program.add(instr);
        lineCount++;
        return instr;
    }

    private int offset(ParseTree node)  {
        return this.result.getOffset(node);
    }

    private boolean shared(ParseTree node) { return this.result.isShared(node); }

    /** Returns a register for a given parse tree node,
     * creating a fresh register if there is none for that node. */
    private Reg reg(ParseTree node) {
        Reg result = this.registers.get(node);
        if (result == null) {
            result = new Reg("(reg" + this.regCount + ")");
            this.registers.put(node, result);
            this.regCount++;
        }
        return result;
    }

    private void setReg(ParseTree node, Reg reg) {
        this.registers.put(node, reg);
    }

}
