package com.brian.speechtherapistapp.models;

import com.brian.speechtherapistapp.util.Const;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by brian on 27/01/18.
 */
public class ChildTest {

    private int id = 1;
    private String firstName = "brian";
    private String secondName = "Coveney";
    private String email =  "brian@email.com";

    @Test
    public void testBuilderWithDefaultValues() {

        print("valid child");

        Child childWithDefaultValues = Child.builder(id, firstName, secondName, email).build();

        print("success: default values for optional params");

        assertThat(Const.ParamsNames.CHILD_EMAIL, is(childWithDefaultValues.getPassword()));
        assertThat(Const.ParamsNames.CHILD_BIRTHDAY, is(childWithDefaultValues.getBirthday()));
    }

    @Test
    public void testBuilderWithOptionalArguments() {
        Child childWithOptionalArguments = Child.builder(id, firstName, secondName, email)
                .withBirthday(null)
                .build();

        print("success: default values for optional params");

        assertThat(Const.ParamsNames.CHILD_EMAIL, is(childWithOptionalArguments.getPassword()));
        assertThat(Const.ParamsNames.CHILD_BIRTHDAY, is(childWithOptionalArguments.getBirthday()));
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
        String expected = "telephone";
        child.setWord("telephone");

        assertEquals(expected, child.getWord());
    }

    @Test
    public void testToString() {
        Child child = Child.builder(id, firstName, secondName, email).build();
        child.setWord("telephone");

        StringBuilder sb = new StringBuilder("Child{");
        sb.append("id="+child.getId()+", ");
        sb.append("firstName='"+child.getFirstName()+"', ");
        sb.append("secondName='"+child.getSecondName()+"', ");
        sb.append("email='"+child.getEmail()+"', ");
        sb.append("birthday='"+child.getBirthday()+"', ");
        sb.append("password='"+child.getPassword()+"', ");
        sb.append("word="+child.getWord()+"}");

        assertEquals(sb.toString(), child.toString());
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