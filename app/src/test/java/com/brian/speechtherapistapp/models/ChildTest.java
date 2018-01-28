package com.brian.speechtherapistapp.models;

import com.brian.speechtherapistapp.util.Const;

import org.exparity.hamcrest.date.DateMatchers;
import org.junit.Test;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by brian on 27/01/18.
 */
public class ChildTest {

    private String firstName = "Brian";
    private String secondName = "Coveney";
    private String email =  "brian@email.com";

    @Test
    public void testBuilderWithDefaultValues() throws Exception {

        print("valid child");

        Child childWithDefaultValues = Child.builder(firstName, secondName, email).build();

        print("success: default values for optional params");

        assertThat(Const.ParamsNames.CHILD_GENDER, is(childWithDefaultValues.getGender()));
        assertThat(Const.ParamsNames.CHILD_SCHOOL, is(childWithDefaultValues.getSchool()));
        assertThat(Const.ParamsNames.CHILD_BIRTHDAY,
                DateMatchers.within(2, ChronoUnit.SECONDS, childWithDefaultValues.getBirthday()));
    }

    @Test
    public void testBuilderWithOptionalArguments() throws Exception {
        Child childWithOptionalArguments = Child.builder(firstName, secondName, email)
                .withBirthday(null)
                .withGender(null)
                .withSchool(null)
                .build();

        print("success: default values for optional params");

        assertThat(Const.ParamsNames.CHILD_GENDER, is(childWithOptionalArguments.getGender()));
        assertThat(Const.ParamsNames.CHILD_SCHOOL, is(childWithOptionalArguments.getSchool()));
        assertThat(Const.ParamsNames.CHILD_BIRTHDAY,
                DateMatchers.within(2, ChronoUnit.SECONDS, childWithOptionalArguments.getBirthday()));

    }

    @Test(expected = NullPointerException.class)
    public void testBuilderWithNullFirstName() {
        print("failure: firstName cannot be null)");

        Child.builder(null, secondName, email)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void testBuilderWithNullSecondName() {
        print("failure: secondName cannot be null");

        Child.builder(firstName, null, email)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void testBuilderWithNullEmail() {
        print("failure: email cannot be null");

        Child.builder(firstName, secondName, null)
                .build();
    }

    @Test
    public void testIsEmailValid() {
        Child child = Child.builder(firstName, secondName, email).build();


        List<String> validEmailList = Arrays.asList("joe@gmail.com", "joe@yahoo.co.uk",
                "joe.peters@aol.com", "joe_peters@hotmail.com");

        print("success: valid email");

        for (String email : validEmailList) {
            assertEquals(child.isEmailValid(email), true);
        }


        List<String> invalidEmailList = Arrays.asList(
                ".username@yahoo.com", "username@yahoo.com.", "username@yahoo..com",
                "username@yahoo.c", "username@yahoo.corporate", "@yahoo.com");

        print("failure: invalid email");

        for (String invalidEmail : invalidEmailList) {
            assertEquals(child.isEmailValid(invalidEmail), false);
        }
    }

    private void print(String message) {
        System.out.println("* " + message);
    }
}