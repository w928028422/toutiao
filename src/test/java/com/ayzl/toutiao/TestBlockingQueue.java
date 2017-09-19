package com.ayzl.toutiao;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Producer implements Runnable{
    BlockingQueue<String> queue;

    Producer(BlockingQueue<String> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try{
            for (int i = 0; i < 10; i++) {
                Thread.sleep(10);
                queue.put("produce:" + String.valueOf(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable{
    BlockingQueue<String> queue;

    Consumer(BlockingQueue<String> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try{
            while (true){
                System.out.println(Thread.currentThread().getName() + " consume:" + queue.take());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class TestBlockingQueue {
    public static void main(String[] args){
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(blockingQueue)).start();
        new Thread(new Consumer(blockingQueue), "Consumer1").start();
        new Thread(new Consumer(blockingQueue), "Consumer2").start();
    }
}
