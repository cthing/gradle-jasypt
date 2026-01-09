/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import javax.inject.Inject;

import org.gradle.api.GradleException;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.TaskAction;


/**
 * Performs Jasypt string decryption.
 */
public class DecryptStringTask extends AbstractStringEncryptorTask {

    static final String TASK_NAME = "decryptString";

    private static final String SPRING_PREFIX = "ENC(";
    private static final int SPRING_PREFIX_LENGTH = SPRING_PREFIX.length();

    @SuppressWarnings("this-escape")
    @Inject
    public DecryptStringTask(final ProviderFactory providerFactory) {
        super(providerFactory);

        setDescription("Decrypts a string using Jasypt");
    }

    /**
     * Performs the string decryption.
     */
    @TaskAction
    public void decrypt() {
        if (this.string == null) {
            throw new GradleException("String to decrypt must be specified");
        }

        final String decrypted = getEncryptor().decrypt(unwrap(this.string));
        clearPassword();
        System.out.printf("%nDecrypted string: %s%n%n", decrypted);
    }

    /**
     * Strips 'ENC(' and ')' if they wrap the string.
     *
     * @param value String to unwarp
     */
    private static String unwrap(final String value) {
        if (value.startsWith(SPRING_PREFIX) && value.endsWith(")")) {
            return value.substring(SPRING_PREFIX_LENGTH, value.length() - 1);
        }
        return value;
    }
}
