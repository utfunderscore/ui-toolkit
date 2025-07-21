plugins {
    `java-library`
}

group = "org.readutf"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.nexomc.com/releases/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("net.minestom:minestom:2025.07.14-1.21.7")

    implementation("net.kyori:adventure-api:4.22.0")

    implementation("com.google.code.gson:gson:2.13.1")

    implementation("team.unnamed:creative-api:1.9.1")
    implementation("team.unnamed:creative-serializer-minecraft:1.9.1")
    implementation("team.unnamed:creative-server:1.9.1")


    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("org.tinylog:slf4j-tinylog:2.7.0")
}

tasks.test {
    useJUnitPlatform()
}