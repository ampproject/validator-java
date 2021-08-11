package dev.amp.validator;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SrcsetSourceDefTest {

    @Test

    public void testSrcsetSourceDef() {
        SrcsetSourceDef alpha = new SrcsetSourceDef("www.yahoo.com", "1024 × 768");
        Assert.assertEquals(alpha.getUrl(), "www.yahoo.com");
        Assert.assertEquals(alpha.getWidthOrPixelDensity(), "1024 × 768");
    }

}
