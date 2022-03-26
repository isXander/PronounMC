dependencies {
    val fabricLoaderVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    implementation("com.github.llamalad7:mixinextras:0.0.+")
    annotationProcessor("com.github.llamalad7:mixinextras:0.0.+")
}

architectury {
    common()
}
