import org.cthing.gradle.plugins.jasypt.EncryptStringTask
import org.cthing.gradle.plugins.jasypt.DecryptStringTask

plugins {
    id("base")
    id("org.cthing.jasypt")
}

tasks {
    "encryptString"(EncryptStringTask::class) {
        setPassword("password1234")
        setString("hello world")
    }
}

tasks {
    "decryptString"(DecryptStringTask::class) {
        setPassword("password1234")
        setString("Tw1dLEf1uzftn7roG22rthjTfhmh3YU6CWT1SldAMi2sr4swStu4X1f5jxKjuxwn")
    }
}
