plugins {
    java
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.runelite.net")
    }
    mavenCentral()
}

var runeLiteVersion = "1.8.16"


dependencies {

    implementation(group = "net.runelite", name = "client", version = runeLiteVersion)
    implementation(group = "net.runelite", name = "cache", version = runeLiteVersion)

    implementation("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.slf4j:slf4j-simple:1.7.36")


    implementation(group = "com.github.joonasvali.naturalmouse", name = "naturalmouse", version = "2.0.2")
    implementation(group = "javassist", name = "javassist", version = "3.12.1.GA")
    implementation(group = "net.sf.jopt-simple", name = "jopt-simple", version = "5.0.4")
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/java"))
        }
        resources {
            setSrcDirs(listOf("src/main/resources"))
        }
    }
}

project.configurations.implementation {
    isCanBeResolved = true
}

tasks.withType<Jar> {
    manifest {
                attributes["Main-Class"] = "rsb.botLauncher.Application"
                attributes["Class-Path"] = configurations.implementation.get()
                        .joinToString(separator = " ") { file ->
                            file.name
                        }
    }
    from({
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    })

    exclude("META-INF/*.RSA")
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

group = "osrsb"
version = "1.0.0"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}