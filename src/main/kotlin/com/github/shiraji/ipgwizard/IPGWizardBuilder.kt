package com.github.shiraji.ipgwizard

import com.github.shiraji.ipgwizard.step.IPGWizardOptionsStep
import com.github.shiraji.ipgwizard.step.IPGWizardSupportLanguageStep
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.projectView.actions.MarkRootActionBase
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
import org.jetbrains.jps.model.java.JavaResourceRootType
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.plugins.gradle.frameworkSupport.BuildScriptDataBuilder
import org.jetbrains.plugins.gradle.service.settings.GradleProjectSettingsControl
import org.jetbrains.plugins.gradle.settings.GradleProjectSettings
import org.jetbrains.plugins.gradle.util.GradleConstants
import java.io.File
import java.io.IOException

class IPGWizardBuilder : AbstractExternalModuleBuilder<GradleProjectSettings>(ProjectSystemId("Intellij Plugin with Gradle"), GradleProjectSettings()) {

    val TEMPLATE_GRADLE_SETTINGS = "Gradle Settings.gradle"
    val TEMPLATE_PLUGIN_XML = "IPGWizard_Plugin.xml"
    val TEMPLATE_RUNIDEA_XML = "IPGWizard_runIdea.xml"
    val TEMPLATE_BUILDPLUNGIN_XML = "IPGWizard_buildPlugin.xml"
    val TEMPLATE_BUILD_GRADLE = "IPGWizard_Gradle Build Script.gradle"

    // DO NOT CHANGE
    val TEMPLATE_ATTRIBUTE_PROJECT_NAME = "PROJECT_NAME"
    val TEMPLATE_ATTRIBUTE_MODULE_PATH = "MODULE_PATH"
    val TEMPLATE_ATTRIBUTE_MODULE_NAME = "MODULE_NAME"

    val TEMPLATE_ATTRIBUTE_PLUGIN_NAME = "PLUGIN_NAME"
    val TEMPLATE_ATTRIBUTE_PLUGIN_VERSION = "PLUGIN_VERSION"
    val TEMPLATE_ATTRIBUTE_PLUGIN_ID = "PLUGIN_ID"
    val TEMPLATE_ATTRIBUTE_INTELLIJ_VERSION = "INTELLIJ_VERSION"
    val TEMPLATE_ATTRIBUTE_GRADLE_PLUGIN_VERSION = "GRADLE_PLUGIN_VERSION"
    val TEMPLATE_ATTRIBUTE_VENDOR_EMAIL = "VENDOR_EMAIL"
    val TEMPLATE_ATTRIBUTE_VENDOR_NAME = "VENDOR_NAME"
    val TEMPLATE_ATTRIBUTE_VENDOR_URL = "VENDOR_URL"
    val TEMPLATE_ATTRIBUTE_LANGUAGE = "LANGUAGE"
    val TEMPLATE_ATTRIBUTE_UPDATE_SINCE_UNTIL_BUILD = "UPDATE_SINCE_UNTIL_BUILD"
    val TEMPLATE_ATTRIBUTE_SAME_SINCE_UNTIL_BUILD = "SAME_SINCE_UNTIL_BUILD"
    val TEMPLATE_ATTRIBUTE_INSTRUMENT_CODE = "INSTRUMENT_CODE"
    val TEMPLATE_ATTRIBUTE_INTELLIJ_VERSION_TYPE = "INTELLIJ_VERSION_TYPE"
    val TEMPLATE_ATTRIBUTE_PUBLISH_NAME = "PUBLISH_NAME"
    val TEMPLATE_ATTRIBUTE_PUBLISH_CHANNEL = "PUBLISH_CHANNEL"
    val TEMPLATE_ATTRIBUTE_DOWNLOAD_SOURCE = "DOWNLOAD_SOURCE"
    val TEMPLATE_ATTRIBUTE_ALTERNATIVE_IDE_PATH = "ALTERNATIVE_IDE_PATH"

    val BUILD_SCRIPT_DATA = Key.create<BuildScriptDataBuilder>("gradle.module.buildScriptData")

    var rootProjectPath: String? = null

    // TODO filter only plugin sdk
    override fun getModuleType() = StdModuleTypes.JAVA

    var pluginVersion: String = ""
    var pluginName: String = ""
    var pluginId: String = ""
    var vendorEmail: String = ""
    var vendorUrl: String = ""
    var vendorName: String = ""
    var gradlePluginVersion: String = ""
    var intellijVersion: String = ""
    var language: String = ""

    var isUpdateSinceUntilBuild: Boolean = true
    var isSameSinceUntilBuild: Boolean = false
    var isInstrumentCode = true
    var intellijVersionType = ""
    var publishName = ""
    var publishChannel = ""
    var isDownloadSource = true
    var alternativeIdePath = ""

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel?) {
        val contentEntryPath = contentEntryPath
        if (contentEntryPath.isNullOrEmpty()) return
        val contentRootDir = File(contentEntryPath)
        FileUtilRt.createDirectory(contentRootDir)
        val fileSystem = LocalFileSystem.getInstance()
        val modelContentRootDir = fileSystem.refreshAndFindFileByIoFile(contentRootDir) ?: return
        modifiableRootModel ?: return
        modifiableRootModel.addContentEntry(modelContentRootDir);
        if (myJdk == null) {
            modifiableRootModel.inheritSdk();
        } else {
            modifiableRootModel.sdk = myJdk;
        }

        val project = modifiableRootModel.project;

        // TODO fix this for adding new module
//        if (myParentProject != null) {
//            rootProjectPath = myParentProject.getLinkedExternalProjectPath();
//        } else {
        rootProjectPath =
                FileUtil.toCanonicalPath(project.basePath)// : modelContentRootDir.getPath());
//        }

        val gradleBuildFile = setupGradleBuildFile(modelContentRootDir)
        setupGradleSettingsFile(
                rootProjectPath!!, modelContentRootDir, modifiableRootModel.project.name,
                modifiableRootModel.module.name, true)

        setupPluginFile(modifiableRootModel, modelContentRootDir)
        setupSourceDirectory(modifiableRootModel, modelContentRootDir)
        setupRunConfigurations(modelContentRootDir)

        if (gradleBuildFile != null) {
            modifiableRootModel.module.putUserData(
                    BUILD_SCRIPT_DATA, BuildScriptDataBuilder(gradleBuildFile));
        }
    }

    private fun setupRunConfigurations(modelContentRootDir: VirtualFile) {
        val ideaPath = "${modelContentRootDir.path}/.idea/runConfigurations"
        val attributes = hashMapOf<String, String?>(TEMPLATE_ATTRIBUTE_PLUGIN_NAME to pluginName)
        val buildPluginFile: VirtualFile = getOrCreateExternalProjectConfigFile(ideaPath, "buildPlugin.xml") ?: return
        saveFile(buildPluginFile, TEMPLATE_BUILDPLUNGIN_XML, attributes)
        val runIdeaFile: VirtualFile = getOrCreateExternalProjectConfigFile(ideaPath, "runIdea.xml") ?: return
        saveFile(runIdeaFile, TEMPLATE_RUNIDEA_XML, attributes)
    }

    private fun setupSourceDirectory(modifiableRootModel: ModifiableRootModel, modelContentRootDir: VirtualFile) {
        val resourceRootPath = "${modelContentRootDir.path}/src/main/java"
        val contentRoot = LocalFileSystem.getInstance().findFileByPath(contentEntryPath!!)
        val contentEntry = MarkRootActionBase.findContentEntry(modifiableRootModel, contentRoot!!)
        contentEntry?.addSourceFolder(VfsUtilCore.pathToUrl(resourceRootPath), JavaSourceRootType.SOURCE)

        VfsUtil.createDirectories("$resourceRootPath/${pluginId.replace(".", "/")}")
    }

    fun setupPluginFile(modifiableRootModel: ModifiableRootModel, modelContentRootDir: VirtualFile): VirtualFile? {
        val resourceRootPath = "${modelContentRootDir.path}/src/main/resources/"
        val contentRoot = LocalFileSystem.getInstance().findFileByPath(contentEntryPath!!)
        val contentEntry = MarkRootActionBase.findContentEntry(modifiableRootModel, contentRoot!!)
        contentEntry?.addSourceFolder(VfsUtilCore.pathToUrl(resourceRootPath), JavaResourceRootType.RESOURCE)
        val file: VirtualFile = getOrCreateExternalProjectConfigFile("$resourceRootPath/META-INF", "plugin.xml") ?: return null
        val attributes = hashMapOf<String, String?>().apply {
            put(TEMPLATE_ATTRIBUTE_PLUGIN_NAME, pluginName)
            put(TEMPLATE_ATTRIBUTE_PLUGIN_VERSION, pluginVersion)
            put(TEMPLATE_ATTRIBUTE_PLUGIN_ID, pluginId)
            put(TEMPLATE_ATTRIBUTE_VENDOR_EMAIL, vendorEmail)
            put(TEMPLATE_ATTRIBUTE_VENDOR_URL, vendorUrl)
            put(TEMPLATE_ATTRIBUTE_VENDOR_NAME, vendorName)
            put(TEMPLATE_ATTRIBUTE_INTELLIJ_VERSION, intellijVersion)
        }
        saveFile(file, TEMPLATE_PLUGIN_XML, attributes)
        return file
    }

    fun setupGradleSettingsFile(rootProjectPath: String, modelContentRootDir: VirtualFile, projectName: String, moduleName: String, renderNewFile: Boolean): VirtualFile? {
        val file: VirtualFile = getOrCreateExternalProjectConfigFile(rootProjectPath, GradleConstants.SETTINGS_FILE_NAME) ?: return null
        val moduleDirName = VfsUtilCore.getRelativePath(modelContentRootDir, file.parent, '/')
        val attributes = hashMapOf<String, String?>().apply {
            put(TEMPLATE_ATTRIBUTE_PROJECT_NAME, projectName)
            put(TEMPLATE_ATTRIBUTE_MODULE_PATH, moduleDirName)
            put(TEMPLATE_ATTRIBUTE_MODULE_NAME, moduleName)
        }
        saveFile(file, TEMPLATE_GRADLE_SETTINGS, attributes)
        return file;
    }

    fun setupGradleBuildFile(modelContentRootDir: VirtualFile): VirtualFile? {
        val file = getOrCreateExternalProjectConfigFile(modelContentRootDir.path, GradleConstants.DEFAULT_SCRIPT_NAME) ?: return null
        val attributes = hashMapOf<String, String?>().apply {
            put(TEMPLATE_ATTRIBUTE_PLUGIN_NAME, pluginName)
            put(TEMPLATE_ATTRIBUTE_PLUGIN_VERSION, pluginVersion)
            put(TEMPLATE_ATTRIBUTE_PLUGIN_ID, pluginId)
            put(TEMPLATE_ATTRIBUTE_INTELLIJ_VERSION, intellijVersion)
            put(TEMPLATE_ATTRIBUTE_GRADLE_PLUGIN_VERSION, gradlePluginVersion)
            put(TEMPLATE_ATTRIBUTE_LANGUAGE, language)
            if (!isUpdateSinceUntilBuild) put(TEMPLATE_ATTRIBUTE_UPDATE_SINCE_UNTIL_BUILD, isUpdateSinceUntilBuild.toString())
            if (isSameSinceUntilBuild) put(TEMPLATE_ATTRIBUTE_SAME_SINCE_UNTIL_BUILD, isSameSinceUntilBuild.toString())
            if (!isInstrumentCode) put(TEMPLATE_ATTRIBUTE_INSTRUMENT_CODE, isInstrumentCode.toString())
            if (intellijVersionType.isNotBlank() && !intellijVersionType.equals("IC")) put(TEMPLATE_ATTRIBUTE_INTELLIJ_VERSION_TYPE, intellijVersionType)
            if (publishName.isNotBlank()) put(TEMPLATE_ATTRIBUTE_PUBLISH_NAME, publishName)
            if (publishChannel.isNotBlank()) put(TEMPLATE_ATTRIBUTE_PUBLISH_CHANNEL, publishChannel)
            if (!isDownloadSource) put(TEMPLATE_ATTRIBUTE_DOWNLOAD_SOURCE, isDownloadSource.toString())
            if (alternativeIdePath.isNotBlank()) put(TEMPLATE_ATTRIBUTE_ALTERNATIVE_IDE_PATH, alternativeIdePath)
        }
        saveFile(file, TEMPLATE_BUILD_GRADLE, attributes)
        return file
    }

    fun getOrCreateExternalProjectConfigFile(parent: String, fileName: String): VirtualFile? {
        val file = File(parent, fileName)
        FileUtilRt.createIfNotExists(file)
        return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
    }

    private fun saveFile(file: VirtualFile, templateName: String, templateAttributes: Map<String, String?>?) {
        val manager = FileTemplateManager.getDefaultInstance()
        val template = manager.getInternalTemplate(templateName)
        try {
            appendToFile(file, if (templateAttributes != null) template.getText(templateAttributes) else template.text)
        } catch (e: IOException) {
            throw ConfigurationException(e.message, e.stackTrace.toString())
        }
    }

    fun appendToFile(file: VirtualFile, text: String) {
        val lineSeparator: String = LoadTextUtil.detectLineSeparator(file, true) ?: CodeStyleSettingsManager.getSettings(ProjectManagerEx.getInstanceEx().defaultProject).lineSeparator
        val existingText = VfsUtilCore.loadText(file).trimEnd()
        val content = (if (existingText.isNullOrEmpty()) "" else existingText + lineSeparator) + StringUtil.convertLineSeparators(text, lineSeparator)
        VfsUtil.saveText(file, content)
    }

    override fun createWizardSteps(wizardContext: WizardContext, modulesProvider: ModulesProvider): Array<out ModuleWizardStep>? {
        return arrayOf(IPGWizardOptionsStep(wizardContext, this),
                ExternalModuleSettingsStep<GradleProjectSettings>(wizardContext, this, GradleProjectSettingsControl(externalProjectSettings)))
    }

    override fun isSuitableSdkType(sdkType: SdkTypeId?) = sdkType is JavaSdkType

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?): ModuleWizardStep? {
        val step = IPGWizardSupportLanguageStep(context, this)
        Disposer.register(parentDisposable!!, step)
        return step
    }
}