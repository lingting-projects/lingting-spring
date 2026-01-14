plugins {
    id("lingting_jvm")
}

dependencies {
    api("live.lingting.framework:lingting-jackson")
    api("live.lingting.framework:lingting-jackson-xml")

    implementation(project(":lingting-spring-core"))

    compileOnly("org.springframework:spring-web")
    testCompileOnly("org.springframework:spring-web")
}
