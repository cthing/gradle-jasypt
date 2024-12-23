rootProject.name = "jasypt-project"

pluginManagement {
    repositories {
        maven {
            setUrl(providers.gradleProperty("cthing.nexus.downloadUrl").get())
        }
        gradlePluginPortal()
    }
}
