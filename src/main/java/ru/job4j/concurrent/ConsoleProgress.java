package ru.job4j.concurrent;

/*
Эта схема является шаблоном.
Если используются методы sleep(), join(), wait()
или аналогичные временно блокирующие поток методы,
то нужно в блоке catch вызвать прерывание.
 */
public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        var process = new char[] {'-', '\\', '|', '/'};
        while (!Thread.currentThread().isInterrupted()) {
            for (char c : process) {
                System.out.print("\r load: " + c);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000);
        progress.interrupt();
    }
}
