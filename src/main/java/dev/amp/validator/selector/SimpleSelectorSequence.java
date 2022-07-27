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
import java.util.List;
import java.util.function.Consumer;

/**
 * Models a simple selector sequence, e.g. '*|foo#id'.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SimpleSelectorSequence extends Selector {
    /**
     * @param typeSelector a type selector
     * @param otherSelectors array of selectors
     */
    public SimpleSelectorSequence(@Nonnull final TypeSelector typeSelector, @Nonnull final List<Selector> otherSelectors) {
        super();
        this.typeSelector = typeSelector;
        this.otherSelectors = otherSelectors;
    }

    @Override
    public void forEachChild(final Consumer<Selector> lambda) {
        for (final Selector selector : this.otherSelectors) {
            lambda.accept(selector);
        }
    }

    @Override
    public void accept(final SelectorVisitor visitor) {
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.SIMPLE_SELECTOR_SEQUENCE;
    }

    /**
     * The type selector.
     */
    private TypeSelector typeSelector;

    /**
     * The list of other selectors.
     */
    private List<Selector> otherSelectors;
}
