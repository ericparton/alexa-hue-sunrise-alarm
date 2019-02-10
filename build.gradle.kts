plugins {
    java
    application
    id("com.bmuschko.docker-java-application") version "4.4.1"
}

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.apache.logging.log4j:log4j-core:2.11.2")
    //TODO: submit pull request for changes to this library and try to get it up on jcenter or maven central
    implementation("com.github.zeroone3010:yetanotherhueapi:0.2.0-SNAPSHOT")
}

application {
    mainClassName = "com.djungelorm.alexa.hue.wakeup.timer.App"
}

docker {
    javaApplication {
        baseImage.set("openjdk:11-jdk-stretch")
    }
}