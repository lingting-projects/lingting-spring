plugins {
    id("lingting_jvm")
}

dependencies {
    api("live.lingting.framework:lingting-security")

    implementation(project(":lingting-spring-core"))
    implementation("org.springframework.security:spring-security-crypto")
}
