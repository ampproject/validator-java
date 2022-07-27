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
 * Changes to the original project are Copyright 2019, Yahoo Inc..
 */

import dev.amp.validator.utils.ExtensionsUtils;
import org.xml.sax.Attributes;

import javax.annotation.Nonnull;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
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

        this.extensionName = "";
        this.extensionVersion = "";
        this.path = "";
        this.isAmpDomain = false;
        this.isExtension = false;
        this.isRuntime = false;
        this.hasValidPath = false;
        this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.UNKNOWN;

        boolean isAsync = false;
        boolean isModule = false;
        boolean isNomodule = false;
        String src = "";

        if (!tagName.equals("SCRIPT")) {
            return;
        }

        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getLocalName(i).equals("async")) {
                isAsync = true;
            } else if (
                    (attrs.getLocalName(i).equals("custom-element"))
                            || (attrs.getLocalName(i).equals("custom-template")
                            || (attrs.getLocalName(i).equals("host-service")))) {
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
                // For AMP Extensions, validate path and extract name and version.
                final Matcher matcher = EXTENSION_SCRIPT_PATH_REGEX.matcher(this.path);
                if (this.isExtension && matcher.find()) {
                    this.hasValidPath = true;
                    final MatchResult reResult = matcher.toMatchResult();
                    if (reResult.groupCount() == 2) {
                        this.extensionName = reResult.group(1);
                        this.extensionVersion = reResult.group(2);
                    }
                }

                // Determine the release version (LTS, module, standard, etc).
                if ((isModule && MODULE_LTS_SCRIPT_PATH_REGEX.matcher(path).find())
                        || (isNomodule && NOMODULE_LTS_SCRIPT_PATH_REGEX.matcher(path).find())) {
                    this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.MODULE_NOMODULE_LTS;
                } else if (
                        (isModule && MODULE_SCRIPT_PATH_REGEX.matcher(path).find())
                                || (isNomodule && NO_MODULE_SCRIPT_PATH_REGEX.matcher(path).find())) {
                    this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.MODULE_NOMODULE;
                } else if (LTS_SCRIPT_PATH_REGEX.matcher(path).find()) {
                    this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.LTS;
                } else {
                    this.releaseVersion = ExtensionsUtils.ScriptReleaseVersion.STANDARD;
                }
            }
        }
    }

    /**
     * Getter for Amp Domain
     *
     * @return True iff isAmpDomain
     */
    public boolean isAmpDomain() {
        return isAmpDomain;
    }

    /**
     * Getter for isRuntime
     *
     * @return True iff isRuntime
     */
    public boolean isRuntime() {
        return isRuntime;
    }

    /**
     * Getter for Extension Name
     *
     * @return Extension Name
     */
    public String getExtensionName() {
        return extensionName;
    }

    /**
     * Getter for Extension Version
     *
     * @return Extension Version
     */
    public String getExtensionVersion() {
        return extensionVersion;
    }

    /**
     * Getter for hasValidPath
     *
     * @return hasValidPath
     */
    public boolean hasValidPath() {
        return hasValidPath;
    }

    /**
     * Getter for path
     *
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * Getter for release version
     *
     * @return release version
     */
    @Nonnull
    public ExtensionsUtils.ScriptReleaseVersion getReleaseVersion() {
        return releaseVersion;
    }

    /**
     * extensionName
     */
    private String extensionName;

    /**
     * extensionVersion
     */
    private String extensionVersion;

    /**
     * isAmpDomain
     */
    private boolean isAmpDomain;

    /**
     * path
     */
    private String path;

    /**
     * isExtension
     */
    private boolean isExtension;

    /**
     * isRuntime
     */
    private boolean isRuntime;

    /**
     * hasValidPath
     */
    private boolean hasValidPath;

    /**
     * releaseVersion
     */
    @Nonnull
    private ExtensionsUtils.ScriptReleaseVersion releaseVersion;

    /**
     * ampProjectDomain
     */
    @Nonnull
    private static final String AMP_PROJECT_DOMAIN = "https://cdn.ampproject.org/";

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
    private static final Pattern RUNTIME_SCRIPT_PATH_REGEX =
            Pattern.compile("^(lts\\/)?v0\\.m?js(\\?f=sxg)?$", Pattern.CASE_INSENSITIVE);

    /**
     * lts/v0.mjs
     * lts/v0/amp-ad-0.1.mjs
     */
    @Nonnull
    private static final Pattern MODULE_LTS_SCRIPT_PATH_REGEX =
            Pattern.compile("^lts\\/(v0|v0\\/amp-[a-z0-9-]*-[a-z0-9.]*)\\.mjs$", Pattern.CASE_INSENSITIVE);

    /**
     * lts/v0.js
     * lts/v0/amp-ad-0.1.js
     */

    @Nonnull
    private static final Pattern NOMODULE_LTS_SCRIPT_PATH_REGEX =
            Pattern.compile("lts\\/(v0|v0/amp-[a-z0-9-]*-[a-z0-9.]*)\\.js$", Pattern.CASE_INSENSITIVE);

    /**
     * v0.mjs
     * amp-ad-0.1.mjs
     */
    @Nonnull
    private static final Pattern MODULE_SCRIPT_PATH_REGEX =
            Pattern.compile("^(v0|v0\\/amp-[a-z0-9-]*-[a-z0-9.]*)\\.mjs$", Pattern.CASE_INSENSITIVE);

    /**
     * v0.js
     * v0/amp-ad-0.1.js
     */
    @Nonnull
    private static final Pattern NO_MODULE_SCRIPT_PATH_REGEX =
            Pattern.compile("(v0|v0\\/amp-[a-z0-9-]*-[a-z0-9.]*)\\.js$", Pattern.CASE_INSENSITIVE);
    /**
     * lts/v0.js
     * lts/v0/amp-ad-0.1.js
     */
    @Nonnull
    private static final Pattern LTS_SCRIPT_PATH_REGEX =
            Pattern.compile("^lts\\/(v0|v0/amp-[a-z0-9-]*-[a-z0-9.]*)\\.js$", Pattern.CASE_INSENSITIVE);
    /**
     * lts/v0/amp-ad-0.1.js
     * lts/v0/amp-ad-0.1.js?f=sxg
     * ts/v0/amp-ad-0.1.mjs
     * v0/amp-ad-0.1.js
     * v0/amp-ad-0.1.js?f=sxg
     * v0/am-ad-0.1.mjs
     */
    @Nonnull
    private static final Pattern EXTENSION_SCRIPT_PATH_REGEX =
            Pattern.compile("^(?:lts\\/)?v0\\/(amp-[a-z0-9-]*)-([a-z0-9.]*)\\.(?:m)?js(?:\\?f=sxg)?$",
                    Pattern.CASE_INSENSITIVE);
}
