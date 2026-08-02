package hub.guzio.serveryor

import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import org.slf4j.LoggerFactory

object Serveryor : ModInitializer {
	const val MOD_ID: String = "serveryor"

	private val LOGGER = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!")
	}

	fun id(path: String): ResourceLocation
		= ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}
