package online.yudream.intl.action;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import online.yudream.intl.ui.AddIntlValueDialog;
import online.yudream.intl.utils.NoticeUtils;
import org.jetbrains.annotations.NotNull;

public class AddIntlValueAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
// 获取编辑器对象
        Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);

        // 获取当前的 Caret 对象
        Caret caret = editor.getCaretModel().getPrimaryCaret();

        // 检查是否有选中的文本
        if (caret.hasSelection()) {
            // 获取选中的文本
            String selectedText = caret.getSelectedText();
            if (selectedText == null) {
                selectedText = "";
            }
            if (selectedText.length() >= 2 &&
                    (selectedText.charAt(0) == '"' && selectedText.charAt(selectedText.length() - 1) == '"' ||
                            selectedText.charAt(selectedText.length() - 1) == '\'' && selectedText.charAt(0) == '"')) {


                selectedText = selectedText.substring(1, selectedText.length() - 1);
                Project project = anActionEvent.getProject();
                AddIntlValueDialog dialog = new AddIntlValueDialog(project, selectedText);
                boolean ok = dialog.showAndGet();
                if (ok) {
                    String key = dialog.getKey();
                    String replacementText = "S.current." + key;
                    editor.getDocument().replaceString(caret.getSelectionStart(), caret.getSelectionEnd(), replacementText);
                    NoticeUtils.showNotification("替换内容成功!", NotificationType.INFORMATION);
                }
            } else {
                NoticeUtils.showNotification("选中内容需要包含字符串!", NotificationType.ERROR);

            }

        } else {
            NoticeUtils.showNotification("未选中内容!", NotificationType.ERROR);
        }
    }
}
