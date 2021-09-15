package com.pankov.roadtoseniour.io.analyzer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public interface FileAnalyzer {

    String PUNCTUATION_MARKS = "(?<=\\.)|(?<=!)|(?<=\\?)";

    Result analyze(String pathToFile, String searchWord) throws IOException;

    default String readContent(String pathToFile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(pathToFile)))) {
            char[] buffer = new char[3];
            int length;
//            TODO! файл на русском + buffer[3]
            while ((length = bufferedReader.read(buffer)) > 0) {
                stringBuilder.append(new String(buffer, 0, length));
            }
        }

        return stringBuilder.toString();
    }

    List<String> splitIntoSentences(String content);

    List<String> filter(List<String> sentences, String searchWord);

    int countWordOccurrences(List<String> filteredSentences, String searchWord);

    class Result {
        List<String> sentences;
        int count;

        Result(List<String> sentences, int count) {
            this.count = count;
            this.sentences = sentences;
        }
    }
}
