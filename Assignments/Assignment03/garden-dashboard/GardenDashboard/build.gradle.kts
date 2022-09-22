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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

application {
    mainClass.set("org.garden.GardenDashboard")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}