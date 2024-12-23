import com.cthing.gradle.plugins.jasypt.EncryptStringTask
import com.cthing.gradle.plugins.jasypt.DecryptStringTask
import org.cthing.projectversion.BuildType
import org.cthing.projectversion.ProjectVersion

plugins {
    id("base")
    id("com.cthing.jasypt")
}

version = ProjectVersion("0.1.0", BuildType.snapshot)

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
