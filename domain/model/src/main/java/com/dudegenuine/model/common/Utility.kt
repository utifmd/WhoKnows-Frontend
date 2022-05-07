package com.dudegenuine.model.common

import android.os.Build
import android.util.Base64
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Sat, 12 Feb 2022
 * WhoKnows by utifmd
 **/
object Utility {
    val isOreoCompatibility: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    fun <T> concatenate(vararg lists: List<T>): List<T> {
        return listOf(*lists).flatten()
    }

    private const val ALGORITHM = "AES"
    private const val KEY = "1Hbfh667adfDEJ78"

    @Throws(Exception::class)
    fun encrypt(value: String): String {
        val key: Key = generateKey()
        val cipher: Cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedByteValue: ByteArray =
            cipher.doFinal(value.toByteArray(charset("utf-8"))) //cipher.doFinal(value.toByteArray(charset("utf-8")))
        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun decrypt(value: String?): String {
        val key: Key = generateKey()
        val cipher: Cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decryptedValue64: ByteArray = Base64.decode(value, Base64.DEFAULT)
        val decryptedByteValue: ByteArray = cipher.doFinal(decryptedValue64)
        return String(decryptedByteValue, Charsets.UTF_8)
    }

    @Throws(Exception::class)
    private fun generateKey(): Key {
        return SecretKeySpec(KEY.toByteArray(), ALGORITHM)
    }
}