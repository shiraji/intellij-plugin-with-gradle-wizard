package com.github.shiraji.ipgwizard.step;

import com.github.shiraji.ipgwizard.IPGWizardBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class IPGWizardSupportLanguageStep extends ModuleWizardStep implements Disposable {
    private JPanel panel;
    private JBList list;
    private JComboBox languageComboBox;
    private WizardContext context;
    private IPGWizardBuilder builder;

    private List<String> languages = new ArrayList<String>() {{
        add("Java");
        add("Kotlin");
    }};

    static {
    }

    public IPGWizardSupportLanguageStep(WizardContext context, IPGWizardBuilder builder) {
        this.context = context;
        this.builder = builder;

        for (String language : languages) {
            languageComboBox.addItem(language);
        }
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public void updateDataModel() {
        builder.setLanguage(languages.get(languageComboBox.getSelectedIndex()));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void disposeUIResources() {
        Disposer.dispose(this);
    }
}
