plugins {
    idea
    id("java")
    id("java-library")
    id("io.freefair.lombok") version "8.6"
}

group = "org.example"
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hibernate:hibernate-gradle-plugin:5.6.3.Final")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("org.postgresql:postgresql:42.2.8")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar>() {
    manifest {
        attributes["Main-Class"] = "Main"
    }
    val dependencies = configurations
            .runtimeClasspath
            .get()
            .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

//    dependsOn(":shared:jar")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(arrayOf("--release", "17"))
}

tasks.compileJava{
    options.encoding = "UTF-8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}

tasks.create("deploy") {

    doLast {
        val user = (System.getenv("DEPLOYUSER") ?: "ERROR")
        val userAndHost : String = user + "@" + (System.getenv ("DEPLOYHOST")  ?: "ERROR")

        val pwd : String = System.getenv("DEPLOYPWD") ?: "ERROR"

        println("$userAndHost :$pwd")

        exec {
            workingDir(".")
            commandLine("pscp", "-pw", pwd, "-P", 2222, "${project.rootDir}/build/libs/**.jar", "$userAndHost:/home/studs/$user/databases/lab3")
//            commandLine("plink", "-x", "-a", "-ssh", userAndHost, "-P", "2222", "-pw", pwd)
        }
    }
}