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

import com.steadystate.css.parser.Token;
import dev.amp.validator.css.CssValidationException;
import dev.amp.validator.css.TokenType;
import dev.amp.validator.visitor.SelectorVisitor;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

/**
 * A pseudo selector can match either pseudo classes or pseudo elements.
 * http://www.w3.org/TR/css3-selectors/#pseudo-classes
 * http://www.w3.org/TR/css3-selectors/#pseudo-elements.
 *
 * Typically written as ':visited', ':lang(fr)', and '::first-line'.
 *
 * isClass: Pseudo selectors with a single colon (e.g., ':visited')
 * are pseudo class selectors. Selectors with two colons (e.g.,
 * '::first-line') are pseudo elements.
 *
 * func: If it's a function style pseudo selector, like lang(fr), then func
 * the function tokens. TODO(powdercloud): parse this in more detail.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class PseudoSelector extends Selector {
    /**
     * Constructor.
     *
     * @param isClass pseudo class flag
     * @param name the name
     * @param func the function tokens
     */
    public PseudoSelector(final boolean isClass, @Nonnull final String name,
                          @Nonnull final List<com.steadystate.css.parser.Token> func) {
        super();
        this.isClass = isClass;
        this.name = name;
        this.func = func;
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
        visitor.visitPseudoSelector(this);
    }

    /**
     * get token type
     * @return the token type
     */
    @Override
    public TokenType getTokenType() {
        return TokenType.PSEUDO_SELECTOR;
    }

    /**
     * if is a class
     * @return true if isClass
     */
    public boolean isClass() {
        return isClass;
    }

    /**
     * getter for name
     * @return the name of selector
     */
    public String getName() {
        return name;
    }

    /**
     * Pseudo class flag.
     */
    private boolean isClass;

    /**
     * Pseudo selector name.
     */
    private final String name;

    /**
     * Function tokens.
     */
    private final List<Token> func;
}
