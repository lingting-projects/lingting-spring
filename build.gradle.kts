import java.net.URI
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

val projectGroup = "live.lingting.spring"
val projectVersion = "2024.11.21-Bata-4"

// 用于子模块获取包管理信息
val catalogLibs = libs
// 用于声明依赖的项目
val dependencyProjects = subprojects.filter { it.name.endsWith("dependencies") }
// java 项目
val javaProjects = subprojects.filter { it.name.startsWith("lingting-") && !dependencyProjects.contains(it) }
// 使用的java版本
val javaVersion = JavaVersion.VERSION_21
// 字符集
val encoding = "UTF-8"
val ideaLanguageLevel = IdeaLanguageLevel(javaVersion);

plugins {
    id("idea")
    id("signing")
    alias(libs.plugins.publish)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.ksp)
}

idea {
    project {
        languageLevel = ideaLanguageLevel
        targetBytecodeVersion = javaVersion
    }
}

allprojects {
    group = projectGroup
    version = projectVersion
}

configure(dependencyProjects) {

    apply {
        plugin("java-platform")
    }
}

configure(javaProjects) {
    apply {
        plugin(catalogLibs.plugins.kotlin.jvm.get().pluginId)
        plugin(catalogLibs.plugins.kotlin.ksp.get().pluginId)
    }

    dependencies {
        catalogLibs.bundles.dependencies.get().forEach {
            implementation(platform(it))
        }
        implementation(catalogLibs.bundles.implementation)

        annotationProcessor(catalogLibs.bundles.annotation)
        ksp(catalogLibs.bundles.ksp)
        compileOnly(catalogLibs.bundles.compile)
        testImplementation(catalogLibs.bundles.test)
    }

    // 这样子Java代码直接卸载kotlin里面就可以被访问了
    sourceSets {
        main { java { srcDir("src/main/kotlin") } }
        test { java { srcDir("src/main/kotlin") } }
    }

    configure<KotlinJvmProjectExtension> {
        jvmToolchain(javaVersion.majorVersion.toInt())
        compilerOptions {
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    tasks.withType<Test> {
        enabled = gradle.startParameter.taskNames.contains("test")
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = encoding
        options.compilerArgs.add("-parameters")
        options.setIncremental(true)
    }

    tasks.withType<Javadoc> {
        isFailOnError = false
        options.encoding(encoding)
    }
}

configure(javaProjects + dependencyProjects) {

    apply {
        plugin(catalogLibs.plugins.publish.get().pluginId)
    }

    val isSnapshot = project.version.toString().lowercase().endsWith("snapshot")
    val host = (if (isSnapshot) properties["moppo.publish.snapshots"] else properties["moppo.publish.public"])?.toString()
    val username = properties["moppo.publish.username"]?.toString()
    val password = properties["moppo.publish.password"]?.toString()
    if (host.isNullOrBlank() || username.isNullOrBlank() || password.isNullOrBlank()) {
        return@configure
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                if (javaProjects.contains(project)) {
                    from(components["java"])

                    java {
                        withJavadocJar()
                        withSourcesJar()
                    }
                } else {
                    from(components["javaPlatform"])
                }

                pom {
                    name = project.name
                    description = project.description
                }
            }
        }

        repositories {
            maven {
                url = URI.create(host)
                isAllowInsecureProtocol = true
                credentials {
                    this@credentials.username = username
                    this@credentials.password = password
                }
            }
        }
    }
}
