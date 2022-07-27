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

package dev.amp.validator.visitor;

import dev.amp.validator.ValidatorProtos;
import dev.amp.validator.css.CssValidationException;
import dev.amp.validator.css.ErrorToken;
import dev.amp.validator.selector.AttrSelector;
import dev.amp.validator.selector.ClassSelector;
import dev.amp.validator.selector.Combinator;
import dev.amp.validator.selector.IdSelector;
import dev.amp.validator.selector.PseudoSelector;
import dev.amp.validator.selector.Selector;
import dev.amp.validator.selector.SimpleSelectorSequence;
import dev.amp.validator.selector.TypeSelector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static dev.amp.validator.css.CssTokenUtil.copyPosTo;

/**
 * A visitor for selector.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SelectorSpecVisitor extends SelectorVisitor {

    /**
     * @param errorBuffer an array of ErrorTokens
     * @param spec the underlying selector spec
     */
    public SelectorSpecVisitor(@Nonnull final ValidatorProtos.SelectorSpec spec,
                               @Nonnull final List<ErrorToken> errorBuffer) {
        super(errorBuffer);
        this.selectorSpec = spec;
        this.errorBuffer = errorBuffer;
    }

    @Override
    public void visitTypeSelector(@Nonnull final TypeSelector typeSelector) {
    }

    @Override
    public void visitIdSelector(@Nonnull final IdSelector idSelector) {
    }

    /**
     * visitor for attr selector
     * @param attrSelector to visit
     * @throws CssValidationException CssValidationException
     */
    @Override
    public void visitAttrSelector(@Nonnull final AttrSelector attrSelector) throws CssValidationException {
        for (final String allowedName : this.selectorSpec.getAttributeNameList()) {
            if (allowedName.equals("*") || allowedName.equals(attrSelector.getAttrName())) {
                return;
            }
        }

        final List<String> params = new ArrayList<>();
        params.add("");
        params.add(attrSelector.getAttrName());

        final ErrorToken errorToken = new ErrorToken(
                ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_ATTR_SELECTOR,
                params);
        copyPosTo(attrSelector, errorToken);
        this.errorBuffer.add(errorToken);
    }

    /**
     * visitor for pseudo selector
     * @param pseudoSelector to visit
     * @throws CssValidationException CssValidationException
     */
    @Override
    public void visitPseudoSelector(@Nonnull final PseudoSelector pseudoSelector) throws CssValidationException {
        if (pseudoSelector.isClass()) {  // pseudo-class
            for (final String allowedPseudoClass : this.selectorSpec.getPseudoClassList()) {
                if (allowedPseudoClass.equals("*") || allowedPseudoClass.equals(pseudoSelector.getName())) {
                    return;
                }
            }
            final List<String> params = new ArrayList<>();
            params.add("");
            params.add(pseudoSelector.getName());

            final ErrorToken errorToken = new ErrorToken(
                    ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_PSEUDO_CLASS,
                    params);
            copyPosTo(pseudoSelector, errorToken);
            this.errorBuffer.add(errorToken);
        } else {  // pseudo-element
            for (final String allowedPseudoElement : this.selectorSpec.getPseudoElementList()) {
                if (allowedPseudoElement.equals("*")
                        || allowedPseudoElement.equals(pseudoSelector.getName())) {
                    return;
                }
            }
            final List<String> params = new ArrayList<>();
            params.add("");
            params.add(pseudoSelector.getName());

            final ErrorToken errorToken = new ErrorToken(
                    ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_PSEUDO_ELEMENT,
                    params);
            copyPosTo(pseudoSelector, errorToken);
            this.errorBuffer.add(errorToken);
        }
    }

    @Override
    public void visitClassSelector(@Nonnull final ClassSelector classSelector) {
    }

    @Override
    public void visitSimpleSelectorSequence(@Nonnull final SimpleSelectorSequence sequence) {
    }

    @Override
    public void visitCombinator(@Nonnull final Combinator combinator) {
    }

    @Override
    public void visitSelectorsGroup(@Nonnull final Selector selector) {
    }

    /**
     * underlying selector spec
     */
    private final ValidatorProtos.SelectorSpec selectorSpec;

    /**
     * error buffer
     */
    private final List<ErrorToken> errorBuffer;
}
