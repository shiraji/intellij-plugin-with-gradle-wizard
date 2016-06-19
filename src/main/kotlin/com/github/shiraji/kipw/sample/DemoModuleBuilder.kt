package com.github.shiraji.kipw.sample

import com.intellij.ide.projectView.actions.MarkRootActionBase
import com.intellij.ide.util.projectWizard.JavaModuleBuilder
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtilCore
import org.jetbrains.idea.devkit.build.PluginBuildConfiguration
import org.jetbrains.jps.model.java.JavaResourceRootType

class DemoModuleBuilder : JavaModuleBuilder() {

    override fun getModuleType() = DemoModuleType.getInstance()

    override fun setupRootModel(rootModel: ModifiableRootModel?) {
        super.setupRootModel(rootModel)
        val contentEntryPath = contentEntryPath ?: return
        val resourceRootPath = "$contentEntryPath/resources"
        val contentRoot = LocalFileSystem.getInstance().findFileByPath(contentEntryPath) ?: return
        rootModel ?: return
        MarkRootActionBase.findContentEntry(rootModel, contentRoot)?.let {
            it.addSourceFolder(VfsUtilCore.pathToUrl(resourceRootPath), JavaResourceRootType.RESOURCE)
        }

        val defaultPluginXMLLocation = "$resourceRootPath/META-INF/plugin.xml"
        val module = rootModel.module
        val project = module.project
        StartupManager.getInstance(project).runWhenProjectIsInitialized {
            PluginBuildConfiguration.getInstance(module)?.let {
                it.setPluginXmlPathAndCreateDescriptorIfDoesntExist(defaultPluginXMLLocation)
            }

            LocalFileSystem.getInstance().findFileByPath(defaultPluginXMLLocation)?.let {
                FileEditorManager.getInstance(project).openFile(it, true)
            }
        }
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?) = DemoModuleWizardStep()
}

