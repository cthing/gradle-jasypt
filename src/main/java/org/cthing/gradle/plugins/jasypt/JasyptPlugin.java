/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import org.gradle.api.Plugin;
import org.gradle.api.Project;


/**
 * Provides Jasypt encryption and decryption tasks.
 * <p>
 *     To encrypt a string, run the following command:
 * </p>
 * <pre>
 *     ./gradlew encryptString --string=string_to_encrypt [--password=password] [--password-file=file]
 * </pre>
 * <p>
 *     To encrypt the contents of a file, run the following command:
 * </p>
 * <pre>
 *     ./gradlew encryptString --file=pathname [--password=password] [--password-file=file]
 * </pre>
 * <p>
 *     By default, the encrypted string is output as a base64 encoded string. If {@code --format=spring} is
 *     specified, the encoded string is wrapped in "ENC(encoded_string)" which is the format expected by
 *     Spring Boot for encrypted data.
 * </p>
 * <p>
 *     To decrypt a string, run the following command:
 * </p>
 * <pre>
 *     ./gradlew decryptString --string=string_to_decrypt [--password=password] [--password-file=file]
 * </pre>
 * <p>
 *     The encryption/decryption password can be provided in a number of ways. In order of highest to lowest
 *     precedence:
 * </p>
 * <ol>
 *     <li>Using the {@code --password} command-line option</li>
 *     <li>From the file specified by the {@code --password-file} command-line option</li>
 *     <li>Using the {@code CTHING_JASYPT_PASSWORD} environment variables</li>
 *     <li>Using the {@code org.cthing.jasypt.password} system property</li>
 * </ol>
 * <p>>
 *     If none of these mechanisms are used, a prompt is displayed to securely enter the password.
 * </p>
 */
public class JasyptPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.getTasks().register(EncryptStringTask.TASK_NAME, EncryptStringTask.class);
        project.getTasks().register(DecryptStringTask.TASK_NAME, DecryptStringTask.class);
    }
}
