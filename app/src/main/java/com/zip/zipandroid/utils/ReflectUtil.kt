@file:Suppress("UNCHECKED_CAST", "unused")

package com.zip.zipandroid.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

object ReflectUtil {


    @JvmStatic
    fun <VB : ViewBinding> inflateWithGeneric(
        genericOwner: Any,
        layoutInflater: LayoutInflater, index: Int
    ): VB? =
        withGenericBindingClass<VB>(genericOwner, index) { clazz ->
            clazz?.getMethod("inflate", LayoutInflater::class.java)
                ?.invoke(null, layoutInflater) as VB
        }?.withLifecycleOwner(genericOwner)

    @JvmStatic
    fun <VB : ViewBinding> inflateWithGeneric(
        genericOwner: Any,
        parent: ViewGroup,
        index: Int
    ): VB? =
        inflateWithGeneric(genericOwner, LayoutInflater.from(parent.context), parent, false, index)

    @JvmStatic
    fun <VB : ViewBinding> inflateWithGeneric(
        genericOwner: Any,
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean, index: Int
    ): VB? =
        withGenericBindingClass<VB>(genericOwner, index) { clazz ->
            clazz?.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
                ?.invoke(null, layoutInflater, parent, attachToParent) as VB
        }?.withLifecycleOwner(genericOwner)


    private fun <VB : ViewBinding> VB.withLifecycleOwner(genericOwner: Any) = apply {
        if (this is ViewDataBinding && genericOwner is ComponentActivity) {
            lifecycleOwner = genericOwner
        } else if (this is ViewDataBinding && genericOwner is Fragment) {
            lifecycleOwner = genericOwner.viewLifecycleOwner
        }
    }

    private fun <T> withGenericBindingClass(
        genericOwner: Any, index: Int,
        block: (Class<T>?) -> T?
    ): T? {
        var genericSuperclass = genericOwner.javaClass.genericSuperclass
        var superclass = genericOwner.javaClass.superclass
        while (superclass != null) {
            if (genericSuperclass is ParameterizedType) {
                val type = genericSuperclass.actualTypeArguments[index]
                try {
                    return block.invoke(type as Class<T>?)
                } catch (e: NoSuchMethodException) {
                } catch (e: ClassCastException) {
                } catch (e: InvocationTargetException) {
                    var tagException: Throwable? = e
                    while (tagException is InvocationTargetException) {
                        tagException = e.cause
                    }
                    Log.e("ReflectUtil", "Presenter generic was found, but creation failed.")
                }
            }
            genericSuperclass = superclass.genericSuperclass
            superclass = superclass.superclass
        }
        Log.e("ReflectUtil", "There is no generic of Presenter.")
        return block.invoke(null)
    }
}
