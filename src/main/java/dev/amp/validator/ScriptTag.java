package dev.amp.validator;

import dev.amp.validator.utils.ExtensionsUtils;
import org.xml.sax.Attributes;

import javax.annotation.Nonnull;

public class ScriptTag {

    /**
     * Constructor for ScriptTag
     *
     * @param tagName base tag name
     * @param attrs   attributes
     */
    public ScriptTag(@Nonnull final String tagName, @Nonnull final Attributes attrs) {

        this.isAmpDomain = false;
        this.isExtension = false;
        this.isRuntime = false;
        this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.UNKNOWN;

        boolean isAsync = false;
        boolean isModule = false;
        boolean isNomodule = false;
        String path = "";
        String src = "";

        if (!tagName.equals("SCRIPT")) {
            return;
        }

        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getLocalName(i).equals("async")) {
                isAsync = true;
            } else if (
                    (attrs.getLocalName(i).equals("custom-element")) ||
                            (attrs.getLocalName(i).equals("custom-template") || (attrs.getLocalName(i).equals("host-service")))) {
                this.isExtension = true;
            } else if (attrs.getLocalName(i).equals("nomodule")) {
                isNomodule = true;
            } else if (attrs.getLocalName(i).equals("src")) {
                src = attrs.getValue(i);
            } else if ((attrs.getLocalName(i).equals("type")) && (attrs.getValue(i).equals("module"))) {
                isModule = true;
            }
        }

        // Determine if this has a valid AMP domain and separate the path from the
        // attribute 'src'.
        if (src.startsWith(ampProjectDomain)) {
            this.isAmpDomain = true;
            path = src.substring(ampProjectDomain.length());

            // Only look at script tags that have attribute 'async'.
            if (isAsync) {
                // Determine if this is the AMP Runtime.
                if (!this.isExtension && runtimeScriptPathRegex.test(path)) {
                    this.isRuntime = true;
                }

                // Determine the release version (LTS, module, standard, etc).
//                if ((is_module && moduleLtsScriptPathRegex.test(path)) ||
//                        (is_nomodule && nomoduleLtsScriptPathRegex.test(path))) {
//                    this.release_version = ScriptReleaseVersion.MODULE_NOMODULE_LTS;
//                } else if (
//                        (is_module && moduleScriptPathRegex.test(path)) ||
//                                (is_nomodule && nomoduleScriptPathRegex.test(path))) {
//                    this.release_version = ScriptReleaseVersion.MODULE_NOMODULE;
//                } else if (ltsScriptPathRegex.test(path)) {
//                    this.release_version = ScriptReleaseVersion.LTS;
//                } else {
//                    this.release_version = ScriptReleaseVersion.STANDARD;
//                }
            }
        }
    }

    private boolean isAmpDomain;

    /**
     * isExtension
     */
    private boolean isExtension;

    /**
     * isRuntime
     */
    private boolean isRuntime;

    /**
     * releaseVersion
     */
    @Nonnull
    private ExtensionsUtils.ScriptReleaseVersion releaseVersion;

    /**
     * ampProjectDomain
     */
    @Nonnull
    final String ampProjectDomain = "https://cdn.ampproject.org/";

    /** Runtime JavaScript:
     v0.js
     v0.mjs
     v0.mjs?f=sxg
     lts/v0.js
     lts/v0.js?f=sxg
     lts/v0.mjs */
    const runtimeScriptPathRegex = new RegExp('(lts/)?v0\\.m?js(\\?f=sxg)?','i');
}
