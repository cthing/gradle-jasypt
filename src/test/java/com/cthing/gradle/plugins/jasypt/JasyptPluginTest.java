/*
 * Copyright 2020 C Thing Software
 * All rights reserved.
 */
package com.cthing.gradle.plugins.jasypt;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.cthing.assertj.gradle.GradleProjectAssert.assertThat;


public class JasyptPluginTest {

    @Test
    public void testApply() {
        final Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("com.cthing.jasypt");

        assertThat(project).hasTask("encryptString", "decryptString");
    }
}
