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

package dev.amp.validator.visitor;

import dev.amp.validator.css.CssValidationException;
import dev.amp.validator.css.ErrorToken;
import dev.amp.validator.css.QualifiedRule;
import dev.amp.validator.css.TokenStream;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A super class for making visitors (by overriding the types of interest). The
 * standard RuleVisitor does not recursively parse the prelude of qualified
 * rules for the components of a selector. This visitor re-parses these preludes
 * and then visits the fields within. The parse step has the possibility of
 * emitting new CSS ErrorTokens
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SelectorVisitor implements RuleVisitor {
    /**
     * @param errors an array of ErrorTokens
     */
    public SelectorVisitor(@Nonnull final List<ErrorToken> errors) {
        super();
        this.errors = errors;
    }

    /**
     * @param  qualifiedRule a QualifiedRule object
     * @throws CssValidationException a css validation exception
     */
    public void visitQualifiedRule(final QualifiedRule qualifiedRule) throws CssValidationException {
        final TokenStream tokenStream = new TokenStream(qualifiedRule.getPrelude());
        tokenStream.consume();
        final maybeSelector = parseASelectorsGroup(tokenStream);
        if (maybeSelector instanceof tokenize_css.ErrorToken)
            this.errors_.push(maybeSelector);

        /** @type {!Array<!Selector>} */
    const toVisit = [maybeSelector];
        while (toVisit.length > 0) {
            /** @type {!Selector} */
      const node = toVisit.shift();
            node.accept(this);
            node.forEachChild(child => {
                    toVisit.push(child);
      });
        }
    }

    private final List<ErrorToken> errors;
}
