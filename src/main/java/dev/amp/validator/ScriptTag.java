package dev.amp.validator;
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

import dev.amp.validator.utils.ExtensionsUtils;
import org.xml.sax.Attributes;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * Class to define script tags.
 *
 * @author nhant01
 * @author GeorgeLuo
 * @author jjames
 */

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
        if (src.startsWith(AMP_PROJECT_DOMAIN)) {
            this.isAmpDomain = true;
            path = src.substring(AMP_PROJECT_DOMAIN.length());

            // Only look at script tags that have attribute 'async'.
            if (isAsync) {
                // Determine if this is the AMP Runtime.
                if (!this.isExtension && RUNTIME_SCRIPT_PATH_REGEX.matcher(path).find()) {
                    this.isRuntime = true;
                }

                // Determine the release version (LTS, module, standard, etc).
                if ((isModule && MODULE_LTS_SCRIPT_PATH_REGEX.matcher(path).find()) ||
                        (isNomodule && NOMODULE_LTS_SCRIPT_PATH_REGEX.matcher(path).find())) {
                    this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.MODULE_NOMODULE_LTS;
                } else if (
                        (isModule && MODULE_SCRIPT_PATH_REGEX.matcher(path).find()) ||
                                (isNomodule && NO_MODULE_SCRIPT_PATH_REGEX.matcher(path).find())) {
                    this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.MODULE_NOMODULE;
                } else if (LTS_SCRIPT_PATH_REGEX.matcher(path).find()) {
                    this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.LTS;
                } else {
                    this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.STANDARD;
                }
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
    final String AMP_PROJECT_DOMAIN = "https://cdn.ampproject.org/";

    /**
     * Runtime JavaScript:
     * v0.js
     * v0.mjs
     * v0.mjs?f=sxg
     * lts/v0.js
     * lts/v0.js?f=sxg
     * lts/v0.mjs
     */
    @Nonnull
    final Pattern RUNTIME_SCRIPT_PATH_REGEX =
            Pattern.compile("^(lts\\/)?v0\\.m?js(\\?f=sxg)?$", Pattern.CASE_INSENSITIVE);

    /**
     * lts/v0.mjs
     * lts/v0/amp-ad-0.1.mjs
     */
    @Nonnull
    final Pattern MODULE_LTS_SCRIPT_PATH_REGEX =
            Pattern.compile("^lts\\/(v0|v0\\/amp-[a-z0-9-]*-[a-z0-9.]*)\\.mjs$", Pattern.CASE_INSENSITIVE);

    /**
     * lts/v0.js
     * lts/v0/amp-ad-0.1.js
     */

    @Nonnull
    final Pattern NOMODULE_LTS_SCRIPT_PATH_REGEX =
            Pattern.compile("lts\\/(v0|v0/amp-[a-z0-9-]*-[a-z0-9.]*)\\.js$", Pattern.CASE_INSENSITIVE);

    /**
     * v0.mjs
     * amp-ad-0.1.mjs
     */
    @Nonnull
    final Pattern MODULE_SCRIPT_PATH_REGEX =
            Pattern.compile("^(v0|v0\\/amp-[a-z0-9-]*-[a-z0-9.]*)\\.mjs$", Pattern.CASE_INSENSITIVE);

    /**
     * v0.js
     * v0/amp-ad-0.1.js
     */
    @Nonnull
    final Pattern NO_MODULE_SCRIPT_PATH_REGEX =
            Pattern.compile("(v0|v0\\/amp-[a-z0-9-]*-[a-z0-9.]*)\\.js$", Pattern.CASE_INSENSITIVE);
    /**
     * lts/v0.js
     * lts/v0/amp-ad-0.1.js
     */
    @Nonnull
    final Pattern LTS_SCRIPT_PATH_REGEX =
            Pattern.compile("^lts\\/(v0|v0/amp-[a-z0-9-]*-[a-z0-9.]*)\\.js$", Pattern.CASE_INSENSITIVE);

}
