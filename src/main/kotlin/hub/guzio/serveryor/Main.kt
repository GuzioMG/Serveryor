package hub.guzio.serveryor

import folk.sisby.surveyor.WorldSummary
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import org.slf4j.LoggerFactory

object Main : ModInitializer {
	const val MOD_ID: String = "serveryor"

	private val LOGGER = LoggerFactory.getLogger(MOD_ID)
	private val LEVELS = HashMap<ResourceKey<Level>, Level>()

	override fun onInitialize() {
		LOGGER.info("Serveryor is registering events....")
		ServerWorldEvents.LOAD.register { _, level ->
			LOGGER.info("Serveryor detected a new: "+level.dimension())
			LEVELS[level.dimension()] = level
		}
		ServerWorldEvents.UNLOAD.register { _, level ->
			LOGGER.info("Serveryor is unloading: "+level.dimension())
			LEVELS.remove(level.dimension())
		}

		LOGGER.info("Booting up the (web)server(yor)....")
		//TODO

		LOGGER.info("Serveryor is done!")
	}

	fun id(path: String): ResourceLocation
		= ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}