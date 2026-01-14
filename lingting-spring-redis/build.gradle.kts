plugins {
    id("lingting_jvm")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-redis")

    implementation(project(":lingting-spring-core"))
    implementation(project(":lingting-spring-jackson"))
    implementation("org.aspectj:aspectjweaver")
}
