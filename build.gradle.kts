import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

val projectGroup = "live.lingting.spring"
val projectVersion = "2024.01.24-Bata-3"

// 用于子模块获取包管理信息
val catalogLibs = libs
// 用于声明依赖的项目
val dependencyProjects = subprojects.filter { it.name.endsWith("dependencies") }
// java 项目
val javaProjects = subprojects.filter { it.name.startsWith("lingting-") && !dependencyProjects.contains(it) }
// 使用的java版本
val javaVersion = JavaVersion.VERSION_17
// 字符集
val encoding = "UTF-8"
val ideaLanguageLevel = IdeaLanguageLevel(javaVersion);

plugins {
    id("idea")
    id("java")
    id("com.vanniktech.maven.publish") version "0.29.0"
    id("signing")
}

idea {
    project {
        languageLevel = ideaLanguageLevel
        targetBytecodeVersion = javaVersion
    }
}

buildscript {
    dependencies {
        classpath(platform(libs.frameworkDependencies))
        classpath("io.spring.javaformat:spring-javaformat-gradle-plugin")
    }
}

allprojects {
    group = projectGroup
    version = projectVersion

    val isJava = javaProjects.contains(project)
    val isDependency = dependencyProjects.contains(project)

    apply {
        plugin("idea")
        plugin("com.vanniktech.maven.publish")
        plugin("signing")
    }

    if (isJava) {
        apply {
            plugin("java")
            plugin("java-library")
        }
    }

    if (isDependency) {
        apply {
            plugin("java-platform")
        }
    }

    idea {
        module {
            languageLevel = ideaLanguageLevel
            targetBytecodeVersion = javaVersion

            excludeDirs.add(File(rootDir, "src"))
        }
    }

    mavenPublishing {
        val projectRepository = "lingting-projects/lingting-spring"
        val projectUrl = "https://github.com/$projectRepository"

        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
        signAllPublications()

        pom {
            name = project.name
            description = if (project.description.isNullOrBlank()) {
                project.name
            } else {
                project.description
            }
            url = projectUrl

            licenses {
                license {
                    name = "MIT License"
                    url = "https://www.opensource.org/licenses/mit-license.php"
                    distribution = "repo"
                }
            }

            developers {
                developer {
                    id = "lingting"
                    name = id
                    email = "sunlisten.gzm@gmail.com"
                    url = "https://github.com/lingting"
                }
            }

            scm {
                connection = "scm:git:git@github.com:$projectRepository.git"
                developerConnection = "scm:git:git@github.com:$projectRepository.git"
                url = projectUrl
                tag = "HEAD"
            }
        }

    }

}

configure(javaProjects) {
    apply {
        plugin("io.spring.javaformat")
        plugin("checkstyle")
    }

    dependencies {
        add("checkstyle", platform(catalogLibs.frameworkDependencies))
        add("checkstyle", "io.spring.javaformat:spring-javaformat-checkstyle")

        add("implementation", platform(catalogLibs.frameworkDependencies))

        add("annotationProcessor", platform(catalogLibs.frameworkDependencies))
        add("testAnnotationProcessor", platform(catalogLibs.frameworkDependencies))
        add("annotationProcessor", "org.springframework.boot:spring-boot-configuration-processor")

        add("implementation", "org.slf4j:slf4j-api")
        add("implementation", "org.mapstruct:mapstruct")
        add("compileOnly", "org.projectlombok:lombok")
        add("testCompileOnly", "org.projectlombok:lombok")

        add("annotationProcessor", "org.mapstruct:mapstruct-processor")
        add("annotationProcessor", "org.projectlombok:lombok")
        add("annotationProcessor", "org.projectlombok:lombok-mapstruct-binding")

        add("testAnnotationProcessor", "org.mapstruct:mapstruct-processor")
        add("testAnnotationProcessor", "org.projectlombok:lombok")
        add("testAnnotationProcessor", "org.projectlombok:lombok-mapstruct-binding")

        add("testImplementation", "org.awaitility:awaitility")
        add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = encoding
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Javadoc> {
        isFailOnError = false
        options.encoding(encoding)
    }
}
