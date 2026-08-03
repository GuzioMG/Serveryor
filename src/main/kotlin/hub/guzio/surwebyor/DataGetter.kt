package hub.guzio.surwebyor

import folk.sisby.surveyor.WorldSummary
import io.nayuki.png.PngImage
import io.nayuki.png.image.BufferedGrayImage
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.Objects

object DataGetter {
    fun getImgOfChunk(dim: Level, coords: ChunkPos, zoom: Int): BufferedGrayImage? {
        val map = WorldSummary.of(dim)

        val depthmap = map.terrain()?.get(coords)?.toSingleLayer(dim.minBuildHeight, dim.maxBuildHeight, dim.height)?.depths
        if (Objects.isNull(depthmap)) return null
        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE

        for (depth in depthmap!!) {
            min = min.coerceAtMost(depth)
            max = max.coerceAtLeast(depth)
        }

        val scaledMax = max-min
        val scalingFactor = 255 / scaledMax
        val img = BufferedGrayImage(16, 16, arrayOf(8, 0).toIntArray())

        for ((index, depth) in depthmap.withIndex()) {
            val depthScaled = (depth-min)*scalingFactor
            val pixelData = depthScaled.shl(16)
            img.setPixel(index/16, index%16, pixelData)
        }

        return img
    }
}