/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;


/**
 * Performs Jasypt string decryption.
 */
public class DecryptStringTask extends AbstractStringEncryptorTask {

    static final String TASK_NAME = "decryptString";

    @SuppressWarnings("this-escape")
    public DecryptStringTask() {
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

        final String decrypted = getEncryptor().decrypt(this.string);
        System.out.printf("%nDecrypted string: %s%n%n", decrypted);
    }
}
