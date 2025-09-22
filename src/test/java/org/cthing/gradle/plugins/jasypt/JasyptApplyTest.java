/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.cthing.assertj.gradle.GradleProjectAssert.assertThat;


public class JasyptApplyTest {

    @Test
    public void testApply() {
        final Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("org.cthing.jasypt");

        assertThat(project).hasTask("encryptString", "decryptString");
    }
}
