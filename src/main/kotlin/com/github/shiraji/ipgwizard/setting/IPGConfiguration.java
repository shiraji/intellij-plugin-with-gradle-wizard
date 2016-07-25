package com.github.shiraji.ipgwizard.setting;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IPGConfiguration implements Configurable {
    private JPanel panel;
    private JTextField pluginVersion;
    private JTextField pluginName;
    private JTextField intellijVersion;
    private JTextField version;
    private JTextField pluginId;
    private JTextField vendorEmail;
    private JTextField vendorUrl;
    private JTextField vendorName;
    private JCheckBox updateSinceUntilBuild;
    private JCheckBox sameSinceUntilBuild;
    private JComboBox intellijVersionType;
    private JCheckBox instrumentCode;
    private JTextField publishUsername;
    private JTextField publishChannel;
    private TextFieldWithBrowseButton alternativeIdePath;
    private JCheckBox downloadSource;

    @Nullable
    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }

    @Nls
    @Override
    public String getDisplayName() {
        return "gradle-intellij-plugin wizard";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }
}
