package com.lg.exercise4.unit2;

import java.util.ArrayList;

public class BitVector implements IBitVector {
    private static final int ONE_BYTE = 32;
    private static final int BIT_INDEX=1;
    private static final int BIT_ZERO=0;

    private ArrayList<Integer> bits = new ArrayList<>();

    @Override
    public boolean get(int i) {

    }

    @Override
    public void set(int i) {
        String convert = toBinary(i);
        if(Math.sqrt(i)-bits.size()<=ONE_BYTE){

        }
    }

    @Override
    public void clear(int i) {

    }

    @Override
    public void copy(BitVector b) {

    }

    @Override
    public int size() {

    }

    @Override
    public Iterator<Integer> iterator() {
        return new IteratorBit();
    }

    private  String toBinary(int number){
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

    private class IteratorBit implements Iterator<Integer> {

        private int currentIndex=0;
        @Override
        public boolean hasAnotherElement() {
            for(int i=currentIndex;i<bits.size();i++){
                if(bits.get(i)==BIT_INDEX){
                    return true;
                }
            }
            return false;
        }

        @Override
        public Integer nextElement() {
            if(bits.get(currentIndex)==BIT_INDEX){
                return currentIndex++;
            }else{
                currentIndex++;
                return nextElement();
            }
        }
    }
}
