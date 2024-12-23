/*
 * Copyright 2024 C Thing Software
 * All rights reserved.
 */
package com.cthing.gradle.plugins.jasypt;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;

import com.cthing.gradle.plugins.test.AbstractPluginTest;

import static org.assertj.core.api.Assertions.assertThat;


public class JasyptIntegTest extends AbstractPluginTest {

    @Test
    public void testEncrypt() {
        copyProject("jasypt-project");

        final BuildResult result = createGradleRunner("encryptString").build();
        verifyBuild(result, "encryptString");

        assertThat(result.getOutput()).contains("Encrypted string:");
    }

    @Test
    public void testDecrypt() {
        copyProject("jasypt-project");

        final BuildResult result = createGradleRunner("decryptString").build();
        verifyBuild(result, "decryptString");

        assertThat(result.getOutput()).contains("Decrypted string: hello world");
    }
}
