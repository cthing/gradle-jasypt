/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import org.cthing.jasypt.CthingStringEncryptor;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.options.Option;
import org.jasypt.encryption.StringEncryptor;
import org.jspecify.annotations.Nullable;


/**
 * Base class for string encryption and decryption tasks.
 */
public abstract class AbstractStringEncryptorTask extends DefaultTask {

    @Nullable
    protected String string;

    @Nullable
    private String password;

    @Nullable
    private StringEncryptor encryptor;

    @SuppressWarnings("this-escape")
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
