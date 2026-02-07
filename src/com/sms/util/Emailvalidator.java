package com.sms.util;

import java.util.regex.Pattern;

public class Emailvalidator {
    private static final String EMAIL_REGEX="^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static boolean isvalidEmail(String email){
        return Pattern.matches(EMAIL_REGEX,email);
    }
}
