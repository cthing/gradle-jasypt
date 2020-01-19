import com.cthing.gradle.plugins.core.ProjectInfoExtension

plugins {
    java
}

buildscript {
    dependencies {
        classpath("com.cthing:gradle-core-plugins:0.1.0-+")
        classpath("com.cthing:gradle-dependency-analysis-plugin:0.1.0-+")
    }
}

apply {
    plugin("com.cthing.gradle-plugin-project")
    plugin("com.cthing.dependency-analysis")
}

configure<ProjectInfoExtension> {
    description = "Plugin for encrypting and decrypting values using Jasypt."
    projectUrl = "https://bitbucket.org/cthing/gradle-jasypt-plugin/"
}

configure<GradlePluginDevelopmentExtension> {
    plugins.register("jasyptPlugin") {
        id = "com.cthing.jasypt"
        implementationClass = "com.cthing.gradle.plugins.jasypt.JasyptPlugin"
    }
}

dependencies {
    implementation("com.cthing:gradle-core-plugins:0.1.0-+")
    implementation("com.cthing:cthing-jasypt:0.1.0-+")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.assertj:assertj-core:3.14.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}
