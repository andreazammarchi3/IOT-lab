plugins {
    id("java")
    application
}

group = "org.garden"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    implementation("io.github.java-native:jssc:2.9.4")
    implementation("io.vertx:vertx-core:4.3.4")
    implementation("io.vertx:vertx-mqtt:4.3.4")
}

application {
    mainClass.set("org.garden.GardenService")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}