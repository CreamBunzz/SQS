package com.serverless;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Hoge.class)
class HogeTest {

    @Test
    public void testCheck() {
        Hoge huga = PowerMockito.spy(new Hoge());
        try {
            PowerMockito.when(huga, "check","aaa").thenReturn("aaa");
        } catch (Exception e) {
            e.printStackTrace();
        }
       // Assertions.assertEquals("a",huga.check("aaa"));


     }
}