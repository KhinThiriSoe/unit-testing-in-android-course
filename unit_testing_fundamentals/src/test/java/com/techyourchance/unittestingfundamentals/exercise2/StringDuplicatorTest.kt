package com.techyourchance.unittestingfundamentals.exercise2

import org.junit.Assert.assertEquals
import org.junit.Test

class StringDuplicatorTest {

    private val duplicator = StringDuplicator()

    @Test
    fun duplicate_emptyString_EmptyStringReturned() {
        assertEquals("", duplicator.duplicate(""))
    }

    @Test
    fun duplicate_oneCharacter_twoCharactersReturned() {
        assertEquals("aa", duplicator.duplicate("a"))
        assertEquals("$$", duplicator.duplicate("$"))
        assertEquals("11", duplicator.duplicate("1"))

    }

    @Test
    fun reverse_longString_duplicatedStringReturned() {
        assertEquals("khin788@E\$nkhin788@E\$n", duplicator.duplicate("khin788@E\$n"))
        assertEquals("Aung,Aung,", duplicator.duplicate("Aung,"))
        assertEquals("ThiriThiri", duplicator.duplicate("Thiri"))
    }
}