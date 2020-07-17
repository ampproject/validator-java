package dev.amp.validator.selector;

import dev.amp.validator.css.TokenType;
import dev.amp.validator.visitor.SelectorVisitor;

import java.util.function.Consumer;

public class Combinator extends Selector {
    @Override
    public void forEachChild(Consumer<Selector> selector) {

    }

    @Override
    public void accept(SelectorVisitor visitor) {

    }

    @Override
    public TokenType getTokenType() {
        return TokenType.COMBINATOR;
    }
}
