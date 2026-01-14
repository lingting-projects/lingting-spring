plugins {
    id("lingting_jvm")
}

dependencies {
    api("live.lingting.framework:lingting-aws")

    implementation(project(":lingting-spring-core"))
}
