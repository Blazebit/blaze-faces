/*
 * Copyright 2013 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.blazefaces.component.inputfiles;

import com.blazebit.blazefaces.apt.JsfAttribute;
import com.blazebit.blazefaces.apt.JsfComponent;
import com.blazebit.blazefaces.apt.JsfDescription;
import com.blazebit.blazefaces.component.BaseUIInput;
import com.blazebit.blazefaces.component.BaseUIOutput;
import com.blazebit.blazefaces.component.FileUpload;
import com.blazebit.blazefaces.component.Styleable;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

/**
 *
 * @author Christian
 */
@JsfComponent(
		parent = UIInput.class,
		renderer = InputFilesRenderer.class,
		description = @JsfDescription(
				displayName = "InputFiles",
				description = "InputFiles is an input component for files."
		)
)
@ResourceDependencies({
    
    @ResourceDependency(name = "css/blueimp-gallery.css", library="blueimp-gallery"),
    @ResourceDependency(name = "css/jquery.fileupload.css", library="blueimp-file-upload"),
    @ResourceDependency(name = "css/jquery.fileupload-ui.css", library="blueimp-file-upload"),
    
    @ResourceDependency(name = "js/jquery.js", library = "jquery"),
    @ResourceDependency(name = "js/jquery-ui.js", library = "jquery-ui"),
    @ResourceDependency(name = "js/jquery-ui-i18n.js", library = "jquery-ui"),
    
    @ResourceDependency(name = "js/tmpl.js", library="blueimp-templates"),
    @ResourceDependency(name = "js/load-image.js", library="blueimp-load-image"),
    @ResourceDependency(name = "js/canvas-to-blob.js", library="blueimp-canvas-to-blob"),
    @ResourceDependency(name = "js/jquery.blueimp-gallery.js", library="blueimp-gallery"),
    
    @ResourceDependency(name = "js/jquery.iframe-transport.js", library="blueimp-file-upload"),
    @ResourceDependency(name = "js/jquery.fileupload.js", library="blueimp-file-upload"),
    @ResourceDependency(name = "js/jquery.fileupload-process.js", library="blueimp-file-upload"),
    @ResourceDependency(name = "js/jquery.fileupload-image.js", library="blueimp-file-upload"),
    @ResourceDependency(name = "js/jquery.fileupload-audio.js", library="blueimp-file-upload"),
    @ResourceDependency(name = "js/jquery.fileupload-video.js", library="blueimp-file-upload"),
    @ResourceDependency(name = "js/jquery.fileupload-validate.js", library="blueimp-file-upload"),
    @ResourceDependency(name = "js/jquery.fileupload-ui.js", library="blueimp-file-upload"),
    @ResourceDependency(name = "js/jquery.fileupload-jquery-ui.js", library="blueimp-file-upload"),

    @ResourceDependency(name = "core/core.js", library = "blazefaces"),
    @ResourceDependency(name = "inputfiles/js/inputfiles.js", library = "blazefaces"),
})
public class InputFiles extends InputFilesBase implements BaseUIInput, FileUpload, Styleable, ClientBehaviorHolder{


    public void broadcast(javax.faces.event.FacesEvent event) throws javax.faces.event.AbortProcessingException {
        super.broadcast(event);
        
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        MethodExpression me = getFileUploadListener();
//        
//        if (me != null && event instanceof com.blazebit.blazefaces.event.FileUploadEvent) {
//            me.invoke(facesContext.getELContext(), new Object[] {event});
//        }
    }
}
