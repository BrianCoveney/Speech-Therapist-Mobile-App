package com.brian.speechtherapistapp.models;

import com.brian.speechtherapistapp.util.Const;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by brian on 26/01/18.
 */

public class Child {

    private Address address;

    // Required fields
    private String firstName;
    private String secondName;
    private String email;

    // Optional fields
    private Date birthday;
    private String gender;
    private String school;


    private Child() {
        birthday = Const.setDefaultBirthday();
        gender = Const.ParamsNames.CHILD_GENDER;
        school = Const.ParamsNames.CHILD_SCHOOL;
    }


    public static ChildBuilder builder(String firstName, String secondName, String email) {
        return new ChildBuilder(firstName, secondName, email);
    }

    public void changeAddress(final Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getSchool() {
        return school;
    }

    boolean isEmailValid(String eMail) {
        if(eMail != null) {
            Pattern pattern = Pattern.compile(Const.VALID_EMAIL_REGEX);
            Matcher matcher = pattern.matcher(eMail);
            return matcher.matches();
        }

        return false;
    }


    @Override
    public String toString() {
        return "Child{" + "\n" +
                "firstName = '" + firstName + '\'' + "\n" +
                "secondName = '" + secondName + '\'' + "\n" +
                "email = '" + email + '\'' + "\n" +
                "birthday = " + Const.formatDate(birthday) + "\n" +
                "gender = '" + gender + '\'' + "\n" +
                "school = '" + school + '\'' + "\n" +
                ",Address = " + address + "\n" +
                '}';
    }

    public static class ChildBuilder {

        private final Child child;

        public ChildBuilder(String firstName, String secondName, String email) {

            validateRequiredFields(firstName, secondName, email);

            child = new Child();
            child.email = email;
            child.secondName = secondName;
            child.firstName = firstName;
        }

        public ChildBuilder withBirthday(Date birthday) {
            if (birthday != null) {
                child.birthday = birthday;
            }
            return this;
        }

        public ChildBuilder withGender(String gender) {
            if (gender != null) {
                child.gender = gender;
            }
            return this;
        }

        public ChildBuilder withSchool(String school) {
            if (school != null) {
                child.school = school;
            }
            return this;
        }

        public Child build() {
            return child;
        }


        private void validateRequiredFields(Object... objects) {
            for (Object object : objects) {
                if (object == null) {
                    throw new NullPointerException("Object's required values cannot be null");
                }
            }
        }
    }
}
