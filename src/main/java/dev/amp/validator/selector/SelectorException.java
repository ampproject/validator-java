package dev.amp.validator.selector;

import dev.amp.validator.css.ErrorToken;

/**
 * Selector validation exception.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SelectorException extends Exception {
    /**
     * Constructor.
     * @param message the message.
     */
    public SelectorException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param errorToken the token.
     */
    public SelectorException(final ErrorToken errorToken) {
        this.errorToken = errorToken;
    }


    public ErrorToken getErrorToken() {
        return errorToken;
    }

    private ErrorToken errorToken;
}
