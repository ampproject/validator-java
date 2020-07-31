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
    public SelectorSpecVisitor(@Nonnull List<ErrorToken> errors) {
        super(errors);
    }

    @Override
    public void visitTypeSelector(@Nonnull TypeSelector typeSelector) {

    }

    @Override
    public void visitIdSelector(@Nonnull IdSelector idSelector) {

    }

    @Override
    public void visitAttrSelector(@Nonnull AttrSelector attrSelector) {

    }

    @Override
    public void visitPseudoSelector(@Nonnull PseudoSelector pseudoSelector) {

    }

    @Override
    public void visitClassSelector(@Nonnull ClassSelector classSelector) {

    }

    @Override
    public void visitSimpleSelectorSequence(@Nonnull SimpleSelectorSequence sequence) {

    }

    @Override
    public void visitCombinator(@Nonnull Combinator combinator) {

    }

    @Override
    public void visitSelectorsGroup(@Nonnull Selector selector) {

    }
}
