package com.liuyun.core.web.editors;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTimeEditor
 *
 * @author W.d
 * @since 2022/7/18 14:46
 **/
public class LocalDateTimeEditor extends PropertyEditorSupport {

    public LocalDateTimeEditor() {
        // to do nothing
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}