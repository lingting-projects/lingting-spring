dependencies {
    implementation(project(":lingting-spring-core"))
    implementation(project(":lingting-spring-jackson"))
    implementation("org.aspectj:aspectjweaver")

    api(libs.redisson)
}
