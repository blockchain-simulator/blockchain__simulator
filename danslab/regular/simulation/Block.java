/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.util.Arrays;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class Block {

    long PrevTimeStamp;
    int NodeNumber;
    long TimeStamp;
    int MinerId;
    int HopCount;

    public Block(long PrevTimeStamp, int NodeNumber, long TimeStamp, int MinerId) {

        this.PrevTimeStamp = PrevTimeStamp;
        this.NodeNumber = NodeNumber;
        this.TimeStamp = TimeStamp;
        this.MinerId=MinerId;
    }

    public String toString() {
        String temp = " NodeNumber=" + NodeNumber + " PrevTimeStamp="+PrevTimeStamp+ " TimeStamp=" + TimeStamp +" MinerId="+MinerId;
        return temp;
    }

}
