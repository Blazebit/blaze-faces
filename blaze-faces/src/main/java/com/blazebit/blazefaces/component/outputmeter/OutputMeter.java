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
package com.blazebit.blazefaces.component.outputmeter;

import com.blazebit.blazefaces.apt.JsfAttribute;
import com.blazebit.blazefaces.apt.JsfComponent;
import com.blazebit.blazefaces.apt.JsfDescription;
import com.blazebit.blazefaces.component.BaseUIOutput;
import com.blazebit.blazefaces.component.Styleable;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;

/**
 *
 * @author Christian
 */
@JsfComponent(
		parent = UIOutput.class,
		renderer = OutputMeterRenderer.class,
		description = @JsfDescription(
				displayName = "OutputMeter",
				description = "OutputMeter is an output component for numbers. It makes use of the HTML5 meter-Tag and thus provides semantic information for the value."
		),
                attributes = {
                    @JsfAttribute(name = "min", type = Double.class, defaultValue = "0", description = @JsfDescription(description = "The minimum allowed value. The default value is 0")),
                    @JsfAttribute(name = "max", type = Double.class, defaultValue = "1", description = @JsfDescription(description = "The maximum allowed value. If the maximum value is less than the minimum value, then the minimum value is used as the max. If it is not specified then the value is 1.")),
                    @JsfAttribute(name = "low", type = Double.class, defaultValue = "getMin()", description = @JsfDescription(description = "This is considered to be the low part of the value range. It must be less than or equal to the value of the high attribute. Also, if the low value is less than the min value, then the low value is the same as the min value.")),
                    @JsfAttribute(name = "high", type = Double.class, defaultValue = "getMax()", description = @JsfDescription(description = "This is considered to be the high part of the value range. If the high value is less than the low boundary, then the high value is the same as the low value. Also, if the high value is greater than the max value, then the high value is the same as the max value.")),
                    @JsfAttribute(name = "optimum", type = Double.class, description = @JsfDescription(description = "This is considered to be the optimum value and needs to be somewhere between min and max. It can be greater than the high attribute"))
                }
)
@ResourceDependencies({
    @ResourceDependency(name = "core/html5.js", target = "head_lt_ie9"),
    @ResourceDependency(name = "core/innershiv.js")
})
public class OutputMeter extends OutputMeterBase implements BaseUIOutput, Styleable, ClientBehaviorHolder{
    
}
