package com.ayzl.toutiao;

class MyThread extends Thread{
    private int id;

    public MyThread(int id){
        this.id = id;
    }

    @Override
    public void run() {
        super.run();
        try{
            for(int i=0;i<10;i++){
                sleep(100);
                System.out.println(id + ":" + String.valueOf(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class MultiThread {
    public static void main(String[] args){
        /*for(int i=0;i<10;i++){
            new MyThread(i).start();
        }

        for(int i=0;i<10;i++){
            int id = i;
            new Thread(() -> {
                try{
                    for(int i1 = 0; i1 <10; i1++){
                        Thread.sleep(100);
                        System.out.println(id + ":" + String.valueOf(i1));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        }*/

        for(int i=0;i<10;i++){
            new Thread(() -> {
                testSynchronized1();
                testSynchronized2();
            }).start();
        }
    }

    private static Object obj = new Object();

    public static void testSynchronized1(){
        synchronized (obj){
            try{
                for(int i=0;i<10;i++){
                    Thread.sleep(100);
                    System.out.println("t1:" + String.valueOf(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2(){
        synchronized (obj){
            try{
                for(int i=0;i<10;i++){
                    Thread.sleep(100);
                    System.out.println("t2:" + String.valueOf(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
