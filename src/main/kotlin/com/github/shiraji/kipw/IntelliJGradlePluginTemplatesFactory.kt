package com.github.shiraji.kipw

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.platform.ProjectTemplatesFactory
import com.intellij.platform.templates.BuilderBasedTemplate
import org.jetbrains.kotlin.resolve.jvm.platform.JvmPlatform

class IntelliJGradlePluginTemplatesFactory : ProjectTemplatesFactory() {
    override fun getGroups(): Array<out String> = arrayOf("Intellij Platform Plugin (Gradle)")

    override fun createTemplates(group: String?, context: WizardContext?) =
            arrayOf(
                    BuilderBasedTemplate(MyModuleBuilder(JvmPlatform, "Java", "Java module of Intellij Platform Plugin"))
            )
}