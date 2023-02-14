package com.liuyun.core.web.editors;

/**
 * LongEditor
 *
 * @author W.d
 * @since 2022/7/18 14:45
 **/
public class LongEditor extends NumberEditor {

    public LongEditor() {
        // to do nothing
    }

    @Override
    public String getJavaInitializationString() {
        Object var1 = this.getValue();
        return var1 != null ? var1 + "L" : "null";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : Long.decode(text));
    }

}
