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
 *     ./gradlew encryptString --string=string_to_encrypt --password=password
 * </pre>
 * <p>
 *     To decrypt a string, run the following command:
 * </p>
 * <pre>
 *     ./gradlew decryptString --string=string_to_decrypt --password=password
 * </pre>
 */
public class JasyptPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.getTasks().register(EncryptStringTask.TASK_NAME, EncryptStringTask.class);
        project.getTasks().register(DecryptStringTask.TASK_NAME, DecryptStringTask.class);
    }
}
