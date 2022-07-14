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
import java.util.ArrayDeque;
import java.util.function.Consumer;

/**
 * Abstract super class for CSS Selectors. The Token class, which this
 * class inherits from, has line, col, and tokenType fields.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SelectorsGroup extends Selector {
    /**
     * @param elements array of selectors
     */
    public SelectorsGroup(@Nonnull final ArrayDeque<Selector> elements) {
        super();
        this.elements = elements;
    }

    /**
     *  method to run lambda on all elements
     * @param lambda function to executed
     */
    public void forEachChild(final Consumer<Selector> lambda) {
        for (final Selector selector : this.elements) {
            lambda.accept(selector);
        }
    }

    /**
     * visits a selector
     * @param visitor a SelectorVisitor instance
     */
    public void accept(@Nonnull final SelectorVisitor visitor) {
        visitor.visitSelectorsGroup(this);
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.SELECTORS_GROUP;
    }

    /**
     * getter for elements
     * @return this' elements
     */
    public ArrayDeque<Selector> getElements() {
        return elements;
    }

    /**
     * The array of selectors.
     */
    private final ArrayDeque<Selector> elements;
}
