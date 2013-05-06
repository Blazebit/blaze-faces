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
package com.blazebit.blazefaces.component.outputpanel;

import com.blazebit.blazefaces.apt.JsfAttribute;
import com.blazebit.blazefaces.apt.JsfComponent;
import com.blazebit.blazefaces.apt.JsfDescription;
import com.blazebit.blazefaces.component.BaseUIComponent;
import com.blazebit.blazefaces.component.Styleable;
import javax.faces.component.UIPanel;
import javax.faces.component.behavior.ClientBehaviorHolder;

/**
 *
 * @author Christian
 */
@JsfComponent(
		parent = UIPanel.class,
		renderer = OutputPanelRenderer.class,
		description = @JsfDescription(
				displayName = "OutputPanel",
				description = "OutputPanel is a display only element that's useful in various cases such as adding placeholders to a page."
		),
                attributes = {
                    @JsfAttribute(name = "layout", type = String.class, defaultValue = "inline", description = @JsfDescription(description = "Layout of the panel, valid values are inline(span) or block(div).")),
//                    @JsfAttribute(name = "autoUpdate", type = Boolean.class, defaultValue = "false", description = @JsfDescription(description = ""))
                }
)
public class OutputPanel extends OutputPanelBase implements BaseUIComponent, Styleable, ClientBehaviorHolder{
    
}
