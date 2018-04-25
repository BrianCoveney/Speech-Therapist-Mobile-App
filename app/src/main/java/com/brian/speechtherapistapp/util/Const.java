package com.brian.speechtherapistapp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public final class
Const {

    public static final class PhonologicalProcesses {
        public static final String TEFFONE = "teffone";
        public static final String PIDER = "pider";
        public static final String BU = "bu";
        public static final String CA = "ca";
        public static final String DAH = "dah";
        public static final String WEG = "weg";
        public static final String YEYO = "yeyo";
        public static final String EFANT = "efant";
        public static final String EEP = "eep";
    }

    // Liquid Gliding (LG) - When a child substitutes a glide sound (w, y) for a liquid sound (r, l)
    public static final List<String> GLIDING_OF_LIQUIDS_INVALID_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    "teffone",  // telephone
                    "pider",        // spider
                    "weg",          // leg
                    "pway",         // play
                    "wun",          // run
                    "yeyo",         // yellow
                    "lif",          // leaf
                    "weal",         // real
                    "wabit",        // rabbit
                    "wook",         // look
                    "bwed",         // bread
                    "gween",        // green
                    "bwack",        // black
                    "gwas"          // glass
            )
    );

    // Liquid Gliding (LG) - When a child substitutes a glide sound (w, y) for a liquid sound (r, l)
    public static final List<String> GLIDING_OF_LIQUIDS_VALID_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    "telephone",
                    "spider",
                    "leg",
                    "play",
                    "run",
                    "yellow",
                    "leaf",
                    "real",
                    "rabbit",
                    "look",
                    "bread",
                    "green",
                    "black",
                    "glass",
                    "menu"
            )
    );

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
