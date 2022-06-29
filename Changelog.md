# Changelog

## AMP HTML Validator 1.0.32
- upgrade junit from 4.13.2 to 5.8.2
- upgrade protobuf-java from 3.21.0 to 3.21.1
- upgrade maven-enforcer-plugin from 3.0.0 to 3.1.0
- upgrade maven-pmd-plugin from 3.16.0 to 3.17.0
- upgrade maven-scm-plugin from 1.12.2 to 1.13.0
- upgrade maven-surefire-plugin from 3.0.0-M6 to 3.0.0-M7
- upgrade puppycrawl.checkstyle from 10.2 to 10.3
- upgrade spotbugs from 4.7.0 to 4.7.1
- upgrade spotbugs-annotations from 4.7.0 to 4.7.1
- upgrade spotbugs-maven-plugin from 4.6.0.0 to 4.7.0.0
- sync validator-all.protoascii with amphtml (2022.06.16)

## AMP HTML Validator 1.0.31
- upgrade protobuf-java from 3.20.1 to 3.21.0
- upgrade jackson-databind from 2.13.2.2 to 2.13.3
- upgrade jackson-core from 2.13.3 to 2.13.2
- upgrade testng from 7.5 to 7.6.0
- fix bug for pseudo class function :not in css

## AMP HTML Validator 1.0.30
- upgrade protobuf-java from 3.16.1 to 3.20.1
- upgrade exec-maven-plugin from 1.6.0 to 3.0.0
- upgrade maven-dependency-plugin from 3.2.0 to 3.3.0
- upgrade maven-javadoc-plugin from 3.3.2 to 3.4.0
- upgrade maven-pmd-plugin from 3.13.0 to 3.16.0
- upgrade nexus-staging-maven-plugin from 1.6.12 to 1.6.13
- upgrade puppycrawl checkstyle from 8.32 to 10.2
- upgrade spotbugs-maven-plugin from 4.0.0 to 4.6.0.0
- upgrade spotbugs from 4.0.3 to 4.7.0
- upgrade spotbugs-annotations from 4.6.0 to 4.7.0
- fix bug for *[class="CLASS"] delimiter parsing error

## AMP HTML Validator 1.0.29
- upgrade jackson-core from 2.13.1 to 2.13.2
- upgrade jackson-databind from 2.13.1 to 2.13.2.2
- upgrade maven-clean-plugin from 3.1.0 to 3.2.0
- upgrade maven-antrun-plugin from 3.0.0 to 3.1.0
- upgrade antlr3-maven-plugin from 3.5.2 to 3.5.3
- upgrade jacoco-maven-plugin from 0.8.7 to 0.8.8
- upgrade maven-surefire-plugin from 3.0.0-M5 to 3.0.0-M6

## AMP HTML Validator 1.0.28
- upgrade git actions from v2 to v3
- upgrade spotbugs-annotations from 4.0.3 to 4.6.0
- upgrade maven-compiler-plugin from 3.8.1 to 3.10.1
- upgrade nexus-staging-maven-plugin from 1.6.8 to 1.6.12
- upgrade maven-javadoc-plugin from 3.3.1 to 3.3.2
- sync validator-all.protoascii with amphtml (2022.05.03)

## AMP HTML Validator 1.0.27
- upgrade cssparser from 0.9.27 to 0.9.29

## AMP HTML Validator 1.0.26
- upgrade build-helper-maven-plugin from 3.1.0 to 3.3.0
- upgrade maven-dependency-plugin from 3.1.2 to 3.2.0
- upgrade maven-javadoc-plugin from 3.1.1 to 3.3.1
- upgrade os-maven-plugin from 1.6.2 to 1.7.0
- upgrade protobuf from 3.12.2 to 3.16.1
- upgrade maven-surefire-plugin from 3.0.0-M4 to 3.0.0-M5
- upgrade fasterxml.jackson from 2.11.0 to 2.13.1
- upgrade maven-resources-plugin from 3.1.0 to 3.2.0
- upgrade maven-scm-plugin from 1.11.2 to 1.12.2
- upgrade maven-enforcer-plugin from 3.0.0-M3 to 3.0.0
- upgrade maven-checkstyle-plugin from 3.1.1 to 3.1.2
- upgrade testng from 7.1.0 to 7.5
- upgrade junit from 4.13.1 to 4.13.2
- upgrade commons-lang3 from 3.10 to 3.12.0
- upgrade commons-text from 1.8 to 1.9
- sync validator-all.protoascii with amphtml
- Use SACParserCSS3Constants enum values for all tests
- disable testCSSSyntaxDisallowedPropertyValue due to change in amp4email position allowing any value now

## AMP HTML Validator 1.0.25
- Upgrade tagchowder to 2.0.23 to fix void tag parsing error that returned '_' for ending /> if there was a space preceding the '/'

## AMP HTML Validator 1.0.0 released
- AMP HTML Validator 1.0.0 is an initial release. The goal of this project is to keep updating the code with small fixes and features. This project requires unit tests and code coverage by default, and uses github and a automated pipe-line to publish new versions in a automated fashion for every commit that is merged, without humans need for each release.
