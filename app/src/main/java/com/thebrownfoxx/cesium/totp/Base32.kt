package com.thebrownfoxx.cesium.totp

import com.thebrownfoxx.models.totp.Base32
import org.apache.commons.codec.binary.Base32 as ApacheBase32

private val base32 = ApacheBase32()

fun String.toBase32() = Base32(base32.encodeToString(toByteArray()))

fun Base32.toPlainText() = String(base32.decode(value))