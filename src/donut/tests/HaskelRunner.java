package donut.tests;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Gijs on 22-Jun-16.
 */

public class HaskelRunner {

    public HaskelRunner() {
        // Empty by design.
    }

    /**
     * Create and runs specified file and returns value from given localMemory address of the Sprockell.
     * @param fileName Haskell file to run.
     * @param address localMemory address to read, starting at zero.
     * @return
     */

    public int runHaskell(String fileName, int address) {
        if (createExecutable(fileName)) {
            String data = runExecutable();
            ArrayList<Integer> localMemory = getLocalMemory(data);
            return localMemory.get(address);
        }
        return -1;
    }


    private String runExecutable() {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("result.exe", null, new File("C:\\Users\\Gijs\\IdeaProjects\\CompilerConstruction\\Donut"));

            Collector errorCollector = new
                    Collector(proc.getErrorStream(), "ERROR");

            Collector outputCollector = new
                    Collector(proc.getInputStream(), "OUTPUT");

            errorCollector.start();
            outputCollector.start();

            int exitVal = proc.waitFor();
//            System.out.println("Process exitValue: " + exitVal);
            String res = outputCollector.getResult();
            return res;
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
            Process proc = rt.exec(command, null, new File("C:\\Users\\Gijs\\IdeaProjects\\CompilerConstruction\\Donut"));

//            Collector errorCollector = new
//                    Collector(proc.getErrorStream(), "ERROR");
//
//            Collector outputCollector = new
//                    Collector(proc.getInputStream(), "OUTPUT");
//
//            errorCollector.start();
//            outputCollector.start();

            int exitVal = proc.waitFor();
//            System.out.println("Process exitValue: " + exitVal);
//                String res = outputCollector.result;
//                filterData(res);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return true;
    }


    public ArrayList<Integer> getLocalMemory(String data) {
        ArrayList<Integer> localMem = new ArrayList<>();
//        System.out.println(data);
        String mem = data.split("localMem")[1];
        String[] m = mem.split(",");

        // First slot in local memory is always zero.
        localMem.add(0);
        // skip first local memory slot, this cannot be changed and will always be zero.
        for (int i = 1; i < m.length - 1; i++) {
//            System.out.println(m[i]);
            localMem.add(Integer.parseInt(m[i]));
        }
        String last = m[m.length - 1].split("\\]")[0];
        localMem.add(Integer.parseInt(last));
        return localMem;
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
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(type + ">" + line);
                if (line.contains("localMem")) {
                    result = line;
                }
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


    public String getResult() {
        return result;
    }
}

