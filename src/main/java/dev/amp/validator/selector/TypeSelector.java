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

import java.util.function.Consumer;


/**
 * This node models type selectors and universal selectors.
 * http://www.w3.org/TR/css3-selectors/#type-selectors
 * http://www.w3.org/TR/css3-selectors/#universal-selector
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class TypeSelector extends Selector {
    /**
     * Choices for namespacePrefix:
     * - 'a specific namespace prefix' means 'just that specific namespace'.
     * - '' means 'without a namespace'
     * - '*' means 'any namespace including without a namespace'
     * - null means the default namespace if one is declared, and '*' otherwise.
     *
     * The universal selector is covered by setting the elementName to '*'.
     *
     * @param namespacePrefix a namespace prefix
     * @param elementName element name
     */
    public TypeSelector(final String namespacePrefix, final String elementName) {
        super();

        this.namespacePrefix = namespacePrefix;
        this.elementName = elementName;
    }

    @Override
    public void forEachChild(final Consumer<Selector> selector) {
    }

    /** @param visitor a SelectorVisitor instance */
    public void accept(final SelectorVisitor visitor) {
        visitor.visitTypeSelector(this);
    }

    /**
     * Getter for token type returns stylesheet.
     *
     * @return TokenType.STYLESHEET
     */
    @Override
    public TokenType getTokenType() {
        return TokenType.TYPE_SELECTOR;
    }

    /**
     * Serializes the selector to a string (in this case CSS syntax that
     * could be used to recreate it).
     * @return a string
     */
    public String toString() {
        if (this.namespacePrefix == null) {
            return this.elementName;
        }
        return this.namespacePrefix + '|' + this.elementName;
    }

    /** Namespace prefix */
    private final String namespacePrefix;

    /** Element name */
    private final String elementName;
}
