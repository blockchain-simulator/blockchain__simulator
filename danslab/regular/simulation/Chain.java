/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class Chain {

    private Tree Root = null;
    int TreeHeight = 0;
    int BlocksCount = 0;

    class Tree {

        Block block;
        int height;
        int HopCount;
        ArrayList<Tree> Childs = new ArrayList<>();

        public Tree(Block temp) {
            this.block = temp;
        }
    }

    public void reset() {

        Root = null;
        TreeHeight = 0;
        BlocksCount = 0;

    }

    public Chain(Block Genesis) {
        Root = new Tree(Genesis);
        Root.height = 0;
        TreeHeight = 0;
    }

    public boolean add(Block NewBlock) {
        if (exists(Root, NewBlock)) {
            System.out.println("Block already exists");
            return true;
        }

        Tree PrevNode = find(Root, NewBlock.PrevTimeStamp);
        //if result is not null then update tree height and add the child
        if (PrevNode != null) {
            Tree tempTree = new Tree(NewBlock);
            tempTree.height = PrevNode.height + 1;
            if (tempTree.height > TreeHeight) {
                TreeHeight = tempTree.height;
            }
            PrevNode.Childs.add(tempTree);
            BlocksCount++;
            return true;
        } else {
            System.out.println("Prev Node doesnt exist");
        }

        return false;
    }

    private Tree find(Tree root, long PrevNodeNumber) {
        if (root == null) {
            return null;
        }
        if (root.block.NodeNumber == PrevNodeNumber) {
            return root;
        }
        for (int i = 0; i < root.Childs.size(); i++) {
            Block temp = root.Childs.get(i).block;
            Tree tempTree = find(root.Childs.get(i), PrevNodeNumber);
            if (tempTree != null) {
                return tempTree;
            }
        }
        return null;
    }

    private Tree getLongestChainLastBlock(Tree root) {
        if (root == null) {
            return null;
        }

        if (root.height == TreeHeight) {
            return root;
        }
        for (int i = 0; i < root.Childs.size(); i++) {
            Tree tempTree = root.Childs.get(i);
            Tree tempTree1 = getLongestChainLastBlock(tempTree);
            if (tempTree1 != null) {
                return tempTree1;
            }
        }
        return null;

    }

    public void printTree() {
        print(Root);
    }

    public Tree getFinalBlock() {
        return getLongestChainLastBlock(Root);
    }

    private void print(Tree root) {

        if (root == null) {
            return;
        }
        System.out.println(root.block.toString());
        for (int i = 0; i < root.Childs.size(); i++) {
            print(root.Childs.get(i));
        }
        return;
    }

    public boolean exists(Block NBlock) {
        return exists(Root, NBlock);
    }

    private boolean exists(Tree root, Block NBlock) {
        if (root == null) {
            return false;
        }
        if (root.block.PrevTimeStamp == NBlock.PrevTimeStamp && root.block.TimeStamp == NBlock.TimeStamp && root.block.MinerId==NBlock.MinerId) {
            return true;
        }
        for (int i = 0; i < root.Childs.size(); i++) {
            Block temp = root.Childs.get(i).block;
            boolean res = exists(root.Childs.get(i), NBlock);
            if (res) {
                return true;
            }
        }
        return false;
    }
}
