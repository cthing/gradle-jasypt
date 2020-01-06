/*
 * Copyright 2020 C Thing Software
 * All rights reserved.
 */
package com.cthing.gradle.plugins.jasypt;

import org.gradle.api.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.cthing.gradle.plugins.test.GradleProjectAssert;
import com.cthing.gradle.plugins.test.GradleTestProjectExtension;


public class JasyptPluginTest {

    @RegisterExtension
    public final GradleTestProjectExtension extension = new GradleTestProjectExtension("project", "com.cthing.jasypt");

    private Project project;

    @BeforeEach
    public void setUp(final Project project) {
        this.project = project;
    }

    @Test
    public void testApply() {
        GradleProjectAssert.assertThat(this.project).hasTask("encryptString");
        GradleProjectAssert.assertThat(this.project).hasTask("decryptString");
    }
}
