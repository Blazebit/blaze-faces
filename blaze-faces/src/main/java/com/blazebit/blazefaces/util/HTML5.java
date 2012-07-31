/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.util;

public class HTML5 {
    
    /**
     * The global attributes except id and class
     */
    public static final String[] COMMON_TAG_ATTRIBUTES = {
        "accesskey",
        "contenteditable",
        "contextmenu",
        "dir",
        "draggable",
        "dropzone",
        "hidden",
        "lang",
        "spellcheck",
        "style",
        "tabindex",
        "title",
        "itemid",
        "itemtype",
        "itemscope",
        "itemprop"
    };
    
    public static final String[] SHARED_EVENT_ATTRIBUTES = {
        "onblur",
        "onfocus"
    };
            
    public static final String[] WINDOW_EVENT_ATTRIBUTES = ArrayUtils.concat(SHARED_EVENT_ATTRIBUTES, new String[]{
        "onafterprintNew",
        "onbeforeprintNew",
        "onbeforeonloadNew",
        "onerrorNew",
        "onhaschangeNew",
        "onload",
        "onmessageNew",
        "onofflineNew",
        "ononlineNew",
        "onpagehideNew",
        "onpageshowNew",
        "onpopstateNew",
        "onredoNew",
        "onresizeNew",
        "onstorageNew",
        "onundoNew",
        "onunloadNew"
    });
    
    public static final String[] FORM_EVENT_ATTRIBUTES = ArrayUtils.concat(SHARED_EVENT_ATTRIBUTES, new String[]{
        "onchange",
        "oncontextmenuNew",
        "onformchangeNew",
        "onforminputNew",
        "oninputNew",
        "oninvalidNew",
        "onselect",
        "onsubmit"
    });
    
    public static final String[] KEYBOARD_EVENT_ATTRIBUTES = {
        "onkeydown",
        "onkeypress",
        "onkeyup"
    };
    
    public static final String[] MOUSE_EVENT_ATTRIBUTES = {
        "onclick",
        "ondblclick",
        "ondragNew",
        "ondragendNew",
        "ondragenterNew",
        "ondragleaveNew",
        "ondragoverNew",
        "ondragstartNew",
        "ondropNew",
        "onmousedown",
        "onmousemove",
        "onmouseout",
        "onmouseover",
        "onmouseup",
        "onmousewheelNew",
        "onscrollNew"
    };
    
    public static final String[] COMMON_EVENT_ATTRIBUTES = ArrayUtils.concat(KEYBOARD_EVENT_ATTRIBUTES, MOUSE_EVENT_ATTRIBUTES);
    
    public static final String[] COMMON_ATTRIBUTES = ArrayUtils.concat(COMMON_TAG_ATTRIBUTES, COMMON_EVENT_ATTRIBUTES);
    
    public static final String[] MEDIA_EVENT_ATTRIBUTES = {
        "onabort",
        "oncanplayNew",
        "oncanplaythroughNew",
        "ondurationchangeNew",
        "onemptiedNew",
        "onendedNew",
        "onerrorNew",
        "onloadeddataNew",
        "onloadedmetadataNew",
        "onloadstartNew",
        "onpauseNew",
        "onplayNew",
        "onplayingNew",
        "onprogressNew",
        "onratechangeNew",
        "onreadystatechangeNew",
        "onseekedNew",
        "onseekingNew",
        "onstalledNew",
        "onsuspendNew",
        "ontimeupdateNew",
        "onvolumechangeNew",
        "onwaitingNew"
    };
    
    public static final String[] METER_ELEMENT_ATTRIBUTES = {
        "min",
        "max",
        "value",
        "low",
        "high",
        "optimum"
    };
}
