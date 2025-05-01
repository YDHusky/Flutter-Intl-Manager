package online.yudream.intl.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import online.yudream.intl.common.AIClient;
import online.yudream.intl.common.GlobalSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class GlobalSettingsConfigurable implements Configurable {

    private JTextField baseUrlField;
    private JTextField tokenField;
    private JTextField defaultLanguageField;
    private JTextField modelField;

    public GlobalSettingsConfigurable() {

    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Global Settings";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel main = new JPanel();
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        defaultLanguageField = new JBTextField(40);
        baseUrlField = new JBTextField(40);
        tokenField = new JBTextField(40);
        modelField = new JBTextField(40);

        // 设置约束参数
        gbc.gridx = 0; // 列
        gbc.gridy = 0; // 行
        gbc.anchor = GridBagConstraints.WEST; // 锚点
        gbc.fill = GridBagConstraints.HORIZONTAL; // 填充方式
        gbc.insets = JBUI.insets(5); // 边距

        // 将组件添加到 panel，并应用约束
        panel.add(new JLabel("模型地址:"), gbc);
        gbc.gridx++;
        panel.add(baseUrlField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("ApiKey:"), gbc);
        gbc.gridx++;
        panel.add(tokenField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("模型:"), gbc);
        gbc.gridx++;
        panel.add(modelField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("默认语言:"), gbc);
        gbc.gridx++;
        panel.add(defaultLanguageField, gbc);

        main.add(panel);
        return main;
    }

    @Override
    public boolean isModified() {
        return !baseUrlField.getText().equals(GlobalSettings.getInstance().getBaseUrl()) ||
                !tokenField.getText().equals(GlobalSettings.getInstance().getApiKey()) ||
                !defaultLanguageField.getText().equals(GlobalSettings.getInstance().getDefaultLanguage()) ||
                !modelField.getText().equals(GlobalSettings.getInstance().getModelName());
    }

    @Override
    public void apply() {
        GlobalSettings.getInstance().setBaseUrl(baseUrlField.getText());
        GlobalSettings.getInstance().setApiKey(tokenField.getText());
        GlobalSettings.getInstance().setDefaultLanguage(defaultLanguageField.getText());
        GlobalSettings.getInstance().setModelName(modelField.getText());
        AIClient.resetInstance();
    }

    @Override
    public void reset() {
        baseUrlField.setText(GlobalSettings.getInstance().getBaseUrl());
        tokenField.setText(GlobalSettings.getInstance().getApiKey());
        defaultLanguageField.setText(GlobalSettings.getInstance().getDefaultLanguage());
        modelField.setText(GlobalSettings.getInstance().getModelName());
    }
}