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
package com.blazebit.blazefaces.component;

import com.blazebit.blazefaces.apt.JsfAttribute;
import com.blazebit.blazefaces.apt.JsfComponent;
import com.blazebit.blazefaces.apt.JsfDescription;
import javax.faces.component.UIComponent;

/**
 *
 * @author Christian
 */
@JsfComponent
public interface BaseUIComponent {
    
    @JsfAttribute(description = @JsfDescription(description = "Unique identifier of the component in a namingContainer."))
    public String getId();
    public void setId(String id);
    
    @JsfAttribute(description = @JsfDescription(description = "Boolean value to specify the rendering of the component, when set to false component will not be rendered."))
    public boolean isRendered();
    public void setRendered(boolean rendered);
    
    @JsfAttribute(description = @JsfDescription(description = "An el expression referring to a server side UIComponent instance in a backing bean."))
    public UIComponent getBinding();
    public void setBinding(UIComponent binding);
}
