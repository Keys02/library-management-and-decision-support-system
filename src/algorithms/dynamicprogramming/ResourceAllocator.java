package algorithms.dynamicprogramming;

public class ResourceAllocator {

    public int maximumValue(int[] values, int capacity) {

        int[] dp = new int[capacity + 1];

        for (int value : values) {

            for (int j = capacity; j >= value; j--) {

                dp[j] = Math.max(
                        dp[j],
                        dp[j - value] + value
                );

            }

        }

        return dp[capacity];

    }

}