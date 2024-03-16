dependencies {
    api("live.lingting.framework:lingting-mybatis") {
        exclude("com.github.jsqlparser", "jsqlparser")
    }
    api(project(":lingting-spring-datascope-jsql")) {
        exclude("com.github.jsqlparser", "jsqlparser")
    }
    api("com.github.jsqlparser", "jsqlparser", libs.versions.jSqlParserLower.get())
    api("com.baomidou:mybatis-plus-spring-boot3-starter")

    implementation(project(":lingting-spring-core"))
    implementation(project(":lingting-spring-jackson"))

    testImplementation("com.mysql:mysql-connector-j")
}
