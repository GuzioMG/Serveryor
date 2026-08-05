package hub.guzio.surwebyor


const val BITMASK_MC_RED: Int = 16711680
const val BITMASK_MC_GREEN: Int = 65280
const val BITMASK_MC_BLUE: Int = 255
const val BITMASK_MC_RELEVANT: Int = BITMASK_MC_RED.or(BITMASK_MC_GREEN).or(BITMASK_MC_BLUE)

const val BITMASK_PNG_RED: Long = 71776119061217280
const val BITMASK_PNG_GREEN: Long = 1095216660480
const val BITMASK_PNG_BLUE: Long = 16711680
const val BITMASK_PNG_RELEVANT: Long = BITMASK_PNG_RED.or(BITMASK_PNG_GREEN).or(BITMASK_PNG_BLUE)

data class RGB(val r: UByte, val g: UByte, val b: UByte) {
    companion object

    fun tint(by: UByte): RGB{
        val scale = by.toDouble() / 255.0
        return RGB((r.toDouble()*scale).toInt().toUByte(), (g.toDouble()*scale).toInt().toUByte(), (b.toDouble()*scale).toInt().toUByte())
    }

    fun toPng(): Long{
        return r.toLong().shl(48).or(g.toLong().shl(32)).or(b.toLong().shl(16))
    }

    override fun toString(): String {
        return "RGB($r,$g,$b)"
    }
}

fun RGB.Companion.fromMc(mcColor: Int): RGB {
    val mcColorRGB = mcColor.and(BITMASK_MC_RELEVANT)
    val r = BITMASK_MC_RED.and(mcColorRGB)
    val g = BITMASK_MC_GREEN.and(mcColorRGB)
    val b = BITMASK_MC_BLUE.and(mcColorRGB)
    val rB = r.shr(16).toUByte()
    val gB = g.shr(8).toUByte()
    val bB = b.toUByte()
    return RGB(rB, gB, bB)
}

fun RGB.Companion.fromPng(pngColor: Long): RGB {
    val pngColorRelevant = pngColor.and(BITMASK_PNG_RELEVANT)
    val r = BITMASK_PNG_RED.and(pngColorRelevant)
    val g = BITMASK_PNG_GREEN.and(pngColorRelevant)
    val b = BITMASK_PNG_BLUE.and(pngColorRelevant)
    val rB = r.shr(48).toUByte()
    val gB = g.shr(32).toUByte()
    val bB = b.shr(16).toUByte()
    return RGB(rB, gB, bB)
}

fun RGB.Companion.fromAvg(of: Array<RGB>): RGB {
    var r = 0u
    var g = 0u
    var b = 0u
    for ((red, green, blue) in of){
        r+=red
        g+=green
        b+=blue
    }
    return RGB((r/of.size.toUInt()).toUByte(), (g/of.size.toUInt()).toUByte(), (b/of.size.toUInt()).toUByte())
}

fun RGB.Companion.fromWhite(): RGB {
    return RGB(255.toUByte(), 255.toUByte(), 255.toUByte())
}