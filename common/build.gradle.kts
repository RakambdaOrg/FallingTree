dependencies {
    testImplementation(libs.bundles.junit)
    implementation(libs.log4jApi)
    implementation(libs.lwjglGlfw)
    implementation(libs.guava)
    implementation(libs.commonsLang3)
    implementation(libs.gson)
    implementation(libs.brigadier)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
