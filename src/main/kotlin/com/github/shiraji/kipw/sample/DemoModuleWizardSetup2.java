package com.github.shiraji.kipw.sample;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
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
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public void updateDataModel() {
        builder.setPluginName(pluginName.getText());
    }

    @Override
    public void dispose() {

    }

    @Override
    public void disposeUIResources() {
        Disposer.dispose(this);
    }
}
