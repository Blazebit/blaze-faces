/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.web.showcase.bean;

import com.blazebit.blazefaces.event.FileUploadEvent;
import com.blazebit.blazefaces.model.UploadedFile;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Christian Beikov
 */
@ManagedBean
@ViewScoped
public class InputFileBean implements Serializable{

    private static final Logger log = LoggerFactory.getLogger(InputFileBean.class);
    private UploadedFile files[];
    
    public void preRender(ComponentSystemEvent event){
        System.out.println("Render");
    }
    
    public void fileUploadListener(FileUploadEvent event){
        System.out.println(event);
    }

    public UploadedFile[] getFiles() {
        return files;
    }

    public void setFiles(UploadedFile[] files) {
        this.files = files;
    }
    
}
