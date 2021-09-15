package com.pankov.roadtoseniour.io.analyzer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileAnalyzerTest {

    private static final List<String> SPLITTED_SENTENCES_WITHOUT_SEARCH_WORD = new ArrayList<>();
    private static final List<String> SPLITTED_SENTENCES = new ArrayList<>();

    final static String CONTENT = "Duck? But who duck should come to meet her on the bridge, not her snow-white Duck at all, " +
            "but her twelve sons! Sentence without search word. Before the mother could cry out to them to stop, "
            + "the witch of the trolls, who turned out to be a wicked witch after all. "
            + "Threw a spell upon them all and turned them into twelve ducks!";

    FileAnalyzer fileAnalyzer = new FileAnalyzerStream();

    @BeforeAll
    public static void before() throws IOException {
        SPLITTED_SENTENCES_WITHOUT_SEARCH_WORD.add("Sentence!");
        SPLITTED_SENTENCES_WITHOUT_SEARCH_WORD.add("Sentence without search word.");

        SPLITTED_SENTENCES.add("Sentence!");
        SPLITTED_SENTENCES.add("Duck?");
        SPLITTED_SENTENCES.add("Sentence without search word.");
        SPLITTED_SENTENCES.add("Threw a spell upon them all and turned them into twelve ducks!");
    }

    @Test
    @DisplayName("Test split content")
    public void testSplitContent() {
        List<String> sentences = fileAnalyzer.splitIntoSentences(CONTENT);
        assertEquals(5, sentences.size());
    }

    @Test
    @DisplayName("Test split content with one sentence")
    public void testSplitContentWithOneSentence() {
        String content = "Duck! Dog?";
        List<String> sentences = fileAnalyzer.splitIntoSentences(content);
        List<String> expectedList = new ArrayList<String>() {{
            add("Duck!");
            add("Dog?");
        }};

        assertEquals(expectedList, sentences);
        assertEquals(2, sentences.size());
    }

    @Test
    @DisplayName("Test split when content is empty")
    public void testSplitWhenContentIsEmpty() {
        String content = "";
        List<String> sentences = fileAnalyzer.splitIntoSentences(content);
        assertEquals(0, sentences.size());
    }

    @Test
    @DisplayName("Test filter on empty content")
    public void testFilterOnEmptyContent() {
        List<String> filteredSentences = fileAnalyzer.filter(fileAnalyzer.splitIntoSentences(""),
                "duck");
        assertEquals(0, filteredSentences.size());
    }

    @Test
    @DisplayName("Test filter when content does not have search word")
    public void testFilterWhenContentDoesNotHaveSearchWord() {
        List<String> filteredSentences = fileAnalyzer.filter(SPLITTED_SENTENCES_WITHOUT_SEARCH_WORD, "duck");

        assertEquals(0, filteredSentences.size());
    }

    @Test
    @DisplayName("Test filter")
    public void testFilter() {
        List<String> expectedList = new ArrayList<String>() {{
            add("Duck?");
            add("Threw a spell upon them all and turned them into twelve ducks!");
        }};

        List<String> filteredSentences = fileAnalyzer.filter(SPLITTED_SENTENCES, "duck");

        assertEquals(2, filteredSentences.size());
        assertEquals(expectedList, filteredSentences);
    }

    @Test
    @DisplayName("Test count of occurrences when no search word")
    public void testContWordOccurrencesWhenNoSearchWord() {
        assertEquals(0, fileAnalyzer.countWordOccurrences(SPLITTED_SENTENCES_WITHOUT_SEARCH_WORD,
                "duck"));
    }

    @Test
    @DisplayName("Test count word occurrences")
    public void testContWordOccurrences() {
        assertEquals(2, fileAnalyzer.countWordOccurrences(SPLITTED_SENTENCES,
                "duck"));
    }

    @Test
    @DisplayName("Test count of occurrences when one sentence contains two search words")
    public void testContWordOccurrencesWhenOneSentenceContainsTwoSearchWords() {
        List<String> sentence = new ArrayList<>();
        sentence.add("Duck twice in one sentence ducks?");
        fileAnalyzer.countWordOccurrences(sentence,
                "duck");
        assertEquals(2, fileAnalyzer.countWordOccurrences(sentence,
                "duck"));
    }
}
