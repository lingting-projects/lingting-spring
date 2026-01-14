plugins {
    id("lingting_jvm")
}

dependencies {
    api(project(":lingting-spring-security"))
    api(project(":lingting-spring-grpc"))
    api("live.lingting.framework:lingting-security-grpc")

    implementation(project(":lingting-spring-core"))
}
