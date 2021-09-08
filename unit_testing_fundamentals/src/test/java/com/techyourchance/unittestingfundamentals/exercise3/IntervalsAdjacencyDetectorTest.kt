package com.techyourchance.unittestingfundamentals.exercise3

import com.techyourchance.unittestingfundamentals.example3.Interval
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class IntervalsAdjacencyDetectorTest {

    lateinit var intervalsAdjacencyDetector: IntervalsAdjacencyDetector

    @Before
    fun setup() {
        intervalsAdjacencyDetector = IntervalsAdjacencyDetector()
    }

    // Interval1 before Interval2
    @Test
    fun isAdjacent_Interval1BeforeInterval2_falseReturned() {
        val interval1 = Interval(-3, 0)
        val interval2 = Interval(1, 3)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertFalse(result)
    }

    // Interval1 same Interval2 on start and interval 2 bigger
    @Test
    fun isAdjacent_Interval1SameInterval2OnStartAndInterval2Bigger_falseReturned() {
        val interval1 = Interval(-3, 0)
        val interval2 = Interval(-3, 3)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertFalse(result)
    }

    // Interval1 adjacent Interval2 on start
    @Test
    fun isAdjacent_Interval1AdjacentInterval2OnStart_trueReturned() {
        val interval1 = Interval(-3, 0)
        val interval2 = Interval(0, 3)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertTrue(result)
    }

    // Interval1 overlap Interval2 on start
    @Test
    fun isAdjacent_Interval1OverlapInterval2OnStart_falseReturned() {
        val interval1 = Interval(-3, 0)
        val interval2 = Interval(-2, 3)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertFalse(result)
    }

    // Interval1 is contained within Interval2
    @Test
    fun isAdjacent_Interval1IsContainedWithinInterval2_falseReturned() {
        val interval1 = Interval(-3, 3)
        val interval2 = Interval(-4, 5)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertFalse(result)
    }

    // Interval2 contains Interval1
    @Test
    fun isAdjacent_Interval1ContainsInterval2_falseReturned() {
        val interval1 = Interval(-3, 3)
        val interval2 = Interval(-1, 2)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertFalse(result)
    }

    // Interval1 equals Interval2
    @Test
    fun isAdjacent_Interval1EqualInterval2_falseReturned() {
        val interval1 = Interval(-3, 3)
        val interval2 = Interval(-3, 3)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertFalse(result)
    }

    // Interval1 same Interval2 on end and interval 2 bigger
    @Test
    fun isAdjacent_Interval1SameInterval2OnEndAndInterval2Bigger_falseReturned() {
        val interval1 = Interval(0, 3)
        val interval2 = Interval(-3, 3)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertFalse(result)
    }

    // Interval1 overlap Interval2 on end
    @Test
    fun isAdjacent_Interval1OverlapInterval2OnEnd_falseReturned() {
        val interval1 = Interval(-3, 0)
        val interval2 = Interval(-2, 3)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertFalse(result)
    }

    // Interval1 adjacent Interval2 on end
    @Test
    fun isAdjacent_Interval1AdjacentInterval2OnEnd_trueReturned() {
        val interval1 = Interval(0, 3)
        val interval2 = Interval(3, 6)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertTrue(result)
    }

    // Interval1 after Interval 2
    @Test
    fun isAdjacent_Interval1AfterInterval2_falseReturned() {
        val interval1 = Interval(4, 6)
        val interval2 = Interval(1, 2)
        val result = intervalsAdjacencyDetector.isAdjacent(interval1, interval2)
        Assert.assertFalse(result)
    }

}