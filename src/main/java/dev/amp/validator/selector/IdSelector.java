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
 * An attribute selector matches document nodes based on their attributes.
 * http://www.w3.org/TR/css3-selectors/#attribute-selectors
 *
 * Typically written as '[foo=bar]'.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class IdSelector extends Selector {
    /**
     * @param value to set to
     */
    public IdSelector(@Nonnull final String value) {
        super();
        this.value = value;
    }

    @Override
    public void forEachChild(final Consumer<Selector> selector) {
    }

    /**
     * visits a selector
     * @param visitor a SelectorVisitor instance
     */
    public void accept(@Nonnull final SelectorVisitor visitor) {
        visitor.visitIdSelector(this);
    }

    /**
     * generates a string representation
     * @return to string representation
     * */
    public String toString() {
        return "#" + this.value;
    }

    /**
     * get token type
     * @return the token type
     */
    @Override
    public TokenType getTokenType() {
        return TokenType.ID_SELECTOR;
    }

    /**
     * underlying value of selector
     */
    private final String value;
}
