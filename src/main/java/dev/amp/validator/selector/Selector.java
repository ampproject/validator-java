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
import dev.amp.validator.css.Token;
import dev.amp.validator.visitor.SelectorVisitor;

import java.util.function.Consumer;

/**
 * Abstract super class for CSS Selectors. The Token class, which this
 * class inherits from, has line, col, and tokenType fields.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public abstract class Selector extends Token {
    /** @param selector selector function */
    public abstract void forEachChild(Consumer<Selector> selector);

    /** @param visitor a SelectorVisitor instance
     * @throws CssValidationException
     */
    public abstract void accept(SelectorVisitor visitor) throws CssValidationException;
}
