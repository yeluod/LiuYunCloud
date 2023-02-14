package com.liuyun.core.web.editors;

/**
 * FloatEditor
 *
 * @author W.d
 * @since 2022/7/18 14:44
 **/
public class FloatEditor extends NumberEditor {

    public FloatEditor() {
        // to do nothing
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : Float.valueOf(text));
    }

}
