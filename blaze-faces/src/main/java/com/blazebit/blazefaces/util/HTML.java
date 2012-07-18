/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.util;

public class HTML {
	
	public static String[] CLICK_EVENT = {"onclick"};

	public static String[] BLUR_FOCUS_EVENTS = {
		"onblur",
		"onfocus"
	};
	
	public static String[] CHANGE_SELECT_EVENTS = {
		"onchange",
		"onselect"
	};
	
	public static String[] COMMON_EVENTS = {
		"onclick",
		"ondblclick",
		"onkeydown",
		"onkeypress",
		"onkeyup",
		"onmousedown",
		"onmousemove",
		"onmouseout",
		"onmouseover",
		"onmouseup"
	};
	
	//StyleClass is omitted
	public static String[] IMG_ATTRS_WITHOUT_EVENTS = {
		"alt",
		"width",
		"height",
		"title",
		"dir",
		"lang",
		"ismap",
		"usemap",
		"style"
	};
	
	//StyleClass is omitted
	public static String[] LINK_ATTRS_WITHOUT_EVENTS = {
		"accesskey",
		"charset",
		"coords",
		"dir",
		"disabled",
		"hreflang",
		"rel",
		"rev",
		"shape",
		"tabindex",
		"style",
		"target",
		"title",
		"type"
	};
	
	//StyleClass is omitted
	public static String[] BUTTON_ATTRS_WITHOUT_EVENTS = {
		"accesskey",
		"alt",
		"dir",
		"disabled",
		"label",
		"lang",
		"readonly",
		"style",
		"tabindex",
		"title",
		"type"
	};
	
	//StyleClass is omitted
	public static String[] MEDIA_ATTRS = {
		"height",
		"width",
		"style"
	};
	
	public static String[] INPUT_TEXT_ATTRS_WITHOUT_EVENTS = {
		"accesskey",
		"alt",
        "autocomplete",
		"dir",
		"disabled",
		"lang",
		"maxlength",
		"readonly",
		"size",
		"style",
		"tabindex",
		"title"
	};

	public static String[] TEXTAREA_ATTRS = {
		"cols",
		"rows"
	};
	
	public static String[] LINK_EVENTS = ArrayUtil.concat(COMMON_EVENTS, BLUR_FOCUS_EVENTS);
	
	public static String[] BUTTON_EVENTS = ArrayUtil.concat(LINK_EVENTS, CHANGE_SELECT_EVENTS);
	
	public static String[] IMG_ATTRS = ArrayUtil.concat(IMG_ATTRS_WITHOUT_EVENTS, COMMON_EVENTS);
	
	public static String[] LINK_ATTRS = ArrayUtil.concat(LINK_ATTRS_WITHOUT_EVENTS, LINK_EVENTS);
	
	public static String[] BUTTON_ATTRS = ArrayUtil.concat(BUTTON_ATTRS_WITHOUT_EVENTS, BUTTON_EVENTS);	
	
	public static final String[] INPUT_TEXT_ATTRS = ArrayUtil.concat(INPUT_TEXT_ATTRS_WITHOUT_EVENTS, COMMON_EVENTS, CHANGE_SELECT_EVENTS, BLUR_FOCUS_EVENTS);

    public static final String[] INPUT_TEXTAREA_ATTRS = ArrayUtil.concat(INPUT_TEXT_ATTRS, TEXTAREA_ATTRS);
}
