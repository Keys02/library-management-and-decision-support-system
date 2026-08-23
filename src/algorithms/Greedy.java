package algorithms;

import datastructures.linear.DynamicArray;
import model.Resource;

/**
 * Greedy resource-assignment algorithm.
 *
 * Scenario: assign resources to service requests up to a budget limit,
 * choosing the highest-value resource at each step.
 *
 * COUNTEREXAMPLE included: greedy-by-value fails when total cost exceeds budget
 * but a cheaper combination would have given a better value-per-dollar ratio.
 */
public class Greedy {

    /**
     * Greedy assignment: sort by value descending, pick while budget allows.
     * Returns the selected resources.
     */
    public static DynamicArray<Resource> greedyByValue(DynamicArray<Resource> resources, double budget) {
        // Sort by value descending (insertion sort)
        for (int i = 1; i < resources.size(); i++) {
            Resource key = resources.get(i); int j = i - 1;
            while (j >= 0 && resources.get(j).getValue() < key.getValue()) {
                resources.set(j + 1, resources.get(j)); j--;
            }
            resources.set(j + 1, key);
        }

        DynamicArray<Resource> selected = new DynamicArray<>();
        double remaining = budget;
        for (int i = 0; i < resources.size(); i++) {
            Resource r = resources.get(i);
            if (r.getCost() <= remaining) { selected.add(r); remaining -= r.getCost(); }
        }
        return selected;
    }
}
