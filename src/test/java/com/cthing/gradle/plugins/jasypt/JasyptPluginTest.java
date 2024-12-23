/*
 * Copyright 2020 C Thing Software
 * All rights reserved.
 */
package com.cthing.gradle.plugins.jasypt;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import com.cthing.gradle.plugins.test.GradleProjectAssert;


public class JasyptPluginTest {

    @Test
    public void testApply() {
        final Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("com.cthing.jasypt");

        GradleProjectAssert.assertThat(project).hasTask("encryptString");
        GradleProjectAssert.assertThat(project).hasTask("decryptString");
    }
}
