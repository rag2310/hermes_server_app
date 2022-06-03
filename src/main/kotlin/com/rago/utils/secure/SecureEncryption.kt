package com.rago.utils.secure

interface SecureEncryption {
    fun encryptString(plaintext: String): String
    fun decryptString(base64CiphertextAndNonceAndSalt: String): String
}