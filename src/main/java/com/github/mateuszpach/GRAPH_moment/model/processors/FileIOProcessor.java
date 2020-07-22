package com.github.mateuszpach.GRAPH_moment.model.processors;

import java.io.*;

public class FileIOProcessor {

    public static void save(String source, File file) throws IllegalArgumentException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(source, 0, source.length());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static String read(File file) throws IllegalArgumentException {
        StringBuilder destination = new StringBuilder();
        String line = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((line = bufferedReader.readLine()) != null) {
                destination.append(line + '\n');
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        return destination.toString();
    }

}