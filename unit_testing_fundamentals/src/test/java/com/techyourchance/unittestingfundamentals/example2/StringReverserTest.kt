package com.techyourchance.unittestingfundamentals.example2

import org.junit.Assert.assertEquals
import org.junit.Test

class StringReverserTest {

    private val reverser = StringReverser()

    @Test
    @Throws(Exception::class)
    fun reverse_emptyString_emptyStringReturned() {
        assertEquals("", reverser.reverse(""))
    }

    @Test
    @Throws(Exception::class)
    fun reverse_singleCharacter_sameStringReturned() {
        assertEquals("a", reverser.reverse("a"))
        assertEquals("1", reverser.reverse("1"))
        assertEquals("$", reverser.reverse("$"))
    }

    @Test
    @Throws(Exception::class)
    fun reverse_longString_reversedStringReturned() {
        assertEquals("vonakuZ yilisaV", reverser.reverse("Vasiliy Zukanov"))
        assertEquals("ahihT gnuA", reverser.reverse("Aung Thiha"))
        assertEquals("eoS irihT nihK", reverser.reverse("Khin Thiri Soe"))
    }
}