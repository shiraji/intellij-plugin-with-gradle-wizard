@file:JvmName("DemoModuleConfig")

package com.github.shiraji.ipgwizard.config

import com.intellij.ide.util.PropertiesComponent

object IPGWizardConfig {
    const val PLUGIN_ID_KEY = "com.github.shiraji.foo.pluginid"
    const val PLUGIN_NAME_KEY = "com.github.shiraji.foo.pluginname"
    const val PLUGIN_VERSION_KEY = "com.github.shiraji.foo.pluginversion"
    const val VENDOR_EMAIL = "com.github.shiraji.foo.vendoremail"
    const val VENDOR_URL = "com.github.shiraji.foo.vendorurl"
    const val VENDOR_NAME = "com.github.shiraji.foo.vendorname"
    const val GRADLE_PLUGIN_VERSION = "com.github.shiraji.foo.gradlepluginversion"
    const val INTELLIJ_VERSION = "com.github.shiraji.foo.intellijversion"
    const val SETUPDATESINCEUNTILBUILD = "com.github.shiraji.ipgwizard.config.IPGWizardConfig.SETUPDATESINCEUNTILBUILD"
    const val SAMESINCEUNILBUILD = "com.github.shiraji.ipgwizard.config.IPGWizardConfig.SAMESINCEUNILBUILD"
    const val INSTRUMENT_CODE = "com.github.shiraji.ipgwizard.config.IPGWizardConfig.INSTRUMENT_CODE"
    const val INTELLIJ_VERSION_TYPE = "com.github.shiraji.ipgwizard.config.IPGWizardConfig.INTELLIJ_VERSION_TYPE"
    const val PUBLISH_USERNAME = "com.github.shiraji.ipgwizard.config.IPGWizardConfig.PUBLISH_USERNAME"
    const val PUBLISH_CHANNEL = "com.github.shiraji.ipgwizard.config.IPGWizardConfig.PUBLISH_CHANNEL"
    const val ALTERNATIVE_IDE_PATH = "com.github.shiraji.ipgwizard.config.IPGWizardConfig.ALTERNATIVE_IDE_PATH"
    const val DOWNLOAD_SOURCE = "com.github.shiraji.ipgwizard.config.IPGWizardConfig.DOWNLOAD_SOURCE"

    @JvmStatic fun setPluginId(value: String) = PropertiesComponent.getInstance().setValue(PLUGIN_ID_KEY, value)
    @JvmStatic fun setPluginName(value: String) = PropertiesComponent.getInstance().setValue(PLUGIN_NAME_KEY, value)
    @JvmStatic fun setPluginVersion(value: String) = PropertiesComponent.getInstance().setValue(PLUGIN_VERSION_KEY, value)
    @JvmStatic fun setVendorEmail(value: String) = PropertiesComponent.getInstance().setValue(VENDOR_EMAIL, value)
    @JvmStatic fun setVendorUrl(value: String) = PropertiesComponent.getInstance().setValue(VENDOR_URL, value)
    @JvmStatic fun setVendorName(value: String) = PropertiesComponent.getInstance().setValue(VENDOR_NAME, value)
    @JvmStatic fun setGradlePluginVersion(value: String) = PropertiesComponent.getInstance().setValue(GRADLE_PLUGIN_VERSION, value)
    @JvmStatic fun setIntellijVersion(value: String) = PropertiesComponent.getInstance().setValue(INTELLIJ_VERSION, value)
    @JvmStatic fun setUpdateSinceUntilBuild(value: Boolean) = PropertiesComponent.getInstance().setValue(SETUPDATESINCEUNTILBUILD, value)
    @JvmStatic fun setSameSinceUntilBuild(value: Boolean) = PropertiesComponent.getInstance().setValue(SAMESINCEUNILBUILD, value)

    var instrumentCode: Boolean
        @JvmStatic get() = PropertiesComponent.getInstance().getBoolean(INSTRUMENT_CODE, true)
        @JvmStatic set(value) = PropertiesComponent.getInstance().setValue(INSTRUMENT_CODE, value)

    var intellijVersionType: String?
        @JvmStatic get() = PropertiesComponent.getInstance().getValue(INTELLIJ_VERSION_TYPE)
        @JvmStatic set(value) = PropertiesComponent.getInstance().setValue(INTELLIJ_VERSION_TYPE, value)

    var publishUsername: String?
        @JvmStatic get() = PropertiesComponent.getInstance().getValue(PUBLISH_USERNAME)
        @JvmStatic set(value) = PropertiesComponent.getInstance().setValue(PUBLISH_USERNAME, value)

    var publishChannel: String?
        @JvmStatic get() = PropertiesComponent.getInstance().getValue(PUBLISH_CHANNEL)
        @JvmStatic set(value) = PropertiesComponent.getInstance().setValue(PUBLISH_CHANNEL, value)

    var downloadSource: Boolean
        @JvmStatic get() = PropertiesComponent.getInstance().getBoolean(DOWNLOAD_SOURCE, true)
        @JvmStatic set(value) = PropertiesComponent.getInstance().setValue(DOWNLOAD_SOURCE, value)

    var alternativeIdePath: String?
        @JvmStatic get() = PropertiesComponent.getInstance().getValue(ALTERNATIVE_IDE_PATH)
        @JvmStatic set(value) = PropertiesComponent.getInstance().setValue(ALTERNATIVE_IDE_PATH, value)

    @JvmStatic fun getPluginId() = PropertiesComponent.getInstance().getValue(PLUGIN_ID_KEY)
    @JvmStatic fun getPluginName() = PropertiesComponent.getInstance().getValue(PLUGIN_NAME_KEY)
    @JvmStatic fun getPluginVersion() = PropertiesComponent.getInstance().getValue(PLUGIN_VERSION_KEY)
    @JvmStatic fun getVendorEmail() = PropertiesComponent.getInstance().getValue(VENDOR_EMAIL)
    @JvmStatic fun getVendorUrl() = PropertiesComponent.getInstance().getValue(VENDOR_URL)
    @JvmStatic fun getVendorName() = PropertiesComponent.getInstance().getValue(VENDOR_NAME)
    @JvmStatic fun getGradlePluginVersion() = PropertiesComponent.getInstance().getValue(GRADLE_PLUGIN_VERSION)
    @JvmStatic fun getIntellijVersion() = PropertiesComponent.getInstance().getValue(INTELLIJ_VERSION)
    @JvmStatic fun isUpdateSinceUntilBuild() = PropertiesComponent.getInstance().getBoolean(SETUPDATESINCEUNTILBUILD, true)
    @JvmStatic fun isSameSinceUntilBuild() = PropertiesComponent.getInstance().getBoolean(SAMESINCEUNILBUILD, false)
}