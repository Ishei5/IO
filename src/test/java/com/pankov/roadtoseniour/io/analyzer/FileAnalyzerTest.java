package com.pankov.roadtoseniour.io.analyzer;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileAnalyzerTest {

    public FileAnalyzerTest() throws FileNotFoundException {
    }

    private static final String PATH_TO_RESOURCES = "src\\test\\resources\\";

    private static final String SEARCH_WORD = "duck";

    private FileAnalyzer fileAnalyzer = new FileAnalyzer(PATH_TO_RESOURCES + "test.txt", SEARCH_WORD);


    @Test
    public void testGetCountOfOccurrencesAndReadFile() throws IOException {
        int expectedCount = 5;
        assertEquals(expectedCount, fileAnalyzer.getCountOfOccurrences());
    }

    @Test
    public void testReadFileException() {
        assertThrows(FileNotFoundException.class, () -> new FileAnalyzer(
                PATH_TO_RESOURCES + "file.txt", SEARCH_WORD));
    }

    @Test
    public void testGetCountOfOccurrencesOnEmptyFile() throws IOException {
        FileAnalyzer fileAnalyzer = new FileAnalyzer(PATH_TO_RESOURCES + "emptyFile.txt", SEARCH_WORD);
        assertEquals(0, fileAnalyzer.getCountOfOccurrences());
    }

}
