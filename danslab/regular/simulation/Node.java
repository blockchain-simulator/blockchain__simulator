/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class Node {

    int Id;
    OptimizedChain chain;
    Block Last;
    long NextEventTime = Long.MAX_VALUE;
    long MiningTime = 0;
    int LastBlockDelay = 0;

    Node(Block Genesis, int Id) {

        //next event time intialize
        Last = Genesis;
        chain = new OptimizedChain(Genesis);
        this.Id = Id;

    }

    long getMiningTime() {

        int NoNodes = Sharables.NoOfNodes;
        int Interval = Sharables.BlockInterval;
        double P = (double) 1 / (Interval * NoNodes);

        long Out = 0;
        while (Out == 0) {
            Out = ((long) ((Math.log(Math.random()) / Math.log(1 - P))));
        }

        //convert to milliseconds
        if (Out * 1000 > 0) {
            return Out * 1000;
        } else {
            return Long.MAX_VALUE;
        }
    }

    public void init() {

        CreateMiningEvent(1, Sharables.startTime);

    }

    public void CallBlack(long EventTime, int BlockNumber) {

        if (Last.NodeNumber < BlockNumber) {
            //mined a new block
            int timeTakentoMine = (int) ((EventTime - Last.TimeStamp));

            //creat the new block
            Block temp = new Block(Last.TimeStamp, Last.NodeNumber + 1, EventTime, Id);
            Last = temp;
            chain.add(temp); //add to chain

            //System.out.println("Mined a block and forwarding by node " + Id + " current tree length is " + Last.NodeNumber+" mining time is"+MiningTime);
            CreateMiningEvent(BlockNumber + 1, EventTime);

            Broadcast(Last);

        }

    }

    void CreateMiningEvent(int BlockNumber, long CurrentTime) {
        if (BlockNumber <= Sharables.ChainLength) {
            MiningTime = getMiningTime();
            NextEventTime = CurrentTime + MiningTime;
            if (NextEventTime < 0) {
                NextEventTime = Long.MAX_VALUE;
            }
            Scheduler.addEvent(Id, NextEventTime, BlockNumber, this);
            //System.out.println("Mining time of block " + BlockNumber + " for node " + Id + " is " + NextEventTime);
        } else {
            Scheduler.init();
        }
    }

    public void NewBlock(Block temp, int MyDelay, int ReceivedFrom) {

        if (!chain.exists(temp)) {

            if(Sharables.EnablePiChu){
                int NoOfChunks =  (int) (Math.ceil((double) (Sharables.BlockSize - Sharables.HeaderSize) / Sharables.ChunkSize));
                int ChunkTdelay = (int) (((double) ((Sharables.ChunkSize + Sharables.SignatureSize) * 1000) / Sharables.NodesBandwidth[Id]));;
                MyDelay += NoOfChunks * ChunkTdelay * Sharables.Cm;
            }
            
            if (temp.NodeNumber > Last.NodeNumber) {

                //check whether this node will mine the block before it receives
                if ((temp.TimeStamp + MyDelay) < NextEventTime) {
                    Last = temp;
                    LastBlockDelay = MyDelay;
                    CreateMiningEvent(temp.NodeNumber + 1, temp.TimeStamp + MyDelay);
                }

            }
            //check if another block can be received in short time
            if (temp.NodeNumber == Last.NodeNumber) {
                if (LastBlockDelay > MyDelay) {
                    Last = temp;
                    LastBlockDelay = MyDelay;
                }
            }
            chain.add(temp);
            //System.out.println("Tree height at " + Id + " is " + chain.TreeHeight + " Hopcount: " + HopCount);

            //determining the maximum broadcast time
            if (MyDelay > Sharables.MaxDelay[temp.NodeNumber - 1]) {
                Sharables.MaxDelay[temp.NodeNumber - 1] = MyDelay;
            }

            //System.out.println("block received by " + Id + " from " + ReceivedFrom + " with total delay=" + MyDelay);
        }

    }

    void Broadcast(Block temp) {

        class Ntemp {

            int Parent;
            int NodeNumber;
            int HopCount;

            public Ntemp(int NodeNumber, int HopCount, int Parent) {
                this.Parent = Parent;
                this.NodeNumber = NodeNumber;
                this.HopCount = HopCount;
            }
        }

        Queue<Ntemp> queue = new LinkedList<>();

        HashSet<Integer> InQueue = new HashSet<>();
        InQueue.add(Id);

        //add this node connections to the queue
        int MyConnectionsCount = Sharables.ConnectionsCount[Id];

        if (MyConnectionsCount == 0) {
            System.out.println("This node doesnot have connection:" + Id);
            return;
        }

        long Size = 0;
        if (Sharables.EnablePiChu) {
            Size = Sharables.HeaderSize;
        } else {
            Size = Sharables.BlockSize;
        }
        
        if(Sharables.EnableRedFork){
            Size = Sharables.RedForkPartASize;
        }
        int Tdelay = (int) (((double) (Size * 1000) / Sharables.NodesBandwidth[Id]));
        int validationDelay = (int) ((Size / (2 * 8 * 1024)) / 4);
        int delay = (MyConnectionsCount - 1) * Tdelay + Sharables.NodesLatency[Id]+validationDelay;
        //System.out.println(validationDelay);
        for (int i = 0; i < MyConnectionsCount; i++) {

            int NodeNumber = Sharables.Connections[Id][i];
            Ntemp n = new Ntemp(NodeNumber, delay, Id);
            queue.add(n);
            InQueue.add(NodeNumber);
        }

        //now start the BFS
        while (queue.size() != 0) {

            Ntemp Vertex = queue.remove();
            int NodeNumber = Vertex.NodeNumber;
            int HopCount = Vertex.HopCount;

            //if it is not visited call that Node Newblock function
            Sharables.Nodes[NodeNumber].NewBlock(temp, HopCount, Vertex.Parent);

            //add it connections to the queue
            for (int i = 0; i < Sharables.ConnectionsCount[NodeNumber]; i++) {
                int Itemp = Sharables.Connections[NodeNumber][i];
                if (!InQueue.contains(Itemp)) {
                    int Tdelay1 = (int) (((double) (Size * 1000) / Sharables.NodesBandwidth[NodeNumber]));
                    int validationDelay1 = (int) ((Size / (2 * 8 * 1024)) / 4);
                    Ntemp n = new Ntemp(Itemp, HopCount + (Sharables.ConnectionsCount[NodeNumber] - 1) * Tdelay1 + Sharables.NodesLatency[NodeNumber]+validationDelay1, NodeNumber);
                    queue.add(n);
                    InQueue.add(Itemp);
                }
            }

        }
    }
}

