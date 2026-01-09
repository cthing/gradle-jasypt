/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.BiFunction;

import javax.swing.SwingUtilities;

import org.cthing.jasypt.CthingStringEncryptor;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskExecutionException;
import org.gradle.api.tasks.options.Option;
import org.jasypt.encryption.StringEncryptor;
import org.jspecify.annotations.Nullable;


/**
 * Base class for string encryption and decryption tasks.
 */
public abstract class AbstractStringEncryptorTask extends DefaultTask {

    private static final String PASSWORD_ENV_VAR = "CTHING_JASYPT_PASSWORD";
    private static final String PASSWORD_PROPERTY = "org.cthing.jasypt.password";

    @Nullable
    protected String string;

    private char[] password;

    @Nullable
    private Path passwordFile;

    @Nullable
    private StringEncryptor encryptor;

    private final Provider<String> passwordEnvironmentProvider;
    private final Provider<String> passwordPropertyProvider;

    @SuppressWarnings("this-escape")
    protected AbstractStringEncryptorTask(final ProviderFactory providerFactory) {
        setGroup("Encryption");

        this.password = new char[0];
        this.passwordEnvironmentProvider = providerFactory.environmentVariable(PASSWORD_ENV_VAR);
        this.passwordPropertyProvider = providerFactory.systemProperty(PASSWORD_PROPERTY);
    }

    @Option(option = "password", description = "Password (prompted if not provided)")
    public void setPassword(final String password) {
        this.password = password.toCharArray();
    }

    @Option(option = "password-file", description = "File containing password") // SUPPRESS CHECKSTYLE self-explanatory
    public void setPasswordFile(final String passwordFile) {
        setPasswordFile(Path.of(passwordFile));
    }

    public void setPasswordFile(final Path passwordFile) {
        this.passwordFile = passwordFile;
    }

    /**
     * Clears the password held by the task.
     */
    public void clearPassword() {
        Arrays.fill(this.password, '\0');
    }

    @Option(option = "string", description = "String to encrypt or decrypt")
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
        if (this.encryptor != null) {
            return this.encryptor;
        }

        if (isEmpty(this.password) && this.passwordFile != null) {
            try {
                this.password = readPassword(this.passwordFile);
                if (isEmpty(this.password)) {
                    throw new GradleException("Password read from '" + this.passwordFile + "' is empty");
                }
            } catch (final IOException ex) {
                throw new TaskExecutionException(this, ex);
            }
        }

        if (isEmpty(this.password)) {
            final String env = this.passwordEnvironmentProvider.getOrNull();
            if (env != null) {
                this.password = env.toCharArray();
            }
        }

        if (isEmpty(this.password)) {
            final String prop = this.passwordPropertyProvider.getOrNull();
            if (prop != null) {
                this.password = prop.toCharArray();
            }
        }

        if (isEmpty(this.password)) {
            this.password = prompt();
        }

        if (isEmpty(this.password)) {
            throw new GradleException("Password must be specified");
        }

        final CthingStringEncryptor enc = new CthingStringEncryptor();
        enc.setPassword(this.password);
        this.encryptor = enc;
        return this.encryptor;
    }

    private char[] prompt() {
        final Console console = System.console();

        final BiFunction<String, String, char[]> readPassword = (prompt1, prompt2) -> {
            final char[] pw = (console == null) ? promptSwing(prompt1) : console.readPassword(prompt2);
            if (pw == null) {
                throw new GradleException("Password entry cancelled.");
            }
            return pw;
        };

        while (true) {
            final char[] pw1 = readPassword.apply("dialog.prompt1", "Enter password: ");
            final char[] pw2 = readPassword.apply("dialog.prompt2", "Confirm password: ");

            if (Arrays.equals(pw1, pw2)) {
                return pw1;
            }

            System.err.println("Passwords do not match. Please try again.");
        }
    }

    private char @Nullable [] promptSwing(final String messageId) {
        if (GraphicsEnvironment.isHeadless()) {
            throw new GradleException("Cannot show dialog in headless environment.");
        }

        final char[][] result = new char[1][];

        try {
            SwingUtilities.invokeAndWait(() -> result[0] = new PasswordDialog(messageId).getPassword());
        } catch (final Exception ex) {
            throw new TaskExecutionException(this, ex);
        }

        return result[0];
    }

    private char[] readPassword(final Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            final char[] buffer = new char[256];
            final int numRead = reader.read(buffer);

            if (numRead == -1) {
                return new char[0];
            }

            final char[] pw = Arrays.copyOf(buffer, numRead);
            Arrays.fill(buffer, '\0');
            return trimTrailingNewlines(pw);
        }
    }

    private char[] trimTrailingNewlines(final char[] pw) {
        int length = pw.length;
        while (length > 0 && (pw[length - 1] == '\n' || pw[length - 1] == '\r')) {
            length--;
        }
        return Arrays.copyOf(pw, length);
    }

    private boolean isEmpty(final char[] array) {
        return array.length == 0;
    }
}
