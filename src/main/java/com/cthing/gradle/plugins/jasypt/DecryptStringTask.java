/*
 * Copyright 2020 C Thing Software
 * All rights reserved.
 */
package com.cthing.gradle.plugins.jasypt;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;


/**
 * Performs Jasypt string decryption.
 */
public class DecryptStringTask extends AbstractStringEncryptorTask {

    static final String TASK_NAME = "decryptString";

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
        System.out.println(String.format("%nDecrypted string: %s%n", decrypted));
    }
}
