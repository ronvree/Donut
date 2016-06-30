package donut;

import donut.spril.Program;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Gijs on 24-Jun-16.
 */
public class HaskellWriter {

    private String INIT_FILE =
            "module Main where\n" +
                    "\n" +
                    "import BasicFunctions\n" +
                    "import HardwareTypes\n" +
                    "import Sprockell\n" +
                    "import System\n" +
                    "import Simulation\n" +
                    "\n"
            ;

    public HaskellWriter() {
        // Empty by design.
    }

    public void writeFile(List<Program> programs) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(INIT_FILE);
        buffer.append(programs.get(0).writeProgram("program"));
        for (int i = 1; i < programs.size(); i++) {
            buffer.append("\n");
            buffer.append(programs.get(i).writeProgram("thread" + i));
        }

        buffer.append("\n\n")
                .append("main = sysTest [program,");
        for (int i = 1; i < programs.size(); i++) {
            buffer.append(programs.get(i).getProgramName() + ",");
        }
        buffer.deleteCharAt(buffer.toString().length()-1).append("]");
        try {
            Files.write(Paths.get("threadResult.hs"), buffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
