package com.brian.speechtherapistapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.brian.speechtherapistapp.util.Const;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Child implements Parcelable{

    // Required fields
    private int id;
    private String firstName;
    private String secondName;
    private String email;

    // Optional fields
    private String birthday;
    private String password;

    // Composition
    private Word word;

    private Child() {
        birthday = Const.ParamsNames.CHILD_BIRTHDAY;
        password = Const.ParamsNames.CHILD_EMAIL;
        this.word = new Word();
    }

    public static ChildBuilder builder(int id, String firstName, String secondName, String email) {
        return new ChildBuilder(id, firstName, secondName, email);
    }

    public String getWordName() {
        return word.getName();
    }

    public void setWordName(String word) {
        this.word.setName(word);
    }

    public int getWordFreq() {
        return word.getFrequency();
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

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", password='" + password + '\'' +
                ", word=" + word +
                '}';
    }

    public static final Parcelable.Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel parcel) {
            int id = parcel.readInt();
            String firstName = parcel.readString();
            String secondName = parcel.readString();
            String email = parcel.readString();
            String word = parcel.readString();

            Child child = Child.builder(id, firstName, secondName, email).withWord(word)
                    .build();
            return child;
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(firstName);
        parcel.writeString(secondName);
        parcel.writeString(email);
        parcel.writeString(word.getName());
    }

    public static class ChildBuilder {
        private Child child;

        public ChildBuilder(int id, String firstName, String secondName, String email) {

            validateRequiredFields(id, firstName, secondName, email);

            child = new Child();
            child.id = id;

            child.email = email;
            child.secondName = secondName;
            child.firstName = firstName;
        }

        public ChildBuilder withWordName(Word word) {
            if (word != null) {
                child.word = word;
            }
            return this;
        }

            public ChildBuilder withWord(String word) {
            if (word != null) {
                child.word.setName(word);
            }
            return this;
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
