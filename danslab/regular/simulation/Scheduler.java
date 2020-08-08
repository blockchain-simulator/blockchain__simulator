/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
class Scheduler {

    static class Event implements Comparator<Event> {

        int CallerId;
        int BlockNumber;
        long EventTime;
        Node GeneratedBy;

        public Event() { //empty constructor for comparator
        }

        public Event(int Id, long TimeStamp, int BlockLength, Node EventBy) {
            this.CallerId = Id;
            this.EventTime = TimeStamp;
            this.BlockNumber = BlockLength;
            this.GeneratedBy = EventBy;
        }

        @Override
        public int compare(Event E1, Event E2) {
            if (E1.EventTime < E2.EventTime) {
                return -1;
            } else if (E1.EventTime > E2.EventTime) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static PriorityQueue<Event> Pqueue = new PriorityQueue<Event>(new Event());

    public static void init() {
        Pqueue.clear();
    }

    public static synchronized void addEvent(int CallerId, long EventTime, int BlockNumber, Node GeneratedBy) {
        Event E = new Event(CallerId, EventTime, BlockNumber, GeneratedBy);
        //System.out.println("New event added by "+GeneratedBy.Id);
        Pqueue.add(E);
    }

    public static void start() {
        while (Pqueue.size() != 0) {
            Event E = Pqueue.remove(); //get first event
            E.GeneratedBy.CallBlack(E.EventTime, E.BlockNumber); //call the event generator to take appropriate action
        }
    }

//    public static List<List> temp(String args[]) {
//
////        Pqueue.add(new Event(1, 1, 1, null));
////        Pqueue.add(new Event(1, 2, 1, null));
////        Pqueue.add(new Event(1, -1, 1, null));
////        start();
//        int rows = 10;
//        ArrayList<ArrayList<Integer>> out = new ArrayList<>();
//
//        ArrayList<Integer> One = new ArrayList<>();
//        One.add(1);
//        ArrayList<Integer> two = new ArrayList<>();
//
//        for (int i = 0; i < rows; i++) {
//
//            out.add(One);
//
//            two.add(1);
//            for (int j = 0; j < One.size() - 1; j++) {
//                two.add(One.get(j) + One.get(j + 1));
//            }
//
//            two.add(1);
//
//            One = two;
//        }
//        
//        return out;
//    }

}
