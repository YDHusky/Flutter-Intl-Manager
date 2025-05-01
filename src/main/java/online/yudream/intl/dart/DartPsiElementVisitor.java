package online.yudream.intl.dart;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DartPsiElementVisitor extends PsiElementVisitor {
    private List<FoldingDescriptor> descriptors;

    public DartPsiElementVisitor(List<FoldingDescriptor> descriptors) {
        this.descriptors = descriptors;
    }

    @Override
    public void visitElement(PsiElement element) {
        String text = element.getText();

        // 匹配 S.current.xxx 的正则表达式
        Pattern currentPattern = Pattern.compile("S\\.current\\.(\\w+)");
        Matcher currentMatcher = currentPattern.matcher(text);
        if (currentMatcher.find()) {
            // 获取匹配的文本范围
            int start = currentMatcher.start();
            int end = currentMatcher.end();
            // 创建 TextRange 来表示折叠区域
            TextRange range = new TextRange(element.getTextRange().getStartOffset() + start, element.getTextRange().getStartOffset() + end);
            // 创建折叠描述符
            FoldingDescriptor descriptor = new FoldingDescriptor(element.getNode(), range);
            descriptors.add(descriptor);
        }

        Pattern ofPattern = Pattern.compile("S\\.of\\((.*?)\\)\\.(\\w+)");
        Matcher ofMatcher = ofPattern.matcher(text);
        if (ofMatcher.find()) {
            // 获取 S.of(context) 和 .xxxx 部分的文本范围
            int start = ofMatcher.start();  // 获取 S.of 的起始位置
            int end = ofMatcher.end();  // 获取 .xxxx 的结束位置

            // 创建折叠区域
            TextRange range = new TextRange(element.getTextRange().getStartOffset() + start, element.getTextRange().getStartOffset() + end);
            FoldingDescriptor descriptor = new FoldingDescriptor(element.getNode(), range);
            descriptors.add(descriptor);  // 添加到折叠区域列表
        }

        super.visitElement(element);
    }
}
