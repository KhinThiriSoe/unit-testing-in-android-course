package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NegativeNumberValidatorTest {

    lateinit var negativeNumberValidator: NegativeNumberValidator

    @Before
    fun setup() {
        negativeNumberValidator = NegativeNumberValidator()
    }

    @Test
    fun isTheNumberNegative() {
         val result = negativeNumberValidator.isNegative(-1)
        Assert.assertTrue(result)
    }

    @Test
    fun isTheNumberZero() {
        val result = negativeNumberValidator.isNegative(0)
        Assert.assertFalse(result)
    }

    @Test
    fun isTheNumberPositive() {
        val result = negativeNumberValidator.isNegative(1)
        Assert.assertFalse(result)
    }
}