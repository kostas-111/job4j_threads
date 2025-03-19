package ru.job4j.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;

/*
Программа скачивает файл из сети с ограничением по скорости.
Чтобы ограничить скорость скачивания, нужно засечь время скачивания 1024 байт.
Если время меньше указанного, то нужно выставить паузу за счет Thread.sleep.
Пауза должна вычисляться, а не быть константой.
Чтобы загрузить параметры в программу в настройках запуска в IDEA укажите нужные данные.
 */
public class Wget implements Runnable {

    private final String url;
    private final int speed;
    private final String fileName;

    public Wget(String url, int speed, String fileName) {
        this.url = url;
        this.speed = speed;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        long startAt = System.currentTimeMillis();
        File file = new File(fileName);
        try (InputStream input = URI.create(url).toURL().openStream();
             FileOutputStream output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            long howManyBytesRead = 0;
            long startTime = System.currentTimeMillis();
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                long downloadAt = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                long time = System.nanoTime() - downloadAt;
                System.out.println("Read 1024 bytes : " + time + " nano.");
                howManyBytesRead += bytesRead;
                if (howManyBytesRead >= speed) {
                    long timeToDownload = System.currentTimeMillis() - startTime;
                    if (timeToDownload < 1000) {
                        try {
                            Thread.sleep(1000 - timeToDownload);
                        } catch (InterruptedException e) {
                            throw new RuntimeException();
                        }
                    }
                    startTime = System.currentTimeMillis();
                    howManyBytesRead = 0;
                }
                System.out.println(Files.size(file.toPath()) + " bytes");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        if (args.length != 3) {
            throw new IllegalArgumentException("Недостаточно аргументов. Пожалуйста, укажите URL, скорость и имя файла.");
        }

        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String fileName = args[2];

        if (speed <= 0) {
            throw new IllegalArgumentException("Скорость должна быть положительным целым числом.");
        }

        Thread wget = new Thread(new Wget(url, speed, fileName));
        wget.start();
        wget.join();
    }
}