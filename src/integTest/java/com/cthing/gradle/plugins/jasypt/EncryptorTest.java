/*
 * Copyright 2018 C Thing Software
 * All rights reserved.
 */
package com.cthing.gradle.plugins.jasypt;

import org.gradle.api.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.cthing.gradle.plugins.test.BuildOutcome;
import com.cthing.gradle.plugins.test.GradleTestProjectExtension;

import static com.cthing.gradle.plugins.test.PluginTestUtils.assertThat;
import static com.cthing.gradle.plugins.test.PluginTestUtils.copyResources;
import static com.cthing.gradle.plugins.test.PluginTestUtils.runBuild;


public class EncryptorTest {

    @RegisterExtension
    public final GradleTestProjectExtension extension = new GradleTestProjectExtension("project");

    private Project project;

    @BeforeEach
    public void setup(final Project project) {
        this.project = project;
        copyResources(this.project, "build.gradle.kts");
    }

    @Test
    public void testEncrypt() {
        final BuildOutcome outcome = runBuild(this.project, "encryptString");
        assertThat(outcome).isSuccess();
        org.assertj.core.api.Assertions.assertThat(outcome.getOutput()).contains("Encrypted string:");
    }

    @Test
    public void testDecrypt() {
        final BuildOutcome outcome = runBuild(this.project, "decryptString");
        assertThat(outcome).isSuccess();
        org.assertj.core.api.Assertions.assertThat(outcome.getOutput()).contains("Decrypted string: hello world");
    }
}
