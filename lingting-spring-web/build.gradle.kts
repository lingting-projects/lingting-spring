dependencies {
    api("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    api("org.springframework.boot:spring-boot-starter-undertow")
    api("org.hibernate.validator:hibernate-validator")

    implementation(project(":lingting-spring-core"))
    implementation(project(":lingting-spring-jackson"))
}
