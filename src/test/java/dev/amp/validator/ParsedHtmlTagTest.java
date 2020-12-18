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

package dev.amp.validator;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.Attributes;

/**
 * Tests for {@link ParsedHtmlTag}
 *
 * @author sphatak01
 */

public class ParsedHtmlTagTest {

    @Test
    public void testgettersSetters() {
        final Attributes attrs = Mockito.mock(Attributes.class);
        Mockito.when(attrs.getLocalName(0)).thenReturn("crossorigin");
        Mockito.when(attrs.getValue(0)).thenReturn("true");

        final ParsedHtmlTag parsedHtmltag = new ParsedHtmlTag("TITLE", attrs);

        Assert.assertNull(parsedHtmltag.hasDuplicateAttrs());

        Mockito.when(attrs.getLength()).thenReturn(1);

        Assert.assertFalse(parsedHtmltag.isEmpty());
        Assert.assertEquals(parsedHtmltag.lowerName(), "title");
        Assert.assertEquals(parsedHtmltag.upperName(), "TITLE");
        Assert.assertEquals(parsedHtmltag.attrs(), attrs);

        Assert.assertEquals(parsedHtmltag.attrsByKey().get("crossorigin"), "true");

        // second time should be populated
        Assert.assertEquals(parsedHtmltag.attrsByKey().get("crossorigin"), "true");

        Assert.assertNull(parsedHtmltag.hasDuplicateAttrs());

        Assert.assertEquals(parsedHtmltag.getValue("crossorigin", 0), "true");

        Mockito.when(attrs.getLength()).thenReturn(2);
        Mockito.when(attrs.getLocalName(1)).thenReturn("crossorigin");
        Mockito.when(attrs.getValue(1)).thenReturn("false");

        Assert.assertEquals(parsedHtmltag.hasDuplicateAttrs(), "crossorigin");

        parsedHtmltag.cleanup();

    }

    @Test
    public void testIsModuleLtsScriptTag() {
        final Attributes attrs = Mockito.mock(Attributes.class);
        Mockito.when(attrs.getLocalName(0)).thenReturn("type");
        Mockito.when(attrs.getValue(0)).thenReturn("module");
        Mockito.when(attrs.getLocalName(1)).thenReturn("src");
        Mockito.when(attrs.getValue(1)).thenReturn("https://cdn.ampproject.org/lts/v0/amp-ad-0.1.mjs");
        Mockito.when(attrs.getLocalName(2)).thenReturn("async");
        Mockito.when(attrs.getValue(2)).thenReturn("");

        Mockito.when(attrs.getLength()).thenReturn(3);

        final ParsedHtmlTag parsedHtmltag = new ParsedHtmlTag("SCRIPT", attrs);
       Assert.assertTrue(parsedHtmltag.isModuleLtsScriptTag());
    }

    @Test
    public void testIsNomoduleLtsScriptTag() {
        final Attributes attrs = Mockito.mock(Attributes.class);
        Mockito.when(attrs.getLocalName(0)).thenReturn("nomodule");
        Mockito.when(attrs.getValue(0)).thenReturn("");
        Mockito.when(attrs.getLocalName(1)).thenReturn("src");
        Mockito.when(attrs.getValue(1)).thenReturn("https://cdn.ampproject.org/lts/v0/amp-ad-0.1.js");
        Mockito.when(attrs.getLocalName(2)).thenReturn("async");
        Mockito.when(attrs.getValue(2)).thenReturn("");

        Mockito.when(attrs.getLength()).thenReturn(3);

        final ParsedHtmlTag parsedHtmltag = new ParsedHtmlTag("SCRIPT", attrs);
        Assert.assertTrue(parsedHtmltag.isNomoduleLtsScriptTag());
    }

    @Test
    public void testIsModuleScriptTag() {
        final Attributes attrs = Mockito.mock(Attributes.class);
        Mockito.when(attrs.getLocalName(0)).thenReturn("type");
        Mockito.when(attrs.getValue(0)).thenReturn("module");
        Mockito.when(attrs.getLocalName(1)).thenReturn("src");
        Mockito.when(attrs.getValue(1)).thenReturn("https://cdn.ampproject.org/v0/amp-ad-0.1.mjs");
        Mockito.when(attrs.getLocalName(2)).thenReturn("async");
        Mockito.when(attrs.getValue(2)).thenReturn("");

        Mockito.when(attrs.getLength()).thenReturn(3);

        final ParsedHtmlTag parsedHtmltag = new ParsedHtmlTag("SCRIPT", attrs);
        Assert.assertTrue(parsedHtmltag.isModuleScriptTag());
    }

    @Test
    public void testIsNomoduleScriptTag() {
        final Attributes attrs = Mockito.mock(Attributes.class);
        Mockito.when(attrs.getLocalName(0)).thenReturn("nomodule");
        Mockito.when(attrs.getValue(0)).thenReturn("");
        Mockito.when(attrs.getLocalName(1)).thenReturn("src");
        Mockito.when(attrs.getValue(1)).thenReturn("https://cdn.ampproject.org/v0.js");
        Mockito.when(attrs.getLocalName(2)).thenReturn("async");
        Mockito.when(attrs.getValue(2)).thenReturn("");

        Mockito.when(attrs.getLength()).thenReturn(3);

        final ParsedHtmlTag parsedHtmltag = new ParsedHtmlTag("SCRIPT", attrs);
        Assert.assertTrue(parsedHtmltag.isNomoduleScriptTag());
    }

    @Test
    public void testIsLtsScriptTag() {
        final Attributes attrs = Mockito.mock(Attributes.class);
        Mockito.when(attrs.getLocalName(0)).thenReturn("type");
        Mockito.when(attrs.getValue(0)).thenReturn("module");
        Mockito.when(attrs.getLocalName(1)).thenReturn("src");
        Mockito.when(attrs.getValue(1)).thenReturn("https://cdn.ampproject.org/lts/v0.js");
        Mockito.when(attrs.getLocalName(2)).thenReturn("async");
        Mockito.when(attrs.getValue(2)).thenReturn("");

        Mockito.when(attrs.getLength()).thenReturn(3);

        final ParsedHtmlTag parsedHtmltag = new ParsedHtmlTag("SCRIPT", attrs);
        Assert.assertTrue(parsedHtmltag.isLtsScriptTag());
    }
}