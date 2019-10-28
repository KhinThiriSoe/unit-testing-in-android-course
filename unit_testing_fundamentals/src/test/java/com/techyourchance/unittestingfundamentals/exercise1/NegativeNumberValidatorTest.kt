package com.techyourchance.unittestingfundamentals.exercise1

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NegativeNumberValidatorTest {

    lateinit var numberValidator: NegativeNumberValidator

    @Before
    fun setup(){
        numberValidator = NegativeNumberValidator()
    }

    @Test
    fun shouldNotBeNegative(){
        assertFalse(numberValidator.isNegative(1))
        assertFalse(numberValidator.isNegative(10))
        assertFalse(numberValidator.isNegative(100))
    }

    @Test
    fun shouldNotZero(){
        assertFalse(numberValidator.isNegative(0))
    }

    @Test
    fun shouldBeNegative(){
        assertTrue(numberValidator.isNegative(-1))
        assertTrue(numberValidator.isNegative(-100))
    }
}