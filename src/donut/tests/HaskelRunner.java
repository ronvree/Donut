package donut.tests;

import java.io.*;

/**
 * Created by Gijs on 22-Jun-16.
 */

public class HaskelRunner {

    public HaskelRunner() {
        // Empty by design.
    }

    public int runHaskell(String fileName) {
        createExecutable(fileName);
        String data = runExecutable();
        String tmp = data.split("localMem")[1].split(",")[1].split(",")[0].trim();
        return Integer.parseInt(tmp);
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

    private void createExecutable(String fileName) {
        String command = "ghc -o result " + fileName;
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

