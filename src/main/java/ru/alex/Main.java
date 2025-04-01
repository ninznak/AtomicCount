package ru.alex;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final AtomicInteger length3 = new AtomicInteger();
    private static final AtomicInteger length4 = new AtomicInteger();
    private static final AtomicInteger length5 = new AtomicInteger();

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        executor.execute(() -> checkPalindromes(texts));
        executor.execute(() -> checkAllSameLetters(texts));
        executor.execute(() -> checkAllIncreasingLetters(texts));

        executor.shutdown();

        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения программы: " + (endTime - startTime) + " мс");

        System.out.printf("Красивых слов с длиной 3: %d шт%n", length3.get());
        System.out.printf("Красивых слов с длиной 4: %d шт%n", length4.get());
        System.out.printf("Красивых слов с длиной 5: %d шт%n", length5.get());
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    private static void checkPalindromes(String[] nicknames) {
        for (String nickname : nicknames) {
            if (nickname.contentEquals(new StringBuilder(nickname).reverse())) {
                incrementCounter(nickname.length());
            }
        }
    }

    private static void checkAllSameLetters(String[] texts) {
        for (String text : texts) {
            if (hasSameLetters(text)) {
                incrementCounter(text.length());
            }
        }
    }

    private static void checkAllIncreasingLetters(String[] nicknames) {
        for (String nickname : nicknames) {
            if (hasIncreasingLetters(nickname)) {
                incrementCounter(nickname.length());
            }
        }
    }

    private static void incrementCounter(int length) {
        switch (length) {
            case 3 -> length3.incrementAndGet();
            case 4 -> length4.incrementAndGet();
            case 5 -> length5.incrementAndGet();
        }
    }

    private static boolean hasSameLetters(String text) {
        char first = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != first) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasIncreasingLetters(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) > text.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
