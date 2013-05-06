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
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;

/**
 *
 * @author Christian
 */
@JsfComponent
public interface BaseUIOutput extends BaseUIComponent, ValueHolder {
    
    /**
     * {@inheritDoc}
     */
    @JsfAttribute(description = @JsfDescription(description = "Value of the component."))
    @Override
    public Object getValue();

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Object value);

    /**
     * {@inheritDoc}
     */
    @JsfAttribute(description = @JsfDescription(description = "An el expression or a literal text that defines a converter for the component. When it's an EL expression, it's resolved to a converter instance. \n In case it's a static text, it must refer to a converter id."))
    @Override
    public Converter getConverter();

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConverter(Converter converter);
}
