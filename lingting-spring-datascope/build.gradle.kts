plugins {
    id("lingting_jvm")
}

dependencies {
    api("live.lingting.framework:lingting-datascope")
    implementation(project(":lingting-spring-core"))
}
