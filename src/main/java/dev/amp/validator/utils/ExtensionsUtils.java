package dev.amp.validator.utils;

import dev.amp.validator.Context;
import dev.amp.validator.ParsedHtmlTag;
import dev.amp.validator.ValidatorProtos;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static dev.amp.validator.utils.ExtensionsUtils.ScriptReleaseVersion.LTS;
import static dev.amp.validator.utils.ExtensionsUtils.ScriptReleaseVersion.UNKNOWN;
import static dev.amp.validator.ValidatorProtos.ValidationError.Code.DISALLOWED_AMP_DOMAIN;

/**
 * Utils to handle extension validation
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public final class ExtensionsUtils {
    /**
     * Private constructor.
     */
    private ExtensionsUtils() {
    }

    // If any script in the page uses LTS, all scripts must use LTS. This is used to
    // record when a script is seen and validate following script tags.
    public enum ScriptReleaseVersion {
        /**
         * UNKNOWN
         */
        UNKNOWN,
        /**
         * STANDARD
         */
        STANDARD,
        /**
         * LTS
         */
        LTS,
        /**
         * MODULE_NOMODULE
         */
        MODULE_NOMODULE,
        /**
         * MODULE_NOMODULE
         */
        MODULE_NOMODULE_LTS
    }

    /**
     * Tests if a tag is an async script tag.
     *
     * @param tag to test
     * @return true iff a tag is an async script tag.
     */
    public static boolean isAsyncScriptTag(final ParsedHtmlTag tag) {
        return "SCRIPT".equals(tag.upperName()) && tag.attrsByKey().
                containsKey("async") && tag.attrsByKey().containsKey("src");
    }

    /**
     * Validates the 'src' attribute for AMP JavaScript (Runtime and Extensions)
     * script tags. This validates:
     * - the script is using an AMP domain
     * - the script path is valid (for extensions only, runtime uses attrSpec)
     * - that the same script release version is used for all script sources
     *
     * @param tag       tag
     * @param attrValue
     * @param tagSpec   the spec to check against
     * @param context   global context
     * @param result    record to update
     */
    public static void validateAmpScriptSrcAttr(@Nonnull final ParsedHtmlTag tag, @Nonnull final String attrValue,
                                                @Nonnull final ValidatorProtos.TagSpec tagSpec, @Nonnull final Context context,
                                                @Nonnull final ValidatorProtos.ValidationResult.Builder result) {
        if (!tag.isAmpDomain()) {

            context.addError(
                    DISALLOWED_AMP_DOMAIN,
                    context.getLineCol(), /* params */new ArrayList<>(), /* spec_url*/ "", result);
        }
        if (tag.isExtensionScript() && tagSpec.hasExtensionSpec()) {
            final ValidatorProtos.ExtensionSpec extensionSpec = tagSpec.getExtensionSpec();
            final String extensionName = tag.getExtensionName();
            final String extensionVersion = tag.getExtensionVersion();
        }
        //todo
        final ScriptReleaseVersion scriptReleaseVersion = getScriptReleaseVersion(tag);
        if (context.getScriptReleaseVersion() != scriptReleaseVersion) {
            List<String> params = new ArrayList<>();
            final String specName = (tagSpec.hasExtensionSpec())
                    ? tagSpec.getExtensionSpec().getName() : tagSpec.getSpecName();
            params.add(specName);
            context.addError(
                    scriptReleaseVersion == LTS
                            ? ValidatorProtos.ValidationError.Code.LTS_SCRIPT_AFTER_NON_LTS
                            : ValidatorProtos.ValidationError.Code.NON_LTS_SCRIPT_AFTER_LTS,
                    context.getLineCol(), params,
                    "",
                    result);
        }
    }

    /**
     * @param tag
     * @return ScriptReleaseVersion of tag
     */
    public static ScriptReleaseVersion getScriptReleaseVersion(@Nonnull final ParsedHtmlTag tag) {
        if (tag.isModuleLtsScriptTag() || tag.isNomoduleLtsScriptTag()) {
            return ScriptReleaseVersion.MODULE_NOMODULE_LTS;
        }
        if (tag.isModuleScriptTag() || tag.isNomoduleScriptTag()) {
            return ScriptReleaseVersion.MODULE_NOMODULE;
        }
        if (tag.isLtsScriptTag()) {
            return ScriptReleaseVersion.LTS;
        }
        return ScriptReleaseVersion.STANDARD;
    }

    /**
     * list of extension script names
     */
    public static final String[] EXTENSION_SCRIPT_NAMES = {"custom-element", "custom-template", "host-service"};
}
