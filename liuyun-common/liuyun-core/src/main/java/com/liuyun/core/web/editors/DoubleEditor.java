package com.liuyun.core.web.editors;

/**
 * DoubleEditor
 *
 * @author W.d
 * @since 2022/7/18 14:44
 **/
public class DoubleEditor extends NumberEditor {

    public DoubleEditor() {
        // to do nothing
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : Double.valueOf(text));
    }


}
