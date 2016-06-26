package com.github.shiraji.kipw.sample;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBList;

import javax.swing.*;

public class DemoModuleSupportLanguage extends ModuleWizardStep implements Disposable {
    private JPanel panel;
    private JBList list;
    private JComboBox languageComboBox;
    private WizardContext context;
    private DemoModuleBuilder builder;

    public DemoModuleSupportLanguage(WizardContext context, DemoModuleBuilder builder) {
        this.context = context;
        this.builder = builder;

        languageComboBox.addItem("Java");
        languageComboBox.addItem("Java and/or Kotlin");
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public void updateDataModel() {
        if (languageComboBox.getSelectedIndex() == 1) builder.setKotlin(true);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void disposeUIResources() {
        Disposer.dispose(this);
    }
}
