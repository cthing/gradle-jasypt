/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;

import org.gradle.api.GradleException;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;
import org.gradle.api.tasks.options.Option;
import org.jspecify.annotations.Nullable;


/**
 * Performs Jasypt string encryption.
 */
public class EncryptStringTask extends AbstractStringEncryptorTask {

    private enum Format {
        plain,
        spring
    }

    static final String TASK_NAME = "encryptString";

    @Nullable
    private Path file;

    private Format format = Format.plain;

    @SuppressWarnings("this-escape")
    @Inject
    public EncryptStringTask(final ProviderFactory providerFactory) {
        super(providerFactory);

        setDescription("Encrypts a string using Jasypt");
    }

    @Option(option = "file", description = "File to encrypt")       // SUPPRESS CHECKSTYLE self-explanatory
    public void setFile(final String file) {
        setFile(Path.of(file));
    }

    public void setFile(final Path file) {
        this.file = file;
    }

    @Option(option = "format", description = "Encrypted string format")
    public void setFormat(final Format format) {
        this.format = format;
    }

    /**
     * Performs the string encryption.
     */
    @TaskAction
    public void encrypt() {
        if (this.string == null && this.file == null) {
            throw new GradleException("Either string or file must be specified");
        }

        if (this.string != null && this.file != null) {
            throw new GradleException("Cannot specify both string and file");
        }

        try {
            final String rawString = (this.string == null) ? Files.readString(this.file) : this.string;
            final String encrypted = getEncryptor().encrypt(rawString);
            clearPassword();
            if (this.format == Format.plain) {
                System.out.printf("%nEncrypted string: %s%n%n", encrypted);
            } else {
                System.out.printf("%nEncrypted string: ENC(%s)%n%n", encrypted);
            }
        } catch (final IOException ex) {
            throw new TaskExecutionException(this, ex);
        }
    }
}
