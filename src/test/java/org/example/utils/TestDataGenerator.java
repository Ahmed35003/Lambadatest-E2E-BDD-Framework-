package org.example.utils;

import java.util.Random;
import java.util.UUID;

public final class TestDataGenerator {

    private static final Random RANDOM = new Random();

    private static final String[] FIRST_NAMES = {
            "James", "Emily", "Chris", "Sarah", "Michael",
            "Emma",  "David", "Olivia", "Alex", "Sophia"
    };

    private static final String[] LAST_NAMES = {
            "Smith",   "Johnson", "Brown",   "Taylor", "Wilson",
            "Davis",   "Miller",  "Moore",   "Anderson", "Thomas"
    };

    private TestDataGenerator() {
        // Utility class — not instantiable
    }

    /**
     * Generates a unique email address on every call.
     * Format: qa.auto.<10-char-uid>@testmail.io
     */
    public static String generateEmail() {
        String uid = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        return "qa.auto." + uid + "@testmail.io";
    }

    /** Picks a random first name from the predefined pool. */
    public static String generateFirstName() {
        return FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
    }

    /** Picks a random last name from the predefined pool. */
    public static String generateLastName() {
        return LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
    }

    /**
     * Generates an 11-digit phone number.
     * Format: 07<9 random digits>
     */
    public static String generatePhone() {
        StringBuilder phone = new StringBuilder("07");
        for (int i = 0; i < 9; i++) {
            phone.append(RANDOM.nextInt(10));
        }
        return phone.toString();
    }

    /**
     * Generates a password that satisfies typical ecommerce strength rules:
     * uppercase, lowercase, digit, special character, minimum 8 chars.
     * Format: Test@<8-char-uid>1
     */
    public static String generatePassword() {
        String uid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "Test@" + uid + "1";
    }
}