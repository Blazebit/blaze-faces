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
package com.blazebit.blazefaces.webapp;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.Application;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import com.blazebit.blazefaces.apt.JsfSystemEventListener;
import com.blazebit.blazefaces.util.Constants;

/**
 *
 * Displays BlazeFaces version information on startup
 */
@JsfSystemEventListener(source = Application.class, event = PostConstructApplicationEvent.class)
public class PostConstructApplicationEventListener implements SystemEventListener {

    private final static Logger logger = Logger.getLogger(PostConstructApplicationEventListener.class.getName());

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        logger.log(Level.INFO, "Running on BlazeFaces {0}", Constants.VERSION);
    }
}
