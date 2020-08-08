/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class Sharables {

    static int Latency[][];
    static int NoOfNodes = 65536;
    static int BlockInterval = 600; //in seconds
    static final int MaxBandwidth = 38 * 1024 * 1024; //bits per second //25 mbps for correctness
    static final int MinBandWidth = 30 * 1024 * 1024;
    static final int AvgBandWidth = (MaxBandwidth + MinBandWidth) / 2;
    static final int MaxLatencyDelay = 300;
    static final int MinLatencyDelay = 200;

    static final int ChainLength = 1; //longest chain max length; use +1 than required; coded like this for optimization
    static final int TimeToVerify = 0;
    static int MaxDelay[] = new int[ChainLength];
    static long BlockSize = 532 * 1024 * 8; //in bits
    static int Tdelay = 0;

    static final long startTime = 0;
    static int ConnectionsCount[];
    static int Connections[][];
    static Node Nodes[];
    static int NodesBandwidth[];
    static int NodesLatency[];

    //PiChu settings
    static final int HeaderSize = 904; //in bits
    static final int ChunkSize = 128 * 8 * 1024;
    static final boolean EnablePiChu = false;
    static final int SignatureSize = 512;
    static int Cm = 0;

    
    //RedFork settings
    static final boolean EnableRedFork = false;
    static final int RedForkPartASize = 1024 * 3; //in bits

}
