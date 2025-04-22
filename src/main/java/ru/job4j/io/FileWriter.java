package ru.job4j.io;

import java.io.*;

/*
Класс отвечает за запись содержимого в файл
 */
public class FileWriter {
    private final File file;

    public FileWriter(File file) {
        this.file = file;
    }

    public void saveContent(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file)))) {
            writer.write(content);
        }
    }
}
