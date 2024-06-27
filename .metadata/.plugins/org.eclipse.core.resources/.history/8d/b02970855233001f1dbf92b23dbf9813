package com.devsu.micropersona.dominio.util;

import java.util.Date;

import java.text.SimpleDateFormat;

public class UtilServicio {

    public static final String PATTERN = "dd-MM-yyyy";

    public static String DateToString(Date date, String pattern) {
        if (pattern == null)
            pattern = PATTERN;

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

}

