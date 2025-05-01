package online.yudream.intl.windows;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import online.yudream.intl.ui.IntlManagerUI;
import org.jetbrains.annotations.NotNull;

public class IntlManagerWindows implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        IntlManagerUI intlViewerPanel = new IntlManagerUI(project);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(intlViewerPanel.getMainPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
