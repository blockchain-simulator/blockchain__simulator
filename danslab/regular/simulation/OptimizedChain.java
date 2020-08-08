/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.util.HashSet;

/**
 *
 * @author kapnb
 */
public class OptimizedChain {

    HashSet<Block> set = new HashSet<Block>();

    public OptimizedChain(Block Gensis) {
        set.add(Gensis);
    }

    boolean add(Block B) {

        return set.add(B);
    }

    boolean exists(Block B) {

        return set.contains(B);
    }
    
    int size(){
        return set.size();
    }

}
