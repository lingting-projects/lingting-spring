dependencies {
    api("live.lingting.framework:lingting-mybatis")
    api("com.baomidou:mybatis-plus-spring-boot3-starter") {
        exclude("org.springframework.boot")
        exclude("org.springframework")
    }
    api("org.springframework.boot:spring-boot-starter-jdbc")

    implementation(project(":lingting-spring-core"))
    implementation(project(":lingting-spring-jackson"))

    compileOnly(project(":lingting-spring-datascope"))
    testImplementation(project(":lingting-spring-datascope"))
    testImplementation("com.mysql:mysql-connector-j")
}
