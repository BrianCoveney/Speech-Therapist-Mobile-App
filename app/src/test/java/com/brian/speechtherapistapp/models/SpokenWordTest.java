package com.brian.speechtherapistapp.models;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class SpokenWordTest {

    private SpokenWord spokenWord;
    private String wordInDictionary = "pider";
    private String wordInDictionary2 = "teffone";
    private String wordNotInDictionary = "bus";

    @Before
    public void setUp() throws Exception {
        spokenWord = new SpokenWord(wordInDictionary);
    }

    @Test
    public void hasMatch() throws Exception {
        assertTrue(spokenWord.hasMatch(wordInDictionary));
        assertFalse(spokenWord.hasMatch(wordNotInDictionary));
        assertEquals(1, spokenWord.getFrequency());

        assertTrue(spokenWord.hasMatch(wordInDictionary2));
        assertEquals(2, spokenWord.getFrequency());
    }

}