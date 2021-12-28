plugins {
    java
    id("com.cthing.gradle-plugin-project") version("0.1.0-+")
    id("com.cthing.dependency-analysis") version("0.1.0-+")
}

projectInfo {
    description.set("Plugin for encrypting and decrypting values using Jasypt.")
    projectUrl.set("https://github.com/baron1405/gradle-jasypt-plugin/")
}

gradlePlugin {
    plugins.create("jasyptPlugin") {
        id = "com.cthing.jasypt"
        implementationClass = "com.cthing.gradle.plugins.jasypt.JasyptPlugin"
    }
}

dependencies {
    implementation("com.cthing:gradle-core-plugins:0.1.0-+")
    implementation("com.cthing:cthing-jasypt:0.1.0-+")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.assertj:assertj-core:3.21.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}
