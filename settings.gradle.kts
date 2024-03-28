dependencyResolutionManagement {
    val frameworkVersion = "2024.01.24-SNAPSHOT"
    val jSqlParserLowerVersion = "4.6"

    versionCatalogs {
        create("libs") {
            version("jSqlParserLower", jSqlParserLowerVersion)

            library("frameworkDependencies", "live.lingting.framework", "lingting-dependencies").version(frameworkVersion)
        }
    }

}

rootProject.name = "lingting-spring"
// 遍历rootDir, 获取符合条件的文件夹名称,  使用代码 include 所有文件夹
rootDir.listFiles()?.filter { it.isDirectory && it.name.startsWith("lingting-") }?.forEach {
    include(it.name)
}
