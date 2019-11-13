package com.techyourchance.unittestingfundamentals.exercise3

import com.techyourchance.unittestingfundamentals.example3.Interval
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IntervalsAdjacencyDetectorTest {

    private val adjacencyDetector = IntervalsAdjacencyDetector()

    // interval1 adjacent interval2 from start
    @Test
    fun isAdjacency_interval1AndInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(5, 12)
        assertTrue(adjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval2 is adjacency interval1
    @Test
    fun isAdjacency_interval2AndInterval1_trueReturned() {
        val interval1 = Interval(9, 14)
        val interval2 = Interval(14, 15)
        assertTrue(adjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval1 is before interval2
    @Test
    fun isAdjacency_interval1BeforeInterval2_falseReturned() {
        val interval1 = Interval(2, 7)
        val interval2 = Interval(9, 12)
        assertFalse(adjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval2 is before interval1
    @Test
    fun isAdjacency_interval2BeforeInterval1_falseReturned() {
        val interval1 = Interval(9, 12)
        val interval2 = Interval(-1, 6)
        assertFalse(adjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval1 overlaps interval2 on start
    @Test
    fun isAdjacency_interval1AdjacencyInterval2OnStart_falseReturned() {
        val interval1 = Interval(1, 5)
        val interval2 = Interval(1, 6)
        assertFalse(adjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval1 overlaps interval2 on end
    @Test
    fun isAdjacency_interval1AdjacencyInterval2OnEnd_falseReturned() {
        val interval1 = Interval(1, 5)
        val interval2 = Interval(-6, 5)
        assertFalse(adjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval2 contains interval1
    @Test
    fun isAdjacency_interval1ContainsInterval2OnEnd_falseReturned() {
        val interval1 = Interval(-10, 5)
        val interval2 = Interval(-1, 4)
        assertFalse(adjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval1 contains interval2
    @Test
    fun isAdjacency_interval2ContainsInterval2OnEnd_falseReturned() {
        val interval1 = Interval(1, 5)
        val interval2 = Interval(-5, 9)
        assertFalse(adjacencyDetector.isAdjacent(interval1, interval2))
    }
}