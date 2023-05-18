apply(from = File(gradle.gradleUserHomeDir, "cthing-repositories.gradle.kts"))

plugins {
    java
    alias(libs.plugins.pluginProject)
    alias(libs.plugins.dependencyAnalysis)
}

projectInfo {
    description.set("Plugin for encrypting and decrypting values using Jasypt.")
    projectUrl.set("https://github.com/cthing/gradle-jasypt/")
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

    testImplementation(libs.junitApi)
    testImplementation(libs.assertJ)

    testRuntimeOnly(libs.junitEngine)
}
