@file:JvmName("DemoModuleConfig")

package com.github.shiraji.ipgwizard

import com.intellij.ide.util.PropertiesComponent

object DemoModuleConfig {
    val PLUGIN_ID_KEY = "com.github.shiraji.foo.pluginid"
    val PLUGIN_NAME_KEY = "com.github.shiraji.foo.pluginname"
    val PLUGIN_VERSION_KEY = "com.github.shiraji.foo.pluginversion"
    val VENDOR_EMAIL = "com.github.shiraji.foo.vendoremail"
    val VENDOR_URL = "com.github.shiraji.foo.vendorurl"
    val VENDOR_NAME = "com.github.shiraji.foo.vendorname"
    val GRADLE_PLUGIN_VERSION = "com.github.shiraji.foo.gradlepluginversion"
    val INTELLIJ_VERSION = "com.github.shiraji.foo.intellijversion"

    @JvmStatic fun setPluginId(value: String) = PropertiesComponent.getInstance().setValue(PLUGIN_ID_KEY, value)
    @JvmStatic fun setPluginName(value: String) = PropertiesComponent.getInstance().setValue(PLUGIN_NAME_KEY, value)
    @JvmStatic fun setPluginVersion(value: String) = PropertiesComponent.getInstance().setValue(PLUGIN_VERSION_KEY, value)
    @JvmStatic fun setVendorEmail(value: String) = PropertiesComponent.getInstance().setValue(VENDOR_EMAIL, value)
    @JvmStatic fun setVendorUrl(value: String) = PropertiesComponent.getInstance().setValue(VENDOR_URL, value)
    @JvmStatic fun setVendorName(value: String) = PropertiesComponent.getInstance().setValue(VENDOR_NAME, value)
    @JvmStatic fun setGradlePluginVersion(value: String) = PropertiesComponent.getInstance().setValue(GRADLE_PLUGIN_VERSION, value)
    @JvmStatic fun setIntellijVersion(value: String) = PropertiesComponent.getInstance().setValue(INTELLIJ_VERSION, value)

    @JvmStatic fun getPluginId() = PropertiesComponent.getInstance().getValue(PLUGIN_ID_KEY)
    @JvmStatic fun getPluginName() = PropertiesComponent.getInstance().getValue(PLUGIN_NAME_KEY)
    @JvmStatic fun getPluginVersion() = PropertiesComponent.getInstance().getValue(PLUGIN_VERSION_KEY)
    @JvmStatic fun getVendorEmail() = PropertiesComponent.getInstance().getValue(VENDOR_EMAIL)
    @JvmStatic fun getVendorUrl() = PropertiesComponent.getInstance().getValue(VENDOR_URL)
    @JvmStatic fun getVendorName() = PropertiesComponent.getInstance().getValue(VENDOR_NAME)
    @JvmStatic fun getGradlePluginVersion() = PropertiesComponent.getInstance().getValue(GRADLE_PLUGIN_VERSION)
    @JvmStatic fun getIntellijVersion() = PropertiesComponent.getInstance().getValue(INTELLIJ_VERSION)
}