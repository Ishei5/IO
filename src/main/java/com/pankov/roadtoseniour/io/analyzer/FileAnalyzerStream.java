package com.pankov.roadtoseniour.io.analyzer;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileAnalyzerStream implements FileAnalyzer {

    @Override
    public Result analyze(String pathToFile, String searchWord) throws IOException {
        List<String> filteredSentences = Stream.of(readContent(pathToFile))
                .map(this::splitIntoSentences)
                .map(sentence -> filter(sentence, searchWord))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        int count = countWordOccurrences(filteredSentences, searchWord);

        return new Result(filteredSentences, count);
    }

    @Override
    public List<String> splitIntoSentences(String content) {
        if (content.isEmpty()) {
            return Collections.emptyList();
        }

        return Stream.of(content.split(PUNCTUATION_MARKS))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> filter(List<String> sentences, String searchWord) {
        return sentences.stream()
                .filter(sentence -> sentence.toLowerCase().contains(searchWord))
                .collect(Collectors.toList());
    }

    @Override
    public int countWordOccurrences(List<String> filteredSentences, String searchWord) {
        return filteredSentences.stream()
                .mapToInt(sentence -> {
                    int index = 0;
                    int count = 0;
                    while ((index = sentence.toLowerCase().indexOf(searchWord.toLowerCase(), index)) != -1) {
                        index += searchWord.length();
                        count++;
                    }
                    return count;
                }).sum();
    }
}
