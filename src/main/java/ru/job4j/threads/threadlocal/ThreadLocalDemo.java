package ru.job4j.threads.threadlocal;

/*
Создадим в классе ThreadLocalDemo метод main,
в котором запишем данные в переменную ThreadLocal,
а также создадим экземпляры созданных классов-нитей и запустим их.

В результате работы программы видим, что каждое значение,
которое мы записывали из разных нитей в переменную tl,
вывелось на экран, то есть в каждой нити у нас хранится своя копия переменной tl.
 */
public class ThreadLocalDemo {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        Thread first = new FirstThread();
        Thread second = new SecondThread();
        threadLocal.set("Это поток main.");
        System.out.println(threadLocal.get());
        first.start();
        second.start();
        first.join();
        second.join();
    }
}
