plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
}

val spigot = property("spigot") as String
val projectVersion: String by project

dependencies {
    compileOnly("org.spigotmc:spigot-api:$spigot")
}

tasks {
    compileJava.get().options.encoding = Charsets.UTF_8.name()
    javadoc.get().options.encoding = Charsets.UTF_8.name()
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set(rootProject.name + "-${projectVersion}.jar")
        archiveClassifier.set("")
    }
    compileJava.get().dependsOn(clean)
    build.get().dependsOn(shadowJar)
}
