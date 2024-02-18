pluginManagement {
    repositories {
        mavenLocal()

        maven(url = "https://mirrors.huaweicloud.com/repository/maven/")
        maven(url = "https://maven.aliyun.com/repository/public/")
        maven(url = "https://maven.aliyun.com/repository/spring")

        mavenCentral()
    }
}

dependencyResolutionManagement {
    val frameworkVersion = "2024.01.24-SNAPSHOT"

    versionCatalogs {
        create("libs") {
            library("frameworkDependencies", "live.lingting.framework", "lingting-dependencies").version(frameworkVersion)
        }
    }

}

rootProject.name = "lingting-spring"
include("lingting-spring-core")
include("lingting-spring-jackson")
include("lingting-spring-datascope")
include("lingting-spring-grpc")
include("lingting-spring-security")
include("lingting-spring-security-grpc")
include("lingting-spring-dependencies")
include("lingting-spring-actuator")
