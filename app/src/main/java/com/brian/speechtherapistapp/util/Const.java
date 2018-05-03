package com.brian.speechtherapistapp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public final class
Const {


    public static final List<String> GLIDING_OF_LIQUIDS_WORDS_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    "wed",     // red
                    "yeg",     // leg
                    "wook",    // look
                    "wabbit",  // rabbit
                    "one",     // run
                    "wadder",  // ladder
                    "pway",    // leaf
                    "cawy",    // carry
                    "yucky",   // lucky
                    "wat"      // rock
            )
    );

    public static final List<String> CLUSTER_REDUCTION_WORDS_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    "fren",
                    "tuck",
                    "side",
                    "bown",
                    "spat",
                    "sool",
                    "coal",
                    "pider",
                    "poon",
                    "sate"
            )
    );

    public static final List<String> FINAL_CONSONANT_DELETION_WORDS_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    "daw",
                    "sop",
                    "pi",
                    "cuh",
                    "dah",
                    "ho",
                    "ba",
                    "moo",
                    "weigh",
                    "no"
            )
    );

    public static final List<String> CORRECT_WORDS_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    "red",
                    "leg",
                    "look",
                    "rabbit",
                    "run",
                    "ladder",
                    "play",
                    "carry",
                    "lucky",
                    "rock",
                    "friend",
                    "truck",
                    "slide",
                    "brown",
                    "spot",
                    "school",
                    "cold",
                    "spider",
                    "spoon",
                    "skate",
                    "dog",
                    "soap",
                    "pig",
                    "cup",
                    "home",
                    "bat",
                    "moon",
                    "weight",
                    "nose"
            )
    );

    public static final String WORD_IN_CORRECT_WORDS = "red";
    public static final String WORD_IN_GLIDING_OF_LIQUIDS = "wabbit";
    public static final String WORD_IN_CLUSTER_REDUCTION = "tuck";
    public static final String WORD_IN_FINAL_CONSONANT_DELETION = "dah";


    public static final class ParamsNames {
        public static final int CHILD_ID = 0;
        public static final String CHILD_GENDER = "male";
        public static final String CHILD_SCHOOL = "Scoil Barra Naofa";
        public static final String CHILD_PASSWORD = "password";
        public static final String CHILD_EMAIL = "name@email.com";
        public static final String CHILD_FIRST_NAME = "firstname";
        public static final String CHILD_SECOND_NAME = "secondtname";
        public static final String CHILD_BIRTHDAY = setDefaultBirthday();
    }

    public static final String NULL_PARAMETER = "ERROR_NULL_PARAMETER";
    public static final String VALID_EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";


    public static String setDefaultBirthday() {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 1, 18);
        DateFormat dateFormatDay = new SimpleDateFormat("F EEEE, dd/MM/yyyy");
        String date = dateFormatDay.format(cal.getTime());
        return date;
    }


}
