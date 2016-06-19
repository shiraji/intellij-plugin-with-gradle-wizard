package com.github.shiraji.kipw

import com.intellij.ide.util.projectWizard.JavaModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.projectRoots.SdkTypeId
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.util.Condition
import org.jetbrains.kotlin.idea.KotlinIcons
import org.jetbrains.kotlin.resolve.TargetPlatform

class MyModuleBuilder(val targetPlatform: TargetPlatform, val builderName: String, val builderDescription: String) : JavaModuleBuilder() {
    override fun getBuilderId() = "myModuleBuilder"
    override fun getName() = builderName
    override fun getPresentableName() = builderName
    override fun getDescription() = builderDescription
    override fun getBigIcon() = KotlinIcons.KOTLIN_LOGO_24
    override fun getNodeIcon() = KotlinIcons.SMALL_LOGO
    override fun getGroupName() = "Intellij Platform Plugin (Gradle)"
    override fun createWizardSteps(wizardContext: WizardContext, modulesProvider: ModulesProvider) = ModuleWizardStep.EMPTY_ARRAY

    override fun modifySettingsStep(settingsStep: SettingsStep): ModuleWizardStep {
//        return KotlinModuleSettingStep(targetPlatform, this, settingsStep)
        return MyModuleWizardStep(settingsStep, this, Condition<SdkTypeId> { isSuitableSdkType(it) })
    }
}