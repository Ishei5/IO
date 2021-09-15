package com.pankov.roadtoseniour.io.analyzer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileAnalyzerITest {

    final static String CONTENT = "Duck? But who duck should come to meet her on the bridge, not her snow-white Duck at all, " +
            "but her twelve sons! Sentence without search word. Before the mother could cry out to them to stop, "
            + "the witch of the trolls, who turned out to be a wicked witch after all. "
            + "Threw a spell upon them all and turned them into twelve ducks!";

    final static String RUSSIAN_CONTENT = "Сплошь и рядом вместо конкретных и точных для определенного случая слов, подходящих "
            + "именно для Данного случая синонимов употребляются одни и те же излюбленные слова, создающие речевой стандарт";

    FileAnalyzer fileAnalyzer = new FileAnalyzerStream();

    @BeforeAll
    public static void before() throws IOException {

        File testFile = new File("src/test/resources", "testFile.txt");
        testFile.createNewFile();

        File testRussianFile = new File("src/test/resources", "testRussianFile.txt");
        testRussianFile.createNewFile();

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                new FileOutputStream(testFile.getAbsoluteFile()))) {
            byte[] buffer = CONTENT.getBytes();
            bufferedOutputStream.write(buffer);
        }

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                        new FileOutputStream(testRussianFile.getAbsoluteFile()), StandardCharsets.UTF_8)) {
            outputStreamWriter.write(RUSSIAN_CONTENT);
        }
    }


    @Test
    @DisplayName("Test read file")
    public void testReadFile() throws IOException {
        String fileContent = fileAnalyzer.readContent("src/test/resources/testFile.txt");

        assertEquals(CONTENT, fileContent);
    }

    @Test
    @DisplayName("Test read file on russian")
    public void testReadFileOnRussian() throws IOException {
        String fileContent = fileAnalyzer.readContent("src/test/resources/testRussianFile.txt");

        assertEquals(RUSSIAN_CONTENT, fileContent);
    }

    @AfterAll
    public static void after() throws IOException{
        File testFile = new File("src/test/resources", "testFile.txt");
        if (!testFile.delete()) {
            throw new IOException("Error during removing");
        }

        File testRussianFile = new File("src/test/resources", "testRussianFile.txt");
        if (!testRussianFile.delete()) {
            throw new IOException("Error during removing");
        }
    }
}
