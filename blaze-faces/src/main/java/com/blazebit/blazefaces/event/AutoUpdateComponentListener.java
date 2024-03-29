/*
 * Copyright 2011-2012 Blazebit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.blazefaces.event;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import com.blazebit.blazefaces.apt.JsfSystemEventListener;
import com.blazebit.blazefaces.apt.JsfSystemEventListeners;
import com.blazebit.blazefaces.component.AutoUpdatable;

/**
 * Registers components to auto update before rendering
 */
//@JsfSystemEventListeners({
//	@JsfSystemEventListener(source = com.blazebit.blazefaces.component.messages.Messages.class, event = PostAddToViewEvent.class),
//	@JsfSystemEventListener(source = com.blazebit.blazefaces.component.growl.Growl.class, event = PostAddToViewEvent.class),
//	@JsfSystemEventListener(source = com.blazebit.blazefaces.component.outputpanel.OutputPanel.class, event = PostAddToViewEvent.class)
//})
public class AutoUpdateComponentListener implements SystemEventListener {

    public void processEvent(SystemEvent cse) throws AbortProcessingException {
        AutoUpdatable component = (AutoUpdatable) cse.getSource();
        FacesContext context = FacesContext.getCurrentInstance();
        
        if(component.isAutoUpdate() && context.getRenderKit().getResponseStateManager().isPostback(context)) {
            context.getPartialViewContext().getRenderIds().add(component.getClientId(context));
        }
    }

    public boolean isListenerForSource(Object o) {
        return true;
    }
}