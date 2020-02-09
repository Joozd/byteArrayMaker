/**
 *  toByteArray extensions (and back)
 *  Copyright (c) 2020 Joost Welle
 *  You may redistribute/use/copy/modify/etc under GNU AGPL license.
 *  http://www.gnu.org/licenses/agpl-3.0.html
 *
 *  Changes primitive types to arrays of bytes with the same values
 */

const val eightOnes = 255L

fun Double.toByteArray(): ByteArray = java.lang.Double.doubleToLongBits(this).toByteArray()
fun doubleFromBytes(bytes: ByteArray): Double = java.lang.Double.longBitsToDouble(longFromBytes(bytes))

fun Float.toByteArray(): ByteArray = java.lang.Float.floatToIntBits(this).toByteArray()
fun floatFromBytes(bytes: ByteArray): Float = java.lang.Float.intBitsToFloat(intFromBytes(bytes))

/**
 * returns an array of Long.SIZE_BYTES (==8) bytes, containing the same bits
 */
fun Long.toByteArray(): ByteArray{
    val bytes = ByteArray(Long.SIZE_BYTES)
    repeat(Long.SIZE_BYTES){pos ->
        bytes[pos] = this.shr(8*pos).and(eightOnes).toByte()
    }
    return bytes.reversedArray()
}

/**
 * returns a Long with the same bits as a Long.SIZE_BYTES byte ByteArray
 */
fun longFromBytes(bytes: ByteArray): Long{
    require(bytes.size == Long.SIZE_BYTES)
    var long = 0L

    // Least significant byte first, shift left by n bytes every time
    bytes.reversed().forEachIndexed {i, byte ->
        val comparator: Long = (if (byte >=0) byte.toLong() else 256+byte.toLong()).shl(8*i)
        long = long or comparator
    }
    return long
}

/**
 * returns an array of Int.SIZE_BYTES (==8) bytes, containing the same bits
 */
fun Int.toByteArray(): ByteArray{
    val bytes = ByteArray(Int.SIZE_BYTES)
    val eightOnes = 255
    repeat(Int.SIZE_BYTES){pos ->
        bytes[pos] = this.shr(8*pos).and(eightOnes).toByte()
    }
    return bytes.reversedArray()
}

/**
 * Makes an Int from an Int.SIZE_BYTES byte ByteArray
 */
fun intFromBytes(bytes: ByteArray): Int{
    require(bytes.size == Int.SIZE_BYTES)
    var int = 0

    // Least significant byte first, shift left by n bytes every time
    bytes.reversed().forEachIndexed {i, byte ->
        val comparator: Int = (if (byte >=0) byte.toInt() else 256+byte.toInt()).shl(8*i)
        int = int or comparator
    }
    return int
}

/**
 * returns an array of Char.SIZE_BYTES (==8) bytes, containing the same bits
 */
fun Char.toByteArray(): ByteArray{
    return this.toInt().toByteArray().takeLast(Char.SIZE_BYTES).toByteArray()
}

/**
 * Makes an Int from an Int.SIZE_BYTES byte ByteArray
 */
fun charFromBytes(bytes: ByteArray): Char{
    return intFromBytes(ByteArray(Int.SIZE_BYTES - Char.SIZE_BYTES) + bytes).toChar()
}
