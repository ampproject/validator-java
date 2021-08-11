package dev.amp.validator.selector;

import dev.amp.validator.css.TokenType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TypeSelectorTest {

    @Test
    public void stringOutputTest() {
        TypeSelector alpha = new TypeSelector("checker", "checked");
        String a = alpha.toString();
        Assert.assertEquals(a, "checker|checked");
    }

    @Test
    public void stringOutputTestNil() {
        TypeSelector beta = new TypeSelector(null, "checked");
        String b = beta.toString();
        Assert.assertEquals(b, "checked");

    }

    @Test
    public void tokenTest() {
        TypeSelector alpha = new TypeSelector("checker", "checked");
        TokenType a = alpha.getTokenType();
        Assert.assertEquals(a, TokenType.TYPE_SELECTOR);
    }
}

