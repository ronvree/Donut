package donut.tests;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gijs on 22-Jun-16.
 */

public class HaskelRunner {

    public static final String FILE_DIR = "C:\\Users\\Gijs\\IdeaProjects\\CompilerConstruction\\Donut";
    public static final int NR_OF_SPROCKELLS = 4;
    public static final int NR_OF_LINES = 5 + NR_OF_SPROCKELLS;
    public static final int SHARED_MEM_SIZE = 16;

    private ArrayList<ArrayList> localMem;
    private ArrayList<Integer> sharedMem;

    public HaskelRunner() {
        // Empty by design.
    }

    /**
     * Create and runs specified file and returns value from given localMemory address of the Sprockell.
     * @param fileName Haskell file to run.
     */

    public void runHaskell(String fileName) {
        if (createExecutable(fileName)) {
            String data = runExecutable();
            localMem = getLocalMemory(data);
            sharedMem = getSharedMemory(data);
        }
    }


    private String runExecutable() {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("result.exe", null, new File(FILE_DIR));

            Collector errorCollector = new
                    Collector(proc.getErrorStream(), "ERROR");

            Collector outputCollector = new
                    Collector(proc.getInputStream(), "OUTPUT");

            errorCollector.start();
            outputCollector.start();

            int exitVal = proc.waitFor();
//            System.out.println("Process exitValue: " + exitVal);
            return outputCollector.getResult();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    private boolean createExecutable(String fileName) {

        // Because we create a .exe file, this will not run on operating systems other than Windows;
        String os = System.getProperty("os.name");
        if (!os.toLowerCase().contains("windows")) {
            System.err.println("HaskellRunner not available on your OS. \nSorry...");
            return false;
        }

        String command = "ghc -o result " + fileName;       // Create executable.
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command, null, new File(FILE_DIR));

            Collector errorCollector = new
                    Collector(proc.getErrorStream(), "ERROR");

            Collector outputCollector = new
                    Collector(proc.getInputStream(), "OUTPUT");

            errorCollector.start();
            outputCollector.start();

            int exitVal = proc.waitFor();
//            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        return true;
    }


    private ArrayList<ArrayList> getLocalMemory(String data) {
        ArrayList<ArrayList> result = new ArrayList<>();
//        System.out.println(data);
        String[] mem = data.split("localMem");
//        System.out.println("mem.length = " + mem.length);
        for (int i = 1; i < mem.length; i++) {
//            System.out.println(i + " " + mem[i]);

            String[] m = mem[1].split(",");

            ArrayList<Integer> localMem = new ArrayList<>();

            // First slot in local memory is always zero.
            localMem.add(0);
            // skip first local memory slot, this cannot be changed and will always be zero.
            for (int j = 1; j < m.length - 1; j++) {
                try {
                    localMem.add(Integer.parseInt(m[j].trim()));
                } catch (NumberFormatException e) {
                    localMem.add(Integer.parseInt(m[j].split("\\]")[0]));
                    break;
                }
            }
            result.add(localMem);
        }
//        System.out.println("Result " + result);
        return result;
    }

    private ArrayList<Integer> getSharedMemory(String data) {
        ArrayList<Integer> sharedMem = new ArrayList<>();
        String mem = data.substring(data.length() - (SHARED_MEM_SIZE*2+2)).split("\\]")[0];
//        System.out.println("mem: " + mem);
        String[] res = mem.split(",");
        for(int i = 0; i < res.length; i ++) {
            sharedMem.add(Integer.parseInt(res[i]));
        }
        return sharedMem;
    }


    public ArrayList<ArrayList> getLocalMem() {
        return localMem;
    }

    public ArrayList<Integer> getSharedMem() {
        return sharedMem;
    }
}

class Collector extends Thread {
    private InputStream is;
    private String type;
    private String result;

    Collector(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        StringBuffer buffer = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
//                System.out.println(type + "> " + line);
                buffer.append(line + "\n");
                if (i > HaskelRunner.NR_OF_LINES) {
                    buffer = new StringBuffer();
                    i = 0;
                }
                i++;
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
//        System.out.println("buffer: " + buffer.toString());
        result = buffer.toString();
    }


    public String getResult() {
        return result;
    }
}

