package com.wg.messengerclient;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        HashMap<Integer, Integer> a = new HashMap<Integer, Integer>() {{
            put(1, 1);
            put(2, 2);
        }};
    }
}