package com.techyourchance.unittestingfundamentals.example2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class StringReverserTest {

    StringReverser SUT;

    @Before
    public void setup() throws Exception {
        SUT = new StringReverser();
    }

    @Test
    public void reverse_emptyString_emptyStringReturned() throws Exception {
        String result = SUT.reverse("");
        assertThat(result, is(""));
    }

    @Test
    public void reverse_singleCharacter_sameStringReturned() throws Exception {
        String result = SUT.reverse("a");
        assertThat(result, is("a"));
    }

    @Test
    public void reverse_longString_reversedStringReturned() throws Exception {
        String result = SUT.reverse("Vasiliy Zukanov");
        assertThat(result, is("vonakuZ yilisaV"));
    }
}