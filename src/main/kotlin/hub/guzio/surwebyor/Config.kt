package hub.guzio.surwebyor

import kotlinx.serialization.Serializable

@Serializable
data class Config(val port: Int, val defaultX: Int, val defaultZ: Int, val title: String, val biomes: Array<BiomeEntryRaw>){
    fun applyOntoSite(site: String): String = site.replace("\$PAGETITLE", title).replace("\$POSX", defaultX.toString()).replace("\$POSZ", defaultZ.toString())
}

@Serializable
data class BiomeEntryRaw(val namespace: String, val name: String, val colors: BiomeEntryProcessed)

@Serializable
data class BiomeEntryProcessed(val grass: RGB, val plants: RGB)