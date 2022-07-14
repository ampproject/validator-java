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

package dev.amp.validator.utils;

import com.steadystate.css.parser.Token;
import dev.amp.validator.ValidatorProtos;
import dev.amp.validator.css.CssTokenUtil;
import dev.amp.validator.css.CssValidationException;
import dev.amp.validator.css.EOFToken;
import dev.amp.validator.css.ErrorToken;
import dev.amp.validator.css.TokenStream;
import dev.amp.validator.css.TokenType;
import dev.amp.validator.selector.AttrSelector;
import dev.amp.validator.selector.ClassSelector;
import dev.amp.validator.selector.Combinator;
import dev.amp.validator.selector.IdSelector;
import dev.amp.validator.selector.PseudoSelector;
import dev.amp.validator.selector.Selector;
import dev.amp.validator.selector.SelectorException;
import dev.amp.validator.selector.SimpleSelectorSequence;
import dev.amp.validator.selector.TypeSelector;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static dev.amp.validator.css.Canonicalizer.consumeAFunction;
import static dev.amp.validator.css.CssTokenUtil.copyPosTo;
import static dev.amp.validator.css.CssTokenUtil.getTokenType;

/**
 * Methods to handle selector validation.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public final class SelectorUtils {
    /**
     * Private constructor.
     */
    private SelectorUtils() {
    }

    /**
     * Whether or not the provided token could be the start of a simple
     * selector sequence. See the simple_selector_sequence production in
     * http://www.w3.org/TR/css3-selectors/#grammar.
     *
     * @param token to analyze
     * @return is SimpleSelector Sequence Start
     */
    public static boolean isSimpleSelectorSequenceStart(@Nonnull final com.steadystate.css.parser.Token token) {
        // Type selector start.
        if (isDelim(token, "*") || isDelim(token, "|") || (getTokenType(token) == TokenType.IDENT)) {
            return true;
        }
        // Id selector start.
        if (getTokenType(token) == TokenType.HASH) {
            return true;
        }
        // Class selector start.
        if (isDelim(token, ".")) {
            return true;
        }
        // Attr selector start.
        if (getTokenType(token) == TokenType.OPEN_SQUARE) {
            return true;
        }
        // A pseudo selector.
        if (getTokenType(token) == TokenType.COLON) {
            return true;
        }
        // TODO(johannes): add the others.
        return false;
    }

    /**
     * Helper function for determining whether the provided token is a specific
     * delimiter.
     *
     * @param token     to analyze
     * @param delimChar to check
     * @return if this is a delim char
     */
    public static boolean isDelim(@Nonnull final com.steadystate.css.parser.Token token, @Nonnull final String delimChar) {
        return getTokenType(token) == TokenType.DELIM && (token.image.trim()).equals(delimChar);
    }

    /**
     * tokenStream.current must be the first token of the sequence.
     * This function will return an error if no selector is found.
     *
     * @param tokenStream token stream
     * @return the SimpleSelectorSequence instance
     * @throws CssValidationException the CssValidationException
     * @throws SelectorException the SelectorException
     */
    public static SimpleSelectorSequence parseASimpleSelectorSequence(@Nonnull final TokenStream tokenStream)
            throws SelectorException, CssValidationException {
        final Token start = tokenStream.current();
        TypeSelector typeSelector = null;
        if (isDelim(tokenStream.current(), "*")
                || isDelim(tokenStream.current(), "|")
                || getTokenType(tokenStream.current()) == TokenType.IDENT) {
            typeSelector = parseATypeSelector(tokenStream);
        }
        final List<Selector> otherSelectors = new ArrayList<>();
        while (true) {
            if (getTokenType(tokenStream.current()) == TokenType.HASH) {
                otherSelectors.add(parseAnIdSelector(tokenStream));
            } else if (isDelim(tokenStream.current(), ".")
                    && getTokenType(tokenStream.next()) == TokenType.IDENT) {
                otherSelectors.add(parseAClassSelector(tokenStream));
            } else if (getTokenType(tokenStream.current()) == TokenType.OPEN_SQUARE) {
                AttrSelector maybeAttrSelector = parseAnAttrSelector(tokenStream);
                otherSelectors.add(maybeAttrSelector);
            } else if (getTokenType(tokenStream.current()) == TokenType.COLON) {
                PseudoSelector maybePseudo;
                try {
                    maybePseudo = parseAPseudoSelector(tokenStream);
                } catch (SelectorException selectorException) {
                    throw selectorException;
                }

                otherSelectors.add(maybePseudo);
                // NOTE: If adding more 'else if' clauses here, be sure to udpate
                // isSimpleSelectorSequenceStart accordingly.
            } else {
                if (typeSelector == null) {
                    if (otherSelectors.size() == 0) {
                        final List<String> params = new ArrayList<>();
                        params.add("style");
                        final ErrorToken errorToken = new ErrorToken(
                                ValidatorProtos.ValidationError.Code.CSS_SYNTAX_MISSING_SELECTOR,
                                params);
                        throw new SelectorException((ErrorToken) copyPosTo(tokenStream.current(), errorToken));
                    }
                    // If no type selector is given then the universal selector is
                    // implied.
                    typeSelector = (TypeSelector) copyPosTo(start, new TypeSelector(
                            /*namespacePrefix=*/ null, /*elementName=*/ "*"));
                }
                return (SimpleSelectorSequence) copyPosTo(start, new SimpleSelectorSequence(typeSelector, otherSelectors));
            }
        }
    }

    /**
     * tokenStream.current() must be the ColonToken. Returns an error if
     * the pseudo token can't be parsed (e.g., a lone ':').
     *
     * @param tokenStream token stream
     * @return the pseudo selector
     * @throws CssValidationException the CssValidationException
     * @throws SelectorException the SelectorException
     */
    private static PseudoSelector parseAPseudoSelector(@Nonnull final TokenStream tokenStream)
            throws SelectorException, CssValidationException {
        if (getTokenType(tokenStream.current()) != TokenType.COLON) {
            throw new SelectorException("Precondition violated: must be a \":\"");
        }
        final Token firstColon = tokenStream.current();
        tokenStream.consume();
        boolean isClass = true;
        if (getTokenType(tokenStream.current()) == TokenType.COLON) {
            // '::' starts a pseudo element, ':' starts a pseudo class.
            isClass = false;
            tokenStream.consume();
        }
        if (getTokenType(tokenStream.current()) == TokenType.IDENT) {
            final Token ident = tokenStream.current();
            String name = ident.image;
            tokenStream.consume();
            return (PseudoSelector) copyPosTo(firstColon, new PseudoSelector(isClass, name, new ArrayList<>()));
        } else if (getTokenType(tokenStream.current()) == TokenType.FUNCTION_TOKEN) {
            Token funcToken = tokenStream.current();
            List<ErrorToken> errors = new ArrayList<>();
            final List<Token> func = extractAFunction(tokenStream, errors);
            if (errors.size() > 0) {
                throw new SelectorException(errors.get(0));
            }
            tokenStream.consume();
            return (PseudoSelector) copyPosTo(firstColon, new PseudoSelector(isClass, funcToken.image, func));
        } else {
            final List<String> params = new ArrayList<>();
            params.add("style");
            final ErrorToken errorToken = new ErrorToken(
                    ValidatorProtos.ValidationError.Code.CSS_SYNTAX_ERROR_IN_PSEUDO_SELECTOR,
                    params);
            throw new SelectorException((ErrorToken) copyPosTo(tokenStream.current(), errorToken));
        }
    }

    /**
     * The selector production from
     * http://www.w3.org/TR/css3-selectors/#grammar
     * Returns an ErrorToken if no selector is found.
     *
     * @param tokenStream token stream
     * @return the selector
     * @throws CssValidationException the CssValidationException
     * @throws SelectorException the SelectorException
     */
    public static Selector parseASelector(@Nonnull final TokenStream tokenStream)
            throws CssValidationException, SelectorException {
        if (!isSimpleSelectorSequenceStart(tokenStream.current())) {
            final List<String> params = new ArrayList<>();
            params.add("style");
            final ErrorToken errorToken = new ErrorToken(
                    ValidatorProtos.ValidationError.Code.CSS_SYNTAX_NOT_A_SELECTOR_START,
                    params);
            throw new SelectorException((ErrorToken) copyPosTo(tokenStream.current(), errorToken));
        }

        SimpleSelectorSequence parsed;

        try {
            parsed = parseASimpleSelectorSequence(tokenStream);
        } catch (final SelectorException selectorException) {
            throw selectorException;
        } catch (final CssValidationException cssValidationException) {
            throw cssValidationException;
        }

        Selector left = parsed;
        while (true) {
            // Consume whitespace in front of combinators, while being careful
            // to not eat away the infamous "whitespace operator" (sigh, haha).
            if ((getTokenType(tokenStream.current()) == TokenType.WHITESPACE && !isSimpleSelectorSequenceStart(tokenStream.next()))) {
                tokenStream.consume();
            }
            // If present, grab the combinator token which we'll use for line
            // / column info.
            if (!(((getTokenType(tokenStream.current()) == TokenType.WHITESPACE && isSimpleSelectorSequenceStart(tokenStream.next()))
                    || isDelim(tokenStream.current(), "+")
                    || isDelim(tokenStream.current(), ">")
                    || isDelim(tokenStream.current(), "~")))) {
                return left;
            }
            final Token combinatorToken = tokenStream.current();
            tokenStream.consume();
            if (getTokenType(tokenStream.current()) == TokenType.WHITESPACE) {
                tokenStream.consume();
            }

            final SimpleSelectorSequence right = parseASimpleSelectorSequence(tokenStream);

            left = (Selector) copyPosTo(combinatorToken, new Combinator(
                    combinatorTypeForToken(combinatorToken), left, right));
        }
    }

    /**
     * The CombinatorType for a given token; helper function used when
     * constructing a Combinator instance.
     *
     * @param token the token
     * @return the combinator type
     * @throws CssValidationException the CssValidationException
     */
    public static Combinator.CombinatorType combinatorTypeForToken(@Nonnull final Token token) throws CssValidationException {
        if (getTokenType(token) == TokenType.WHITESPACE) {
            return Combinator.CombinatorType.DESCENDANT;
        } else if (isDelim(token, ">")) {
            return Combinator.CombinatorType.CHILD;
        } else if (isDelim(token, "+")) {
            return Combinator.CombinatorType.ADJACENT_SIBLING;
        } else if (isDelim(token, "~")) {
            return Combinator.CombinatorType.GENERAL_SIBLING;
        }

        throw new CssValidationException("'Internal Error: not a combinator token");
    }

    /**
     * tokenStream.current() is the first token of the type selector.
     *
     * @param tokenStream token stream
     * @return the type selector
     */
    public static TypeSelector parseATypeSelector(@Nonnull final TokenStream tokenStream) {
        String namespacePrefix = null;
        String elementName = "*";
        Token start = tokenStream.current();

        if (isDelim(tokenStream.current(), "|")) {
            namespacePrefix = "";
            tokenStream.consume();
        } else if (isDelim(tokenStream.current(), "*") && isDelim(tokenStream.next(), "|")) {
            namespacePrefix = "*";
            tokenStream.consume();
            tokenStream.consume();
        } else if (getTokenType(tokenStream.current()) == TokenType.IDENT && isDelim(tokenStream.next(), "|")) {
            Token ident = tokenStream.current();
            namespacePrefix = ident.image;
            tokenStream.consume();
            tokenStream.consume();
        }
        if (isDelim(tokenStream.current(), "*")) {
            elementName = "*";
            tokenStream.consume();
        } else if (getTokenType(tokenStream.current()) == TokenType.IDENT) {
            Token ident = tokenStream.current();
            elementName = ident.image;
            tokenStream.consume();
        }
        return (TypeSelector) copyPosTo(start, new TypeSelector(namespacePrefix, elementName));
    }

    /**
     * tokenStream.current() must be the hash token.
     *
     * @param tokenStream token stream
     * @return the selector
     * @throws SelectorException the SelectorException
     */
    private static Selector parseAnIdSelector(@Nonnull final TokenStream tokenStream) throws SelectorException {
        if (getTokenType(tokenStream.current()) != TokenType.HASH) {
            throw new SelectorException("Precondition violated: must start with HashToken");
        }

        Token hash = tokenStream.current();
        tokenStream.consume();
        return (Selector) copyPosTo(hash, new IdSelector(hash.image));
    }

    /**
     * tokenStream.current() must be the '.' delimiter token.
     *
     * @param tokenStream token stream
     * @return the class selector
     * @throws SelectorException the SelectorException
     */
    private static ClassSelector parseAClassSelector(@Nonnull final TokenStream tokenStream) throws
            SelectorException {
        if (getTokenType(tokenStream.next()) != TokenType.IDENT || !isDelim(tokenStream.current(), ".")) {
            throw new SelectorException("Precondition violated: must start with \".\" and follow with ident");
        }

        Token dot = tokenStream.current();
        tokenStream.consume();
        Token ident = tokenStream.current();
        tokenStream.consume();
        return (ClassSelector) copyPosTo(dot, new ClassSelector(ident.image));
    }

    /**
     * tokenStream.current() must be the open square token.
     *
     * @param tokenStream token stream
     * @return attribute selector
     * @throws CssValidationException the CssValidationException
     * @throws SelectorException the SelectorException
     */
    public static AttrSelector parseAnAttrSelector(@Nonnull final TokenStream tokenStream) throws
            SelectorException, CssValidationException {
        if (getTokenType(tokenStream.current()) != TokenType.OPEN_SQUARE) {
            throw new SelectorException("Precondition violated: must be an OpenSquareToken");
        }

        Token start = tokenStream.current();

        tokenStream.consume();  // Consumes '['.
        if (getTokenType(tokenStream.current()) == TokenType.WHITESPACE) {
            tokenStream.consume();
        }

        // This part is defined in https://www.w3.org/TR/css3-selectors/#attrnmsp:
        // Attribute selectors and namespaces. It is similar to parseATypeSelector.
        String namespacePrefix = null;
        if (isDelim(tokenStream.current(), "|")) {
            namespacePrefix = "";
            tokenStream.consume();
        } else if (isDelim(tokenStream.current(), "*") && isDelim(tokenStream.next(), "|")) {
            namespacePrefix = "*";
            tokenStream.consume();
            tokenStream.consume();
        } else if (getTokenType(tokenStream.current()) == TokenType.IDENT && isDelim(tokenStream.next(), "|")) {
            final Token ident = tokenStream.current();
            namespacePrefix = ident.image;
            tokenStream.consume();
            tokenStream.consume();
        }
        // Now parse the attribute name. This part is mandatory.
        if (getTokenType(tokenStream.current()) != TokenType.IDENT) {
            final List<String> params = new ArrayList<>();
            params.add("style");
            final ErrorToken errorToken = new ErrorToken(
                    ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_ATTR_SELECTOR,
                    params);
            CssTokenUtil.copyPosTo(tokenStream.current(), errorToken);
            throw new SelectorException(errorToken);
        }
        Token ident = tokenStream.current();
        String attrName = ident.image;
        tokenStream.consume();
        if (getTokenType(tokenStream.current()) == TokenType.WHITESPACE) {
            tokenStream.consume();
        }

        // After the attribute name, we may see an operator; if we do, then
        // we must see either a string or an identifier. This covers
        // 6.3.1 Attribute presence and value selectors
        // (https://www.w3.org/TR/css3-selectors/#attribute-representation) and
        // 6.3.2 Substring matching attribute selectors
        // (https://www.w3.org/TR/css3-selectors/#attribute-substrings).

        /** @type {string} */
        String matchOperator = "";
        TokenType current = getTokenType(tokenStream.current());
        if (isDelim(tokenStream.current(), "=")) {
            matchOperator = "=";
            tokenStream.consume();
        } else if (current == TokenType.INCLUDE_MATCH) {
            matchOperator = "~=";
            tokenStream.consume();
        } else if (current == TokenType.DASH_MATCH) {
            matchOperator = "|=";
            tokenStream.consume();
        } else if (current == TokenType.PREFIX_MATCH) {
            matchOperator = "^=";
            tokenStream.consume();
        } else if (current == TokenType.SUFFIX_MATCH) {
            matchOperator = "$=";
            tokenStream.consume();
        } else if (current == TokenType.SUBSTRING_MATCH) {
            matchOperator = "*=";
            tokenStream.consume();
        }
        if (getTokenType(tokenStream.current()) == TokenType.WHITESPACE) {
            tokenStream.consume();
        }

        String value = "";
        if (!matchOperator.equals("")) {  // If we saw an operator, parse the value.
            current = getTokenType(tokenStream.current());
            if (current == TokenType.IDENT) {
                ident = tokenStream.current();
                value = ident.image;
                tokenStream.consume();
            } else if (current == TokenType.STRING) {
                value = tokenStream.current().image;
                tokenStream.consume();
            }
        }
        if (getTokenType(tokenStream.current()) == TokenType.WHITESPACE) {
            tokenStream.consume();
        }
        // The attribute selector must in any case terminate with a close square
        // token.
        if (getTokenType(tokenStream.current()) != TokenType.CLOSE_SQUARE) {
            final List<String> params = new ArrayList<>();
            params.add("style");
            final ErrorToken errorToken = new ErrorToken(
                    ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_ATTR_SELECTOR,
                    params);
            CssTokenUtil.copyPosTo(tokenStream.current(), errorToken);
            throw new SelectorException(errorToken);
        }
        tokenStream.consume();
        final AttrSelector selector =
                new AttrSelector(namespacePrefix, attrName, matchOperator, value);
        return (AttrSelector) copyPosTo(start, selector);
    }

    /**
     * Returns a function's contents in tokenList, including the leading
     * FunctionToken, but excluding the trailing CloseParen token and
     * appended with an EOFToken instead.
     *
     * @param tokenStream token stream
     * @param errors array of error tokens
     * @return the list of token
     * @throws CssValidationException the CssValidationException
     */
    public static List<Token> extractAFunction(@Nonnull final TokenStream tokenStream,
                                               @Nonnull final List<ErrorToken> errors) throws CssValidationException {
        final List<Token> consumedTokens = new ArrayList<>();
        if (!consumeAFunction(tokenStream, consumedTokens, /*depth*/ 0)) {
            final List<String> params = new ArrayList<>();
            params.add("style");
            final ErrorToken errorToken = new ErrorToken(
                    ValidatorProtos.ValidationError.Code.CSS_EXCESSIVELY_NESTED,
                    params);
            CssTokenUtil.copyPosTo(tokenStream.current(), errorToken);
            errors.add(errorToken);
        }
        // A function always has a start FunctionToken and
        // either a CloseParenToken or EOFToken.
        if (consumedTokens.size() < 2) {
            throw new CssValidationException("Internal Error: extractAFunction precondition not met");
        }

        // Convert end token to EOF.
        int end = consumedTokens.size() - 1;
        consumedTokens.set(end, copyPosTo(consumedTokens.get(end), new EOFToken()));
        return consumedTokens;
    }
}
