package com.github.shiraji.ipgwizard.step;

import com.github.shiraji.ipgwizard.IPGWizardBuilder;
import com.github.shiraji.ipgwizard.config.IPGWizardConfig;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBList;
import org.gradle.api.JavaVersion;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class IPGWizardSupportLanguageStep extends ModuleWizardStep implements Disposable {
    private JPanel panel;
    private JBList list;
    private JComboBox<Language> languageComboBox;
    private JTextField kotlinVersionTextField;
    private JComboBox javaVersionCombobox;
    private JLabel kotlinVersionLabel;
    private JLabel javaVersionLabel;
    private WizardContext context;
    private IPGWizardBuilder builder;

    public enum Language {
        JAVA("Java"),
        KOTLIN("Kotlin");

        private final String name;

        Language(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static {
    }

    public IPGWizardSupportLanguageStep(WizardContext context, IPGWizardBuilder builder) {
        this.context = context;
        this.builder = builder;
        languageComboBox.setModel(new DefaultComboBoxModel<>(Language.values()));
        int language = IPGWizardConfig.getLanguage();
        setKotlinUiVisible(language == Language.KOTLIN.ordinal());
        languageComboBox.setSelectedIndex(language);
        languageComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Language language = (Language) e.getItem();
                setKotlinUiVisible(language == Language.KOTLIN);
            }
        });

        javaVersionCombobox.setModel(new DefaultComboBoxModel<>(JavaVersion.values()));
        javaVersionCombobox.setSelectedIndex(JavaVersion.valueOf(IPGWizardConfig.getJavaVersion()).ordinal());
        kotlinVersionTextField.setText(IPGWizardConfig.getKotlinVersion());
    }

    private void setKotlinUiVisible(boolean flag) {
        kotlinVersionTextField.setVisible(flag);
        kotlinVersionLabel.setVisible(flag);
    }


    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public void updateDataModel() {
        int selectedIndex = languageComboBox.getSelectedIndex();
        String language = Language.values()[selectedIndex].toString().toLowerCase();
        builder.setLanguage(language);
        IPGWizardConfig.setLanguage(selectedIndex);

        JavaVersion javaVersion = (JavaVersion) javaVersionCombobox.getSelectedItem();
        builder.setJavaVersion(javaVersion.name());
        IPGWizardConfig.setJavaVersion(javaVersion.name());

        if (selectedIndex == Language.KOTLIN.ordinal()) {
            String kotlinVersionText = kotlinVersionTextField.getText();
            builder.setKotlinVersion(kotlinVersionText);
            IPGWizardConfig.setKotlinVersion(kotlinVersionText);
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void disposeUIResources() {
        Disposer.dispose(this);
    }
}
