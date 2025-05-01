package online.yudream.intl.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import online.yudream.intl.common.AIClient;
import online.yudream.intl.common.L10nManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AddIntlValueDialog extends DialogWrapper {
    private final JBTextField keyField;
    private final JBTextField valueField;
    private final Project project;
    private String key;

    protected AddIntlValueDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        setTitle("添加Intl Value");
        keyField = new JBTextField(40);
        valueField = new JBTextField(40);
        init();
    }

    public AddIntlValueDialog(@Nullable Project project, @NotNull String key) {
        super(project);
        this.project = project;
        keyField = new JBTextField(40);
        valueField = new JBTextField(40);
        valueField.setText(key);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); // 使用 GridBagLayout 布局
        GridBagConstraints gbc = new GridBagConstraints();


        // 设置约束参数
        gbc.insets = JBUI.insets(5); // 边距
        gbc.anchor = GridBagConstraints.WEST; // 锚点
        gbc.fill = GridBagConstraints.HORIZONTAL; // 填充方式

        // 添加 Key 标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Key:"), gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 输入框水平填充
        panel.add(keyField, gbc);

        // 添加 Value 标签和输入框
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE; // 标签不填充
        panel.add(new JLabel("Value:"), gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 输入框水平填充
        panel.add(valueField, gbc);
        return panel;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        new Thread(() -> {
            key = keyField.getText();
            String value = valueField.getText();
            Map<String, String> intlValue = AIClient.getInstance().getTr(L10nManager.getInstance(project).getLanguages(), value);
            L10nManager.getInstance(project).addValue(key, intlValue);
            IntlManagerUI.getInstance().refreshTable();
        }).start();
    }

    public String getKey() {
        return key;
    }
}