package ru.job4j.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

/*
Программа скачивает файл из сети с ограничением по скорости.
Чтобы ограничить скорость скачивания, нужно засечь время скачивания 1024 байт.
Если время меньше указанного, то нужно выставить паузу за счет Thread.sleep.
Пауза должна вычисляться, а не быть константой.

Чтобы загрузить параметры в программу в настройках запуска в IDEA укажите нужные данные, например:
https://raw.githubusercontent.com/kostas-111/job4j_threads/refs/heads/main/pom.xml 1000
 */
public class Wget implements Runnable {

    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        long startAt = System.currentTimeMillis();
        File file = new File("tmp.xml");
        try (InputStream input = URI.create(url).toURL().openStream();
             FileOutputStream output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                long downloadAt = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                long time = System.nanoTime() - downloadAt;
                System.out.println("Read 1024 bytes : " + time + " nano.");
                long currentSpeed = (1024 * 1000000) / time;
                long pause = currentSpeed / speed;
                if (speed != 6000) {
                    System.out.printf("Calculated pause = %d ms.\n", pause);
                    Thread.sleep(pause);
                }
            }
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        if (args.length != 2) {
            throw new IllegalArgumentException("Недостаточно аргументов. Пожалуйста, укажите URL и скорость.");
        }

        String url = args[0];
        int speed = Integer.parseInt(args[1]);

        if (speed <= 0) {
            throw new IllegalArgumentException("Скорость должна быть положительным целым числом.");
        }

        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
