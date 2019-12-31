package com.cqq.action;

import com.cqq.document.DocumentWrite;
import com.cqq.util.TranslateUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class QuicklyTranslateAction extends AnAction {

    public static final String APPLICATION = "QuicklyTranslate";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Editor editor = PlatformDataKeys.EDITOR.getData(e.getDataContext());
        if (editor != null) {
            CaretModel caretModel = editor.getCaretModel();
            List<Caret> allCarets = caretModel.getAllCarets();
            allCarets.forEach(caret -> {
                final int start = caret.getSelectionStart();
                final int end = caret.getSelectionEnd();
                DocumentWrite documentWrite = new DocumentWrite(e);
                String content = documentWrite.get();
                String target = content.substring(start, end);
                System.out.println(target);
                List<String> chinese = TranslateUtil.getTranslateList(target);
                if (chinese.isEmpty()) {
                    Messages.showErrorDialog("找不到翻译的结果", APPLICATION);
                } else {
//                    String replaceContent = content.substring(0, start) + chinese.get(0) + content.substring(end);
                    CommandProcessor.getInstance().runUndoTransparentAction(() -> {

                        Application app = ApplicationManager.getApplication();
                        app.runWriteAction(() -> {
                            editor.getDocument().replaceString(start, end, chinese.get(0));
                        });
                        CommandProcessor.getInstance().markCurrentCommandAsGlobal(getEventProject(e));

                    });
                }
            });
        }

    }
}
