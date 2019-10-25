package com.cqq.document;


import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;

import java.util.Objects;

public class DocumentWrite {
    private Document document;

    public DocumentWrite(AnActionEvent anActionEvent) {
        document = Objects.requireNonNull(anActionEvent.getData(PlatformDataKeys.EDITOR)).getDocument();
    }

    public void set(String str) {
        document.setText(str);
    }

    public void replace(int first, int end, String string) {
        document.replaceString(first,end,string);
    }

    public String get() {
        return document.getText();
    }
}
