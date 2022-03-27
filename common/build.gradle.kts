dependencies {
    val fabricLoaderVersion: String by rootProject
    val clothVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("me.shedaniel.cloth:cloth-config:$clothVersion")
}

architectury {
    common()
}
