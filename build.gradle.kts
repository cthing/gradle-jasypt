import org.cthing.projectversion.BuildType
import org.cthing.projectversion.ProjectVersion

apply(from = File(gradle.gradleUserHomeDir, "cthing-repositories.gradle.kts"))

plugins {
    java
    alias(libs.plugins.pluginProject)
}

version = ProjectVersion("0.3.0", BuildType.snapshot)

projectInfo {
    description = "Plugin for encrypting and decrypting values using Jasypt."
    projectUrl = "https://github.com/cthing/gradle-jasypt/"
}

gradlePlugin {
    plugins.create("jasyptPlugin") {
        id = "com.cthing.jasypt"
        implementationClass = "com.cthing.gradle.plugins.jasypt.JasyptPlugin"
    }
}

dependencies {
    implementation(libs.corePlugins)
    implementation(libs.cthingJasypt)

    testImplementation(libs.assertJGradle)
    testImplementation(libs.junitApi)

    testRuntimeOnly(libs.junitEngine)
    testRuntimeOnly(libs.junitLauncher)
}
