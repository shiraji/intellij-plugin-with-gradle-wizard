package com.github.shiraji.ipgwizard.step;

import com.github.shiraji.ipgwizard.IPGWizardBuilder;
import com.github.shiraji.ipgwizard.config.IPGWizardConfig;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class IPGWizardOptionsStep extends ModuleWizardStep implements Disposable {
    private JTextField version;
    private JTextField intellijVersion;
    private JTextField pluginVersion;
    private JPanel panel;
    private JTextField pluginName;
    private JTextField pluginId;
    private JTextField vendorEmail;
    private JTextField vendorUrl;
    private JTextField vendorName;
    private JCheckBox updateSinceUntilBuild;
    private JCheckBox sameSinceUntilBuild;
    private JComboBox intellijVersionType;
    private JCheckBox instrumentCode;

    private WizardContext wizardContext;
    private IPGWizardBuilder builder;

    private List<String> intellijVersionTypes = new ArrayList<String>() {{
        add("IC");
        add("IU");
    }};

    public IPGWizardOptionsStep(WizardContext wizardContext, IPGWizardBuilder builder) {
        this.wizardContext = wizardContext;
        this.builder = builder;

        String text = IPGWizardConfig.getGradlePluginVersion();
        if (text != null) version.setText(text);

        text = IPGWizardConfig.getIntellijVersion();
        if (text != null) intellijVersion.setText(text);

        text = IPGWizardConfig.getPluginId();
        if (text != null) pluginId.setText(text);

        text = IPGWizardConfig.getPluginName();
        if (text != null) pluginName.setText(text);

        text = IPGWizardConfig.getPluginVersion();
        if (text != null) pluginVersion.setText(text);

        text = IPGWizardConfig.getVendorEmail();
        if (text != null) vendorEmail.setText(text);

        text = IPGWizardConfig.getVendorName();
        if (text != null) vendorName.setText(text);

        text = IPGWizardConfig.getVendorUrl();
        if (text != null) vendorUrl.setText(text);

        updateSinceUntilBuild.setSelected(IPGWizardConfig.isUpdateSinceUntilBuild());
        sameSinceUntilBuild.setSelected(IPGWizardConfig.isSameSinceUntilBuild());

        for (String versionType : intellijVersionTypes) {
            intellijVersionType.addItem(versionType);
        }

        text = IPGWizardConfig.getIntellijVersionType();
        if (text == null) {
            intellijVersionType.setSelectedIndex(0);
        } else {
            intellijVersionType.setSelectedItem(text);
        }
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public void updateDataModel() {
        String text = version.getText();
        builder.setGradlePluginVersion(text);
        IPGWizardConfig.setGradlePluginVersion(text);

        text = intellijVersion.getText();
        builder.setIntellijVersion(text);
        IPGWizardConfig.setIntellijVersion(text);

        text = pluginName.getText();
        builder.setPluginName(text);
        IPGWizardConfig.setPluginName(text);

        text = pluginId.getText();
        builder.setPluginId(text);
        IPGWizardConfig.setPluginId(text);

        text = pluginVersion.getText();
        builder.setPluginVersion(text);
        IPGWizardConfig.setPluginVersion(text);

        text = vendorEmail.getText();
        builder.setVendorEmail(text);
        IPGWizardConfig.setVendorEmail(text);

        text = vendorName.getText();
        builder.setVendorName(text);
        IPGWizardConfig.setVendorName(text);

        text = vendorUrl.getText();
        builder.setVendorUrl(text);
        IPGWizardConfig.setVendorUrl(text);

        boolean updateSinceUntilBuildSelected = updateSinceUntilBuild.isSelected();
        builder.setUpdateSinceUntilBuild(updateSinceUntilBuildSelected);
        IPGWizardConfig.setUpdateSinceUntilBuild(updateSinceUntilBuildSelected);

        boolean sameSinceUntilBuildSelected = sameSinceUntilBuild.isSelected();
        builder.setSameSinceUntilBuild(sameSinceUntilBuildSelected);
        IPGWizardConfig.setSameSinceUntilBuild(sameSinceUntilBuildSelected);

        boolean instrumentCodeSelected = instrumentCode.isSelected();
        builder.setInstrumentCode(instrumentCodeSelected);
        IPGWizardConfig.setInstrumentCode(instrumentCodeSelected);

        text = (String) intellijVersionType.getSelectedItem();
        builder.setIntellijVersionType(text);
        IPGWizardConfig.setIntellijVersionType(text);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void disposeUIResources() {
        Disposer.dispose(this);
    }
}
