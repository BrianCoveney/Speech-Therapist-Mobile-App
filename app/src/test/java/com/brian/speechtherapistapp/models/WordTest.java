package com.brian.speechtherapistapp.models;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class WordTest {

    private Word word;
    private String wordInDictionary = "pider";
    private String wordInDictionary2 = "teffone";
    private String wordNotInDictionary = "bus";

    @Before
    public void setUp() throws Exception {
        word = new Word();
    }

    @Test
    public void hasMatch() throws Exception {
        assertTrue(word.hasMatch(wordInDictionary));
        assertFalse(word.hasMatch(wordNotInDictionary));
        assertEquals(1, word.getFrequency());

        assertTrue(word.hasMatch(wordInDictionary2));
        assertEquals(2, word.getFrequency());
    }

}