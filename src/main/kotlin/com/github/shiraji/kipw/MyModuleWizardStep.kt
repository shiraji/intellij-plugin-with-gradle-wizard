package com.github.shiraji.kipw

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.SdkSettingsStep
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.openapi.projectRoots.SdkTypeId
import com.intellij.openapi.util.Condition
import javax.swing.JComponent

class MyModuleWizardStep(settingsStep: SettingsStep, moduleBuilder: ModuleBuilder,
                         sdkFilter: Condition<SdkTypeId>) : SdkSettingsStep(settingsStep, moduleBuilder, sdkFilter) {
    override fun getComponent(): JComponent? {
        return MyWizardPanel().root
    }

    override fun updateDataModel() {
        System.out.println()
    }
}