plugins {
    id("lingting_jvm")
}

dependencies {
    api("live.lingting.framework:lingting-core")

    compileOnly("org.aspectj:aspectjweaver")
    testImplementation("org.aspectj:aspectjweaver")

    api("org.springframework:spring-context")
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-autoconfigure")
    api("org.springframework.boot:spring-boot-configuration-processor")
}
