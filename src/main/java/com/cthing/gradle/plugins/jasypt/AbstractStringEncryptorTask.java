/*
 * Copyright 2020 C Thing Software
 * All rights reserved.
 */
package com.cthing.gradle.plugins.jasypt;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.options.Option;
import org.jasypt.encryption.StringEncryptor;

import com.cthing.jasypt.CthingStringEncryptor;


/**
 * Base class for string encryption and decryption tasks.
 */
public abstract class AbstractStringEncryptorTask extends DefaultTask {

    protected String string;

    private String password;
    private StringEncryptor encryptor;

    protected AbstractStringEncryptorTask() {
        setGroup("Encryption");
    }

    @Option(option = "password", description = "password [required]")
    public void setPassword(final String password) {
        this.password = password;
    }

    @Option(option = "string", description = "[required]")
    public void setString(final String string) {
        this.string = string;
    }

    /**
     * Obtains a string encryptor.
     *
     * @return String encryptor.
     */
    @Internal
    protected StringEncryptor getEncryptor() {
        if (this.encryptor == null) {
            if (this.password == null) {
                throw new GradleException("Password must be specified");
            }

            final CthingStringEncryptor enc = new CthingStringEncryptor();
            enc.setPassword(this.password);
            this.encryptor = enc;
        }
        return this.encryptor;
    }
}
