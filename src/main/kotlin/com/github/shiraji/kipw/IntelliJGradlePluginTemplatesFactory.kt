package com.github.shiraji.kipw

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.platform.ProjectTemplatesFactory
import com.intellij.platform.templates.BuilderBasedTemplate
import org.jetbrains.kotlin.idea.KotlinIcons
import org.jetbrains.kotlin.js.resolve.JsPlatform
import org.jetbrains.kotlin.resolve.jvm.platform.JvmPlatform

class IntelliJGradlePluginTemplatesFactory : ProjectTemplatesFactory() {
    override fun getGroups(): Array<out String> = arrayOf("Intellij Platform Plugin (Gradle)")

    override fun getGroupIcon(group: String) = KotlinIcons.SMALL_LOGO

    override fun createTemplates(group: String?, context: WizardContext?) =
            arrayOf(
                    BuilderBasedTemplate(MyModuleBuilder(JvmPlatform, "Kotlin (JVM)", "Kotlin module for JVM target")),
                    BuilderBasedTemplate(MyModuleBuilder(JsPlatform, "Kotlin (JavaScript - experimental)", "Kotlin module for JavaScript target"))
            )
}