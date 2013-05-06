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
package com.blazebit.blazefaces.behavior;

import com.blazebit.blazefaces.apt.JsfAttribute;
import com.blazebit.blazefaces.apt.JsfBehavior;
import com.blazebit.blazefaces.apt.JsfDescription;

/**
 *
 * @author Christian
 */
@JsfBehavior
public interface BaseBehavior {
    
    @JsfAttribute(description = @JsfDescription(description = "Client side event to trigger the behavior. Default value is defined by parent ClientBehaviorHolder component the behavior is attached to."))        
    public String getEvent();
    public void setEvent(String event);
    
    @JsfAttribute(defaultValue = "false", description = @JsfDescription(description = "Disables the behavior."))
    public boolean isDisabled();
    public void setDisabled(boolean rendered);
}
