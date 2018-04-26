package com.brian.speechtherapistapp.models;

import com.brian.speechtherapistapp.util.Const;

import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by brian on 26/01/18.
 */

public class Child {

    private Address address;

    // Required fields
    private ObjectId childObjectId;
    private int id;
    private String firstName;
    private String secondName;
    private String email;
    private String password;

    // Optional fields
    private String birthday;
    private String gender;
    private String school;
    private String word;

    public static ChildBuilder createChild() {
        return new ChildBuilder();
    }

//    public Child() {}

    private Child() {
        birthday = Const.ParamsNames.CHILD_BIRTHDAY;
        gender = Const.ParamsNames.CHILD_GENDER;
        school = Const.ParamsNames.CHILD_SCHOOL;
        password = Const.ParamsNames.CHILD_EMAIL;
        childObjectId = new ObjectId();
    }

    public static ChildBuilder builder(int id, String firstName, String secondName, String email) {
        return new ChildBuilder(id, firstName, secondName, email);
    }

    public int getId() {
        return id;
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

    public String getPassword() { return password; }

    public String getBirthday() { return birthday; }

    public String getGender() {
        return gender;
    }

    public String getSchool() {
        return school;
    }

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

    public ObjectId getChildObjectId() {
        return childObjectId;
    }

    public void setChildObjectId(ObjectId childObjectId) {
        this.childObjectId = childObjectId;
    }

    boolean isEmailValid(String eMail) {
        if(eMail != null) {
            Pattern pattern = Pattern.compile(Const.VALID_EMAIL_REGEX);
            Matcher matcher = pattern.matcher(eMail);
            return matcher.matches();
        }

        return false;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return "Child{" + "\n" +
                "firstName = '" + firstName + '\'' + "\n" +
                "secondName = '" + secondName + '\'' + "\n" +
                "email = '" + email + '\'' + "\n" +
                "birthday = " + birthday + "\n" +
                "gender = '" + gender + '\'' + "\n" +
                "school = '" + school + '\'' + "\n" +
                ",Address = " + address + "\n" +
                '}';
    }



    public static class ChildBuilder {

        private Child child;
        private Child childNotSet;
        private static AtomicInteger next_id = new AtomicInteger(-1);

        public ChildBuilder() {
            childNotSet = new Child();
        }

        public ChildBuilder(int id, String firstName, String secondName, String email) {

            validateRequiredFields(id, firstName, secondName, email);

            child = new Child();
            child.id = next_id.incrementAndGet();
            child.email = email;
            child.secondName = secondName;
            child.firstName = firstName;

        }

        public ChildBuilder withWordSaid(String word) {
            if (word != null) {
                child.word = word;
            }
            return this;
        }

        public ChildBuilder withBirthday(String birthday) {
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

        public ChildBuilder withPassword(String password) {
            if (password != null) {
                child.password = password;
            }
            return this;
        }

        public ChildBuilder withChildObjectId(ObjectId childObjectId) {
            if (childObjectId != null) {
                child.childObjectId = childObjectId;
            }
            return this;
        }

        public Child build() {
            return child;
        }

        public Child create() { return childNotSet; }


        private void validateRequiredFields(Object... objects) {
            for (Object object : objects) {
                Objects.requireNonNull(object, "Object's required values cannot be null");
            }
        }
    }
}
