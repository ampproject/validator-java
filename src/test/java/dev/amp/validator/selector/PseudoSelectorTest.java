package dev.amp.validator.selector;

import dev.amp.validator.css.TokenType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PseudoSelectorTest {

    @Test
    public void getValsPseudoSelector() {
        List myList = new ArrayList();
        PseudoSelector alpha = new PseudoSelector(true, "beta", myList);
        alpha.getName();
        Assert.assertEquals(alpha.getName(), "beta");
        Assert.assertEquals(alpha.isClass(), true);
        TokenType charlie = TokenType.PSEUDO_SELECTOR;
        Assert.assertEquals(alpha.getTokenType(), charlie);
    }

}

