package com.github.mtdrewski.GRAPH_moment.model.dataProcessor;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputProcessorTest {

    @Test
    public void correctInputRepresentation() {

        boolean flag = false;
        DataProcessor dataProcessor = new DataProcessor();
        try {
            dataProcessor.makeGraphFromInput(
                    "6\n" +
                            "8\n" +
                            "1 3\n" +
                            "1 5\n" +
                            "1 6\n" +
                            "2 5\n" +
                            "2 6\n" +
                            "3 4\n" +
                            "3 5\n" +
                            "5 6", DataProcessor.Type.EDGE_LIST);
        } catch (Exception e) {
            flag = true;
        } finally {
            assertFalse(flag);
            flag = false;
        }

        try {
            dataProcessor.makeGraphFromInput(
                    "0 1 1 0 0 0 0 0\n" +
                            "1 0 1 1 0 0 0 0\n" +
                            "1 1 0 0 1 0 0 1\n" +
                            "0 1 0 0 1 0 0 0\n" +
                            "0 0 1 1 0 1 0 0\n" +
                            "0 0 0 0 1 0 1 0\n" +
                            "0 0 0 0 0 1 0 1\n" +
                            "0 0 1 0 0 0 1 0", DataProcessor.Type.ADJACENCY_MATRIX);
        } catch (Exception e) {
            flag = true;
        } finally {
            assertFalse(flag);
            flag = false;
        }

        try {
            dataProcessor.makeGraphFromInput(
                    "1 0 0 0 1 1\n" +
                            "0 1 0 1 0 1\n" +
                            "0 0 1 1 1 0\n" +
                            "1 1 1 0 0 0", DataProcessor.Type.INCIDENCE_MATRIX);
        } catch (Exception e) {
            flag = true;
        } finally {
            assertFalse(flag);
            flag = false;
        }
    }

    @Test
    public void incorrectInputDetection() {

        boolean flag = false;
        DataProcessor dataProcessor = new DataProcessor();
        try {
            dataProcessor.makeGraphFromInput(
                    "5\n" +
                            "8\n" +
                            "1 3\n" +
                            "1 5\n" +
                            "1 6\n" +
                            "2 5\n" +
                            "2 6\n" +
                            "3 4\n" +
                            "3 5\n" +
                            "5 6", DataProcessor.Type.EDGE_LIST);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            flag = true;
        } finally {
            assertTrue(flag);
            flag = false;
        }

        try {
            dataProcessor.makeGraphFromInput(
                    "5\n" +
                            "8\n" +
                            "1 3\n" +
                            "1 5\n" +
                            "1 6\n" +
                            "2 5\n" +
                            "5 6", DataProcessor.Type.EDGE_LIST);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            flag = true;
        } finally {
            assertTrue(flag);
            flag = false;
        }

        try {
            dataProcessor.makeGraphFromInput(
                    null, null);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            flag = true;
        } finally {
            assertTrue(flag);
            flag = false;
        }

        try {
            dataProcessor.makeGraphFromInput(
                    "0 1 1 0 0 0 0 0\n" +
                            "1 0 1 1 0 0 0 0\n" +
                            "1 0\n" +
                            "0 0 0 0 1 0 1 0\n" +
                            "0 0 0 0 0 1 0 1\n" +
                            "0 0 1 0 0 0 1 0", DataProcessor.Type.ADJACENCY_MATRIX);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            flag = true;
        } finally {
            assertTrue(flag);
            flag = false;
        }

        try {
            dataProcessor.makeGraphFromInput(
                    "1 0 0 0 1 1\n" +
                            "00 1\n" +
                            "0 0 1 1 1 0\n" +
                            "1 1 ", DataProcessor.Type.INCIDENCE_MATRIX);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            flag = true;
        } finally {
            assertTrue(flag);
            flag = false;
        }

        try {
            dataProcessor.makeGraphFromInput(
                    "6\n" +
                            "8\n" +
                            "1 3\n" +
                            "1 5\n" +
                            "1 6\n" +
                            "2 5\n" +
                            "2 6\n" +
                            "3 4\n" +
                            "3 5\n" +
                            "5 6", DataProcessor.Type.ADJACENCY_MATRIX);
        } catch (DataProcessor.IncorrectInputFormatException e) {
            flag = true;
        } finally {
            assertTrue(flag);
            flag = false;
        }
    }

}