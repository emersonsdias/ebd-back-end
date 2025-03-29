package br.com.emersondias.ebd.utils;

import java.text.Normalizer;

public class Utils {

    public static String removeAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
