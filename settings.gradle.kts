pluginManagement {
    repositories {
        mavenLocal()

        maven(url = "https://mirrors.huaweicloud.com/repository/maven/")
        maven(url = "https://maven.aliyun.com/repository/public/")
        maven(url = "https://maven.aliyun.com/repository/spring")

        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    val frameworkVersion = "2024.01.24-SNAPSHOT"

    val mapstructVersion = "1.5.3.Final"
    val lombokVersion = "1.18.30"
    val lombokMapstructBindingVersion = "0.2.0"
    val formatterVersion = "0.0.41"

    versionCatalogs {
        create("libs") {
            version("formatterVersion", formatterVersion)
            library("springFormatter", "io.spring.javaformat", "spring-javaformat-checkstyle").version(formatterVersion)

            library("frameworkDependencies", "live.lingting.framework", "lingting-dependencies").version(frameworkVersion)

            library("mapstruct", "org.mapstruct", "mapstruct").version(mapstructVersion)
            library("lombok", "org.projectlombok", "lombok").version(lombokVersion)

            library("mapstructProcessor", "org.mapstruct", "mapstruct-processor").version(mapstructVersion)
            library("lombokMapstruct", "org.projectlombok", "lombok-mapstruct-binding").version(lombokMapstructBindingVersion)

            bundle("implementation", listOf("mapstruct"));
            bundle("compile", listOf("lombok"))
            bundle("annotation", listOf("mapstructProcessor", "lombok", "lombokMapstruct"))

            plugin("springFormat", "io.spring.javaformat").version(formatterVersion)
        }
    }

}


rootProject.name = "lingting-spring"
include("lingting-spring-core")
include("lingting-spring-jackson")
