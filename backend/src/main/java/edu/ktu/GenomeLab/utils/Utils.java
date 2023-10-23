package edu.ktu.GenomeLab.utils;
import java.util.Random;

public class Utils {
    public static String generateRandomSequence(int length) {
        Random random = new Random();
        StringBuilder sequence = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sequence.append(random.nextInt(2));
        }
        return sequence.toString();
    }
}
