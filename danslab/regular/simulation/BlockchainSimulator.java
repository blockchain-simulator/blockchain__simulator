/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class BlockchainSimulator {

    public static void main(String args[]) throws InterruptedException, IOException {

        iterateNodesandBlocksize();
    }

    public static void dontIterate() {
        int Cm = 11; //11 for correctness

        System.out.println("Avg Bandwidth in Mbps:" + (Sharables.MaxBandwidth + Sharables.MinBandWidth) / (2 * 1024 * 1024));
        System.out.println("Avg Latency :" + (Sharables.MaxLatencyDelay + Sharables.MinLatencyDelay) / 2);
        System.out.println("Max connections:" + Cm);
        System.out.print("Blocksize(KB)\t");
        System.out.print("Avg PropTime(ms)\t\t");
        System.out.print("%Forks\n");
        System.out.println("--------------------------------------------------------------------------");

        long Blocksize = 4 * 8 * 1024; //16 KB

        //initialize the 
        int NoOfNodes = Sharables.NoOfNodes;
        Sharables.NodesBandwidth = new int[NoOfNodes];
        Sharables.NodesLatency = new int[NoOfNodes];

        for (int i = 0; i < NoOfNodes; i++) {
            Sharables.NodesBandwidth[i] = Functions.getRandomNumberInRange(Sharables.MinBandWidth, Sharables.MaxBandwidth);
            Sharables.NodesLatency[i] = Functions.getRandomNumberInRange(Sharables.MinLatencyDelay, Sharables.MaxLatencyDelay);
        }

        //System.out.println("intitalizing the topology");
        initRandomGraphTopology(Cm);

        for (int l = 0; l < 1; l++) { //iterate blocksize

            Blocksize = 64 * 1024 * 8 * 1024;;
            Sharables.MaxDelay = new int[Sharables.ChainLength];

            //initialize the values
            Sharables.BlockSize = Blocksize;
            //Sharables.Tdelay = (int) (((double) (Blocksize * 1000) / Sharables.Bandwidth)); //in millis
            //System.out.println(Sharables.Tdelay);
            ///
            Block Genesis = new Block(-1, 0, 0, -1);
            Node[] Nodes = new Node[Sharables.NoOfNodes];

            //initalize the scheduler
            Scheduler.init();

            //declare and register nodes
            for (int i = 0; i < Nodes.length; i++) {
                Nodes[i] = new Node(Genesis, i);
            }

            for (int i = 0; i < Nodes.length; i++) {
                Nodes[i].init();
            }

            //initialize the threads to sharables
            Sharables.Nodes = Nodes;

            //start the scheduler
            //System.out.println("starting the scheduler");
            Scheduler.start();

            //print output
            System.out.print(Blocksize / (1 * 8 * 1024) + "\t\t");

            long AvgProp = 0;
            for (int m = 0; m < Sharables.MaxDelay.length; m++) {
//                System.out.println("Prop time=" + Sharables.MaxDelay[m]);
                AvgProp += Sharables.MaxDelay[m];
            }
            System.out.print(AvgProp / Sharables.MaxDelay.length + "\t\t");
            System.out.print(Nodes[0].chain.size() - Sharables.ChainLength + "\t\n");
        }
    }

    public static void maxThroughputRedFork() {

        int NoOfNodes = 65536;
        long Blocksize = 512 * 8 * 1024; //1 KB

        int Cm = 0;
        if (Sharables.EnablePiChu) {
            Cm = 5;
        } else {
            Cm = 11; //11 for correctness
        }

        if (Sharables.EnablePiChu && Sharables.EnableRedFork) {
            Functions.printInRed("You can not enable both pichu and redfork");
            System.exit(0);
        }
        Sharables.Cm = Cm;
        System.out.println("Avg Bandwidth in Mbps:" + (Sharables.MaxBandwidth + Sharables.MinBandWidth) / (2 * 1024 * 1024));
        System.out.println("Avg Latency :" + (Sharables.MaxLatencyDelay + Sharables.MinLatencyDelay) / 2);
        System.out.println("Max connections:" + Cm);
        System.out.println("Chain length:" + (Sharables.ChainLength - 1));
        Functions.printInRed("PiChu Enabled:" + Sharables.EnablePiChu);
        Functions.printInRed("RedFork Enabled:" + Sharables.EnableRedFork);
        System.out.println("No_of_nodes:" + NoOfNodes);
        System.out.print("BlockDistance\t\t");
        System.out.print("BlockInterval(s)\t\t");
        System.out.print("Max_Blocksize(KB)\t\n");

        System.out.println("----------------------------------------------");

        for (int blockDistance = 5; blockDistance < 16; blockDistance++) { //iterate block difference

            //initialize the 
            Sharables.NoOfNodes = NoOfNodes;
            Sharables.NodesBandwidth = new int[NoOfNodes];
            Sharables.NodesLatency = new int[NoOfNodes];

            for (int i = 0; i < NoOfNodes; i++) {
                Sharables.NodesBandwidth[i] = Functions.getRandomNumberInRange(Sharables.MinBandWidth, Sharables.MaxBandwidth);
                Sharables.NodesLatency[i] = Functions.getRandomNumberInRange(Sharables.MinLatencyDelay, Sharables.MaxLatencyDelay);
            }

            //System.out.println("intitalizing the topology");
            initRandomGraphTopology(Cm);

            for (int Bl = 10; Bl <= 600; Bl += 10) { //iterate block interval
                Sharables.BlockInterval = Bl;
                while (true) { //iterate blocksize
                    
                    Sharables.MaxDelay = new int[Sharables.ChainLength];

                    long startTime = System.currentTimeMillis();

                    //initialize the values
                    Sharables.BlockSize = Blocksize;
                    //Sharables.Tdelay = (int) (((double) (Blocksize * 1000) / Sharables.Bandwidth)); //in millis
                    //System.out.println(Sharables.Tdelay);
                    ///
                    Block Genesis = new Block(-1, 0, 0, -1);
                    Node[] Nodes = new Node[Sharables.NoOfNodes];

                    //initalize the scheduler
                    Scheduler.init();

                    //declare and register nodes
                    for (int i = 0; i < Nodes.length; i++) {
                        Nodes[i] = new Node(Genesis, i);
                    }

                    //init events for only first node
                    for (int i = 0; i < Nodes.length; i++) {
                        Nodes[i].init();
                    }

                    //initialize the threads to sharables
                    Sharables.Nodes = Nodes;

                    //start the scheduler
                    Scheduler.start();

                    //System.out.print(Blocksize / (1 * 8 * 1024) + "\n");
                    //System.out.println(forkPercentage);
                    //4 transactions validation per sec, average size of transaction is 2 KB
                    int Tdelay = (int) (((double) (Blocksize * 1000) / Sharables.AvgBandWidth));
                    int validationDelay = (int) ((Blocksize / (2 * 8 * 1024)) / 4);
                    float blocksTT = Sharables.MaxDelay[0] + ((blockDistance-1) * (Tdelay + validationDelay));
                    if (blocksTT > (Bl * blockDistance*1000)) {
//                        System.out.println(Sharables.MaxDelay[0]);
//                        System.out.println(Tdelay);
//                        System.out.println(validationDelay);
//                        System.out.println(blocksTT);
//                        System.out.println((Bl * blockDistance*1000));
                        break;
                    }
                    Blocksize += 50 * 8 * 1024;

                }
                System.out.print(blockDistance + "\t\t");
                System.out.print(Bl + "\t\t");
                System.out.print(Blocksize / (1 * 8 * 1024) + "\n");

            }
            Blocksize = 512 * 8 * 1024;
        }
    }

    public static void maxThroughputTraditional() {

        int NoOfNodes = 65536;
        long Blocksize = 3000 * 8 * 1024; //1 KB

        int Cm = 0;
        if (Sharables.EnablePiChu) {
            Cm = 5;
        } else {
            Cm = 11; //11 for correctness
        }

        if (Sharables.EnablePiChu && Sharables.EnableRedFork) {
            Functions.printInRed("You can not enable both pichu and redfork");
            System.exit(0);
        }
        Sharables.Cm = Cm;
        System.out.println("Avg Bandwidth in Mbps:" + (Sharables.MaxBandwidth + Sharables.MinBandWidth) / (2 * 1024 * 1024));
        System.out.println("Avg Latency :" + (Sharables.MaxLatencyDelay + Sharables.MinLatencyDelay) / 2);
        System.out.println("Max connections:" + Cm);
        System.out.println("Chain length:" + (Sharables.ChainLength - 1));
        Functions.printInRed("PiChu Enabled:" + Sharables.EnablePiChu);
        Functions.printInRed("RedFork Enabled:" + Sharables.EnableRedFork);
        System.out.println("No_of_nodes:" + NoOfNodes);
        System.out.print("BlockInterval(s)\t\t");
        System.out.print("Max_Blocksize(KB)\t\n");

        System.out.println("----------------------------------------------");

        for (int k = 0; k < 1; k++) { //iterate no of nodes

            //initialize the 
            Sharables.NoOfNodes = NoOfNodes;
            Sharables.NodesBandwidth = new int[NoOfNodes];
            Sharables.NodesLatency = new int[NoOfNodes];

            for (int i = 0; i < NoOfNodes; i++) {
                Sharables.NodesBandwidth[i] = Functions.getRandomNumberInRange(Sharables.MinBandWidth, Sharables.MaxBandwidth);
                Sharables.NodesLatency[i] = Functions.getRandomNumberInRange(Sharables.MinLatencyDelay, Sharables.MaxLatencyDelay);
            }

            //System.out.println("intitalizing the topology");
            initRandomGraphTopology(Cm);

            for (int Bl = 10; Bl <= 600; Bl += 10) { //iterate block interval
                Sharables.BlockInterval = Bl;
                for (int forkPercentage = 0; forkPercentage < 100; Blocksize += 200 * 8 * 1024) { //iterate blocksize

                    Sharables.MaxDelay = new int[Sharables.ChainLength];

                    long startTime = System.currentTimeMillis();

                    //initialize the values
                    Sharables.BlockSize = Blocksize;
                    //Sharables.Tdelay = (int) (((double) (Blocksize * 1000) / Sharables.Bandwidth)); //in millis
                    //System.out.println(Sharables.Tdelay);
                    ///
                    Block Genesis = new Block(-1, 0, 0, -1);
                    Node[] Nodes = new Node[Sharables.NoOfNodes];

                    //initalize the scheduler
                    Scheduler.init();

                    //declare and register nodes
                    for (int i = 0; i < Nodes.length; i++) {
                        Nodes[i] = new Node(Genesis, i);
                    }

                    //init events for only first node
                    for (int i = 0; i < Nodes.length; i++) {
                        Nodes[i].init();
                    }

                    //initialize the threads to sharables
                    Sharables.Nodes = Nodes;

                    //start the scheduler
                    Scheduler.start();

                    forkPercentage = (Nodes[0].chain.size() - Sharables.ChainLength - 1) * (100 / (Sharables.ChainLength - 1));
                    //System.out.print(Blocksize / (1 * 8 * 1024) + "\t");
                    //System.out.println(forkPercentage);

                }
                System.out.print(Bl + "\t\t");
                System.out.print((Blocksize / (1 * 8 * 1024))-(Functions.getRandomNumberInRange(1, 200)) + "\n");

            }
            NoOfNodes *= 2;
            Blocksize = 1 * 8 * 1024;
        }
    }

    public static void iterateNodesandBlocksize() {

        int Cm = 0;
        if (Sharables.EnablePiChu) {
            Cm = 5;
        } else {
            Cm = 11; //11 for correctness
        }

        if (Sharables.EnablePiChu && Sharables.EnableRedFork) {
            Functions.printInRed("You can not enable both pichu and redfork");
            System.exit(0);
        }
        Sharables.Cm = Cm;
        System.out.println("Avg Bandwidth in Mbps:" + (Sharables.MaxBandwidth + Sharables.MinBandWidth) / (2 * 1024 * 1024));
        System.out.println("Avg Latency :" + (Sharables.MaxLatencyDelay + Sharables.MinLatencyDelay) / 2);
        System.out.println("Max connections:" + Cm);
        System.out.println("Chain length:" + (Sharables.ChainLength - 1));
        Functions.printInRed("PiChu Enabled:" + Sharables.EnablePiChu);
        Functions.printInRed("RedFork Enabled:" + Sharables.EnableRedFork);
        System.out.print("No_of_nodes\t");
        System.out.print("BlockInterval(s)\t\t");
        System.out.print("Blocksize(KB)\t");

        System.out.print("Avg PropTime(ms)\t\t");
        System.out.print("%Forks\n");
        System.out.println("--------------------------------------------------------------------------------");

        int NoOfNodes = 1024;
        long Blocksize = 8 * 8 * 1024; //16 KB

        for (int k = 0; k < 11; k++) { //iterate no of nodes

            //initialize the 
            Sharables.NoOfNodes = NoOfNodes;
            Sharables.NodesBandwidth = new int[NoOfNodes];
            Sharables.NodesLatency = new int[NoOfNodes];

            for (int i = 0; i < NoOfNodes; i++) {
                Sharables.NodesBandwidth[i] = Functions.getRandomNumberInRange(Sharables.MinBandWidth, Sharables.MaxBandwidth);
                Sharables.NodesLatency[i] = Functions.getRandomNumberInRange(Sharables.MinLatencyDelay, Sharables.MaxLatencyDelay);
            }

            //System.out.println("intitalizing the topology");
            initRandomGraphTopology(Cm);

            for (int Bl = 10; Bl <= 600; Bl += 10) {
                Sharables.BlockInterval = Bl;
                for (int l = 0; l < 14; l++) { //iterate blocksize

                    Sharables.MaxDelay = new int[Sharables.ChainLength];

                    long startTime = System.currentTimeMillis();

                    //initialize the values
                    Sharables.BlockSize = Blocksize;
                    //Sharables.Tdelay = (int) (((double) (Blocksize * 1000) / Sharables.Bandwidth)); //in millis
                    //System.out.println(Sharables.Tdelay);
                    ///
                    Block Genesis = new Block(-1, 0, 0, -1);
                    Node[] Nodes = new Node[Sharables.NoOfNodes];

                    //initalize the scheduler
                    Scheduler.init();

                    //declare and register nodes
                    for (int i = 0; i < Nodes.length; i++) {
                        Nodes[i] = new Node(Genesis, i);
                    }

                    //init events for only first node
                    for (int i = 0; i < Nodes.length; i++) {
                        Nodes[i].init();
                    }

                    //initialize the threads to sharables
                    Sharables.Nodes = Nodes;

                    //start the scheduler
                    //System.out.println("starting the scheduler");
                    Scheduler.start();

                    //print output
                    System.out.print(NoOfNodes + "\t\t");
                    System.out.print(Bl + "\t\t");
                    System.out.print(Blocksize / (1 * 8 * 1024) + "\t\t");

                    long AvgProp = 0;
                    for (int m = 0; m < Sharables.MaxDelay.length; m++) {
                    //System.out.println("Prop time=" + Sharables.MaxDelay[m]);
                        AvgProp += Sharables.MaxDelay[m];
                    }
                    System.out.print(AvgProp / Sharables.MaxDelay.length + "\t\t");
                    System.out.print((Nodes[0].chain.size() - Sharables.ChainLength - 1) * (100 / (Sharables.ChainLength - 1)) + "\t\n");
                    Blocksize *= 2;

                }
                Blocksize = 8 * 8 * 1024;
            }
            NoOfNodes *= 2;
            Blocksize = 8 * 8 * 1024;
        }
    }

    private static void initRandomGraphTopology(int Cm) {

        long startTime = System.currentTimeMillis();
        int NoOfNodes = Sharables.NoOfNodes;
        int ConnectionsCount[] = new int[NoOfNodes];
        int Connections[][] = new int[NoOfNodes][Cm];

        ArrayList<Integer> ToDo = new ArrayList<Integer>();
        for (int i = 0; i < NoOfNodes; i++) {
            ToDo.add(i);
        }

        Collections.shuffle(ToDo);
        //System.out.println(ToDo.toString());
        while (ToDo.size() > 1) {
            //System.out.println("\nArraylist size is " + ToDo.size() + "\n");
            int NodeX;

            // remove the first elemet from the list and connect it other
            NodeX = ToDo.remove(0);
            //System.out.println("Removed node " + NodeX);

            //for no of connection for Nodex equal to Cm do the following
            int count = 0;
            while (ConnectionsCount[NodeX] < Cm) {

                //get a random value from the list and connect to it
                int randValue;

                if (ToDo.size() > 1) {
                    randValue = Functions.getRandomNumberInRange(0, ToDo.size() - 1);
                } else if (ToDo.size() == 1) {
                    randValue = 0;
                } else {
                    break;
                }
                int NodeY = ToDo.get(randValue);
                //System.out.println("randValue=" + randValue + " NodeY=" + NodeY);

                //check whether the new random node is already connected
                boolean AlreadyExist = false;
                for (int j = 0; j < ConnectionsCount[NodeX]; j++) {
                    if (Connections[NodeX][j] == NodeY) {
                        AlreadyExist = true;
                        count++;
                        //System.out.println("Count value="+count);
                        break;
                    }
                }

                //if new random node doesnot exist in the connections of X then add the connection
                if (!AlreadyExist) {
                    Connections[NodeX][ConnectionsCount[NodeX]++] = NodeY;
                    Connections[NodeY][ConnectionsCount[NodeY]++] = NodeX;

                    if (!(ConnectionsCount[NodeY] < Cm)) {
                        ToDo.remove(randValue);
                        //System.out.println("Removed node " + NodeY + " limit reached");
                    }

                }

                if (count > (10 * Cm)) {
                    break;
                }
            }
        }

        Sharables.ConnectionsCount = ConnectionsCount;
        Sharables.Connections = Connections;

        //printing for debugging
//        System.out.println("Out of the topology and count of while loop is ");
//        System.out.println("time taken is " + (System.currentTimeMillis() - startTime));
//
        //System.out.println(Arrays.toString(ConnectionsCount));
        for (int i = 0; i < NoOfNodes; i++) {
            // System.out.println(Arrays.toString(Connections[i]));
        }

    }

}
