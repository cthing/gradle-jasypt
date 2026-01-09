import java.nio.file.Files
import org.cthing.gradle.plugins.jasypt.DecryptStringTask
import org.cthing.gradle.plugins.jasypt.EncryptStringTask

plugins {
    id("base")
    id("org.cthing.jasypt")
}

tasks {
    "encryptString"(EncryptStringTask::class) {
        setPassword("password1234")
        setString("hello world")
    }

    register("encryptFile", EncryptStringTask::class) {
        val contentFile = Files.createTempFile("gradle-jasypt", null)
        Files.writeString(contentFile, "hello world")

        setPassword("password1234")
        setFile(contentFile)

        doLast {
            Files.delete(contentFile)
        }
    }

    register("encryptPasswordFile", EncryptStringTask::class) {
        val pwdFile = Files.createTempFile("gradle-jasypt", null)
        Files.writeString(pwdFile, "password1234\n")

        setPasswordFile(pwdFile)
        setString("hello world")

        doLast {
            Files.delete(pwdFile)
        }
    }

    register("encrypt", EncryptStringTask::class) {
        setString("hello world")
    }

    "decryptString"(DecryptStringTask::class) {
        setPassword("password1234")
        setString("Tw1dLEf1uzftn7roG22rthjTfhmh3YU6CWT1SldAMi2sr4swStu4X1f5jxKjuxwn")
    }

    register("decryptPasswordFile", DecryptStringTask::class) {
        val pwdFile = Files.createTempFile("gradle-jasypt", null)
        Files.writeString(pwdFile, "password1234\n")

        setPasswordFile(pwdFile)
        setString("Tw1dLEf1uzftn7roG22rthjTfhmh3YU6CWT1SldAMi2sr4swStu4X1f5jxKjuxwn")

        doLast {
            Files.delete(pwdFile)
        }
    }

    register("decryptSpring", DecryptStringTask::class) {
        setPassword("password1234")
        setString("ENC(Tw1dLEf1uzftn7roG22rthjTfhmh3YU6CWT1SldAMi2sr4swStu4X1f5jxKjuxwn)")
    }

    register("decrypt", DecryptStringTask::class) {
        setString("Tw1dLEf1uzftn7roG22rthjTfhmh3YU6CWT1SldAMi2sr4swStu4X1f5jxKjuxwn")
    }
}
