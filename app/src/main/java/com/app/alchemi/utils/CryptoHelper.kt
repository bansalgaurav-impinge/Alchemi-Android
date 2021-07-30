package com.app.alchemi.utils

import android.util.Base64
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.jvm.Throws

class CryptoHelper private constructor() {
    private var ivSpec: IvParameterSpec ?=null
    private var keySpec: SecretKeySpec?=null
    private var cipher: Cipher? = null

    init {
        ivSpec= IvParameterSpec(SecretKey.toByteArray())
        keySpec = SecretKeySpec(SecretKey.toByteArray(), "AES")
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }

    }

    @Throws(Exception::class)
    private fun encryptInternal(text: String?): ByteArray? {
        if (text == null || text.isEmpty()) {
            throw Exception("Empty string")
        }
        var encrypted: ByteArray? = null
        try {
            cipher?.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            encrypted = cipher!!.doFinal(text.toByteArray())
        } catch (e: Exception) {
            throw Exception("[encrypt] " + e.message)
        }

        return encrypted
    }

    @Throws(Exception::class)
    private fun decryptInternal(code: String?): ByteArray? {
        if (code == null || code.isEmpty()) {
            throw Exception("Empty string")
        }
        var decrypted: ByteArray? = null
        try {
            cipher?.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            decrypted = cipher?.doFinal(Base64.decode(code,Base64.DEFAULT))
        } catch (e: Exception) {
            throw Exception("[decrypt] " + e.message)
        }

        return decrypted
    }

    companion object {
        private const val SecretKey = Constants.AES_SECRET_KEY//16 char secret key

        @Throws(Exception::class)
        fun encrypt(valueToEncrypt: String): String {
            val enc = CryptoHelper()
            return Base64.encodeToString(enc.encryptInternal(valueToEncrypt), Base64.DEFAULT)
        }

        @Throws(Exception::class)
        fun decrypt(valueToDecrypt: String): String {
            val enc = CryptoHelper()
            return String(enc.decryptInternal(valueToDecrypt)!!)
        }
    }
}
