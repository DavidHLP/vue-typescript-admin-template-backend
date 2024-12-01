package com.david.hlp.SpringBootWork.system;

public class Main {
    public static void main(String[] args) {
        int num = 3;
        int bitCount = Integer.toBinaryString(num).length();
        int mask = (1 << bitCount) - 1;


    }
}
