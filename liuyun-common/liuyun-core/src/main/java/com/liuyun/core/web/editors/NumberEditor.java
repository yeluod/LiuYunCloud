package com.liuyun.core.web.editors;

import java.beans.PropertyEditorSupport;

/**
 * NumberEditor
 *
 * @author W.d
 * @since 2022/7/18 14:44
 **/
public class NumberEditor extends PropertyEditorSupport {

    public NumberEditor() {
        // to do nothing
    }

    @Override
    public String getJavaInitializationString() {
        Object var1 = this.getValue();
        return var1 != null ? var1.toString() : "null";
    }

}
