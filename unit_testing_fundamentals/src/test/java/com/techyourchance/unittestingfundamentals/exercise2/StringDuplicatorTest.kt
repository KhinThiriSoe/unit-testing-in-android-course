package com.techyourchance.unittestingfundamentals.exercise2

import org.junit.Assert.assertEquals
import org.junit.Test

class StringDuplicatorTest {

    private val duplicator = StringDuplicator()

    @Test
    @Throws(Exception::class)
    fun duplicate_emptyString_EmptyStringReturned() {
        assertEquals("", duplicator.duplicate(""))
    }

    @Test
    @Throws(Exception::class)
    fun duplicate_twoCharacters_threeCharactersReturned() {
        assertEquals("aaa", duplicator.duplicate("a"))
        assertEquals("$$$", duplicator.duplicate("$"))
        assertEquals("111", duplicator.duplicate("1"))

    }

    @Test
    @Throws(Exception::class)
    fun reverse_longString_duplicatedStringReturned() {
        assertEquals("khinkhinkhin", duplicator.duplicate("khin"))
        assertEquals("AungAungAung", duplicator.duplicate("Aung"))
        assertEquals("ThiriThiriThiri", duplicator.duplicate("Thiri"))
    }
}