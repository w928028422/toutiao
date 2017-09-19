package com.ayzl.toutiao;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TestAtomic {
    private static int counter=0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args){
        /*for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                sleep(10);
                for (int j = 0; j < 10; j++) {
                    System.out.println(atomicInteger.incrementAndGet());
                }
            }).start();
        }*/

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                sleep(10);
                for (int j = 0; j < 10; j++) {
                    System.out.println(++counter);
                }
            }).start();
        }
    }

    public static void sleep(int millseconds){
        try{
            Thread.sleep(new Random().nextInt(millseconds));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
