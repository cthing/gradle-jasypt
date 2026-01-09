/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.io.file.PathUtils;
import org.cthing.jasypt.CthingStringEncryptor;
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
    private static final String TEST_PASSWORD = "password1234";
    private static final Pattern PLAIN_ENCRYPTED_PATTERN = Pattern.compile("Encrypted string:\\s(.+)");
    private static final Pattern SPRING_ENCRYPTED_PATTERN = Pattern.compile("Encrypted string:\\sENC\\((.+)\\)");

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
    public void testEncryptString(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "encryptString").build();
        verifyBuild(result, "encryptString");
        verifyPlainEncryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testEncryptStringOption(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "encrypt", "--password=" + TEST_PASSWORD).build();
        verifyBuild(result, "encrypt");
        verifyPlainEncryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testEncryptSpringBoot(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "encryptString", "--format=spring").build();
        verifyBuild(result, "encryptString");
        verifySpringEncryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testEncryptFile(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "encryptFile").build();
        verifyBuild(result, "encryptFile");
        verifyPlainEncryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testEncryptPasswordFile(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "encryptPasswordFile").build();
        verifyBuild(result, "encryptPasswordFile");
        verifyPlainEncryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testEncryptPasswordEnvironment(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "encrypt")
                .withEnvironment(Map.of("CTHING_JASYPT_PASSWORD", TEST_PASSWORD))
                .build();
        verifyBuild(result, "encrypt");
        verifyPlainEncryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testEncryptPasswordProperty(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "encrypt",
                                                      "-Dorg.cthing.jasypt.password=" + TEST_PASSWORD).build();
        verifyBuild(result, "encrypt");
        verifyPlainEncryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testDecryptString(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "decryptString").build();
        verifyBuild(result, "decryptString");
        verifyDecryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testDecryptSpringBoot(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "decryptSpring").build();
        verifyBuild(result, "decryptSpring");
        verifyDecryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testDecryptPasswordFile(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "decryptPasswordFile").build();
        verifyBuild(result, "decryptPasswordFile");
        verifyDecryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testDecryptPasswordEnvironment(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "decrypt")
                .withEnvironment(Map.of("CTHING_JASYPT_PASSWORD", TEST_PASSWORD))
                .build();
        verifyBuild(result, "decrypt");
        verifyDecryption(result);
    }

    @ParameterizedTest
    @MethodSource("gradleVersionProvider")
    public void testDecryptPasswordProperty(final String gradleVersion) throws IOException {
        copyProject();

        final BuildResult result = createGradleRunner(gradleVersion, "decrypt",
                                                      "-Dorg.cthing.jasypt.password=" + TEST_PASSWORD).build();
        verifyBuild(result, "decrypt");
        verifyDecryption(result);
    }

    private void copyProject() throws IOException {
        final URL projectUrl = getClass().getResource("/jasypt-project");
        assertThat(projectUrl).isNotNull();
        PathUtils.copyDirectory(Path.of(projectUrl.getPath()), this.projectDir);
    }

    private GradleRunner createGradleRunner(final String gradleVersion, final String taskName,
                                            final String... arguments) {
        final List<String> argumentsList = new ArrayList<>();
        argumentsList.add(taskName);
        if (arguments.length > 0) {
            argumentsList.addAll(Arrays.asList(arguments));
        }

        return GradleRunner.create()
                           .withProjectDir(this.projectDir.toFile())
                           .withTestKitDir(WORKING_DIR.toFile())
                           .withArguments(argumentsList)
                           .withPluginClasspath()
                           .withGradleVersion(gradleVersion);
    }

    private void verifyBuild(final BuildResult result, final String taskName) {
        final BuildTask task = result.task(":" + taskName);
        assertThat(task).isNotNull();
        assertThat(task.getOutcome()).as(result.getOutput()).isEqualTo(TaskOutcome.SUCCESS);
    }

    private void verifyPlainEncryption(final BuildResult result) {
        verifyEncryption(result, PLAIN_ENCRYPTED_PATTERN);
    }

    private void verifySpringEncryption(final BuildResult result) {
        verifyEncryption(result, SPRING_ENCRYPTED_PATTERN);
    }

    private void verifyEncryption(final BuildResult result, final Pattern regex) {
        assertThat(result.getOutput()).containsPatternSatisfying(regex, matcher -> {
            final CthingStringEncryptor enc = new CthingStringEncryptor();
            enc.setPassword(TEST_PASSWORD);

            final String encryptedString = matcher.group(1);
            final String decryptedString = enc.decrypt(encryptedString);
            assertThat(decryptedString).isEqualTo("hello world");
        });
    }

    private void verifyDecryption(final BuildResult result) {
        assertThat(result.getOutput()).contains("Decrypted string: hello world");
    }
}
