package br.com.pucrs.remote.api;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        TesteThread testeThread = new TesteThread();
        new Thread(testeThread).start();

        Scanner scanner = new Scanner(System.in);

        System.out.println("The thread should be going... say something to try to stop");

        String a = scanner.nextLine();
        System.out.println("u say " + a);

        testeThread.await();
//
//        System.out.println("The thread await?");
//
//        scanner.nextLine();
//
//        testeThread.follow();
    }


    private static class TesteThread implements Runnable {

        private boolean shouldStop;

        public TesteThread() {
            this.shouldStop = false;
        }

        public void await() throws InterruptedException {
            System.out.println("Will try to wait thread");
            System.out.println(Thread.currentThread().getState());
            this.wait();
        }

        public void follow() throws InterruptedException {
            System.out.println("Will try to awake thread");
            this.notify();
        }

        @Override
        public void run() {
            System.out.println("Start call");
            String threadname = Thread.currentThread().getName();
            try {
                while (true) {
                    System.out.println(threadname + " started ");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
