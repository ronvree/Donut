package donut.tests;

import donut.BottomUpCFGBuilder;
import donut.graphassets.Graph;
import donut.graphassets.Node;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class CFGBuilderTest {

    @Test
    public void test() {
        Graph g = run(new File("src/donut/sample/testfiles/testcfg/CFGTest1.donut"));
        Assert.assertTrue(checkGraphG(g));

//        Graph h = run(new File("src/donut/sample/testfiles/testcfg/CFGTest2.donut"));
//        Assert.assertTrue(checkGraphH(h));
    }

    public Graph run(File file) {
        BottomUpCFGBuilder builder = new BottomUpCFGBuilder();
        builder.build(file);
        builder.printGraph(file.getName());
        return builder.getGraph();
    }

    public boolean checkGraphG(Graph g) {
        Set<Node> nodes = g.getNodes();
        boolean correct = true;
        for (Node n : nodes) {
            if (correct) {
                switch (n.getNr()) {
                    case 0:
                        correct = n.getEdges().size() == 1;
                        break;
                    case 1:
                        correct = n.getEdges().size() == 1;
                        break;
                    case 2:
                        correct = n.getEdges().size() == 1;
                        break;
                    case 3:
                        correct = n.getEdges().size() == 1;
                        break;
                    case 4:
                        correct = n.getEdges().size() == 1;
                        break;
                    case 5:
                        correct = n.getEdges().size() == 2;
                        break;
                    case 6:
                        correct = n.getEdges().size() == 1;
                        break;
                    case 7:
                        correct = n.getEdges().size() == 1;
                        break;
                    case 8:
                        correct = n.getEdges().size() == 1;
                        break;
                    case 9:
                        correct = n.getEdges().size() == 2;
                        break;
                    case 10:
                        correct = n.getEdges().size() == 2;
                        break;
                    case 11:
                        correct = n.getEdges().size() == 1;
                        break;
                    case 12:
                        correct = n.getEdges().size() == 0;
                        break;
                }
            } else {
                break;
            }
        }
        return correct;
    }



    public boolean checkGraphH(Graph h) {
        // TODO make another test.



        return false;
    }

}
