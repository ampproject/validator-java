package dev.amp.validator.selector;

import dev.amp.validator.css.TokenType;
import org.testng.annotations.Test;
import org.testng.Assert;

public class IdSelectorTest {

    @Test
    public void getIdSelector() {
        IdSelector a = new IdSelector("Hello");
        Assert.assertEquals(a.toString(), "#Hello");
        TokenType beta = TokenType.ID_SELECTOR;
        Assert.assertEquals(a.getTokenType(), beta);

    }


}
