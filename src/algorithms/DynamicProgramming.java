package algorithms;

import datastructures.linear.DynamicArray;
import model.Resource;

/**
 * 0/1 Knapsack over library resources.
 *
 * Each Resource has a cost (weight) and a value.
 * We select items to maximise total value without exceeding budgetLimit.
 *
 * Uses a 2-D DP table — tabulation approach.
 */
public class DynamicProgramming {

    public static DynamicArray<Resource> knapsack(DynamicArray<Resource> resources, int budgetLimit) {
        int n = resources.size();
        // dp[i][w] = max value using first i items with budget w
        int[][] dp = new int[n + 1][budgetLimit + 1];

        for (int i = 1; i <= n; i++) {
            Resource r = resources.get(i - 1);
            int cost  = (int) r.getCost();
            int value = r.getValue();
            for (int w = 0; w <= budgetLimit; w++) {
                dp[i][w] = dp[i - 1][w];
                if (cost <= w && dp[i - 1][w - cost] + value > dp[i][w])
                    dp[i][w] = dp[i - 1][w - cost] + value;
            }
        }

        // Reconstruct chosen items
        DynamicArray<Resource> chosen = new DynamicArray<>();
        int w = budgetLimit;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                chosen.add(resources.get(i - 1));
                w -= (int) resources.get(i - 1).getCost();
            }
        }
        return chosen;
    }
}
