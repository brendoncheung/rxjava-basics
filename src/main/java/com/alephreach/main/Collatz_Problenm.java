package com.alephreach.main;

public class Collatz_Problenm {


    public static void main(String[] args) {
        System.out.println("Total steps: " + compute(69069069));
    }

    private static synchronized int compute(long n) {

        long number = n;
        int steps = 0;

        System.out.println("Initial number: " + number);
        while (number > 1) {
            if (number % 2 == 0) {
                steps++;
                System.out.println("Step: " + steps + "\tEven number: \t" + number);
                number = number / 2;
            } else {
                steps++;
                System.out.println("Step: " + steps + "\tOdd number: \t" + number);
                number = 3 * number + 1;
            }
        }
        return steps;
    }


}
