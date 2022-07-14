/*
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  ====================================================================
 */


/*
 * Changes to the original project are Copyright 2019, Yahoo Inc..
 */

package dev.amp.validator.selector;

import dev.amp.validator.css.TokenType;
import dev.amp.validator.visitor.SelectorVisitor;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Models a combinator, as described in
 * http://www.w3.org/TR/css3-selectors/#combinators.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class Combinator extends Selector {
    /**
     * @param combinatorType combinator enumeration type
     * @param left the selector
     * @param right the simple selector sequence
     */
    public Combinator(@Nonnull final CombinatorType combinatorType, final Selector left, final SimpleSelectorSequence right) {
        super();
        this.combinatorType = combinatorType;
        this.left = left;
        this.right = right;
    }

    @Override
    public void forEachChild(final Consumer<Selector> selector) {
    }

    @Override
    public void accept(final SelectorVisitor visitor) {
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.COMBINATOR;
    }

    /**
     * The combinator type.
     */
    private CombinatorType combinatorType;

    /**
     * The left selector.
     */
    private Selector left;

    /**
     * The simple selector sequence.
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
        GENERAL_SIBLING;
    }
}
