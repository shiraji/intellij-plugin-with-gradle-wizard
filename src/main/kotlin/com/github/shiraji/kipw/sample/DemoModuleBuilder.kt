package com.github.shiraji.kipw.sample

import com.intellij.ide.util.projectWizard.JavaModuleBuilder
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File

class DemoModuleBuilder : JavaModuleBuilder() {

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
                rootProjectPath, modelContentRootDir, modifiableRootModel.getProject().getName(),
                myProjectId == null ? modifiableRootModel.getModule().getName() : myProjectId.getArtifactId(),
        myWizardContext.isCreatingNewProject() || myParentProject == null
        );

        if (gradleBuildFile != null) {
            modifiableRootModel.getModule().putUserData(
                    BUILD_SCRIPT_DATA, new BuildScriptDataBuilder(gradleBuildFile));
        }
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?) = DemoModuleWizardStep()
}

