dependencies {
    api("live.lingting.framework:lingting-elasticsearch")

    implementation(project(":lingting-spring-core"))
    implementation(project(":lingting-spring-jackson"))

    compileOnly(project(":lingting-spring-datascope"))
    testImplementation(project(":lingting-spring-datascope"))
}
