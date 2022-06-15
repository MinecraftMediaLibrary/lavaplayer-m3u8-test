plugins {
    java
}

group = "io.github.pulsebeat02"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://m2.dv8tion.net/releases")
    }
}

dependencies {
    implementation("com.sedmelluq:lavaplayer:1.3.77")
    implementation("net.dv8tion:JDA:5.0.0-alpha.12")
}