dependencies {
    api("org.springframework:spring-web")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-undertow") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    api("org.hibernate.validator:hibernate-validator")

    implementation(project(":lingting-spring-core"))
    implementation(project(":lingting-spring-jackson"))
}
