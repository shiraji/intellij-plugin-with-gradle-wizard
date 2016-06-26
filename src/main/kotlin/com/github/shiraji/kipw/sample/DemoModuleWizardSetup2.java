package com.github.shiraji.kipw.sample;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;

import javax.swing.*;

public class DemoModuleWizardSetup2 extends ModuleWizardStep implements Disposable {
    private JTextField version;
    private JTextField intellijVersion;
    private JTextField pluginVersion;
    private JPanel panel;
    private JTextField pluginName;
    private JTextField pluginId;
    private JTextField vendorEmail;
    private JTextField vendorUrl;
    private JTextField vendorName;

    private WizardContext wizardContext;
    private DemoModuleBuilder builder;

    public DemoModuleWizardSetup2(WizardContext wizardContext, DemoModuleBuilder builder) {
        this.wizardContext = wizardContext;
        this.builder = builder;

        String text = DemoModuleConfig.getGradlePluginVersion();
        if (text != null) version.setText(text);

        text = DemoModuleConfig.getIntellijVersion();
        if (text != null) intellijVersion.setText(text);

        text = DemoModuleConfig.getPluginId();
        if (text != null) pluginId.setText(text);

        text = DemoModuleConfig.getPluginName();
        if (text != null) pluginName.setText(text);

        text = DemoModuleConfig.getPluginVersion();
        if (text != null) pluginVersion.setText(text);

        text = DemoModuleConfig.getVendorEmail();
        if (text != null) vendorEmail.setText(text);

        text = DemoModuleConfig.getVendorName();
        if (text != null) vendorName.setText(text);

        text = DemoModuleConfig.getVendorUrl();
        if (text != null) vendorUrl.setText(text);
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public void updateDataModel() {
        Project project = wizardContext.getProject();

        String text = version.getText();
        builder.setGradlePluginVersion(text);
        DemoModuleConfig.setGradlePluginVersion(text);

        text = intellijVersion.getText();
        builder.setIntellijVersion(text);
        DemoModuleConfig.setIntellijVersion(text);

        text = pluginName.getText();
        builder.setPluginName(text);
        DemoModuleConfig.setPluginName(text);

        text = pluginId.getText();
        builder.setPluginId(text);
        DemoModuleConfig.setPluginId(text);

        text = pluginVersion.getText();
        builder.setPluginVersion(text);
        DemoModuleConfig.setPluginVersion(text);

        text = vendorEmail.getText();
        builder.setVendorEmail(text);
        DemoModuleConfig.setVendorEmail(text);

        text = vendorName.getText();
        builder.setVendorName(text);
        DemoModuleConfig.setVendorName(text);

        text = vendorUrl.getText();
        builder.setVendorUrl(text);
        DemoModuleConfig.setVendorUrl(text);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void disposeUIResources() {
        Disposer.dispose(this);
    }
}
