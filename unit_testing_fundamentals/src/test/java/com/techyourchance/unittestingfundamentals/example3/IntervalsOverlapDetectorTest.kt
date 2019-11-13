package com.techyourchance.unittestingfundamentals.example3

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IntervalsOverlapDetectorTest {

    private val overlapDetector = IntervalsOverlapDetector()

    // interval1 is before interval2

    @Test
    fun isOverlap_interval1BeforeInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(8, 12)
        assertFalse(overlapDetector.isOverlap(interval1, interval2))
    }

    // interval1 overlaps interval2 on start

    @Test
    fun isOverlap_interval1OverlapsInterval2OnStart_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(3, 12)
        assertTrue(overlapDetector.isOverlap(interval1, interval2))
    }
    // interval1 is contained within interval2

    @Test
    fun isOverlap_interval1ContainedWithinInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-4, 12)
        assertTrue(overlapDetector.isOverlap(interval1, interval2))
    }
    // interval1 contains interval2

    @Test
    fun isOverlap_interval1ContainsInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(0, 3)
        assertTrue(overlapDetector.isOverlap(interval1, interval2))
    }
    // interval1 overlaps interval2 on end

    @Test
    fun isOverlap_interval1OverlapsInterval2OnEnd_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-4, 4)
        assertTrue(overlapDetector.isOverlap(interval1, interval2))
    }
    // interval1 is after interval2

    @Test
    fun isOverlap_interval1AfterInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-10, -3)
        assertFalse(overlapDetector.isOverlap(interval1, interval2))
    }

    @Test
    fun isOverlap_interval1BeforeAdjacentInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(5, 8)
        assertFalse(overlapDetector.isOverlap(interval1, interval2))
    }

    @Test
    fun isOverlap_interval1AfterAdjacentInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-3, -1)
        assertFalse(overlapDetector.isOverlap(interval1, interval2))
    }
}