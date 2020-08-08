/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.math.BigDecimal;

/**
 *
 * @author kapnb
 */
public class ForkTesting {

    public static void main(String args[]) {

        int NoNodes = 65536;
        int Interval = 600;
        double P = ((double) 1 / (Interval * NoNodes));
        System.out.println(Math.log(1-P));
        int NoDigits = (NoNodes + "").length() + 1;

        Random r = new Random();

        long[] Out = new long[NoNodes];

        for (int i = 0; i < NoNodes; i++) {

            int temp=NoNodes * 100;
            double u = Double.parseDouble("0." + String.format("%0" + NoDigits + "d", r.nextInt(NoNodes * 1000)));;

            Out[i] = ((long) ((Math.log(r.nextDouble()) / Math.log(1 - P))));
//            System.out.println("u="+u);
//            System.out.println("Math.log(u)="+Math.log(u));
//            System.out.println("Math.log(1-p)="+Math.log(1-P));
//            System.out.println("Out="+Out[i]);

        }

        Arrays.sort(Out);

        System.out.println("\n\n" + Out[0]);
        System.out.println(Out[1]);
        System.out.println(Out[2]);
        System.out.println(Out[3]);

        System.out.println(Out[NoNodes - 1]);
        System.out.println(Long.MAX_VALUE);
    }

    public void baseMethod() {
        int NoNodes = 100;
        int Interval = 600000;
        double P = (double) 1 / (Interval * NoNodes);

        int NoDigits = (NoNodes + "").length();

        int temp = NoDigits;
        String format = "#.##";
        while (temp-- > 0) {
            format += "#";
        }

        DecimalFormat df = new DecimalFormat(format);

        long[] Out = new long[NoNodes];

        int DivideBy = 1;
        temp = NoDigits - 1;

        while (temp-- > 0) {
            DivideBy *= 10;
        }
        double Divide = (double) NoNodes / DivideBy;
        for (int i = 0; i < NoNodes; i++) {

            double u = Double.parseDouble(df.format(Math.random()));
            System.out.println(((long) ((Math.log(u) / Math.log(1 - P)) / Divide)));
            Out[i] = ((long) ((Math.log(u) / Math.log(1 - P)) / Divide));

//            if(Out[i]==0){
//                Out[i]=1;
//            }
//            
//            if(Long.MAX_VALUE/Out[i] > 1000){
//                Out[i]*=1000;
//            }
//            else{
//                Out[i]=Long.MAX_VALUE;
//            }
        }

        Arrays.sort(Out);

        System.out.println("\n\n" + Out[0]);
        System.out.println(Out[1]);
        System.out.println(Out[2]);
        System.out.println(Out[3]);

        System.out.println(Out[NoNodes - 1]);
        System.out.println(Long.MAX_VALUE);
    }
}
