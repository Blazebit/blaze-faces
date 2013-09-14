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

import javax.faces.component.EditableValueHolder;

/**
 *
 * @author Christian
 */
@JsfComponent
public interface BaseUIInput extends BaseUIOutput, EditableValueHolder {

    /**
     * {@inheritDoc}
     */
    @JsfAttribute(defaultValue = "false", description = @JsfDescription(description = "When evaluates to true, a value for this component is required on a post-back. Default is false"))
    @Override
    public boolean isRequired();

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRequired(boolean required);

    /**
     * {@inheritDoc}
     */
    @JsfAttribute(defaultValue = "false", description = @JsfDescription(description = "When set true, process validations logic is executed at apply request values phase for this component. Default is false."))
    @Override
    public boolean isImmediate();

    /**
     * {@inheritDoc}
     */
    @Override
    public void setImmediate(boolean immediate);

    @JsfAttribute(defaultValue = "false", description = @JsfDescription(description = "When set true, the component is disabled. Default is false."))
    public boolean isDisabled();

    public void setDisabled(boolean immediate);
    
    /*

    <attribute>
        <name>validator</name>
        <required>false</required>
        <type>javax.faces.validator.Validator</type>
        <ignoreInComponent>true</ignoreInComponent>
        <description>A method expression referring to a method validationg the
            input.</description>
    </attribute>
    <attribute>
        <name>valueChangeListener</name>
        <required>false</required>
        <type>javax.faces.event.ValueChangeListener</type>
        <ignoreInComponent>true</ignoreInComponent>
        <method-signature>void valueChange(javax.faces.event.ValueChangeEvent)</method-signature>
        <description>A method binding expression referring to a method for
            handling a valuchangeevent.</description>
    </attribute>
    <attribute>
        <name>requiredMessage</name>
        <required>false</required>
        <type>java.lang.String</type>
        <ignoreInComponent>true</ignoreInComponent>
        <description>Message to display when required field validation fails.</description>
    </attribute>
    <attribute>
        <name>converterMessage</name>
        <required>false</required>
        <type>java.lang.String</type>
        <ignoreInComponent>true</ignoreInComponent>
        <description>Message to display when conversion fails.</description>
    </attribute>
    <attribute>
        <name>validatorMessage</name>
        <required>false</required>
        <type>java.lang.String</type>
        <ignoreInComponent>true</ignoreInComponent>
        <description>Message to display when validation fails.</description>
    </attribute>
    
     */
}
