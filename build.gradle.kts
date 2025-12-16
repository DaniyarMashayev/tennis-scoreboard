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

dependencies {
    // Provided dependencies
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    compileOnly("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0")
    compileOnly("jakarta.servlet.jsp:jakarta.servlet.jsp-api:4.0.0")

    // Runtime dependencies
    runtimeOnly("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")

    // Core implementation dependencies
    implementation("org.hibernate.orm:hibernate-core:$hibernateVersion")
    implementation("com.h2database:h2:$h2Version")

    // Logging
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

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

    jvmArgs = listOf(
        "--add-opens=java.base/java.io=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED"
    )
}

tasks.build {
    group = "build"
    description = "Project compilation"

}

tasks.war {
    archiveFileName.set("${project.name}.war")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from("src/main/webapp")

    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    }

    doLast {
        println("WAR create: ${archiveFile.get()}")
    }
}

tasks.register<Copy>("deployToTomcat") {
    group = "deployment"
    dependsOn("build")
    doNotTrackState("External Tomcat directory")

    val tomcatPath = "/home/daniyar/tomcat/apache-tomcat-10.1.46"
    val webappsDir = "$tomcatPath/webapps"
    val appName = project.name
    val warFile = tasks.war.get().archiveFile.get().asFile

    doFirst {

        val appDir = File("$webappsDir/$appName")
        if (appDir.exists()) {
            project.delete(appDir)
        }

        val dir = File(webappsDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    from(warFile)
    into(webappsDir)

}

tasks.register<Exec>("appLaunch") {
    group = "deployment"
    dependsOn( "deployToTomcat")

    val tomcatPath = "/home/daniyar/tomcat/apache-tomcat-10.1.46"
    val appName = project.name

    doFirst {
        println("Unpacking $appName.war...")
    }

    executable = "zsh"
    args("-c", """
        cd $tomcatPath/webapps
        
        # Tomcat stop
        ../bin/shutdown.sh 2>/dev/null || print "Tomcat has already been stopped"
        sleep 2
        
        rm -rf $appName
        
        unzip -q -o $appName.war -d $appName/
        
        # Tomcat start
        ../bin/startup.sh
        
    """)

    doLast {
        println("Available at: http://localhost:8080/$appName/")
    }
}