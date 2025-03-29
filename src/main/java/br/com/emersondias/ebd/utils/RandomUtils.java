package br.com.emersondias.ebd.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    private static final Random random = new Random();

    public static Integer generateRandomInteger(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("The min value cannot be greater than the max value");
        }
        return random.nextInt((max - min) + 1) + min;
    }

    @SafeVarargs
    public static <T> T chooseRandomElement(T... elements) {
        if (elements == null || elements.length == 0) {
            throw new IllegalArgumentException("The list cannot be null or empty");
        }
        int index = random.nextInt(elements.length);
        return elements[index];
    }

    public static <T> T chooseWeightedRandomElement(List<T> elements, List<Integer> weights) {
        if (elements == null || weights == null || elements.isEmpty() || weights.isEmpty()) {
            throw new IllegalArgumentException("Elements and weights cannot be null or empty");
        }
        if (elements.size() != weights.size()) {
            throw new IllegalArgumentException("Elements and weights must have the same size");
        }
        int totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (int i = 0; i < elements.size(); i++) {
            cumulativeWeight += weights.get(i);
            if (randomValue < cumulativeWeight) {
                return elements.get(i);
            }
        }
        throw new RuntimeException("Unable to select a random element");
    }

    public static LocalDate generateRandomLocalDate(int startYear, int endYear) {
        if (startYear > endYear) {
            throw new IllegalArgumentException("Start year must be greater than end year");
        }
        int year = random.nextInt(endYear - startYear + 1) + startYear;
        int month = random.nextInt(12) + 1;
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        int day = random.nextInt(daysInMonth) + 1;

        return LocalDate.of(year, month, day);
    }


}
