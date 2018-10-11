package com.brian.speechtherapistapp.models;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.brian.speechtherapistapp.util.Const.CLUSTER_REDUCTION_WORDS_LIST;
import static com.brian.speechtherapistapp.util.Const.CORRECT_WORDS_LIST;
import static com.brian.speechtherapistapp.util.Const.FINAL_CONSONANT_DELETION_WORDS_LIST;
import static com.brian.speechtherapistapp.util.Const.GLIDING_OF_LIQUIDS_WORDS_LIST;
import static com.brian.speechtherapistapp.util.Const.WORD_IN_CLUSTER_REDUCTION;
import static com.brian.speechtherapistapp.util.Const.WORD_IN_CORRECT_WORDS;
import static com.brian.speechtherapistapp.util.Const.WORD_IN_FINAL_CONSONANT_DELETION;
import static com.brian.speechtherapistapp.util.Const.WORD_IN_GLIDING_OF_LIQUIDS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;


public class WordTest {

    private Word word;

    @Before
    public void setUp() {
        word = new Word();
        // Given the map has one of each gliding words
        for (String w : GLIDING_OF_LIQUIDS_WORDS_LIST) {
            word.updateMap(w);
        }
    }

    @Test
    public void testHasMatch() {

        // Test 'word' in correct word list
        assertTrue(word.hasMatch(WORD_IN_CORRECT_WORDS, CORRECT_WORDS_LIST));
        assertFalse(word.hasMatch(WORD_IN_CLUSTER_REDUCTION, CORRECT_WORDS_LIST));
        assertEquals(1, word.getFrequency());

        // Test frequency has incremented
        assertTrue(word.hasMatch(WORD_IN_CORRECT_WORDS, CORRECT_WORDS_LIST));
        assertThat("Freq should increment when a word is added", word.getFrequency(), is(2));

        // Test 'word' in gliding of liquids list
        assertTrue(word.hasMatch(WORD_IN_GLIDING_OF_LIQUIDS, GLIDING_OF_LIQUIDS_WORDS_LIST));
        assertFalse(word.hasMatch(WORD_IN_CORRECT_WORDS, GLIDING_OF_LIQUIDS_WORDS_LIST));

        // Test 'word' in cluster reduction
        assertTrue(word.hasMatch(WORD_IN_CLUSTER_REDUCTION, CLUSTER_REDUCTION_WORDS_LIST));
        assertFalse(word.hasMatch(WORD_IN_CORRECT_WORDS, CLUSTER_REDUCTION_WORDS_LIST));

        // Test 'word' in final consonant deletion
        assertTrue(word.hasMatch(WORD_IN_FINAL_CONSONANT_DELETION, FINAL_CONSONANT_DELETION_WORDS_LIST));
        assertFalse(word.hasMatch(WORD_IN_CORRECT_WORDS, CLUSTER_REDUCTION_WORDS_LIST));

    }

    @Test
    public void testMapHasCorrectEntries() {
        Map<String, Integer> map = word.getGlidingLiquidsMap();

        // Validate size and existence of the correct key value pairs
        assertThat(map.size(), equalTo(GLIDING_OF_LIQUIDS_WORDS_LIST.size()));
        assertThat(map, IsMapContaining.hasEntry("moo", 1));
        assertThat(map, not(IsMapContaining.hasEntry("brian", 1)));
    }

    @Test
    public void testUpdateMap() {
        // When a word was spoken again
        word.updateMap("moo");

        // Then the count should increment by one
        assertThat(word.getGlidingLiquidsMap(), IsMapContaining.hasEntry("moo", 2));
    }
}