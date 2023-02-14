package com.liuyun.core.web.editors;

import java.beans.PropertyEditorSupport;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalTimeEditor
 *
 * @author W.d
 * @since 2022/7/18 14:46
 **/
public class LocalTimeEditor extends PropertyEditorSupport {

    public LocalTimeEditor() {
        // to do nothing
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

}