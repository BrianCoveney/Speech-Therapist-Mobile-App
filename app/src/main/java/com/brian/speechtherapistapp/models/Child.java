package com.brian.speechtherapistapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.brian.speechtherapistapp.util.Const;
import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Child implements Parcelable {


    private ObjectId id;

    // Required fields
    private String firstName;
    private String secondName;
    private String email;

    // Optional fields
    private String birthday;
    private String password;
    private Word word;

    // Avoids direct instantiation
    private Child() {
        birthday = Const.ParamsNames.CHILD_BIRTHDAY;
        password = Const.ParamsNames.CHILD_EMAIL;
        this.word = new Word();
    }

    public static ChildBuilder builder(String email) {
        return new ChildBuilder(email);
    }

    public static ChildBuilder builder(String firstName, String secondName, String email) {
        return new ChildBuilder(firstName, secondName, email);
    }

    public ObjectId getId() {
        return id;
    }

    public String getWordName() {
        return word.getName();
    }

    public Map<String, Integer> getWordGlidingLiquidsMap() {
        return word.getGlidingLiquidsMap();
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

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    boolean isEmailValid(String eMail) {
        if (eMail != null) {
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

    // Parcelable
    public static final Parcelable.Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            String firstName = in.readString();
            String secondName = in.readString();
            String email = in.readString();
            String word = in.readString();

            return Child.builder(firstName, secondName, email)
                    .withWord(word)
                    .build();
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
    public void writeToParcel(Parcel dest, int i) {
        String theWord = word.getName();
        dest.writeString(firstName);
        dest.writeString(secondName);
        dest.writeString(email);
        dest.writeString(theWord);
    }
    // end Parcelable


    public static class ChildBuilder {
        private Child child;

        public ChildBuilder(String email) {
            child = new Child();
            child.email = email;
        }

        public ChildBuilder(String firstName, String secondName, String email) {
            validateRequiredFields(firstName, secondName, email);
            child = new Child();
            child.email = email;
            child.secondName = secondName;
            child.firstName = firstName;
        }

        @SerializedName("word")
        public ChildBuilder withWord(String word) {
            if (word != null) {
                child.word.setName(word);
            }
            return this;
        }

        public ChildBuilder withGlidingWordMap(Map<String, Integer> glidingWords) {
            if (glidingWords != null) {
                child.word.setGlidingLiquidsMap(glidingWords);
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

        public ChildBuilder withId(ObjectId id) {
            if (id != null) {
                child.id = id;
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
