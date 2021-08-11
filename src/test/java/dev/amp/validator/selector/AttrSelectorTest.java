package dev.amp.validator.selector;

import org.testng.annotations.Test;
import org.testng.Assert;


public class AttrSelectorTest {
    @Test
    public void getAttrtest() {
        ClassSelector a = new ClassSelector("Hi");
        AttrSelector mark = new AttrSelector("www", "yahoo", ".", "com");
        Assert.assertEquals(mark.getAttrName(), "yahoo");
        mark.getTokenType();
    }

}
