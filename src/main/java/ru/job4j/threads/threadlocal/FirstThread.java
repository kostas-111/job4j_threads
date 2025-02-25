package ru.job4j.threads.threadlocal;

/*
нити в виде двух классов FirstThread и SecondThread,
в которых запишем значение в переменную ThreadLocal.
 */
public class FirstThread extends Thread {
    @Override
    public void run() {
        ThreadLocalDemo.threadLocal.set("Это поток 1.");
        System.out.println(ThreadLocalDemo.threadLocal.get());
    }
}
