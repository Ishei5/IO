package com.pankov.roadtoseniour.io.analyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileAnalyzerDefault implements FileAnalyzer {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Wrong arguments!");
        }

        String pathToFile = args[0];
        String searchWord = args[1];

        FileAnalyzerDefault fileAnalyzer = new FileAnalyzerDefault();
        Result result = fileAnalyzer.analyze(pathToFile, searchWord);

        System.out.println(result.count);
        for (String string : result.sentences) {
            System.out.println(string);
        }
    }

    public Result analyze(String pathToFile, String searchWord) throws IOException {
        String content = readContent(pathToFile);
        List<String> sentences = splitIntoSentences(content);
        List<String> filteredSentences = filter(sentences, searchWord);
        int count = countWordOccurrences(filteredSentences, searchWord);

        return new Result(filteredSentences, count);
    }

    public String readContent(String pathToFile) throws IOException {
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

    public List<String> splitIntoSentences(String content) {
        if (content.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> splitSentences = new ArrayList<>();
        for (String sentence : content.split(PUNCTUATION_MARKS)) {
            splitSentences.add(sentence.trim());
        }

        return splitSentences;
    }

    public List<String> filter(List<String> sentences, String searchWord) {
        List<String> filteredSentences = new ArrayList<>();
        for (String sentence : sentences) {
            if (sentence.toLowerCase().contains(searchWord.toLowerCase())) {
                filteredSentences.add(sentence);
            }
        }

        return filteredSentences;
    }

    public int countWordOccurrences(List<String> filteredSentences, String searchWord) {
        int count = 0;
        for (String sentence : filteredSentences) {
            int index = 0;
            while ((index = sentence.toLowerCase().indexOf(searchWord.toLowerCase(), index)) != -1) {
                index += searchWord.length();
                count++;
            }
        }
        return count;
    }
/*

    static class Result {

        private List<String> sentences;
        private int count;

        private Result(List<String> sentences, int count) {
            this.count = count;
            this.sentences = sentences;
        }
    }
*/
}


