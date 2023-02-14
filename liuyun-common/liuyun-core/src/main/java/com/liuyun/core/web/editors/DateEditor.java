package com.liuyun.core.web.editors;

import lombok.SneakyThrows;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;

/**
 * DateEditor
 *
 * @author W.d
 * @since 2022/7/18 14:43
 **/
public class DateEditor extends PropertyEditorSupport {

    public DateEditor() {
        // to do nothing
    }

    @Override
    @SneakyThrows
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text));
    }


}
