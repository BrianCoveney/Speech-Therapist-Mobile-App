package com.brian.speechtherapistapp.models;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.brian.speechtherapistapp.util.Const.CORRECT_WORDS_LIST;
import static com.brian.speechtherapistapp.util.Const.ParamsNames;
import static com.brian.speechtherapistapp.util.Const.ParamsNames.CHILD_EMAIL;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ChildTest {

    private int id = 1;
    private String firstName = "brian";
    private String secondName = "Coveney";
    private String email =  "name@email.com";

    @Test
    public void testBuilderWithDefaultValues() {

        print("valid child");

        Child childWithDefaultValues = Child.builder(id, firstName, secondName, email).build();

        print("success: default values for optional params");

        assertThat(CHILD_EMAIL, is(childWithDefaultValues.getPassword()));
        assertThat(ParamsNames.CHILD_BIRTHDAY, is(childWithDefaultValues.getBirthday()));
    }

    @Test
    public void testBuilderWithOptionalArguments() {
        Child childWithOptionalArguments = Child.builder(id, firstName, secondName, email)
                .withBirthday(null)
                .build();

        print("success: default values for optional params");

        assertThat(CHILD_EMAIL, is(childWithOptionalArguments.getPassword()));
        assertThat(ParamsNames.CHILD_BIRTHDAY, is(childWithOptionalArguments.getBirthday()));
    }

    @Test(expected = NullPointerException.class)
    public void testBuilderWithNullFirstName() {
        print("failure: firstName cannot be null)");

        Child.builder(id,  null, secondName, email)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void testBuilderWithNullSecondName() {
        print("failure: secondName cannot be null");

        Child.builder(id, firstName, null, email)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void testBuilderWithNullEmail() {
        print("failure: email cannot be null");

        Child.builder(id, firstName, secondName, null)
                .build();
    }

    @Test
    public void testIsEmailValid() {
        Child child = Child.builder(id, firstName, secondName, email).build();

        print("success: valid email");

        List<String> validEmailList = Arrays.asList("joe@gmail.com", "joe@yahoo.co.uk",
                "joe.peters@aol.com", "joe_peters@hotmail.com");
        for (String email : validEmailList) {
            assertEquals(child.isEmailValid(email), true);
        }

        print("failure: invalid email");

        List<String> invalidEmailList = Arrays.asList(
                ".username@yahoo.com", "username@yahoo.com.", "username@yahoo..com",
                "username@yahoo.c", "username@yahoo.corporate", "@yahoo.com");
        for (String invalidEmail : invalidEmailList) {
            assertEquals(child.isEmailValid(invalidEmail), false);
        }
    }

    @Test
    public void testChildWithWord() {
        Child child = Child.builder(id, firstName, secondName, email).build();
        String expected = "red";
        child.setWordName("red");
        assertEquals(expected, child.getWordName());

        Word word = new Word(child.getWordName());
        assertTrue(word.hasMatch(word.getName(), CORRECT_WORDS_LIST));
        assertEquals(1, word.getFrequency());
    }

    @Test
    public void testToString() {
        Child child = Child.builder(id, firstName, secondName, email).build();
        child.setWordName("telephone");

        StringBuilder sb = new StringBuilder("Child{");
        sb.append("id="+child.getId()+", ");
        sb.append("firstName='"+child.getFirstName()+"', ");
        sb.append("secondName='"+child.getSecondName()+"', ");
        sb.append("email='"+child.getEmail()+"', ");
        sb.append("birthday='"+child.getBirthday()+"', ");
        sb.append("password='"+child.getPassword()+"', ");
        sb.append("word="+child.getWordName()+"}");

        assertEquals(sb.toString(), child.toString());
    }

    @Test
    public void testBuilderWithEmailOnly() {
        Child child = Child.builder(CHILD_EMAIL)
                .build();

        print("success: default values for optional params");

        assertThat(CHILD_EMAIL, is(child.getEmail()));
    }

    @BeforeClass
    public static void printClassHeadder() {
        print("[=============" + Child.class.getCanonicalName() + "=============]");
    }

    @AfterClass
    public static void printTestClassFooter() {
        print("[=============" + Child.class.getCanonicalName() + " completed");
    }

    protected static void print(String message) {
        System.out.println("* " + message);
    }
}