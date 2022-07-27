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

package dev.amp.validator.utils;

import dev.amp.validator.Context;
import dev.amp.validator.ParsedHtmlTag;
import dev.amp.validator.ValidatorProtos;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static dev.amp.validator.ValidatorProtos.ValidationError.Code.DISALLOWED_AMP_DOMAIN;
import static dev.amp.validator.utils.TagSpecUtils.getTagDescriptiveName;
import static dev.amp.validator.utils.TagSpecUtils.getTagSpecUrl;

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
     * Validates the 'src' attribute for AMP JavaScript (Runtime and Extensions)
     * script tags. This validates:
     * - the script is using an AMP domain
     * - the script path is valid (for extensions only, runtime uses attrSpec)
     * - that the same script release version is used for all script sources
     *
     * @param tag       tag
     * @param attrValue value
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

            // If the path is invalid, then do not evaluate further.
            if (!tag.hasValidAmpScriptPath()) {
                // If path is not empty use invalid path error, otherwise use the invalid
                // attribute value error. This is to avoid errors saying "has a path ''".
                if (tag.getAmpScriptPath().length() > 0) {
                    final List<String> params = new ArrayList<>();
                    params.add(extensionSpec.getName());
                    params.add(tag.getAmpScriptPath());
                    context.addError(ValidatorProtos.ValidationError.Code.INVALID_EXTENSION_PATH,
                            context.getLineCol(), params, getTagSpecUrl(tagSpec), result);
                } else {
                    final List<String> params = new ArrayList<>();
                    params.add("src");
                    params.add(getTagDescriptiveName(tagSpec));
                    params.add(attrValue);
                    context.addError(ValidatorProtos.ValidationError.Code.INVALID_ATTR_VALUE,
                            context.getLineCol(), params, getTagSpecUrl(tagSpec), result);
                }
                return;
            }

            if (extensionName.equals(extensionSpec.getName())) {
                // Validate deprecated version.
                if (extensionSpec.getDeprecatedVersionList().contains(extensionVersion)) {
                    final List<String> params = new ArrayList<>();
                    params.add(extensionSpec.getName());
                    params.add(extensionVersion);
                    context.addWarning(
                            ValidatorProtos.ValidationError.Code.WARNING_EXTENSION_DEPRECATED_VERSION,
                            context.getLineCol(), params, getTagSpecUrl(tagSpec), result);
                }
                // Validate version.
                if (!extensionSpec.getVersionList().contains(extensionVersion)) {
                    final List<String> params = new ArrayList<>();
                    params.add(extensionSpec.getName());
                    params.add(extensionVersion);
                    context.addError(
                            ValidatorProtos.ValidationError.Code.INVALID_EXTENSION_VERSION,
                            context.getLineCol(), params, getTagSpecUrl(tagSpec), result);
                }
            } else {
                // Extension name does not match extension spec name.
                final List<String> params = new ArrayList<>();
                params.add("src");
                params.add(getTagDescriptiveName(tagSpec));
                params.add(attrValue);
                context.addError(
                        ValidatorProtos.ValidationError.Code.INVALID_ATTR_VALUE,
                        context.getLineCol(), params, getTagSpecUrl(tagSpec), result);
            }
        }
        // Only evaluate the script tag's release version if the first script tag's
        // release version is not UNKNOWN.

        if (context.getScriptReleaseVersion() != ScriptReleaseVersion.UNKNOWN) {
            final ScriptReleaseVersion scriptReleaseVersion = tag.getScriptReleaseVersion();
            if (context.getScriptReleaseVersion() != scriptReleaseVersion) {
                final String specName = (tagSpec.hasExtensionSpec())
                        ? tagSpec.getExtensionSpec().getName() : tagSpec.getSpecName();
                switch (context.getScriptReleaseVersion()) {
                    case LTS:
                    case MODULE_NOMODULE:
                    case MODULE_NOMODULE_LTS:
                    case STANDARD:
                        final List<String> params = new ArrayList<>();
                        params.add(specName);
                        params.add(scriptReleaseVersion.toString());
                        params.add(context.getScriptReleaseVersion().toString());
                        context.addError(
                                ValidatorProtos.ValidationError.Code.INCORRECT_SCRIPT_RELEASE_VERSION,
                                context.getLineCol(), params,
                                "https://amp.dev/documentation/guides-and-tutorials/learn/spec/amphtml#required-markup",
                                result);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * list of extension script names
     */
    public static final String[] EXTENSION_SCRIPT_NAMES = {"custom-element", "custom-template", "host-service"};
}
