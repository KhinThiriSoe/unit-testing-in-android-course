package com.techyourchance.unittestingfundamentals.example1

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PositiveNumberValidatorTest {

    private val validator: PositiveNumberValidator = PositiveNumberValidator()

    @Test
    fun test1() {
        assertFalse(validator.isPositive(-1))
        assertFalse(validator.isPositive(-10))
        assertFalse(validator.isPositive(-900))
        assertFalse(validator.isPositive(-50))
        assertFalse(validator.isPositive(-10000))
    }

    @Test
    fun test2() {
        assertFalse(validator.isPositive(0))
    }

    @Test
    fun test3() {
        assertTrue(validator.isPositive(1))
        assertTrue(validator.isPositive(10))
        assertTrue(validator.isPositive(900))
        assertTrue(validator.isPositive(50))
        assertTrue(validator.isPositive(10000))

    }
}