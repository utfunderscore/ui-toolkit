import java.util.Properties

plugins {
    `java-library`
    `maven-publish`
}

group = "org.readutf.ui"
version = "1.0.4"

repositories {490
    mavenCentral()
    maven("https://repo.nexomc.com/releases/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("net.minestom:minestom:2025.07.14-1.21.7")
    testImplementation("org.tinylog:tinylog-api:2.7.0")
    testImplementation("org.tinylog:tinylog-impl:2.7.0")
    testImplementation("org.tinylog:slf4j-tinylog:2.7.0")

    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("net.kyori:adventure-api:4.22.0")
    implementation("com.google.code.gson:gson:2.13.1")

    api("team.unnamed:creative-api:1.9.1")
    api("team.unnamed:creative-serializer-minecraft:1.9.1")
    api("team.unnamed:creative-server:1.9.1")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "org.readutf.ui"
            artifactId = "ui-toolkit"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}