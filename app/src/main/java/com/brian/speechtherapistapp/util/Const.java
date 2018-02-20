package com.brian.speechtherapistapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by brian on 26/01/18.
 */

public final class Const {

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

    public static final class ParamsNames {
        public static final String CHILD_ID = "childId";
        public static final String CHILD_GENDER = "male";
        public static final String CHILD_SCHOOL = "Scoil Barra Naofa";
        public static final Date CHILD_BIRTHDAY = setDefaultBirthday();
    }

    public static final String NULL_PARAMETER = "ERROR_NULL_PARAMETER";
    public static final String VALID_EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";


    public static Date setDefaultBirthday() {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 1, 18);
        return cal.getTime();
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/YYYY").format(date.getTime());
    }

}
