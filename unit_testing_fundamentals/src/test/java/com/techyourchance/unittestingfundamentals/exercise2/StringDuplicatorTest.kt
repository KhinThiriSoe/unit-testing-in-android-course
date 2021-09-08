package com.techyourchance.unittestingfundamentals.exercise2

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StringDuplicatorTest {

    lateinit var stringDuplicator: StringDuplicator

    @Before
    fun setup() {
        stringDuplicator = StringDuplicator()
    }

    @Test
    fun duplicate_emptyString_duplicatedEmptyStringReturned() {
        val result = stringDuplicator.duplicate("")
        Assert.assertEquals("", result)
    }

    @Test
    fun duplicate_singleChar_duplicatedTwoCharsReturned() {
        val result = stringDuplicator.duplicate("a")
        Assert.assertEquals("aa", result)
    }

    @Test
    fun duplicate_longString_duplicatedLongStringReturned() {
        val result = stringDuplicator.duplicate("khin")
        Assert.assertEquals("khinkhin", result)
    }
}