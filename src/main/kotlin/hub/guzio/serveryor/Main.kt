package hub.guzio.serveryor

import folk.sisby.surveyor.WorldSummary
import org.slf4j.LoggerFactory

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.jakarta.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level

object Main : ModInitializer {
	const val MOD_ID: String = "serveryor"

	private val LOGGER = LoggerFactory.getLogger(MOD_ID)
	private val LEVELS = HashMap<ResourceKey<Level>, Level>()
	private var SERVER: EmbeddedServer<JettyApplicationEngine, JettyApplicationEngineBase.Configuration>? = null

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

		ServerLifecycleEvents.SERVER_STARTED.register { _ ->
			LOGGER.info("Booting up the (web)server(yor)...")
			SERVER?.stop() //Just in case it was running for any reason...
			SERVER = embeddedServer(
				factory = Jetty,
				port = 8080,
				host = "0.0.0.0",
				module = Application::rootModule
			).start()
			LOGGER.info("Serveryor is LIVE!")
		}

		ServerLifecycleEvents.SERVER_STOPPING.register { _ ->
			LOGGER.info("Terminating Serveryor session...")
			SERVER?.stop()
			SERVER = null
			LOGGER.info("Serveryor is DONE with you! >:(   (jk, it's still friendly - the current session isn't tho)")
		}

		LOGGER.info("Serveryor is done starting.")
	}
}

fun Application.rootModule() {
	configureRouting()
}

fun Application.configureRouting() {
	routing {
		get("/") {
			call.respondText("Hello, World!")
		}
	}
}