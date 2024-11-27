dependencies {
    api("live.lingting.framework:lingting-jackson")

    implementation(project(":lingting-spring-core"))

    compileOnly("org.springframework:spring-web")
    testCompileOnly("org.springframework:spring-web")
}
