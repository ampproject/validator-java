#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" == 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    set -e

    # Check the variables are set
    if [ -z "$OSSRH_USERNAME" ]; then
      echo "missing environment value: OSSRH_USERNAME" >&2
      exit 1
    fi

    if [ -z "$OSSRH_PASSWORD" ]; then
      echo "missing environment value: OSSRH_PASSWORD" >&2
      exit 1
    fi

    if [ -z "$GPG_KEY_NAME" ]; then
      echo "missing environment value: GPG_KEY_NAME" >&2
      exit 1
    fi

    if [ -z "$GPG_PASSPHRASE" ]; then
      echo "missing environment value: GPG_PASSPHRASE" >&2
      exit 1
    fi

    # Prepare the local keyring (requires travis to have decrypted the file
    # beforehand)
    gpg --fast-import cd/codesigning.gpg

    # Run the maven deploy steps
    mvn deploy -P publish -DskipTests=true --settings cd/mvnsettings.xml
fi