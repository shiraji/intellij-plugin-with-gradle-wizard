package com.github.shiraji.ipgwizard

import com.intellij.icons.AllIcons
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ui.configuration.ModulesProvider

class IPGModuleType() : ModuleType<IPGWizardBuilder>(ID) {

    companion object {
        val ID: String = "DEMO_MODULE_TYPE"
    }

    override fun createModuleBuilder() = IPGWizardBuilder()

    override fun getBigIcon() = AllIcons.General.Information

    override fun getNodeIcon(isOpened: Boolean) = AllIcons.General.Information

    override fun getName() = "Demo Module Type"

    override fun getDescription() = "Demo Module Type"

    override fun createWizardSteps(wizardContext: WizardContext, moduleBuilder: IPGWizardBuilder, modulesProvider: ModulesProvider): Array<out ModuleWizardStep> {
        return super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider)
    }

}