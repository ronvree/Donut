package donut.generators;

import donut.DonutParser;
import donut.checkers.CheckerResult;
import donut.spril.Program;
import donut.spril.instructions.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for generating the main program
 * Supports threads
 *
 */
public class MainGenerator extends CodeGenerator {

    /** List of all programs (main and threads */
    private List<Program> programs;

    /**
     * Generate a list of programs. The main program is stored at index 0. The rest of the programs store threads
     * @param tree -- The parsed Donut code that will be converted to Spril instructions
     * @param result -- Result from the checker phase
     */
    public List<Program> generate(ParseTree tree, CheckerResult result)   {
        this.setProgram(new Program());
        this.programs = new ArrayList<>();
        this.programs.add(this.getProgram());                       // Add main
        for (int i = 0; i < THREADS; i++)  {
            programs.add(new Program());                            // Add potential threads
        }
        this.setResult(result);
        this.setLineCount(0);

        tree.accept(this);                                          // Visit the tree

        this.getProgram().add(new Read(ZEROREG));                   // Read/Receive makes sure the write buffer is empty at the end of main
        this.getProgram().add(new Receive(ZEROREG));

        for (Program p : programs)  {                               // Append EndProg to all programs
            p.add(new EndProg());
        }

        return programs;
    }

    /*
        Visitor methods
     */

    /** Visit concurrent block - Only the main program can visit concurrent blocks
     *  Divides the statements in the block into partitions that are distributed over the threads.
     *  Generate threads to execute the statements
     *  Add instructions to the main program that will start all threads at the start of the block
     *  and will wait at the end of the block until all threads are finished */
    @Override
    public Integer visitConcurrentBlock(DonutParser.ConcurrentBlockContext ctx) {
        int begin = this.getLineCount();
        // Divide statements between threads
        List<ParseTree> stats = ctx.children.subList(1, ctx.getChildCount() - 1);                           // Obtain statements in block (1st and last children are braces)
        int partitionSize = (int) Math.ceil((double) stats.size()/(double) THREADS);                        // Determine partition size. Round up to ensure the amount of partitions will always equal the amount of threads
        List<List<ParseTree>> partitions = new ArrayList<>();
        int partitionStart = 0;
        for (int threadID = 0; threadID < THREADS && threadID < stats.size(); threadID++)  {                // Take a sublist of the statements
            if (stats.size() - partitionStart >= partitionSize)   {
                partitions.add(stats.subList(partitionStart, partitionStart + partitionSize));
                partitionStart += partitionSize;
            } else {
                partitions.add(stats.subList(partitionStart, stats.size()));
            }
        }

        // Make threads
        for (int threadID = 0; threadID < THREADS && threadID < stats.size(); threadID++)  {
            List<ParseTree> partition = partitions.get(threadID);                                           // Obtain the statements to be executed by this thread
            ThreadGenerator generator = new ThreadGenerator(threadID);                                      // Create a generator that will make the thread

            Program thread = generator.generate(partition, this.getResult(), programs.get(threadID + 1));   // Generate the thread
            programs.set(threadID + 1, thread);                                                             // Set the updated program (at index threadID + 1 because the 1st program is the main program)

            emit(new TestAndSetAI(threadID));                                                               // Start thread by setting the reserved space in memory to 1
            emit(new Receive(reg(ctx)));                                                                    // Make sure the thread is started before continuing
            emit(new BranchI(reg(ctx), 2, false));                                                          // Thread was started     -> continue by branching over the jump instruction
            emit(new JumpI(-3, false));                                                                     // Thread was not started -> Try again
        }

        // Join all threads
        for (int threadID = 0; threadID < THREADS && threadID < stats.size(); threadID++)  {                // For all threads:
            emit(new ReadAI(threadID));                                                                     // Read in shared memory if the thread has indicated that it is finished
            emit(new Receive(reg(ctx)));                                                                    // Receive reply (0 means thread is finished)
            emit(new BranchI(reg(ctx), -2, false));                                                         // If not -> read again
        }

        return begin;
    }

    /** Visit lock block - The main program is not concurrent and thus no locking takes place
     *  The statements in the block will however still be executed */
    @Override
    public Integer visitLockBlock(DonutParser.LockBlockContext ctx) {
        int begin = this.getLineCount();
        super.visitLockBlock(ctx);
        return begin;
    }

    /** Visit lock statement - The main program will ignore the lock and execute the statements */
    @Override
    public Integer visitLockStat(DonutParser.LockStatContext ctx) {
        return visitLockBlock(ctx.lockBlock());
    }


}
