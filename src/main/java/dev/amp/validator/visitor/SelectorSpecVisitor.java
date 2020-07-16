package dev.amp.validator.visitor;

import dev.amp.validator.css.ErrorToken;

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
}
