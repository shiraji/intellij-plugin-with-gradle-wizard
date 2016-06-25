package com.github.shiraji.kipw.sample

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.util.Disposer
import javax.swing.JComponent
import javax.swing.JLabel

class DemoModuleWizardStep() : ModuleWizardStep(), Disposable {
    override fun dispose() {
    }

    override fun disposeUIResources() {
        Disposer.dispose(this)
    }

    override fun getComponent(): JComponent? {
        return JLabel("Provide some setting here")
    }

    override fun updateDataModel() {
    }

    @Throws(ConfigurationException::class)
    override fun validate(): Boolean {
        if (!super.validate()) {
            return false
        }
        return true
    }

    override fun updateStep() {
    }

    override fun isStepVisible(): Boolean {
        return true
    }
}