package com.brian.speechtherapistapp.models;

import com.brian.speechtherapistapp.util.Const;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WordTest {

    private Word word;
    private List<String> wordList = Const.LIST_CORRECT_WORDS;
    private String wordInDictionary = "run";
    private String wordNotInDictionary = "pider";

    @Before
    public void setUp() throws Exception {
        word = new Word();
    }

    @Test
    public void hasMatch() throws Exception {
        assertTrue(word.hasMatch(wordInDictionary, wordList));
        assertFalse(word.hasMatch(wordNotInDictionary, wordList));
        assertEquals(1, word.getFrequency());

        // Add word again
        assertTrue(word.hasMatch(wordInDictionary, wordList));
        // Then frequency should be incremented
        assertThat(word.getFrequency(), is(2));
    }
}