package test.dynamicprogramming;

import algorithms.dynamicprogramming.ResourceAllocator;

public class ResourceAllocatorTest {

    public static void main(String[] args) {

        ResourceAllocator allocator =
                new ResourceAllocator();

        int[] values = {2, 3, 4, 5};

        int result =
                allocator.maximumValue(values, 8);

        System.out.println(
                "Maximum Allocation = " + result
        );

    }

}