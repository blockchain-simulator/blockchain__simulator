/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class test {

    public static void main(String args[]) {

        //assume 100 nodes, each node 1 hash per second -> difficulity for bitcoin is 60000
        double p = (double) 1 / 1000;
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            System.out.println((Math.log(r.nextDouble()) / Math.log(1.0 - p)));
        }
        
    }
}
