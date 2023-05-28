package com.example.managingpromotions.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String shortenString(String input) {
        if (input.length() <= 50) {
            // The string is already short enough
            return input;
        } else {
            // Shorten the string to 50 characters
            return input.substring(0, 47) + "...";
        }
    }
}
