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

package dev.amp.validator.selector;

import dev.amp.validator.css.TokenType;
import dev.amp.validator.visitor.SelectorVisitor;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

/**
 * /**
 * Models a simple selector sequence, e.g. '*|foo#id'.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SimpleSelectorSequence extends Selector {
    /**
     * @param {!TypeSelector}     typeSelector
     * @param {!Array<!Selector>} otherSelectors
     */
    public SimpleSelectorSequence(@Nonnull final TypeSelector typeSelector, @Nonnull final List<Selector> otherSelectors) {
        super();
        /** @type {!TypeSelector} */
        this.typeSelector = typeSelector;
        /** @type {!Array<!Selector>} */
        this.otherSelectors = otherSelectors;
    }

    @Override
    public void forEachChild(Consumer<Selector> selector) {
//        lambda(this.typeSelector);
//        for (const other of this.otherSelectors) {
//            lambda(other);
//        }
    }

    @Override
    public void accept(SelectorVisitor visitor) {

    }

    @Override
    public TokenType getTokenType() {
        return TokenType.SIMPLE_SELECTOR_SEQUENCE;
    }

    /**
     *
     */
    private TypeSelector typeSelector;

    /**
     *
     */
    private List<Selector> otherSelectors;
}
