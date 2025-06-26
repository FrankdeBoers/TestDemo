package com.frank.newapplication.datastructures

import org.junit.Assert.assertEquals
import org.junit.Test

class BinarySearchTest {

    /**
     * TODO: Implement the binary search algorithm.
     *
     * Binary search is an efficient algorithm for finding an item from a sorted array of items.
     * It works by repeatedly dividing in half the portion of the list that could contain the item,
     * until you've narrowed down the possible locations to just one.
     *
     * Precondition: The input array `nums` must be sorted in ascending order.
     *
     * @param nums A sorted array of integers.
     * @param target The integer value to search for.
     * @return The index of the target if found, otherwise -1.
     */
    fun search(nums: IntArray, target: Int): Int {
        if (nums.isEmpty()) {
            return -1
        }

        var left = 0
        var right = nums.size - 1
        while (left <= right) {
            val mid = left + (right - left) / 2
            if (nums[mid] == target) {
                return mid
            } else if (nums[mid] < target) {
                left = mid + 1
            } else {
                right = mid -1
            }
        }

        return -1 // Placeholder return
    }

    @Test
    fun testBinarySearch() {
        val sortedArray = intArrayOf(2, 5, 8, 12, 16, 23, 38, 56, 72, 91)

        // Test case 1: Target is in the middle
        println("Searching for 23...")
        assertEquals(5, search(sortedArray, 23))
        println("Found at index 5.")

        // Test case 2: Target is not in the array
        println("Searching for 15...")
        assertEquals(-1, search(sortedArray, 15))
        println("Not found.")

        // Test case 3: Target is the first element
        println("Searching for 2...")
        assertEquals(0, search(sortedArray, 2))
        println("Found at index 0.")

        // Test case 4: Target is the last element
        println("Searching for 91...")
        assertEquals(9, search(sortedArray, 91))
        println("Found at index 9.")

        // Test case 5: Array is empty
        println("Searching in an empty array...")
        assertEquals(-1, search(intArrayOf(), 5))
        println("Not found.")

        // Test case 6: Array with one element, target is present
        println("Searching for 10 in [10]...")
        assertEquals(0, search(intArrayOf(10), 10))
        println("Found at index 0.")

        // Test case 7: Array with one element, target is not present
        println("Searching for 5 in [10]...")
        assertEquals(-1, search(intArrayOf(10), 5))
        println("Not found.")
    }
} 