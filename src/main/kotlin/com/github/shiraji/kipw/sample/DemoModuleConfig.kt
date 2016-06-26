@file:JvmName("DemoModuleConfig")

package com.github.shiraji.kipw.sample

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project

object DemoModuleConfig {
    val PLUGIN_ID_KEY = "com.github.shiraji.foo.pluginid"
    val PLUGIN_NAME_KEY = "com.github.shiraji.foo.pluginname"
    val PLUGIN_VERSION_KEY = "com.github.shiraji.foo.pluginversion"
    val VENDOR_EMAIL = "com.github.shiraji.foo.vendoremail"
    val VENDOR_URL = "com.github.shiraji.foo.vendorurl"
    val VENDOR_NAME = "com.github.shiraji.foo.vendorname"
    val GRADLE_PLUGIN_VERSION = "com.github.shiraji.foo.gradlepluginversion"
    val INTELLIJ_VERSION = "com.github.shiraji.foo.intellijversion"

    @JvmStatic fun setPluginId(project: Project, value: String) = PropertiesComponent.getInstance(project).setValue(PLUGIN_ID_KEY, value)
    @JvmStatic fun setPluginName(project: Project, value: String) = PropertiesComponent.getInstance(project).setValue(PLUGIN_NAME_KEY, value)
    @JvmStatic fun setPluginVersion(project: Project, value: String) = PropertiesComponent.getInstance(project).setValue(PLUGIN_VERSION_KEY, value)
    @JvmStatic fun setVendorEmail(project: Project, value: String) = PropertiesComponent.getInstance(project).setValue(VENDOR_EMAIL, value)
    @JvmStatic fun setVendorUrl(project: Project, value: String) = PropertiesComponent.getInstance(project).setValue(VENDOR_URL, value)
    @JvmStatic fun setVendorName(project: Project, value: String) = PropertiesComponent.getInstance(project).setValue(VENDOR_NAME, value)
    @JvmStatic fun setGradlePluginVersion(project: Project, value: String) = PropertiesComponent.getInstance(project).setValue(GRADLE_PLUGIN_VERSION, value)
    @JvmStatic fun setIntellijVersion(project: Project, value: String) = PropertiesComponent.getInstance(project).setValue(INTELLIJ_VERSION, value)

    @JvmStatic fun getPluginId(project: Project) = PropertiesComponent.getInstance(project).getValue(PLUGIN_ID_KEY)
    @JvmStatic fun getPluginName(project: Project) = PropertiesComponent.getInstance(project).getValue(PLUGIN_NAME_KEY)
    @JvmStatic fun getPluginVersion(project: Project) = PropertiesComponent.getInstance(project).getValue(PLUGIN_VERSION_KEY)
    @JvmStatic fun getVendorEmail(project: Project) = PropertiesComponent.getInstance(project).getValue(VENDOR_EMAIL)
    @JvmStatic fun getVendorUrl(project: Project) = PropertiesComponent.getInstance(project).getValue(VENDOR_URL)
    @JvmStatic fun getVendorName(project: Project) = PropertiesComponent.getInstance(project).getValue(VENDOR_NAME)
    @JvmStatic fun getGradlePluginVersion(project: Project) = PropertiesComponent.getInstance(project).getValue(GRADLE_PLUGIN_VERSION)
    @JvmStatic fun getIntellijVersion(project: Project) = PropertiesComponent.getInstance(project).getValue(INTELLIJ_VERSION)
}