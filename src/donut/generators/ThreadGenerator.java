package donut.generators;

import donut.checkers.CheckerResultII;
import donut.DonutParser;
import donut.spril.Program;
import donut.spril.Reg;
import donut.spril.instructions.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.List;

/**
 * Responsible for generating threads
 */
public class ThreadGenerator extends CodeGenerator {

    /** Distance in shared memory between a variable and its lock */
    private static final int LOCKDISTANCE = (SHAREDMEMSIZE - THREADS)/2;
    /** Reference to ID register */
    public static final Reg SPRID = new Reg("(regSprID)");

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
        this.setProgram(thread);
        this.setResult(result);
        this.setRegisters(new ParseTreeProperty<>());
        this.setRegCount(1);                    // Register 0 cannot be used
        this.setLineCount(thread.size());       // Continue writing at the end of the program

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

        return this.getProgram();
    }


    /*
        Visitor methods
     */

    /** Visit lock block - Return the first line number */
    @Override
    public Integer visitLockBlock(DonutParser.LockBlockContext ctx) {
        int begin = this.getLineCount();
        this.visitChildren(ctx);
        return begin;
    }

    /** Visit concurrent block - Threads do not support nested concurrency and should thus do nothing (instead of visiting children) */
    @Override
    public Integer visitConcurrentBlock(DonutParser.ConcurrentBlockContext ctx) {
        // Nothing
        return this.getLineCount();
    }

    /** Visit lock statement - Before visiting the children, generates code that checks if the variable can be locked.
     *  Afterwards generates code that unlocks the variable */
    @Override
    public Integer visitLockStat(DonutParser.LockStatContext ctx) {
        int begin = this.getLineCount();
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

    /** Visit thread statement - Threads do not support nested concurrency and should thus do nothing (instead of visiting children)*/
    @Override
    public Integer visitThreadStat(DonutParser.ThreadStatContext ctx) {
        // Nothing
        return this.getLineCount();
    }


}
