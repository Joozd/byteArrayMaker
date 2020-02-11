/**
 *  toByteArray extensions (and back)
 *  Changes primitive types to arrays of bytes with the same values
 *  version 2
 *  Copyright (c) 2020 Joost Welle
 *  You may redistribute/use/copy/modify/etc in whatever way you see fit.
 *  This file is "as is".
 *  It is not intended for any ose other than my own, and therefore I will not accept any liability in case
 *  you decide to use it and it turns out to break all your data.
 *  However, if it does, and you find out why, feel free to let me know so we can fix it.
 *
 *  usage: <Type>.toByteArray() will give a ByteArray with the same amount of bytes as the original type,
 *  containing the same bits as the original.
 *  the reverse, <Type>FromBytes(bytes:ByteArray) will reconstruct a basic type from bytes.
 *
 *  You could use it to stitch the bits of 2 Ints into one Long, or whatever
 *  You could also use it to restructure data from a long stream of unmarked Bytes, or change structured data into such a stream.
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
    require(bytes.size == Char.SIZE_BYTES)
    return intFromBytes(ByteArray(Int.SIZE_BYTES - Char.SIZE_BYTES) + bytes).toChar()
}

/**
 * returns an array of Int.SIZE_BYTES (==8) bytes, containing the same bits
 */
fun Short.toByteArray(): ByteArray{
    return this.toInt().toByteArray().takeLast(Short.SIZE_BYTES).toByteArray()
}
/**
 * Makes an Int from an Int.SIZE_BYTES byte ByteArray
 */
fun shortFromBytes(bytes: ByteArray): Short{
    require(bytes.size == Short.SIZE_BYTES)
    return intFromBytes(ByteArray(Int.SIZE_BYTES - Short.SIZE_BYTES) + bytes).toShort()
}

fun <T, R>  Pair<T, R>.toByteArray(): ByteArray{
    val first = when (this.first){
        is Long     -> (this.first as Long).toByteArray()
        is Int      -> (this.first as Int).toByteArray()
        is Char     -> (this.first as Char).toByteArray()
        is Double   -> (this.first as Double).toByteArray()
        is Float    -> (this.first as Float).toByteArray()
        else        -> error("Unsupported type for ${this.first}")
    }
    val second = when (this.second){
        is Long     -> (this.second as Long).toByteArray()
        is Int      -> (this.second as Int).toByteArray()
        is Char     -> (this.second as Char).toByteArray()
        is Double   -> (this.second as Double).toByteArray()
        is Float    -> (this.second as Float).toByteArray()
        else        -> error("Unsupported type for ${this.second}")
    }
    return first + second
}

//probably not gonna make this for every possibility
fun pairLongLongFromBytes(bytes:ByteArray): Pair<Long, Long>{
    require(bytes.size == Long.SIZE_BYTES + Long.SIZE_BYTES)
    return longFromBytes(bytes.slice(0 until Long.SIZE_BYTES).toByteArray()) to longFromBytes(bytes.slice(Long.SIZE_BYTES until bytes.size).toByteArray())
}

