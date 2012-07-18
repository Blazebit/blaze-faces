/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.web.showcase.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Christian Beikov
 */
@ManagedBean
@ViewScoped
public class IncludeBean implements Serializable {
    
    private Map<String, List<String>> includeMap = new HashMap<String, List<String>>();
    private List<String> dataList;
    private String text;
    private String secret;
    private String selected = "a0";

    public IncludeBean() {
        dataList = new ArrayList<String>();
        dataList.add("a0");
        dataList.add("a1");
        dataList.add("a2");
        dataList.add("a3");
        
        List<String> list = new ArrayList<String>();
        list.add("/includeInputText.xhtml");
        list.add("/includeInputPassword.xhtml");
        includeMap.put("a0", list);
        includeMap.put("a3", list);
        
        list = new ArrayList<String>();
        list.add("/includeInputText.xhtml");
        includeMap.put("a1", list);
        includeMap.put("a2", list);
    }

    public List<String> getIncludeList() {
        return includeMap.get(selected);
    }

    public void setIncludeList(List<String> includeList) {
        this.includeMap.put(selected, includeList);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public void action(ActionEvent event){
        System.out.println("action!!");
        System.out.println(text);
        System.out.println(secret);
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }
    
    public void valueChanged(ValueChangeEvent event){
        System.out.println("Value changed");
    }
    
}
