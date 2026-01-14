plugins {
    id("lingting_jvm")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-actuator-autoconfigure")
    api("org.springframework.boot:spring-boot-starter-actuator")

    compileOnly(project(":lingting-spring-grpc"))
    testCompileOnly(project(":lingting-spring-grpc"))

    compileOnly(project(":lingting-spring-web"))
    testCompileOnly(project(":lingting-spring-web"))
}
