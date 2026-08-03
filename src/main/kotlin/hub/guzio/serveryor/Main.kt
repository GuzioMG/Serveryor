package hub.guzio.serveryor

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import org.slf4j.LoggerFactory

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.jakarta.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.nayuki.png.ImageEncoder
import io.nayuki.png.chunk.Ihdr

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.*

import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import java.io.ByteArrayOutputStream
import java.util.Objects

object Main : ModInitializer {
	const val MOD_ID: String = "serveryor"
	val SITE = String(javaClass.classLoader.getResourceAsStream("assets/serveryor/index.html")?.readAllBytes() ?: "There must've been an error when loading Serveryor and the default index.html couldn't be extracted from its JAR. Please contact the server admin if you're seeing this error while viewing the map of some server, or (if this is singleplayer / you're the admin and are sure you didn't mess anything up) contact Serveryor devs on GitHub.".encodeToByteArray())

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

	fun getLevel(id: ResourceKey<Level>?): Level? {
		return if (Objects.isNull(id)) null
		else LEVELS[id]
	}

	fun getLevelKey(namespace: String?, id: String?): ResourceKey<Level>? {
		for (key in LEVELS.keys) {
			if (key.location().path.equals(id) && key.location().namespace.equals(namespace)) return key
		}
		return null
	}
}

fun Application.rootModule() {
	configureRouting()
}

fun Application.configureRouting() {
	routing {
		get("/") {
			call.respondText(Main.SITE, contentType = if (Main.SITE.startsWith("<!DOCTYPE html>")) ContentType.Text.Html else ContentType.Text.Plain)
		}
		get("/index.html") {
			call.respondText(Main.SITE, contentType = if (Main.SITE.startsWith("<!DOCTYPE html>")) ContentType.Text.Html else ContentType.Text.Plain)
		}
		get("/mapdata/{namespace}/{dimension}/{x}/{z}/{zoom}/tile.png") {
			val params = call.parameters
			val lvl = Main.getLevel(Main.getLevelKey(params["namespace"], params["dimension"]))
			if (Objects.isNull(lvl)) {
				call.respond(HttpStatusCode.NotFound)
				return@get
			}
			val x = Integer.parseInt(params["x"])
			val z = Integer.parseInt(params["z"])
			val zoom = Integer.parseInt(params["zoom"])

			val img = DataGetter.getImgOfChunk(lvl!!, ChunkPos(x, z), zoom)
			if (Objects.isNull(img)) {
				call.respond(HttpStatusCode.NotFound)
				return@get
			}
			val png = ImageEncoder.toPng(img!!, Ihdr.InterlaceMethod.NONE)
			val os = ByteArrayOutputStream()
			png.write(os)
			call.respondBytes(os.toByteArray(), contentType = ContentType.Image.PNG)
		}
	}
}