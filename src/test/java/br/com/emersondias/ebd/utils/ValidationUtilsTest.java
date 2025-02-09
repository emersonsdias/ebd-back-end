package br.com.emersondias.ebd.utils;

import org.junit.jupiter.api.Test;

import static br.com.emersondias.ebd.utils.ValidationUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationUtilsTest {

    @Test
    public void isValidEmail_shouldValidateIfTextIsValidEmail() {
        assertFalse(isValidEmail(null));
        assertFalse(isValidEmail(""));
        assertFalse(isValidEmail("test"));
        assertFalse(isValidEmail("test.com"));
        assertFalse(isValidEmail("test@test .com"));

        assertTrue(isValidEmail("test@test.com"));
    }

    @Test
    public void containsLetters_shouldValidateIfTextContainsLetters() {
        assertFalse(containsLetters(null));
        assertFalse(containsLetters(""));
        assertFalse(containsLetters("123456"));
        assertFalse(containsLetters("123_456"));

        assertTrue(containsLetters("a"));
        assertTrue(containsLetters("A"));
        assertTrue(containsLetters("z"));
        assertTrue(containsLetters("Z"));
        assertTrue(containsLetters("test@test.com"));
    }

    @Test
    public void containsNumbers_shouldValidateIfTextContainsNumbers() {
        assertFalse(containsNumbers(null));
        assertFalse(containsNumbers(""));
        assertFalse(containsNumbers("test"));
        assertFalse(containsNumbers("test@test.com"));

        assertTrue(containsNumbers("1"));
        assertTrue(containsNumbers("123456"));
        assertTrue(containsNumbers("teste@123"));
    }

    @Test
    public void containsSpecialCharacters_shouldValidateIfTextContainsSpecialCharacters() {
        assertFalse(containsSpecialCharacters(null));
        assertFalse(containsSpecialCharacters(""));
        assertFalse(containsSpecialCharacters("test"));
        assertFalse(containsSpecialCharacters("test123"));
        assertFalse(containsSpecialCharacters("9876"));

        assertTrue(containsSpecialCharacters("_"));
        assertTrue(containsSpecialCharacters("test@test.com"));
        assertTrue(containsSpecialCharacters("<3"));
    }

    @Test
    public void isOnlyNumbers_shouldvalidateIfTextContainsOnlyNumbers() {
        assertFalse(isOnlyNumbers(null));
        assertFalse(isOnlyNumbers(""));
        assertFalse(isOnlyNumbers(" "));
        assertFalse(isOnlyNumbers("1 2"));
        assertFalse(isOnlyNumbers("test"));
        assertFalse(isOnlyNumbers("test123"));

        assertTrue(isOnlyNumbers("1"));
        assertTrue(isOnlyNumbers("2"));
        assertTrue(isOnlyNumbers("0123456789"));
    }

}
