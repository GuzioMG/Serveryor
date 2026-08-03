package hub.guzio.surwebyor

import folk.sisby.surveyor.WorldSummary
import io.nayuki.png.image.BufferedRgbaImage
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import java.util.Objects

object DataGetter {
    const val FIXED_POINT_PRECISION = 100

    fun getImgOfChunk(dim: Level, coords: ChunkPos, zoom: Int): BufferedRgbaImage? {
        if (zoom > 4) return null
        if (zoom < 4){
            val topLeftCorner = getImgOfChunk(dim, ChunkPos(2*coords.x, 2*coords.z), zoom+1)
            val topRightCorner = getImgOfChunk(dim, ChunkPos(2*coords.x+1, 2*coords.z), zoom+1)
            val bottomLeftCorner = getImgOfChunk(dim, ChunkPos(2*coords.x, 2*coords.z+1), zoom+1)
            val bottomRightCorner = getImgOfChunk(dim, ChunkPos(2*coords.x+1, 2*coords.z+1), zoom+1)

            val img = squishImgOntoImg(topLeftCorner, BufferedRgbaImage(16, 16, arrayOf(8, 8, 8, 0).toIntArray()), 0, 0)
            squishImgOntoImg(topRightCorner, img, 8, 0)
            squishImgOntoImg(bottomLeftCorner, img, 0, 8)
            return squishImgOntoImg(bottomRightCorner, img, 8, 8);
        }

        val map = WorldSummary.of(dim)
        val min = dim.minBuildHeight
        val max = dim.maxBuildHeight
        val terrainMap = map.terrain()?.get(coords)?.toSingleLayer(min, max, max)
        if (Objects.isNull(terrainMap)) return null
        val depthMap = terrainMap!!.depths
        val existenceMap = terrainMap.exists

        val scalingFactor = 255*FIXED_POINT_PRECISION / (max-min)
        val img = BufferedRgbaImage(16, 16, arrayOf(8, 8, 8, 0).toIntArray())

        for ((index, depth) in depthMap!!.withIndex()) {
            if (!existenceMap[index]) continue
            val yLevel = max - depth
            val yFromBottom = yLevel-min
            val yScaled = yFromBottom*scalingFactor/FIXED_POINT_PRECISION
            img.setPixel(index/16, index%16, yScaled.shl(16).toLong())
        }

        return img
    }

    fun squishImgOntoImg(source: BufferedRgbaImage?, target: BufferedRgbaImage, xShit: Int, yShift: Int): BufferedRgbaImage {
        var x = 0
        while (x<8) {
            if (Objects.isNull(source)) break
            var y = 0
            while (y<8) {
                val topLeftPixel = source!!.getPixel(x*2, y*2).shr(16)
                val topRightPixel = source.getPixel(x*2+1, y*2).shr(16)
                val bottomLeftPixel = source.getPixel(x*2, y*2+1).shr(16)
                val bottomRightPixel = source.getPixel(x*2+1, y*2+1).shr(16)
                val avg = (topLeftPixel+topRightPixel+bottomLeftPixel+bottomRightPixel)/4
                target.setPixel(x+xShit, y+yShift, avg.shl(16))
                y++
            }
            x++
        }
        return target
    }
}