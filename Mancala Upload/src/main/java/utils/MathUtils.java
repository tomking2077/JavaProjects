package utils;

import java.util.Random;

public class MathUtils {
    //generates numElements integers that add to totalCount
    public static int[] randomSequence(int totalCount, int numElements) {
        int[] randomSequenceArray = new int[numElements];
        Random r = new Random();
        for (int i = 0; i < totalCount; ++i) {
            int index = r.nextInt(numElements);
            randomSequenceArray[index] += 1;
        }
        return randomSequenceArray;
    }

}
