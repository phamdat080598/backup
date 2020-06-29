package com.lg.exercise4.unit2;

public interface IBitVector {
    boolean get(int i);

    void set(int i);

    void clear(int i);

    void copy(BitVector b);

    int size();

    public Iterator<Integer> iterator();
}
