plugins {
    kotlin("jvm") version "1.6.10"
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

group = "org.doc.k"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val jimmerVersion = "0.7.65"

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:${jimmerVersion}")
    ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerVersion}")

    runtimeOnly("com.h2database:h2:2.1.212")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}