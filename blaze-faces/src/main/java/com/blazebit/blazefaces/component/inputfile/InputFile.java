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
package com.blazebit.blazefaces.component.inputfile;

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
		renderer = InputFileRenderer.class,
		description = @JsfDescription(
				displayName = "InputFile",
				description = "InputFile is an input component for files."
		)
)
//@ResourceDependencies({
//    @ResourceDependency(name = "core/html5.js", target = "head_lt_ie9"),
//    @ResourceDependency(name = "core/innershiv.js")
//})
public class InputFile extends InputFileBase implements BaseUIInput, FileUpload, Styleable, ClientBehaviorHolder{


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
