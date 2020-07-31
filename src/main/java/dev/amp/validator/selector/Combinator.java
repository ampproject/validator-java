package dev.amp.validator.selector;

import dev.amp.validator.css.TokenType;
import dev.amp.validator.visitor.SelectorVisitor;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class Combinator extends Selector {

    /**
     * @param {!CombinatorType} combinatorType
     * @param {!SimpleSelectorSequence|!Combinator} left
     * @param {!SimpleSelectorSequence} right
     */
    public Combinator(@Nonnull final CombinatorType combinatorType, Selector left, SimpleSelectorSequence right) {
        super();
        this.combinatorType = combinatorType;
        this.left = left;
        this.right = right;
    }

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

    /**
     *
     */
    private CombinatorType combinatorType;

    /**
     *
     */
    private Selector left;

    /**
     *
     */
    private SimpleSelectorSequence right;

    /**
     * Types of Combinators
     */
    public enum CombinatorType {
        /** DESCENDANT */
        DESCENDANT,
        /** CHILD */
        CHILD,
        /** ADJACENT_SIBLING */
        ADJACENT_SIBLING,
        /** GENERAL_SIBLING */
        GENERAL_SIBLING,
    }
}
