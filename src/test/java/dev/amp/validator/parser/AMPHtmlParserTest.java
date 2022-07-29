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

package dev.amp.validator.parser;

import dev.amp.validator.ValidatorProtos;
import dev.amp.validator.ExitCondition;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AMPHtmlParserTest {
    @BeforeClass
    public void init() throws Exception {
        ampHtmlParser = new AMPHtmlParser();

    }

    @Test
    public void testLatency() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/misc/complexPass.html");
            final int maxNode = 10000;
            final long startTime = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            }
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Elapsed: " + elapsedTime + "ms");
            Assert.assertTrue(elapsedTime < 22000);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSCarouselSelectors() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCarouselSelectors.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAmpTimeAgo() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testAmpTimeAgo.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertTrue(onlyErrorIsWarning(result));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAmpSelectorsPass() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testAmpSelectorsPASS.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertEquals(result.getStatus(), ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAmpAsteriskEqualSelectorPass() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testAmpAsteriskEqualSelectorPASS.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertEquals(result.getStatus(), ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDeclarationWithASpaceAfterColon() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testDeclarationWithASpaceAfterColon.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertEquals(result.getStatus(), ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDeclarationNameCaseSensitive() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testDeclarationNameCaseSensitive.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertEquals(result.getStatus(), ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCarousel() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testAmpCarousel.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertEquals(result.getStatus(), ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTransformedValueRe() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testTransformedValueRe.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_ATTR);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAmpList() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testAmpList.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.EXIT_ON_FIRST_ERROR, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAmpVoidTag() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testAmpVoidTag.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.EXIT_ON_FIRST_ERROR, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    //VALIDATION TAG
// TODO - Tagchowder implicitly added html tag to the parent of head.
//    @Test
//    public void testMandatoryTagMissing() {
//        try {
//            String inputHtml =
//                    readFile(
//                            "test-cases/tags/testMandatoryTagMissing.html");
//            final int maxNode = 10000;
//            ValidatorProtos.ValidationResult result =
//                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
//            Assert.assertEquals(result.getErrorsCount(), 3, "Expecting to have 3 error");
//            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.WRONG_PARENT_TAG);
//            Assert.assertTrue(result.getErrors(1).getCode() == ValidatorProtos.ValidationError.Code.WRONG_PARENT_TAG);
//            Assert.assertTrue(result.getErrors(2).getCode() == ValidatorProtos.ValidationError.Code.MANDATORY_TAG_MISSING);
//        } catch (final IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    @Test
    public void testTagRequiredByMissing() {
    }

    @Test
    public void getTagExcludedByTag() {
    }

    @Test
    public void testDisallowedTag() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testDisallowedTag.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_TAG);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDisallowedScriptTag() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testDisallowedScriptTag.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_SCRIPT_TAG);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDuplicateUniqueTag() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testDuplicateUniqueTag.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DUPLICATE_UNIQUE_TAG);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testWrongParentTag() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testWrongParentTag.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.WRONG_PARENT_TAG);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDisallowedTagAncestor() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testDisallowedTagAncestor.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_TAG_ANCESTOR);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testMandatoryLastChildTag() {

    }

    @Test
    public void testMandatoryTagAncestor() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testMandatoryTagAncestor.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.MANDATORY_TAG_ANCESTOR);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testMandatoryTagAncestorWithHint() {

    }

    @Test
    public void testIncorrectNumChildTags() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testIncorrectNumChildTags.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.INCORRECT_NUM_CHILD_TAGS);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testIncorrectMinNumChildTag() {

    }

    @Test
    public void testDisallowedChildTagName() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testDisallowedChildTagName.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_CHILD_TAG_NAME);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDisallowedFirstChildTagName() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testDisallowedFirstChildTagName.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_FIRST_CHILD_TAG_NAME);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testChildTagDoesNotSatisfyReferencePoint() {

    }

    @Test
    public void testTagNotAllowedToHaveSiblings() {

    }

    @Test
    public void testReferencePointConflict() {

    }

    @Test
    public void testChildTagDoesNotSatisfyReferencePointSingular() {

    }

    @Test
    public void testBaseTagMustProceedAllUrls() {

    }

    @Test
    public void testMaxParseNodesException() {
//        try {
//            String inputHtml =
//                    readFile(
//                            "test-cases/testPass1.html");
//            final int maxNode = 2;
//            ValidatorProtos.ValidationResult result =
//                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
//            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have max parse nodes error");
//            //Assert.assertEquals(result.getErrors(0).getParams(0),
//                    //"Maximum nodes reached parsing HTML");
//        } catch (final IOException ex) {
//            ex.printStackTrace();
//        }
    }

    @Test
    public void testMissingRequiredExtension() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testMissingRequiredExtension.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 2, "Expecting to have 2 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.AMP_EMAIL_MISSING_STRICT_CSS_ATTR);
            Assert.assertTrue(result.getErrors(1).getCode() == ValidatorProtos.ValidationError.Code.MISSING_REQUIRED_EXTENSION);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testSpecifiedLayoutInvalid() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testSpecifiedLayoutInvalid.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.SPECIFIED_LAYOUT_INVALID);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTagReferencePointConflict() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testTagReferencePointConflict.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 2, "Expecting to have 2 errors");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_TAG_ANCESTOR);
            Assert.assertTrue(result.getErrors(1).getCode() == ValidatorProtos.ValidationError.Code.TAG_REFERENCE_POINT_CONFLICT);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDeprecatedTag() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testDeprecatedTag.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DEPRECATED_TAG);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testExtensionUnused() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testExtensionUnused.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.EXTENSION_UNUSED);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testNonWhitespaceCdataEncountered() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testNonWhitespaceCdataEncountered.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.NON_WHITESPACE_CDATA_ENCOUNTERED);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void testMandatoryCdataMissingOrIncorrect() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testMandatoryCdataMissingOrIncorrect.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.MANDATORY_CDATA_MISSING_OR_INCORRECT);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testInvalidJsonCdata() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/tags/testInvalidJsonCdata.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.INVALID_JSON_CDATA);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    //VALIDATION ATTRIBUTES

    @Test
    public void testDisallowedAttr() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testDisallowedAttr.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 2, "Expecting to have 2 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_ATTR);
            Assert.assertTrue(result.getErrors(1).getCode() == ValidatorProtos.ValidationError.Code.MANDATORY_ATTR_MISSING);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
            // TODO : write a containsError helper method
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testInvalidAttrValue() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testInvalidAttrValue.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.INVALID_ATTR_VALUE);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    // TODO : waiting on TagChowder fix
    @Test
    public void testDuplicateAttribute() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testDuplicateAttribute.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
//            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
//            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DUPLICATE_ATTRIBUTE);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testMandatoryAttrMissing() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testMandatoryAttrMissing.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.MANDATORY_ATTR_MISSING);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAttrMissingRequiredExtension() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testAttrMissingRequiredExtension.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.ATTR_MISSING_REQUIRED_EXTENSION);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    // VALIDATION TEMPLATE

    @Test
    public void testTemplateInAttrName() {
        try {
            //TODO - TagChowder is removing '{{ }}'.
            String inputHtml =
                    readFile(
                            "test-cases/templates/testTemplateInAttrName.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.TEMPLATE_IN_ATTR_NAME);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
            //Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.TEMPLATE_IN_ATTR_NAME);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testUnescapedTemplateInAttrValue() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/templates/testUnescapedTemplateInAttrValue.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.UNESCAPED_TEMPLATE_IN_ATTR_VALUE);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTemplatePartialInAttrValue() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/templates/testTemplatePartialInAttrValue.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.TEMPLATE_PARTIAL_IN_ATTR_VALUE);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testMissingLayoutAttributes() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testMissingLayoutAttributes.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.MISSING_LAYOUT_ATTRIBUTES);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAttrDisallowedBySpecifiedLayout() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testAttrDisallowedBySpecifiedLayout.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.ATTR_DISALLOWED_BY_SPECIFIED_LAYOUT);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAttrDisallowedByImpliedLayout() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testAttrDisallowedByImpliedLayout.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.ATTR_DISALLOWED_BY_IMPLIED_LAYOUT);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAttrValueRequiredByLayout() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testAttrValueRequiredByLayout.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.ATTR_VALUE_REQUIRED_BY_LAYOUT);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testImpliedLayoutInvalid() {
//        try {
//            String inputHtml =
//                    readFile(
//                            "test-cases/attributes/testImpliedLayoutInvalid.html");
//            final int maxNode = 10000;
//            ValidatorProtos.ValidationResult result =
//                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
//            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
//            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.IMPLIED_LAYOUT_INVALID);
//        } catch (final IOException ex) {
//            ex.printStackTrace();
//        }
    }

    @Test
    public void testInconsistentUnitsForWidthAndHeight() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testInconsistentUnitsForWidthAndHeight.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.INCONSISTENT_UNITS_FOR_WIDTH_AND_HEIGHT);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAttrRequiredButMissing() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testAttrRequiredButMissing.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.ATTR_REQUIRED_BUT_MISSING);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDisallowedPropertyInAttrValue() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testDisallowedPropertyInAttrValue.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_PROPERTY_IN_ATTR_VALUE);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testMissingUrl() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testMissingUrl.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.MISSING_URL);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDisallowedRelativeUrl() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testDisallowedRelativeUrl.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DISALLOWED_RELATIVE_URL);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testInvalidUrlProtocol() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/attributes/testInvalidUrlProtocol.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.INVALID_URL_PROTOCOL);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }


    // VALIDATION CSS

    @Test
    public void testDelimiterPASS() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testDelimiterPASS.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testEqualsDelimiter() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSEqualsDelimiter.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testPseudoClassFunctionNot() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testPseudoClassFunctionNot.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSBlankBoilerplate() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSBlankBoilerplate.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 0 error");
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    // TODO: debugging indicates the CSS is tokenized before the length check. Should validate length before.
    @Test
    public void testStylesheetTooLong() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testStylesheetTooLong.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.STYLESHEET_TOO_LONG);
            Assert.assertTrue(result.getErrors(1).getCode() == ValidatorProtos.ValidationError.Code.DOCUMENT_SIZE_LIMIT_EXCEEDED);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
            Assert.assertEquals(result.getErrorsCount(), 2, "Expecting to have 2 error.");
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testInlineStyleTooLong() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testInlineStyleTooLong.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.INLINE_STYLE_TOO_LONG);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error.");
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxUnterminatedComment() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxUnterminatedComment.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error.");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_UNTERMINATED_COMMENT);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSUnterminatedString() {
        try {
            //TODO - Results add additional CSS_SYNTAX_INVALID_DECLARATION (should only return unterminated error)
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxUnterminatedString.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 4, "Expecting to have 4 error.");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_UNTERMINATED_STRING);
            Assert.assertTrue(result.getErrors(1).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_UNTERMINATED_STRING);
            Assert.assertTrue(result.getErrors(2).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_DECLARATION);
            Assert.assertTrue(result.getErrors(3).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_DECLARATION);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxStrayTrailingBackslash() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxStrayTrailingBackslash.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 2, "Expecting to have 2 errors.");
            Assert.assertTrue(result.getErrors(0).getCode()
                    == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_STRAY_TRAILING_BACKSLASH);
            Assert.assertTrue(result.getErrors(1).getCode()
                    == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_DECLARATION);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxInvalidDeclaration() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxInvalidDeclaration.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error.");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_DECLARATION);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxIncompleteDeclaration() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxIncompleteDeclaration.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error.");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INCOMPLETE_DECLARATION);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxDisallowedMediaType() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxDisallowedMediaType.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error.");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_MEDIA_TYPE);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    //after feb 24 2022 amphtml sync (validator-all.protoascii) this is allowed inside EMAIL_SPECIFIC_DECLARATIONS
    @Test(enabled = false)
    public void testCSSSyntaxDisallowedPropertyValue() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxDisallowedPropertyValue.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error.");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_PROPERTY_VALUE);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxInvalidAtRule() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxInvalidAtRule.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 4, "Expecting to have 4 errors");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_AT_RULE);
            Assert.assertTrue(result.getErrors(1).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_AT_RULE);
            Assert.assertTrue(result.getErrors(2).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_AT_RULE);
            Assert.assertTrue(result.getErrors(3).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_AT_RULE);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxMalformedMediaQuery() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxMalformedMediaQuery.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error.");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_MALFORMED_MEDIA_QUERY);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxDisallowedMediaFeature() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxDisallowedMediaFeature.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error.");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_MEDIA_FEATURE);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxMissingURL() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxMissingURL.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_MISSING_URL);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxBadUrl() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxBadUrl.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_BAD_URL);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSExcessivelyNested() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSExcessivelyNested.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 2, "Expecting to have 2 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_EXCESSIVELY_NESTED);
            Assert.assertTrue(result.getErrors(1).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_ERROR_IN_PSEUDO_SELECTOR);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

//    @Test
//    public void testCSSDisallowedRelativeURL() {
//        try {
//            String inputHtml =
//                    readFile(
//                            "test-cases/css/testCSSDisallowedRelativeURL.html");
//            final int maxNode = 10000;
//            ValidatorProtos.ValidationResult result =
//                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
//            Assert.assertEquals(result.getErrorsCount(), 0, "Expecting to have 1 error");
//            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_RELATIVE_URL);
//        } catch (final IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    @Test
    public void testCSSSyntaxInvalidUrlProtocol() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/css/testCSSSyntaxInvalidUrlProtocol.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_URL_PROTOCOL);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.FAIL);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCSSSyntaxInvalidUrl() {
//        try {
//            String inputHtml =
//                    readFile(
//                            "test-cases/css/testCSSSyntaxInvalidUrl.html");
//            final int maxNode = 10000;
//            ValidatorProtos.ValidationResult result =
//                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
//            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
//            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_URL);
//        } catch (final IOException ex) {
//            ex.printStackTrace();
//        }
    }

    // VALIDATION MISC

    @Test
    public void testWarningExtensionDeprecatedVersion() {
        try {
            String inputHtml =
                    readFile(
                            "test-cases/misc/testWarningExtensionDeprecatedVersion.html");
            final int maxNode = 10000;
            ValidatorProtos.ValidationResult result =
                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP4EMAIL, ExitCondition.FULL_PARSING, maxNode);
            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.WARNING_EXTENSION_DEPRECATED_VERSION);
            Assert.assertTrue(result.getStatus() == ValidatorProtos.ValidationResult.Status.PASS);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    // VALIDATION NON-AMP4EMAIL

    @Test
    public void testDuplicateDimension() {
//        try {
//            String inputHtml =
//                    readFile(
//                            "test-cases/attributes/testDuplicateDimension.html");
//            final int maxNode = 10000;
//            ValidatorProtos.ValidationResult result =
//                    ampHtmlParser.parse(inputHtml, ValidatorProtos.HtmlFormat.Code.AMP, ExitCondition.FULL_PARSING, maxNode);
//            Assert.assertEquals(result.getErrorsCount(), 1, "Expecting to have 1 error");
//            Assert.assertTrue(result.getErrors(0).getCode() == ValidatorProtos.ValidationError.Code.DUPLICATE_DIMENSION);
//        } catch (final IOException ex) {
//            ex.printStackTrace();
//        }
    }

    public static String readFile(@Nonnull final String filePath) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final InputStream is =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                sb.append(currentLine).append("\n");
            }
        }
        return sb.toString();
    }

    public static boolean onlyErrorIsWarning(final ValidatorProtos.ValidationResult result) {
        return (result.getErrorsCount() == 1 && result.getErrors(0).getCode()
                == ValidatorProtos.ValidationError.Code.AMP_EMAIL_MISSING_STRICT_CSS_ATTR);
    }

    /**
     * AMPHtmlParser instance.
     */
    private AMPHtmlParser ampHtmlParser;
}
