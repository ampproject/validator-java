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

import dev.amp.validator.css.Declaration;

import javax.annotation.Nonnull;
import java.util.List;

public class ImportantPropertyVisitor implements RuleVisitor {
    /**
     * walk through list of declarations
     *
     * @param important list of declarations
     */
    public ImportantPropertyVisitor(@Nonnull final List<Declaration> important) {
        super();
        this.important = important;
    }

    /**
     * visits a declaration
     *
     * @param declaration to visit
     */
    public void visitDeclaration(@Nonnull final Declaration declaration) {
        if (declaration.getImportant()) {
            this.important.add(declaration);
        }
    }

    /**
     * the list belonging to visitor
     */
    private final List<Declaration> important;
}
