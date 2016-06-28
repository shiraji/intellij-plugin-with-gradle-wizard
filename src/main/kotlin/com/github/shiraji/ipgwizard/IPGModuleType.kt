package com.github.shiraji.ipgwizard

import com.intellij.icons.AllIcons
import com.intellij.openapi.module.ModuleType

class IPGModuleType() : ModuleType<IPGWizardBuilder>(ID) {

    companion object {
        val ID: String = "INTELLIJ_PLUGIN_WITH_GRADLE_WIZARD"
    }

    override fun createModuleBuilder() = IPGWizardBuilder()

    override fun getBigIcon() = AllIcons.General.Information

    override fun getNodeIcon(isOpened: Boolean) = AllIcons.General.Information

    override fun getName() = "Intellij Plugin with Gradle Wizard"

    override fun getDescription() = "Intellij Plugin with Gradle"
}