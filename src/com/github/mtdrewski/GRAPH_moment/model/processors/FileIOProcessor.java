package com.github.mtdrewski.GRAPH_moment.model.processors;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

//TODO: (IMPORTANT) close buffers in case of exception
public class FileIOProcessor {
    public static boolean saveWithChoice(Stage stage, String source) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile == null) return false;
        Charset charset = Charset.forName("UTF-8");
        BufferedWriter writer = Files.newBufferedWriter(selectedFile.toPath(), charset);
        writer.write(source, 0, source.length());
        writer.close();
        return true;
    }
    public static String pullWithChoice(Stage stage) throws IOException, CancelledException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile == null) throw new CancelledException();
        Charset charset = Charset.forName("UTF-8");
        BufferedReader reader = Files.newBufferedReader(selectedFile.toPath(), charset);
        StringBuilder destination = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            destination.append(line + '\n');
        }
        reader.close();
        return destination.toString();
    }

    public static class CancelledException extends Exception {
    }
}
