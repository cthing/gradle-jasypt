/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.apache.commons.io.file.PathUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.gradle.util.GradleVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class JasyptIntegTest {
    private static final Path BASE_DIR = Path.of(System.getProperty("buildDir"), "integTest");
    private static final Path WORKING_DIR = Path.of(System.getProperty("projectDir"), "testkit");

    static {
        try {
            Files.createDirectories(BASE_DIR);
            Files.createDirectories(WORKING_DIR);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Path projectDir;

    @BeforeEach
    public void setup() throws IOException {
        this.projectDir = Files.createTempDirectory(BASE_DIR, "project");
    }

    public static Stream<Arguments> gradleVersionProvider() {
        return Stream.of(
                arguments("8.0"),
                arguments(GradleVersion.current().getVersion())
        );
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testEncrypt(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "encryptString").build();
        verifyBuild(result, "encryptString");

        assertThat(result.getOutput()).contains("Encrypted string:");
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testDecrypt(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "decryptString").build();
        verifyBuild(result, "decryptString");

        assertThat(result.getOutput()).contains("Decrypted string: hello world");
    }

    private void copyProject() throws IOException {
        final URL projectUrl = getClass().getResource("/jasypt-project");
        assertThat(projectUrl).isNotNull();
        PathUtils.copyDirectory(Path.of(projectUrl.getPath()), this.projectDir);
    }

    private GradleRunner createGradleRunner(final String gradleVersion, final String taskName) {
        return GradleRunner.create()
                           .withProjectDir(this.projectDir.toFile())
                           .withTestKitDir(WORKING_DIR.toFile())
                           .withArguments(taskName)
                           .withPluginClasspath()
                           .withGradleVersion(gradleVersion);
    }

    private void verifyBuild(final BuildResult result, final String taskName) {
        final BuildTask task = result.task(":" + taskName);
        assertThat(task).isNotNull();
        assertThat(task.getOutcome()).as(result.getOutput()).isEqualTo(TaskOutcome.SUCCESS);
    }
}
