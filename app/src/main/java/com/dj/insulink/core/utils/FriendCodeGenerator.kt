package com.dj.insulink.core.utils

import java.security.MessageDigest
import java.math.BigInteger

class DeterministicCodeGenerator {
    companion object {
        private const val CHAR_SET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        private const val CHAR_SET_SIZE = 32

        fun generateCodeFromEmail(email: String, length: Int = 6): String {
            val normalizedEmail = email.lowercase().trim()
            return generateUniqueCode(normalizedEmail, length)
        }

        private fun generateUniqueCode(userIdentifier: String, length: Int = 6): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(userIdentifier.toByteArray())

            val hashInt = BigInteger(1, hash)

            return convertToCharSet(hashInt, length)
        }

        private fun convertToCharSet(number: BigInteger, length: Int): String {
            var num = number
            val base = BigInteger.valueOf(CHAR_SET_SIZE.toLong())
            val result = StringBuilder()

            repeat(length) {
                val remainder = num.remainder(base).toInt()
                result.insert(0, CHAR_SET[remainder])
                num = num.divide(base)
            }

            return result.toString()
        }
    }
}