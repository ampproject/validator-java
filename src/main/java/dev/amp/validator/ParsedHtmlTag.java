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

import dev.amp.validator.utils.ExtensionsUtils;
import org.xml.sax.Attributes;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.regex.Pattern;

import static dev.amp.validator.utils.ExtensionsUtils.EXTENSION_SCRIPT_NAMES;

/**
 * The AMP HTML ParsedHtmlTag class.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class ParsedHtmlTag {
    /**
     * Constructor.
     *
     * @param tagName    the name of the underlying tag in html document.
     * @param attributes the attributes attached to the element.  If
     *                   there are no attributes, it shall be an empty Attributes object.
     */
    public ParsedHtmlTag(@Nonnull final String tagName, @Nonnull final Attributes attributes) {
        this.tagName = tagName.toUpperCase();
        this.attrs = attributes;
        this.scriptTag = new ScriptTag(this.tagName, this.attrs);
        this.attrsByKey = null;
    }

    /**
     * Lower-case tag name.
     *
     * @return returns a lower case tag name.
     */
    public String lowerName() {
        return this.tagName.toLowerCase();
    }

    /**
     * Returns an array of attributes. Each attribute has two fields: name and
     * value. Name is always lower-case, value is the case from the original
     * document. Values are unescaped.
     *
     * @return returns the attributes.
     */
    public Attributes attrs() {
        return this.attrs;
    }

    /**
     * Returns an object mapping attribute name to attribute value. This is
     * populated lazily, as it's not used for most tags.
     *
     * @return a HashMap of attribute name to attribute value
     */
    public HashMap<String, String> attrsByKey() {
        if (this.attrsByKey == null) {
            this.attrsByKey = new HashMap<>();
            for (int i = 0; i < attrs.getLength(); i++) {
                this.attrsByKey.put(attrs.getLocalName(i), attrs.getValue(i));
            }
        }
        return this.attrsByKey;
    }

    /**
     * Returns a duplicate attribute name if the tag contains two attributes
     * named the same, but with different attribute values. Same attribute name
     * AND value is OK. Returns null if there are no such duplicate attributes.
     *
     * @return returns a duplicate attribute name if the tag contains two attributes named the same.
     */
    public String hasDuplicateAttrs() {
        String lastAttrName = "";
        String lastAttrValue = "";
        for (int i = 0; i < attrs.getLength(); i++) {
            if (lastAttrName.equals(attrs.getLocalName(i))
                    && !lastAttrValue.equals(attrs.getValue(i))) {
                return attrs.getLocalName(i);
            }
            lastAttrName = attrs.getLocalName(i);
            lastAttrValue = attrs.getValue(i);
        }
        return null;
    }

    /**
     * Need to replace the value with an empty string if attr name is equal to the value.
     *
     * @param attrName attr name.
     * @param index    index to the Attributes.
     * @return returns the value.
     */
    public String getValue(@Nonnull final String attrName, final int index) {
        String val = attrs.getValue(index);
        if (val != null && val.equals(attrName.toLowerCase())) {
            return "";
        }

        return val;
    }

    /**
     * Gets the name attribute for an extension script tag.
     *
     * @return the name attribute
     */
    private String extensionScriptNameAttribute() {
        if ("SCRIPT".equals(this.upperName())) {
            for (final String attribute : EXTENSION_SCRIPT_NAMES) {
                if (this.attrsByKey().containsKey(attribute)) {
                    return attribute;
                }
            }
        }
        return "";
    }

    /**
     * Tests if this is an extension script tag.
     *
     * @return if is extension script
     */
    public boolean isExtensionScript() {
        return !this.extensionScriptNameAttribute().isEmpty();
    }

    /**
     * Tests if this is an AMP Cache domain.
     *
     * @param src the source to check
     * @return true iff is an amp cache domain
     */
    public boolean isAmpCacheDomain(@Nonnull final String src) {
        return src.startsWith("https://cdn.ampproject.org/");
    }

    /**
     * Returns the value of a given attribute name. If it does not exist then
     * returns null.
     *
     * @param name value
     * @return value of attribute or null.
     */
    private String getAttrValueOrNull(@Nonnull final String name) {
        return this.attrsByKey().get(name);
    }

    /**
     * Tests if this tag is a script with a src of an AMP domain.
     *
     * @return {boolean}
     */
    public boolean isAmpDomain() {
        return this.scriptTag.isAmpDomain();
    }

    /**
     * Tests if this is the AMP runtime script tag.
     *
     * @return true iff is AMP runtime script tag
     */
    public boolean isAmpRuntimeScript() {
        return this.scriptTag.isRuntime();
    }

    /**
     * Tests if this is an async script tag.
     *
     * @param src the source
     * @return true iff this is an async script tag.
     */
    private boolean isAsyncScriptTag(final String src) {
        return "SCRIPT".equals(this.upperName()) && this.attrsByKey().containsKey("async")
                && src != null;
    }

    /**
     * Method to nullify object values.
     */
    public void cleanup() {
        this.tagName = null;
        this.attrsByKey = null;
    }

    /**
     * Upper-case tag name.
     *
     * @return an upper case tag name.
     */
    public String upperName() {
        return this.tagName.toUpperCase();
    }

    /**
     * Returns true if tag name length is zero.
     *
     * @return true if tag name length is zero.
     */
    public boolean isEmpty() {
        return this.tagName.length() == 0;
    }

    /**
     * Returns the extension name.
     *
     * @return the extension name
     */
    public String getExtensionName() {
        return this.scriptTag.getExtensionName();
    }

    /**
     * Returns the extension version.
     *
     * @return the extension version
     */
    public String getExtensionVersion() {
        return this.scriptTag.getExtensionVersion();
    }

    /**
     * Tests if this tag is a script with a valid AMP script path.
     * @return true iff this tag is a script with a valid AMP script path.
     */
    public boolean hasValidAmpScriptPath() {
        return this.scriptTag.hasValidPath();
    }

    /**
     * Returns the script tag path of the 'src' attribute.
     * @return the script tag path of the 'src' attribute.
     */
    public String getAmpScriptPath() {
        return this.scriptTag.getPath();
    }

    /**
     * Returns the script release version, otherwise ScriptReleaseVersion.UNKNOWN.
     * @return the script release version
     */
    public ExtensionsUtils.ScriptReleaseVersion getScriptReleaseVersion() {
        return this.scriptTag.getReleaseVersion();
    }

    /**
     * The parsed tag name.
     */
    @Nonnull
    private String tagName;

    /**
     * The attributes.
     */
    @Nonnull
    private final Attributes attrs;

    /**
     * The underlying script tag
     */
    @Nonnull
    private final ScriptTag scriptTag;

    /**
     * Lazily allocated map from attribute name to value
     */
    private HashMap<String, String> attrsByKey;

    private static final Pattern MODULE_LTS_SCRIPT_SRC_REGEX =
            Pattern.compile("^https:\\/\\/cdn\\.ampproject\\.org\\/lts\\/(v0|v0/amp-[a-z0-9-]*-[a-z0-9.]*)\\.mjs$",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern NOMODULE_LTS_SCRIPT_SRC_REGEX = Pattern.compile(
            "^https:\\/\\/cdn\\.ampproject\\.org\\/lts\\/(v0|v0/amp-[a-z0-9-]*-[a-z0-9.]*)\\.js$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern MODULE_SCRIPT_SRC_REGEX = Pattern.compile(
            "^https://cdn\\.ampproject\\.org\\/(v0|v0/amp-[a-z0-9-]*-[a-z0-9.]*)\\.mjs$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern NOMODULE_SCRIPT_SRC_REGEX = Pattern.compile(
            "^https:\\/\\/cdn\\.ampproject\\.org\\/(v0|v0/amp-[a-z0-9-]*-[a-z0-9.]*)\\.js$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern LTS_SCRIPT_SRC_REGEX = Pattern.compile(
            "/lts\\/(v0|v0/amp-[a-z0-9-]*-[a-z0-9.]*)\\.js$",
            Pattern.CASE_INSENSITIVE);


}