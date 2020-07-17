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

package dev.amp.validator.utils;

import dev.amp.validator.css.ErrorToken;
import dev.amp.validator.css.TokenStream;
import dev.amp.validator.css.TokenType;
import dev.amp.validator.selector.Selector;

import javax.annotation.Nonnull;

import java.util.List;

import static dev.amp.validator.css.CssTokenUtil.getTokenType;

public class SelectorUtils {
    /**
     * Whether or not the provided token could be the start of a simple
     * selector sequence. See the simple_selector_sequence production in
     * http://www.w3.org/TR/css3-selectors/#grammar.
     * @param token to analyze
     * @return is SimpleSelector Sequence Start
     */
    public static boolean isSimpleSelectorSequenceStart(@Nonnull final com.steadystate.css.parser.Token token) {

        // Type selector start.
        if (isDelim(token, "*") || isDelim(token, "|") ||
                (getTokenType(token) == TokenType.IDENT)) {
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
     * @param token to analyze
     * @param delimChar to check
     * @return if this is a delim char
     */
    public static boolean isDelim(@Nonnull final com.steadystate.css.parser.Token token, @Nonnull final String delimChar) {
        return getTokenType(token) == TokenType.DELIM && (token.getValue()).equals(delimChar);
    }

    /**
     * The selector production from
     * http://www.w3.org/TR/css3-selectors/#grammar
     * Returns an ErrorToken if no selector is found.
     * @param {!TokenStream} tokenStream
     * @return {!SimpleSelectorSequence|
     *          !Combinator|!tokenize_css.ErrorToken}
     */
    public static Selector parseASelector(@Nonnull final TokenStream tokenStream, @Nonnull final List<ErrorToken> errors) {
//        if (!isSimpleSelectorSequenceStart(tokenStream.current())) {
//            return tokenStream.current().copyPosTo(new tokenize_css.ErrorToken(
//                    ValidationError.Code.CSS_SYNTAX_NOT_A_SELECTOR_START, ['style']));
//        }
//  const parsed = parseASimpleSelectorSequence(tokenStream);
//        if (parsed.tokenType === tokenize_css.TokenType.ERROR) {
//            return parsed;
//        }
//        let left = /** @type {!SimpleSelectorSequence}*/ (parsed);
//        while (true) {
//            // Consume whitespace in front of combinators, while being careful
//            // to not eat away the infamous "whitespace operator" (sigh, haha).
//            if ((tokenStream.current().tokenType ===
//                    tokenize_css.TokenType.WHITESPACE) &&
//                    !isSimpleSelectorSequenceStart(tokenStream.next())) {
//                tokenStream.consume();
//            }
//            // If present, grab the combinator token which we'll use for line
//            // / column info.
//            if (!(((tokenStream.current().tokenType ===
//                    tokenize_css.TokenType.WHITESPACE) &&
//                    isSimpleSelectorSequenceStart(tokenStream.next())) ||
//                    isDelim(tokenStream.current(), '+') ||
//                    isDelim(tokenStream.current(), '>') ||
//                    isDelim(tokenStream.current(), '~'))) {
//                return left;
//            }
//    const combinatorToken = tokenStream.current();
//            tokenStream.consume();
//            if (tokenStream.current().tokenType === tokenize_css.TokenType.WHITESPACE) {
//                tokenStream.consume();
//            }
//    const right = parseASimpleSelectorSequence(tokenStream);
//            if (right.tokenType === tokenize_css.TokenType.ERROR) {
//                return right;  // TODO(johannes): more than one error / partial tree.
//            }
//            left = combinatorToken.copyPosTo(new Combinator(
//                    combinatorTypeForToken(combinatorToken), left,
//                    /** @type {!SimpleSelectorSequence} */ (right)));
//        }
    }

}
