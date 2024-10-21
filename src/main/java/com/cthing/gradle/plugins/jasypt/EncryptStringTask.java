/*
 * Copyright 2020 C Thing Software
 * All rights reserved.
 */
package com.cthing.gradle.plugins.jasypt;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;


/**
 * Performs Jasypt string encryption.
 */
public class EncryptStringTask extends AbstractStringEncryptorTask {

    static final String TASK_NAME = "encryptString";

    @SuppressWarnings("this-escape")
    public EncryptStringTask() {
        setDescription("Encrypts a string using Jasypt");
    }

    /**
     * Performs the string encryption.
     */
    @TaskAction
    public void encrypt() {
        if (this.string == null) {
            throw new GradleException("String to encrypt must be specified");
        }

        final String encrypted = getEncryptor().encrypt(this.string);
        System.out.printf("%nEncrypted string: %s%n%n", encrypted);
    }
}
