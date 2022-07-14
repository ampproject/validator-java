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
