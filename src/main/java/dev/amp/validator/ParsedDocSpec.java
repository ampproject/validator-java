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

package dev.amp.validator;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Wrapper around DocSpec.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class ParsedDocSpec {

    /**
     * @param spec
     */
    public ParsedDocSpec(@Nonnull final ValidatorProtos.DocSpec spec) {
        /**
         * @type {!generated.DocSpec}
         * @private
         */
        this.spec = spec;
    }

    /**
     * getter for doc spec
     * @return this spec
     */
    public ValidatorProtos.DocSpec spec() {
        return this.spec;
    }

    /**
     * get the disabledBy from this spec
     * @return this disabled by list
     */
    public List<String> disabledBy() {
        return this.spec.getDisabledByList();
    }

    /**
     * get the enabledBy from this spec
     * @return this enabled by list
     */
    public List<String> enabledBy() {
        return this.spec.getEnabledByList();
    }

    /**
     *
     */
    private ValidatorProtos.DocSpec spec;
}
