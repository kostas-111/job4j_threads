package ru.job4j.threads;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

/*
Есть три Thread. Нужно их всех запустить.
Они делают одну и туже работу, но с разным временем.
Например. У нас есть реплики сервера и они делают в нему запрос.
Если нить выполнила свою работу, то остальные нити нужно отметить.
 */
public class CyclicBarrierExample {

    /*
    Флаг, указывающий, что один поток завершил работу
     */
    private static AtomicBoolean threadCompleted = new AtomicBoolean(false);

    /*
    Список потоков для прерывания
     */
    private static Thread[] threads = new Thread[3];

    public static void main(String[] args) throws InterruptedException {

        /*
        Создаём барьер для 3 потоков
         */
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("Все потоки достигли барьера. Задача завершена.");
        });

       /*
       Создаём задачи
        */
        Worker task1 = new Worker("Replica-1", 2000, barrier);
        Worker task2 = new Worker("Replica-2", 4000, barrier);
        Worker task3 = new Worker("Replica-3", 8000, barrier);

        /*
        Инициализируем потоки
         */
        threads[0] = new Thread(task1, "Thread-1");
        threads[1] = new Thread(task2, "Thread-2");
        threads[2] = new Thread(task3, "Thread-3");

        /*
        Запускаем потоки
         */
        for (Thread thread : threads) {
            thread.start();
        }

        /*
        Запускаем мониторинг getNumberWaiting()
         */
        Thread monitorThread = new Thread(() -> {
            while (!threadCompleted.get()) {
                /*
                Первый поток достиг барьера
                 */
                if (barrier.getNumberWaiting() > 0) {
                    System.out.println("Обнаружено завершение одного потока!");
                    threadCompleted.set(true);

                    /*
                    Прерываем остальные потоки
                     */
                    for (Thread thread : threads) {
                        if (thread.isAlive()) {
                            thread.interrupt();
                        }
                    }
                    break;
                }
                try {
                    /*
                    Периодическая проверка
                     */
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Monitor-Thread");
        monitorThread.start();

        // Ждём завершения всех потоков
        for (Thread thread : threads) {
            thread.join();
        }
    }

    static class Worker implements Runnable {
        private final String name;
        private final int workTime;
        private final CyclicBarrier barrier;

        Worker(String name, int workTime, CyclicBarrier barrier) {
            this.name = name;
            this.workTime = workTime;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            boolean reachedBarrier = false;
            try {
                System.out.println(name + " начал работу...");

                /*
                Симуляция работы с проверкой прерывания
                 */
                simulateWork(workTime);

                if (!threadCompleted.getAndSet(true)) {
                    System.out.println(name + " — первый завершивший поток, результат принят!");
                } else {
                    System.out.println(name + " прервана, так как другой поток завершил работу.");
                }

                /*
                Достигаем барьера
                 */
                barrier.await();
                reachedBarrier = true;
            } catch (InterruptedException e) {
                System.out.println(name + " прервана: " + e.getMessage());
            } catch (BrokenBarrierException e) {
                System.out.println(name + " барьер сломан: " + e.getMessage());
            } finally {

                /*
                Гарантируем достижение барьера, даже при прерывании
                 */
                if (!reachedBarrier) {
                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        System.out.println(name + " не смог достичь барьера: " + e.getMessage());
                    }
                }
            }
        }

        private void simulateWork(int millis) throws InterruptedException {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < millis) {
                if (Thread.currentThread().isInterrupted() || threadCompleted.get()) {
                    throw new InterruptedException("Работа прервана");
                }
                /*
                Маленькие паузы для проверки
                 */
                Thread.sleep(100);
            }
        }
    }
}
