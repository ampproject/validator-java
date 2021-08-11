package dev.amp.validator.selector;

import dev.amp.validator.css.TokenType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayDeque;

public class SelectorsGroupTest {
    @Test
    public void getSelectorsGroup() {
        ArrayDeque<Selector> elements = new ArrayDeque<Selector>();
        SelectorsGroup alpha = new SelectorsGroup(elements);
        TokenType beta = TokenType.SELECTORS_GROUP;
        Assert.assertEquals(alpha.getTokenType(), beta);
        Assert.assertEquals(alpha.getElements(), elements);
        /**Ask what does lamba function do*/

    }
}
