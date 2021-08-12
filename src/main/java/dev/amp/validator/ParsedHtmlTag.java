package dev.amp.validator;

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
     * @private
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
     * @private
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
        final String src = this.getAttrValueOrNull("src");
        if (src == null) {
            return false;
        }
        return this.isAmpCacheDomain(src) && this.isAsyncScriptTag(src) && !this.isExtensionScript()
                && (src.endsWith("/v0.js") || src.endsWith("/v0.mjs")
                || src.endsWith("/v0.mjs?f=sxg"));
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
     * Tests if this is the module LTS version script tag.
     *
     * @return true iff this is module LTS version script tag
     */
    public boolean isModuleLtsScriptTag() {
        // Examples:
        // https://cdn.ampproject.org/lts/v0.mjs
        // https://cdn.ampproject.org/lts/v0/amp-ad-0.1.mjs
        final String type = this.getAttrValueOrNull("type");
        if (type == null) {
            return false;
        }
        final String src = this.getAttrValueOrNull("src");
        if (src == null) {
            return false;
        }
        return this.isAsyncScriptTag(src) && type.equals("module")
                && MODULE_LTS_SCRIPT_SRC_REGEX.matcher(src).find();
    }

    /**
     * Tests if this is the nomodule LTS version script tag.
     *
     * @return true iff this is nomodule LTS version script tag
     */
    public boolean isNomoduleLtsScriptTag() {
        // Examples:
        // https://cdn.ampproject.org/lts/v0.js
        // https://cdn.ampproject.org/lts/v0/amp-ad-0.1.js
        final String src = this.getAttrValueOrNull("src");
        if (src == null) {
            return false;
        }
        return this.isAsyncScriptTag(src) && this.attrsByKey().containsKey("nomodule")
                && this.isAmpCacheDomain(src) && NOMODULE_LTS_SCRIPT_SRC_REGEX.matcher(src).find();
    }

    /**
     * Tests if this is the module version script tag.
     *
     * @return true iff module version script tag
     */
    public boolean isModuleScriptTag() {
        // Examples:
        // https://cdn.ampproject.org/v0.mjs
        // https://cdn.ampproject.org/v0/amp-ad-0.1.mjs
        final String type = this.getAttrValueOrNull("type");
        if (type == null) {
            return false;
        }
        final String src = this.getAttrValueOrNull("src");
        if (src == null) {
            return false;
        }

        return this.isAsyncScriptTag(src) && type.equals("module")
                && this.isAmpCacheDomain(src) && MODULE_SCRIPT_SRC_REGEX.matcher(src).find();
    }

    /**
     * Tests if this is the nomodule version script tag.
     *
     * @return true iff nomodule version script tag
     */
    public boolean isNomoduleScriptTag() {
        // Examples:
        // https://cdn.ampproject.org/v0.js
        // https://cdn.ampproject.org/v0/amp-ad-0.1.js
        final String src = this.getAttrValueOrNull("src");
        if (src == null) {
            return false;
        }

        return this.isAsyncScriptTag(src) && this.attrsByKey().containsKey("nomodule")
                && this.isAmpCacheDomain(src) && NOMODULE_SCRIPT_SRC_REGEX.matcher(src).find();
    }

    /**
     * Tests if this is the LTS version script tag.
     *
     * @return true iff lts script tag
     */
    public boolean isLtsScriptTag() {
        // Examples:
        // https://cdn.ampproject.org/lts/v0.js
        // https://cdn.ampproject.org/lts/v0/amp-ad-0.1.js
        final String src = this.getAttrValueOrNull("src");
        if (src == null) {
            return false;
        }
        return this.isAsyncScriptTag(src) && this.isAmpCacheDomain(src)
                && LTS_SCRIPT_SRC_REGEX.matcher(src).find();
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
     * @return returns a upper case tag name.
     */
    public String upperName() {
        return this.tagName.toUpperCase();
    }

    /**
     * Returns true if tag name length is zero.
     *
     * @return returns true if tag name length is zero.
     */
    public boolean isEmpty() {
        return this.tagName.length() == 0;
    }

    /**
     * Returns the extension name.
     *
     * @return {string}
     */
    public String getExtensionName() {
        return this.scriptTag.getExtensionName();
    }

    /**
     * Returns the extension version.
     *
     * @return {string}
     */
    public String getExtensionVersion() {
        return this.scriptTag.getExtensionVersion();
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