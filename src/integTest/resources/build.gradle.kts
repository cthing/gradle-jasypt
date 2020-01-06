import com.cthing.gradle.plugins.jasypt.EncryptStringTask
import com.cthing.gradle.plugins.jasypt.DecryptStringTask

apply {
    plugin("base")
    plugin("com.cthing.jasypt")
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
