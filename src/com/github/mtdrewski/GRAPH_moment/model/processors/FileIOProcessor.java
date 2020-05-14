package com.github.mtdrewski.GRAPH_moment.model.processors;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;


public class FileIOProcessor {
    public static boolean saveAs(Stage stage, String text) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile == null) return false;
        Charset charset = Charset.forName("UTF-8");
        BufferedWriter writer = Files.newBufferedWriter(selectedFile.toPath(), charset);
        writer.write(text, 0, text.length());
        writer.close();
        return true;
    }
}
