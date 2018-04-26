package com.brian.speechtherapistapp.models;

import com.brian.speechtherapistapp.util.Const;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Child {

    // Required fields
    private int id;
    private String firstName;
    private String secondName;
    private String email;

    // Optional fields
    private String birthday;
    private String password;


    private Child() {
        birthday = Const.ParamsNames.CHILD_BIRTHDAY;
        password = Const.ParamsNames.CHILD_EMAIL;
    }

    public static ChildBuilder builder(int id, String firstName, String secondName, String email) {
        return new ChildBuilder(id, firstName, secondName, email);
    }

    public int getId() {
        return id;
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

    public String getPassword() { return password; }

    public String getBirthday() { return birthday; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setId(int id) { this.id = id; }

    boolean isEmailValid(String eMail) {
        if(eMail != null) {
            Pattern pattern = Pattern.compile(Const.VALID_EMAIL_REGEX);
            Matcher matcher = pattern.matcher(eMail);
            return matcher.matches();
        }

        return false;
    }


    public static class ChildBuilder {
        private Child child;
        private Child childNotSet;
        private static AtomicInteger next_id = new AtomicInteger(-1);

        public ChildBuilder(int id, String firstName, String secondName, String email) {

            validateRequiredFields(id, firstName, secondName, email);

            child = new Child();
            child.id = next_id.incrementAndGet();
            child.email = email;
            child.secondName = secondName;
            child.firstName = firstName;

        }

        public ChildBuilder withBirthday(String birthday) {
            if (birthday != null) {
                child.birthday = birthday;
            }
            return this;
        }

        public ChildBuilder withPassword(String password) {
            if (password != null) {
                child.password = password;
            }
            return this;
        }

        public Child build() {
            return child;
        }

        private void validateRequiredFields(Object... objects) {
            for (Object object : objects) {
                Objects.requireNonNull(object, "Object's required values cannot be null");
            }
        }
    }
}
