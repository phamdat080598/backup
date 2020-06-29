package com.lg.exercise4.unit2;

public class Client {
    public static void main(String[] args) {
//        BitVector bitVectorOne = new BitVector();
//        bitVectorOne.set(10);
//        bitVectorOne.set(5);
//        bitVectorOne.set(34);
//
//        System.out.println("\nSize of bit : " + bitVectorOne.size());
//        System.out.println("Clear bit(5) : ");
//        bitVectorOne.clear(5);
//        System.out.println("Is 10 in bit : "+bitVectorOne.get(10));
//        System.out.println("Copy bit vector : ");
//
//        BitVector bitCopy = new BitVector();
//        bitCopy.set(15);
//        bitCopy.set(34);
//
//        bitVectorOne.copy(bitCopy);
//        System.out.println("Size of(Copy) : " + bitVectorOne.size());
//
//        Iterator iteratorBit = bitVectorOne.iterator();
//
//        System.out.print("Iterator bit : ");
//        while (iteratorBit.hasAnotherElement()){
//            System.out.print(iteratorBit.nextElement()+" ");
//        }
//        System.out.println();

        System.out.println(doi(30));
    }

    private static String doi(int number){
        StringBuilder builder = new StringBuilder();
        int dividend = number;
        while (true){
            dividend= dividend/2;
            int du = dividend%2;
            builder.append(du);
            if(dividend==0){
                break;
            }
        }
        return builder.reverse().toString();

    }
}
