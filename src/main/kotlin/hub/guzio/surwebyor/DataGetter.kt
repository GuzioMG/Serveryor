package hub.guzio.surwebyor

import folk.sisby.surveyor.WorldSummary
import io.nayuki.png.image.BufferedGrayImage
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import java.util.Objects

object DataGetter {
    const val FIXED_POINT_PRECISION = 100

    fun getImgOfChunk(dim: Level, coords: ChunkPos, zoom: Int): BufferedGrayImage? {

        val map = WorldSummary.of(dim)
        val min = dim.minBuildHeight
        val max = dim.maxBuildHeight
        val depthmap = map.terrain()?.get(coords)?.toSingleLayer(min, max, max)?.depths
        if (Objects.isNull(depthmap)) return null

        val scalingFactor = 255*FIXED_POINT_PRECISION / (max-min)
        val img = BufferedGrayImage(16, 16, arrayOf(8, 0).toIntArray())

        for ((index, depth) in depthmap!!.withIndex()) {
            val yLevel = max - depth
            val yFromBottom = yLevel-min
            val yScaled = yFromBottom*scalingFactor/FIXED_POINT_PRECISION
            println("Found block at YLevel=$yLevel, which is $yFromBottom blocks up, or $yScaled when multiplied by $scalingFactor")
            val pixelData = yScaled.shl(16)
            img.setPixel(index/16, index%16, pixelData)
        }

        return img
    }
}