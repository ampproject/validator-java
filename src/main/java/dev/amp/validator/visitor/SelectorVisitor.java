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

import com.steadystate.css.parser.Token;
import dev.amp.validator.ValidatorProtos;
import dev.amp.validator.css.TokenType;
import dev.amp.validator.selector.Combinator;
import dev.amp.validator.css.CssTokenUtil;
import dev.amp.validator.css.CssValidationException;
import dev.amp.validator.css.ErrorToken;
import dev.amp.validator.css.QualifiedRule;
import dev.amp.validator.css.TokenStream;
import dev.amp.validator.selector.AttrSelector;
import dev.amp.validator.selector.ClassSelector;
import dev.amp.validator.selector.IdSelector;
import dev.amp.validator.selector.PseudoSelector;
import dev.amp.validator.selector.Selector;
import dev.amp.validator.selector.SelectorException;
import dev.amp.validator.selector.SelectorsGroup;
import dev.amp.validator.selector.SimpleSelectorSequence;
import dev.amp.validator.selector.TypeSelector;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static dev.amp.validator.css.CssTokenUtil.copyPosTo;
import static dev.amp.validator.css.CssTokenUtil.getTokenType;
import static dev.amp.validator.utils.SelectorUtils.isSimpleSelectorSequenceStart;
import static dev.amp.validator.utils.SelectorUtils.parseASelector;

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

public abstract class SelectorVisitor implements RuleVisitor {
    /**
     * @param errors an array of ErrorTokens
     */
    public SelectorVisitor(@Nonnull final List<ErrorToken> errors) {
        super();
        this.errors = errors;
    }

    /**
     * @param qualifiedRule a QualifiedRule object
     * @throws CssValidationException a css validation exception
     */
    public void visitQualifiedRule(final QualifiedRule qualifiedRule) throws CssValidationException {
        final TokenStream tokenStream = new TokenStream(qualifiedRule.getPrelude());
        tokenStream.consume();

        Selector maybeSelector;
        try {
            maybeSelector = parseASelectorsGroup(tokenStream);
        } catch (final SelectorException selectorException) {
            errors.add(selectorException.getErrorToken());
            return;
        }

        ArrayDeque<Selector> selectorQueue = new ArrayDeque<>();
        selectorQueue.push(maybeSelector);
        final ArrayDeque<Selector> toVisit = selectorQueue;

        while (!toVisit.isEmpty()) {

            final Selector node = toVisit.pop();
            node.accept(this);
            node.forEachChild(child -> toVisit.add(child));
        }
    }

    private final List<ErrorToken> errors;

    /**
     * The selectors_group production from
     * http://www.w3.org/TR/css3-selectors/#grammar.
     * In addition, this parsing routine checks that no input remains,
     * that is, after parsing the production we reached the end of |token_stream|.
     *
     * @param tokenStream to work with
     * @return selectors group from top of stream
     * @throws CssValidationException CssValidationException
     * @throws SelectorException SelectorException
     */
    public static Selector parseASelectorsGroup(@Nonnull final TokenStream tokenStream) throws CssValidationException, SelectorException {
        if (!isSimpleSelectorSequenceStart(tokenStream.current())) {
            final List<String> params = new ArrayList<>();
            params.add("style");
            final ErrorToken errorToken = new ErrorToken(
                    ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_MEDIA_TYPE,
                    params);
            throw new SelectorException((ErrorToken) CssTokenUtil.copyPosTo(tokenStream.current(), errorToken));
        }

        final Token start = tokenStream.current();
        Selector selector;

        selector = parseASelector(tokenStream);

        ArrayDeque<Selector> elements = new ArrayDeque<>();
        elements.add(selector);

        while (true) {
            if (getTokenType(tokenStream.current()) == TokenType.WHITESPACE) {
                tokenStream.consume();
            }
            if (getTokenType(tokenStream.current()) == TokenType.COMMA) {
                tokenStream.consume();
                if (getTokenType(tokenStream.current()) == TokenType.WHITESPACE) {
                    tokenStream.consume();
                }
                elements.push(parseASelector(tokenStream));
                continue;
            }
            // We're about to claim success and return a selector,
            // but before we do, we check that no unparsed input remains.
            if (!(getTokenType(tokenStream.current()) == TokenType.EOF_TOKEN)) {
                final List<String> params = new ArrayList<>();
                params.add("style");
                final ErrorToken errorToken = new ErrorToken(
                        ValidatorProtos.ValidationError.Code.CSS_SYNTAX_UNPARSED_INPUT_REMAINS_IN_SELECTOR,
                        params);
                throw new SelectorException(errorToken);
            }
            if (elements.size() == 1) {
                return elements.getFirst();
            }
            return (SelectorsGroup) copyPosTo(start, new SelectorsGroup(elements));
        }
    }

    /**
     *
     * @param typeSelector
     */
    public abstract void visitTypeSelector(@Nonnull TypeSelector typeSelector);

    /**
     *
     * @param idSelector
     */
    public abstract void visitIdSelector(@Nonnull IdSelector idSelector);

    /**
     *
     * @param attrSelector
     * @throws CssValidationException CssValidationException
     */
    public abstract void visitAttrSelector(@Nonnull AttrSelector attrSelector) throws CssValidationException;

    /**
     *
     * @param pseudoSelector
     * @throws CssValidationException CssValidationException
     */
    public abstract void visitPseudoSelector(@Nonnull PseudoSelector pseudoSelector) throws CssValidationException;

    /**
     *
     * @param classSelector
     */
    public abstract void visitClassSelector(@Nonnull ClassSelector classSelector);

    /**
     *
     * @param sequence
     */
    public abstract void visitSimpleSelectorSequence(@Nonnull SimpleSelectorSequence sequence);

    /**
     *
     * @param combinator
     */
    public abstract void visitCombinator(@Nonnull Combinator combinator);

    /**
     *
     * @param selector
     */
    public abstract void visitSelectorsGroup(@Nonnull Selector selector);
}
