plugins {
    id("lingting_jvm")
}

dependencies {
    api("live.lingting.framework:lingting-huawei")

    implementation(project(":lingting-spring-core"))
}
