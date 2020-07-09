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
 /**
 * Models a simple selector sequence, e.g. '*|foo#id'.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SimpleSelectorSequence extends Selector {
    /**
     * @param {!TypeSelector} typeSelector
     * @param {!Array<!Selector>} otherSelectors
     */
    public SimpleSelectorSequence(TypeSelector typeSelector, List<Selector> otherSelectors) {
        super();
        /** @type {!TypeSelector} */
        this.typeSelector = typeSelector;
        /** @type {!Array<!Selector>} */
        this.otherSelectors = otherSelectors;
        /** @type {!tokenize_css.TokenType} */
        this.tokenType = tokenize_css.TokenType.SIMPLE_SELECTOR_SEQUENCE;
    }

    /** @param {function(!Selector)} lambda */
    void forEachChild(SimpleSelectorSequence selector) {}

    /** @param visitor a SelectorVisitor instance */
    void accept(SelectorVisitor visitor) {}
}
