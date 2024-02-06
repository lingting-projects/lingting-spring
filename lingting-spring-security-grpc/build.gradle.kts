dependencies {
    implementation(project(":lingting-spring-core"))

    api(project(":lingting-spring-security"))
    api(project(":lingting-spring-grpc"))
    api("live.lingting.framework:lingting-security-grpc")
}
