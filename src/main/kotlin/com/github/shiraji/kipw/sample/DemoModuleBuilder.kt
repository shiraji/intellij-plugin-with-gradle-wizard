package com.github.shiraji.kipw.sample

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.util.projectWizard.JavaModuleBuilder
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.ex.ProjectManagerEx
import com.intellij.openapi.roots.ModifiableRootModel
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
import org.jetbrains.plugins.gradle.util.GradleConstants
import java.io.File
import java.io.IOException

class DemoModuleBuilder : JavaModuleBuilder() {


    val TEMPLATE_GRADLE_SETTINGS = "Gradle Settings.gradle";
    val TEMPLATE_GRADLE_SETTINGS_MERGE = "Gradle Settings merge.gradle";
    val TEMPLATE_GRADLE_BUILD_WITH_WRAPPER = "Gradle Build Script with wrapper.gradle";
    val DEFAULT_TEMPLATE_GRADLE_BUILD = "Gradle Build Script.gradle";
    val TEMPLATE_ATTRIBUTE_PROJECT_NAME = "PROJECT_NAME";
    val TEMPLATE_ATTRIBUTE_MODULE_PATH = "MODULE_PATH";
    val TEMPLATE_ATTRIBUTE_MODULE_FLAT_DIR = "MODULE_FLAT_DIR";
    val TEMPLATE_ATTRIBUTE_MODULE_NAME = "MODULE_NAME";
    val TEMPLATE_ATTRIBUTE_MODULE_GROUP = "MODULE_GROUP";
    val TEMPLATE_ATTRIBUTE_MODULE_VERSION = "MODULE_VERSION";
    val TEMPLATE_ATTRIBUTE_GRADLE_VERSION = "GRADLE_VERSION";

    val BUILD_SCRIPT_DATA = Key.create<BuildScriptDataBuilder>("gradle.module.buildScriptData")

    var rootProjectPath: String? = null

    override fun getModuleType() = DemoModuleType.getInstance()

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

        if (renderNewFile) {
            val moduleDirName = VfsUtilCore.getRelativePath(modelContentRootDir, file.getParent(), '/');

            val attributes = hashMapOf<String, String?>()
            attributes.put(TEMPLATE_ATTRIBUTE_PROJECT_NAME, projectName);
            attributes.put(TEMPLATE_ATTRIBUTE_MODULE_PATH, moduleDirName);
            attributes.put(TEMPLATE_ATTRIBUTE_MODULE_NAME, moduleName);
            saveFile(file, TEMPLATE_GRADLE_SETTINGS, attributes);
        } else {
            val separatorChar = if (file.parent == null || !VfsUtilCore.isAncestor(file.getParent(), modelContentRootDir, true)) '/' else ':'
            val modulePath = VfsUtil.getPath(file, modelContentRootDir, separatorChar)

            val attributes = hashMapOf<String, String?>()
            attributes.put(TEMPLATE_ATTRIBUTE_MODULE_NAME, moduleName);
            // check for flat structure
            val flatStructureModulePath =
                    if (modulePath != null && StringUtil.startsWith(modulePath, "../")) StringUtil.trimStart(modulePath, "../") else null
            if (StringUtil.equals(flatStructureModulePath, modelContentRootDir.getName())) {
                attributes.put(TEMPLATE_ATTRIBUTE_MODULE_FLAT_DIR, "true");
                attributes.put(TEMPLATE_ATTRIBUTE_MODULE_PATH, flatStructureModulePath);
            } else {
                attributes.put(TEMPLATE_ATTRIBUTE_MODULE_PATH, modulePath);
            }

            appendToFile(file, TEMPLATE_GRADLE_SETTINGS_MERGE, attributes);
        }
        return file;
    }

    fun setupGradleBuildFile(modelContentRootDir: VirtualFile): VirtualFile? {
        return getOrCreateExternalProjectConfigFile(modelContentRootDir.path, GradleConstants.DEFAULT_SCRIPT_NAME)

//        if (file != null) {
//            val templateName = getExternalProjectSettings().getDistributionType() == DistributionType.WRAPPED
//            ? TEMPLATE_GRADLE_BUILD_WITH_WRAPPER
//            : DEFAULT_TEMPLATE_GRADLE_BUILD;
//            Map<String, String> attributes = ContainerUtil.newHashMap();
//            if (myProjectId != null) {
//                attributes.put(TEMPLATE_ATTRIBUTE_MODULE_VERSION, myProjectId.getVersion());
//                attributes.put(TEMPLATE_ATTRIBUTE_MODULE_GROUP, myProjectId.getGroupId());
//                attributes.put(TEMPLATE_ATTRIBUTE_GRADLE_VERSION, GradleVersion.current().getVersion());
//            }
//            saveFile(file, templateName, attributes);
//        }
//        return file
    }

    fun getOrCreateExternalProjectConfigFile(parent: String, fileName: String): VirtualFile? {
        val file = File(parent, fileName)
        FileUtilRt.createIfNotExists(file)
        return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
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

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?) = DemoModuleWizardStep()
}

