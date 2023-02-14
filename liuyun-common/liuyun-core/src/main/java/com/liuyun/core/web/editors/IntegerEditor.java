package com.liuyun.core.web.editors;

/**
 * IntegerEditor
 *
 * @author W.d
 * @since 2022/7/18 14:45
 **/
public class IntegerEditor extends NumberEditor {

    public IntegerEditor() {
        // to do nothing
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : Integer.decode(text));
    }

}
