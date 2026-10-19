import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    java
}

val mc = stonecutter.current.version
val obfuscated = stonecutter.eval(mc, "<26")
apply(plugin = if (obfuscated) "net.fabricmc.fabric-loom-remap" else "net.fabricmc.fabric-loom")

fun prop(name: String) = property(name).toString()

version = "${prop("mod.version")}+$mc"
group = prop("mod.group")
base.archivesName = prop("mod.id")

repositories {
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com/releases")
}

val loom = the<LoomGradleExtensionAPI>()
val modImplementation = if (obfuscated) "modImplementation" else "implementation"
val modCompileOnly = if (obfuscated) "modCompileOnly" else "compileOnly"
val modRuntimeOnly = if (obfuscated) "modLocalRuntime" else "runtimeOnly"

dependencies {
    "minecraft"("com.mojang:minecraft:$mc")
    if (obfuscated) "mappings"(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric_api")}")
    modImplementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")
    modCompileOnly("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
    modRuntimeOnly("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
}

loom.runConfigs.all {
    runDir = "../../run"
    ideConfigGenerated(true)
}

java {
    val version = JavaVersion.toVersion(prop("java.version"))
    sourceCompatibility = version
    targetCompatibility = version
}

tasks.withType<JavaCompile>().configureEach {
    options.release = prop("java.version").toInt()
}

tasks.processResources {
    val props = mapOf(
        "version" to version,
        "mc" to prop("mc.dep"),
        "java" to prop("java.version"),
        "yacl" to prop("deps.yacl").substringBefore('+'),
    )
    inputs.properties(props)
    filesMatching("fabric.mod.json") { expand(props) }
}
