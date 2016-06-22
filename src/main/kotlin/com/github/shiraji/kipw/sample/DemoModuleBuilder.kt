package com.github.shiraji.kipw.sample

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.externalSystem.model.ProjectSystemId
import com.intellij.openapi.externalSystem.service.project.wizard.AbstractExternalModuleBuilder
import com.intellij.openapi.externalSystem.service.project.wizard.ExternalModuleSettingsStep
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.openapi.module.StdModuleTypes
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.ex.ProjectManagerEx
import com.intellij.openapi.projectRoots.JavaSdkType
import com.intellij.openapi.projectRoots.SdkTypeId
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import org.jetbrains.plugins.gradle.frameworkSupport.BuildScriptDataBuilder
import org.jetbrains.plugins.gradle.service.settings.GradleProjectSettingsControl
import org.jetbrains.plugins.gradle.settings.GradleProjectSettings
import org.jetbrains.plugins.gradle.util.GradleConstants
import java.io.File
import java.io.IOException

class DemoModuleBuilder : AbstractExternalModuleBuilder<GradleProjectSettings>(ProjectSystemId("Demo Module Type"), GradleProjectSettings()) {

    val TEMPLATE_GRADLE_SETTINGS = "Gradle Settings.gradle";
    val TEMPLATE_GRADLE_SETTINGS_MERGE = "Gradle Settings merge.gradle";
    val TEMPLATE_GRADLE_BUILD_WITH_WRAPPER = "Gradle Build Script with wrapper.gradle";
    val TEMPLATE_ATTRIBUTE_PROJECT_NAME = "PROJECT_NAME";
    val TEMPLATE_ATTRIBUTE_MODULE_PATH = "MODULE_PATH";
    val TEMPLATE_ATTRIBUTE_MODULE_FLAT_DIR = "MODULE_FLAT_DIR";
    val TEMPLATE_ATTRIBUTE_MODULE_NAME = "MODULE_NAME";
    val TEMPLATE_ATTRIBUTE_MODULE_GROUP = "MODULE_GROUP";
    val TEMPLATE_ATTRIBUTE_MODULE_VERSION = "MODULE_VERSION";
    val TEMPLATE_ATTRIBUTE_GRADLE_VERSION = "GRADLE_VERSION";

    val BUILD_SCRIPT_DATA = Key.create<BuildScriptDataBuilder>("gradle.module.buildScriptData")

    var rootProjectPath: String? = null

    override fun getModuleType() = StdModuleTypes.JAVA

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel?) {
        val contentEntryPath = contentEntryPath
        if (contentEntryPath.isNullOrEmpty()) return
        val contentRootDir = File(contentEntryPath)
        FileUtilRt.createDirectory(contentRootDir)
        val fileSystem = LocalFileSystem.getInstance()
        val modelContentRootDir = fileSystem.refreshAndFindFileByIoFile(contentRootDir) ?: return
        modifiableRootModel ?: return
        modifiableRootModel.addContentEntry(modelContentRootDir);
        // todo this should be moved to generic ModuleBuilder
        if (myJdk == null) {
            modifiableRootModel.inheritSdk();
        } else {
            modifiableRootModel.sdk = myJdk;
        }

        val project = modifiableRootModel.project;
//        if (myParentProject != null) {
//            rootProjectPath = myParentProject.getLinkedExternalProjectPath();
//        } else {
        rootProjectPath =
                FileUtil.toCanonicalPath(project.basePath)// : modelContentRootDir.getPath());
//        }

        // FROM HERE

        val gradleBuildFile = setupGradleBuildFile(modelContentRootDir)
        setupGradleSettingsFile(
                rootProjectPath!!, modelContentRootDir, modifiableRootModel.project.name,
                modifiableRootModel.module.name, true)

        if (gradleBuildFile != null) {
            modifiableRootModel.module.putUserData(
                    BUILD_SCRIPT_DATA, BuildScriptDataBuilder(gradleBuildFile));
        }
    }

    fun setupGradleSettingsFile(rootProjectPath: String, modelContentRootDir: VirtualFile, projectName: String, moduleName: String, renderNewFile: Boolean): VirtualFile? {
        var file: VirtualFile = getOrCreateExternalProjectConfigFile(rootProjectPath, GradleConstants.SETTINGS_FILE_NAME) ?: return null
        val moduleDirName = VfsUtilCore.getRelativePath(modelContentRootDir, file.parent, '/');
        val attributes = hashMapOf<String, String?>()
        attributes.put(TEMPLATE_ATTRIBUTE_PROJECT_NAME, projectName);
        attributes.put(TEMPLATE_ATTRIBUTE_MODULE_PATH, moduleDirName);
        attributes.put(TEMPLATE_ATTRIBUTE_MODULE_NAME, moduleName);
        saveFile(file, TEMPLATE_GRADLE_SETTINGS, attributes);
        return file;
    }

    fun setupGradleBuildFile(modelContentRootDir: VirtualFile): VirtualFile? {
        val file = getOrCreateExternalProjectConfigFile(modelContentRootDir.path, GradleConstants.DEFAULT_SCRIPT_NAME)

        if (file != null) {
            val attributes = hashMapOf<String, String?>()
            attributes.put(TEMPLATE_ATTRIBUTE_MODULE_VERSION, "version");
            attributes.put(TEMPLATE_ATTRIBUTE_MODULE_GROUP, "group");
            attributes.put(TEMPLATE_ATTRIBUTE_GRADLE_VERSION, "2.5");
            saveFile(file, "Gradle Build Script2", attributes)
        }
        return file
    }

    fun getOrCreateExternalProjectConfigFile(parent: String, fileName: String): VirtualFile? {
        val file = File(parent, fileName)
        FileUtilRt.createIfNotExists(file)
        return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
    }

    private fun saveFileFromText(file: VirtualFile, template: String, attributes: Map<String, String?>?) {
        try {
            appendToFile(file, if (attributes != null) FileTemplateUtil.mergeTemplate(attributes, template, false) else template)
        } catch (e: IOException) {
            throw ConfigurationException(e.message, e.stackTrace.toString())
        }
    }

    private fun saveFile(file: VirtualFile, templateName: String, templateAttributes: Map<String, String?>?) {
        val manager = FileTemplateManager.getDefaultInstance();
        val template = manager.getInternalTemplate(templateName);
        try {
            appendToFile(file, if (templateAttributes != null) template.getText(templateAttributes) else template.text);
        } catch (e: IOException) {
            throw ConfigurationException(e.message, e.stackTrace.toString())
        }
    }

    private fun appendToFile(file: VirtualFile, templateName: String, templateAttributes: Map<String, String?>?) {
        val manager = FileTemplateManager.getDefaultInstance();
        val template = manager.getInternalTemplate(templateName);
        try {
            appendToFile(file, if (templateAttributes != null) template.getText(templateAttributes) else template.text);
        } catch (e: IOException) {
            throw ConfigurationException(e.message, e.stackTrace.toString())
        }
    }

    fun appendToFile(file: VirtualFile, text: String) {
        var lineSeparator: String? = LoadTextUtil.detectLineSeparator(file, true);
        if (lineSeparator == null) {
            lineSeparator = CodeStyleSettingsManager.getSettings(ProjectManagerEx.getInstanceEx().defaultProject).lineSeparator
        }
        val existingText = StringUtil.trimTrailing(VfsUtilCore.loadText(file));
        val content = (if (StringUtil.isNotEmpty(existingText)) existingText + lineSeparator else "") + StringUtil.convertLineSeparators(text, lineSeparator!!)
        VfsUtil.saveText(file, content)
    }

    lateinit var wizardContext: WizardContext

    override fun createWizardSteps(wizardContext: WizardContext, modulesProvider: ModulesProvider): Array<out ModuleWizardStep>? {
        this.wizardContext = wizardContext

        return arrayOf(ExternalModuleSettingsStep<GradleProjectSettings>(
                wizardContext, this, GradleProjectSettingsControl(externalProjectSettings)))
    }

    override fun isSuitableSdkType(sdkType: SdkTypeId?) = sdkType is JavaSdkType

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?): ModuleWizardStep? {
        val step = GradleFrameworksWizardStep2(context, this)
        Disposer.register(parentDisposable!!, step)
        return step
    }
}