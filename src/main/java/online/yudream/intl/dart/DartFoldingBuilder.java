package online.yudream.intl.dart;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import online.yudream.intl.common.GlobalSettings;
import online.yudream.intl.common.L10nManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DartFoldingBuilder implements FoldingBuilder, DumbAware {

    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        DartPsiElementVisitor visitor = new DartPsiElementVisitor(descriptors);
        node.getPsi().accept(visitor);  // 通过访问器查找折叠区域
        return descriptors.toArray(new FoldingDescriptor[0]);  // 返回所有折叠区域
    }

    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        String nodeText = node.getText();

        // 匹配 S.current.xxx
        Pattern currentPattern = Pattern.compile("S\\.current\\.(\\w+)");
        Matcher currentMatcher = currentPattern.matcher(nodeText);
        if (currentMatcher.find()) {
            String key = currentMatcher.group(1);  // 获取 xxx 部分
            return L10nManager.getInstance().getLocalizations().get(key).get(GlobalSettings.getInstance().defaultLanguage);
        }

        // 匹配 S.of(.*?).xxxx
        Pattern ofPattern = Pattern.compile("S\\.of\\((.*?)\\)\\.(\\w+)");
        Matcher ofMatcher = ofPattern.matcher(nodeText);
        if (ofMatcher.find()) {
            String key = ofMatcher.group(2);
            return L10nManager.getInstance().getLocalizations().get(key).get(GlobalSettings.getInstance().defaultLanguage);
        }

        // 如果没有匹配任何规则，返回原始文本
        return nodeText;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        // Allow the default collapse behavior, can be customized based on preference
        return true;
    }
}
