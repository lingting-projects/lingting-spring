dependencies {
    api("live.lingting.framework:lingting-datascope-jsql") {
        exclude("com.github.jsqlparser", "jsqlparser")
    }

    implementation(project(":lingting-spring-core"))

    compileOnly("com.github.jsqlparser", "jsqlparser", libs.versions.jSqlParserLower.get())
}
