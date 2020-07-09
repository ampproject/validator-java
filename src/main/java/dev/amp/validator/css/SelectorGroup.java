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
 * Changes to the original project are Copyright 2019, Verizon Media Inc..
 */

package dev.amp.validator.css;

import dev.amp.validator.visitor.SelectorVisitor;

import java.util.List;

/**
 * Abstract super class for CSS Selectors. The Token class, which this
 * class inherits from, has line, col, and tokenType fields.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SelectorGroup extends Selector {
    /**
     * @param {!Array<!SimpleSelectorSequence|
     *         !Combinator>} elements
     */
    public SelectorGroup(final List<Selector> elements) {
        super();
        /**
         @type {!Array<!SimpleSelectorSequence|
         !Combinator>}
         */
        this.elements = elements;
        /** @type {!tokenize_css.TokenType} */
        this.tokenType = tokenize_css.TokenType.SELECTORS_GROUP;
    }

    /** @param {function(!Selector)} lambda */
    void forEachChild(SelectorGroup selector) {}

    /** @param visitor a SelectorVisitor instance */
    void accept(SelectorVisitor visitor) {}
}
