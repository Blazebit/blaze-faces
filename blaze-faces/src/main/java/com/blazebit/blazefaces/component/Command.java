/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component;

/**
 *
 * @author RedShadow
 */
public interface Command {

    public String getAccesskey();

    public void setAccesskey(String accesskey);

    public String getDir();

    public void setDir(String dir);

    public boolean isDisabled();

    public void setDisabled(boolean disabled);

    public String getOnblur();

    public void setOnblur(String onblur);

    public String getOnclick();

    public void setOnclick(String onclick);

    public String getOndblclick();

    public void setOndblclick(String ondblclick);

    public String getOnfocus();

    public void setOnfocus(String onfocus);

    public String getOnkeydown();

    public void setOnkeydown(String onkeydown);

    public String getOnkeypress();

    public void setOnkeypress(String onkeypress);

    public String getOnkeyup();

    public void setOnkeyup(String onkeyup);

    public String getOnmousedown();

    public void setOnmousedown(String onmousedown);

    public String getOnmousemove();

    public void setOnmousemove(String onmousemove);

    public String getOnmouseout();

    public void setOnmouseout(String onmouseout);

    public String getOnmouseover();

    public void setOnmouseover(String onmouseover);

    public String getOnmouseup();

    public void setOnmouseup(String onmouseup);

    public String getStyle();

    public void setStyle(String style);

    public String getStyleClass();

    public void setStyleClass(String styleClass);

    public String getTabindex();

    public void setTabindex(String tabindex);

    public String getTitle();

    public void setTitle(String title);
}
