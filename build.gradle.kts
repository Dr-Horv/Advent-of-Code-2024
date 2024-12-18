plugins {
    application
    kotlin("jvm") version "2.0.21"
}

group = "se.horv"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

application {
    mainClass = "se.horv.MainKt"
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}