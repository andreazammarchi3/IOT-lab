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
    implementation(files("libs/jssc.jar"))
    implementation("io.vertx:vertx-core:4.3.3")
    implementation("io.vertx:vertx-mqtt:4.3.3")
}

application {
    mainClass.set("org.garden.GardenService")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}