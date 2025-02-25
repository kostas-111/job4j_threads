package ru.job4j.threads.threadlocal;

/*
нити в виде двух классов FirstThread и SecondThread,
в которых запишем значение в переменную ThreadLocal.
 */
public class SecondThread extends Thread {
    @Override
    public void run() {
        ThreadLocalDemo.threadLocal.set("Это поток 2.");
        System.out.println(ThreadLocalDemo.threadLocal.get());
    }
}