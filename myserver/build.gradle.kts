plugins {
    kotlin("jvm")
    application
}

group = "com.example.myserver"
version = "1.0-SNAPSHOT"


dependencies {
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    implementation("io.ktor:ktor-serialization-gson:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
}

application {
    mainClass.set("com.example.myserver.MainKt")
}