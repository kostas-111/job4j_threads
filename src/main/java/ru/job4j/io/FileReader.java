package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

/*
класс отвечает за чтение содержимого файла
 */
public class FileReader {
    private final File file;

    public FileReader(File file) {
        this.file = file;
    }

    public String content(Predicate<Character> filter) throws IOException {
        if (!file.exists() || !file.isFile()) {
            throw new IOException("Файла по указанному пути не существует: " + file.getAbsolutePath());
        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            StringBuilder output = new StringBuilder();
            int data;
            while ((data = reader.read()) != -1) {
                char ch = (char) data;
                if (filter.test(ch)) {
                    output.append(ch);
                }
            }
            return output.toString();
        }
    }

    public String getContentWithoutUnicode() throws IOException {
        return content(ch -> ch < 0x80);
    }
}
