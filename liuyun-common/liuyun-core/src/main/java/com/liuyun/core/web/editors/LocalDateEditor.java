package com.liuyun.core.web.editors;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateEditor
 *
 * @author W.d
 * @since 2022/7/18 14:46
 **/
public class LocalDateEditor extends PropertyEditorSupport {

    public LocalDateEditor() {
        // to do nothing
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

}