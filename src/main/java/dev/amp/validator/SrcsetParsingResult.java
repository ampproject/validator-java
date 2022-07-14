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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the result of srcset parsing.
 *
 * @author nhant01
 * @author GeorgeLuo
 */

public class SrcsetParsingResult {
    /** Default constructor. */
    public SrcsetParsingResult() {
        this.success = false;
        this.errorCode = ValidatorProtos.ValidationError.Code.UNKNOWN_CODE;
        this.srcsetImages = new ArrayList<>();
    }

    /**
     * Returns success flag.
     * @return returns success flag.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Setting the success flag.
     * @param success success flag.
     */
    public void setSuccess(final boolean success) {
        this.success = success;
    }

    /**
     * Returns the error code.
     * @return returns the error code.
     */
    public ValidatorProtos.ValidationError.Code getErrorCode() {
        return errorCode;
    }

    /**
     * Setting the error code.
     * @param errorCode the error code.
     */
    public void setErrorCode(@Nonnull final ValidatorProtos.ValidationError.Code errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Returns the srcset images.
     * @return returns the srcset images.
     */
    public List<SrcsetSourceDef> getSrcsetImages() {
        return srcsetImages;
    }

    /**
     * Add source set def to the list.
     * @param srcSetSourceDef the source set def.
     */
    public void add(@Nonnull final SrcsetSourceDef srcSetSourceDef) {
        srcsetImages.add(srcSetSourceDef);
    }

    /**
     * Setting the srcset images.
     * @param srcsetImages srcset images.
     */
    public void setSrcsetImages(@Nonnull final List<SrcsetSourceDef> srcsetImages) {
        this.srcsetImages = srcsetImages;
    }

    /**
     * Returns the size of srcset images.
     * @return returns the size of srcset images.
     */
    public int getSrcsetImagesSize() {
        return srcsetImages.size();
    }

    /** Flag success. */
    private boolean success;

    /** Error code. */
    private ValidatorProtos.ValidationError.Code errorCode;

    /** List of srcset images. */
    private List<SrcsetSourceDef> srcsetImages;
}

