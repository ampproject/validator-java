package dev.amp.validator.visitor;

import dev.amp.validator.css.ErrorToken;
import dev.amp.validator.selector.AttrSelector;
import dev.amp.validator.selector.ClassSelector;
import dev.amp.validator.selector.Combinator;
import dev.amp.validator.selector.IdSelector;
import dev.amp.validator.selector.PseudoSelector;
import dev.amp.validator.selector.Selector;
import dev.amp.validator.selector.SimpleSelectorSequence;
import dev.amp.validator.selector.TypeSelector;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A visitor for selector.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SelectorSpecVisitor extends SelectorVisitor {
    /**
     * @param errors an array of ErrorTokens
     */
    public SelectorSpecVisitor(@Nonnull final List<ErrorToken> errors) {
        super(errors);
    }

    @Override
    public void visitTypeSelector(@Nonnull final TypeSelector typeSelector) {
    }

    @Override
    public void visitIdSelector(@Nonnull final IdSelector idSelector) {
    }

    @Override
    public void visitAttrSelector(@Nonnull final AttrSelector attrSelector) {
    }

    @Override
    public void visitPseudoSelector(@Nonnull final PseudoSelector pseudoSelector) {
    }

    @Override
    public void visitClassSelector(@Nonnull final ClassSelector classSelector) {
    }

    @Override
    public void visitSimpleSelectorSequence(@Nonnull final SimpleSelectorSequence sequence) {
    }

    @Override
    public void visitCombinator(@Nonnull final Combinator combinator) {
    }

    @Override
    public void visitSelectorsGroup(@Nonnull final Selector selector) {
    }
}
