package com.github.shiraji.kipw.sample

import com.intellij.icons.AllIcons
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager

class DemoModuleType() : ModuleType<DemoModuleBuilder>(ID) {

    companion object {
        val ID: String = "DEMO_MODULE_TYPE"

        fun getInstance(): DemoModuleType = ModuleTypeManager.getInstance().findByID(ID) as DemoModuleType

    }

    override fun createModuleBuilder() = DemoModuleBuilder()

    override fun getBigIcon() = AllIcons.General.Information

    override fun getNodeIcon(isOpened: Boolean) = AllIcons.General.Information

    override fun getName() = "Demo Module Type"

    override fun getDescription() = "Demo Module Type"

}