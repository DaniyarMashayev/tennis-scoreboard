plugins {
    id("java")
    id("war")
    id("org.gretty") version "4.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

val hibernateVersion = "6.4.0.Final"
val h2Version = "2.2.224"
val junitJupiterVersion = "5.10.1"
val slf4jVersion = "2.0.9"
val logbackVersion = "1.4.11"

dependencies {
    // Provided dependencies
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    compileOnly("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0")
    compileOnly("jakarta.servlet.jsp:jakarta.servlet.jsp-api:3.1.1")

    // Runtime dependencies
    runtimeOnly("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")

    // Core implementation dependencies
    implementation("org.hibernate.orm:hibernate-core:$hibernateVersion")
    implementation("com.h2database:h2:$h2Version")

    // Logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // For JSP and EL
    implementation("org.glassfish:jakarta.el:4.0.2")

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.8.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

gretty {
    contextPath = "/"
    servletContainer = "tomcat10"
    integrationTestTask = "test"
}

tasks.war {
    archiveFileName.set("${project.name}.war")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from("src/main/webapp")
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.register<Copy>("deployToTomcat") {
    from(tasks.war)
    into("/opt/tomcat/tomcat/webapps")
    description = "Copies WAR file to external Tomcat webapps directory"
    doNotTrackState("Destination directory has restricted access")
}