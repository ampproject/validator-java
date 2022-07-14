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

import dev.amp.validator.css.TokenType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayDeque;

/**
 * Test for {@link SelectorsGroup}
 *
 * @author Jacob James
 */
public class SelectorsGroupTest {
    @Test
    public void getSelectorsGroup() {
        ArrayDeque<Selector> elements = new ArrayDeque<Selector>();
        SelectorsGroup alpha = new SelectorsGroup(elements);
        TokenType beta = TokenType.SELECTORS_GROUP;
        Assert.assertEquals(alpha.getTokenType(), beta);
        Assert.assertEquals(alpha.getElements(), elements);
        /**Ask what does lamba function do*/

    }
}
