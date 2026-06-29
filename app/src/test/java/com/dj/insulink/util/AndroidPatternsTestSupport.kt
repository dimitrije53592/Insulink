package com.dj.insulink.util

import android.util.Patterns
import java.lang.reflect.Field
import java.util.regex.Pattern

/**
 * `android.util.Patterns.EMAIL_ADDRESS` is a `null` static field in the unit-test `android.jar`
 * stub, and being a field (not a method) it cannot be stubbed by MockK. We install a real
 * [Pattern] into it via `sun.misc.Unsafe` (accessed purely reflectively so the compiler need not
 * resolve `sun.misc`), which works regardless of the field being `final` and needs no network
 * access. Call [installEmailPattern] from a test `@Before`.
 */
object AndroidPatternsTestSupport {

    // Mirrors the spirit of Android's email pattern closely enough for the inputs used in tests.
    private val EMAIL_REGEX: Pattern = Pattern.compile(
        "[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}"
    )

    fun installEmailPattern() {
        val unsafeClass = Class.forName("sun.misc.Unsafe")
        val theUnsafe = unsafeClass.getDeclaredField("theUnsafe")
            .apply { isAccessible = true }
            .get(null)

        val field: Field = Patterns::class.java.getField("EMAIL_ADDRESS")
        val base = unsafeClass.getMethod("staticFieldBase", Field::class.java)
            .invoke(theUnsafe, field)
        val offset = unsafeClass.getMethod("staticFieldOffset", Field::class.java)
            .invoke(theUnsafe, field) as Long
        unsafeClass.getMethod("putObject", Any::class.java, Long::class.javaPrimitiveType, Any::class.java)
            .invoke(theUnsafe, base, offset, EMAIL_REGEX)
    }
}
