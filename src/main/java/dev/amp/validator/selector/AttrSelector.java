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

import dev.amp.validator.css.CssValidationException;
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

public class AttrSelector extends Selector {
    /**
     * @param namespacePrefix namespace prefix
     * @param attrName attribute name
     * @param matchOperator is either the string
     * representation of the match operator (e.g., '=' or '~=') or '',
     * in which case the attribute selector is a check for the presence
     * of the attribute.
     * @param value is the value to apply the match operator
     * against, or if matchOperator is '', then this must be empty as
     * well.
     */
    public AttrSelector(@Nonnull final String namespacePrefix, @Nonnull final String attrName,
                        @Nonnull final String matchOperator, @Nonnull final String value) {
        super();

        this.namespacePrefix = namespacePrefix;
        this.attrName = attrName;
        this.matchOperator = matchOperator;
        this.value = value;
    }

    @Override
    public void forEachChild(final Consumer<Selector> selector) {
    }

    /**
     * visits a selector
     * @param visitor a SelectorVisitor instance
     * @throws CssValidationException CssValidationException
     */
    public void accept(@Nonnull final SelectorVisitor visitor) throws CssValidationException {
        visitor.visitAttrSelector(this);
    }

    /**
     * get token type
     * @return the token type
     */
    @Override
    public TokenType getTokenType() {
        return TokenType.ATTR_SELECTOR;
    }

    /**
     * get token type
     * @return the token type
     */
    public String getAttrName() {
        return this.attrName;
    }

    /**
     * Namespace prefix.
     */
    private final String namespacePrefix;

    /**
     * Attribute name.
     */
    private final String attrName;

    /**
     * Matching operator.
     */
    private final String matchOperator;

    /**
     * Value.
     */
    private final String value;
}
