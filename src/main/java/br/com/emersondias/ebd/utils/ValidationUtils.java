package br.com.emersondias.ebd.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        if (isNull(email) || email.isEmpty()) {
            return false;
        }
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean containsLetters(String text) {
        if (isNull(text)) {
            return false;
        }
        String regex = ".*[a-zA-Z]+.*";
        return text.matches(regex);
    }

    public static boolean containsNumbers(String text) {
        if (isNull(text)) {
            return false;
        }
        String regex = ".*\\d+.*";
        return text.matches(regex);
    }

    public static boolean containsSpecialCharacters(String text) {
        if (isNull(text)) {
            return false;
        }
        String regex = ".*[!@#$%^&*_\\-+={}\\[\\]()<>,.:;\"'/?|]+.*";
        return text.matches(regex);
    }

    public static boolean isOnlyNumbers(String text) {
        if (isNull(text) || text.isBlank()) {
            return false;
        }
        return text.matches("\\d+");
    }


}
