package com.rcl.nextshiki

import androidx.compose.ui.awt.ComposeWindow
import sun.misc.Unsafe
import java.awt.Shape
import java.awt.Window
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Method

// thanks to GitHub amir1376/compose-custom-window-frame
object CustomWindowDecorationAccessing {
    init {
        UnsafeAccessing.assignAccessibility(
            UnsafeAccessing.desktopModule,
            listOf(
                "java.awt"
            )
        )
    }

    private val customWindowDecorationInstance: Any? = try {
        val customWindowDecoration = Class.forName("java.awt.Window\$CustomWindowDecoration")
        val constructor = customWindowDecoration.declaredConstructors.first()
        constructor.isAccessible = true
        constructor.newInstance()
    } catch (e: Exception) {
        null
    }

    private val setCustomDecorationEnabledMethod: Method? =
        getMethod("setCustomDecorationEnabled", Window::class.java, Boolean::class.java)

    private val setCustomDecorationTitleBarHeightMethod: Method? =
        getMethod("setCustomDecorationTitleBarHeight", Window::class.java, Int::class.java)

    private val setCustomDecorationHitTestSpotsMethod: Method? =
        getMethod("setCustomDecorationHitTestSpots", Window::class.java, MutableList::class.java)

    private fun getMethod(name: String, vararg params: Class<*>): Method? {
        return try {
            val clazz = Class.forName("java.awt.Window\$CustomWindowDecoration")
            val method = clazz.getDeclaredMethod(
                name, *params
            )
            method.isAccessible = true
            method
        } catch (e: Exception) {
            null
        }
    }

    val isSupported = customWindowDecorationInstance != null && setCustomDecorationEnabledMethod != null

    internal fun setCustomDecorationEnabled(window: ComposeWindow, enabled: Boolean) {
        val instance = customWindowDecorationInstance ?: return
        val method = setCustomDecorationEnabledMethod ?: return
        method.invoke(instance, window, enabled)
    }

    internal fun setCustomDecorationTitleBarHeight(window: ComposeWindow, height: Int) {
        val instance = customWindowDecorationInstance ?: return
        val method = setCustomDecorationTitleBarHeightMethod ?: return
        method.invoke(instance, window, height)
    }

    internal fun setCustomDecorationHitTestSpotsMethod(window: ComposeWindow, spots: Map<Shape, Int>) {
        val instance = customWindowDecorationInstance ?: return
        val method = setCustomDecorationHitTestSpotsMethod ?: return
        method.invoke(instance, window, spots.entries.toMutableList())
    }
}

internal object UnsafeAccessing {
    private val unsafe: Any? by lazy {
        try {
            val theUnsafe = Unsafe::class.java.getDeclaredField("theUnsafe")
            theUnsafe.isAccessible = true
            theUnsafe.get(null) as Unsafe
        } catch (e: Throwable) {
            null
        }
    }

    val desktopModule by lazy {
        ModuleLayer.boot().findModule("java.desktop").get()
    }

    private val ownerModule by lazy {
        (this.javaClass as Class<*>).module
    }

    private val isAccessibleFieldOffset: Long? by lazy {
        try {
            (unsafe as? Unsafe)?.objectFieldOffset(Parent::class.java.getDeclaredField("first"))
        } catch (e: Throwable) {
            null
        }
    }

    private val implAddOpens by lazy {
        try {
            Module::class.java.getDeclaredMethod(
                "implAddOpens", String::class.java, Module::class.java
            ).accessible()
        } catch (e: Throwable) {
            null
        }
    }

    fun assignAccessibility(obj: AccessibleObject) {
        try {
            val theUnsafe = unsafe as? Unsafe ?: return
            val offset = isAccessibleFieldOffset ?: return
            theUnsafe.putBooleanVolatile(obj, offset, true)
        } catch (e: Throwable) {
            // ignore
        }
    }

    fun assignAccessibility(module: Module, packages: List<String>) {
        try {
            packages.forEach {
                implAddOpens?.invoke(module, it, ownerModule)
            }
        } catch (e: Throwable) {
            // ignore
        }
    }

    private class Parent {
        var first = false

        @Volatile
        var second: Any? = null
    }
}

internal fun <T : AccessibleObject> T.accessible(): T {
    return apply {
        UnsafeAccessing.assignAccessibility(this)
    }
}