plugins {
    id("lingting_jvm")
}

dependencies {
    api(project(":lingting-spring-security"))
    api(project(":lingting-spring-web"))

    implementation(project(":lingting-spring-core"))
    implementation("live.lingting.framework:lingting-http")

    testImplementation(project(":lingting-spring-jackson"))

}
