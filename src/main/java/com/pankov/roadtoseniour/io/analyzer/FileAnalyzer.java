package com.pankov.roadtoseniour.io.analyzer;

import java.io.*;

public class FileAnalyzer {

    private String pathToFile;
    private String searchWord;
    private String fileContent;

    final String PUNCTUATION_MARKS = "(?<=\\.)|(?<=!)|(?<=\\?)";

    public FileAnalyzer(String pathToFile, String searchWord) throws FileNotFoundException {
        this.pathToFile = pathToFile;
        this.searchWord = searchWord;

        if (!(new File(pathToFile).exists())) {
            throw new FileNotFoundException("The path to file is not valid");
        }
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public String getSearchWord() {
        return searchWord;
    }

    private void readFile() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(
                new FileInputStream(pathToFile))) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = bufferedInputStream.read(buffer)) > 0) {
                stringBuilder.append(new String(buffer, 0, length));
            }

        }

        fileContent = stringBuilder.toString();
    }

    public int getCountOfOccurrences() throws IOException {
        readFile();
        int count = 0;
        int index = 0;
        while ((index = fileContent.toLowerCase().indexOf(searchWord, index)) != -1) {
            index += searchWord.length();
            count++;
        }
        return count;
    }

    public void printFilteredSentences() {
        for (String line : fileContent.split(PUNCTUATION_MARKS)) {
            if (line.toLowerCase().contains(searchWord)) {
                System.out.println(line.trim());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Wrong arguments!");
        }

        String pathToFile = args[0];
        String searchWord = args[1].toLowerCase();

        FileAnalyzer fileAnalyzer = new FileAnalyzer(pathToFile, searchWord);

        System.out.println(String.format("Count of occurrences of \"%s\" - %d",
                searchWord, fileAnalyzer.getCountOfOccurrences()));

        fileAnalyzer.printFilteredSentences();
    }
}
