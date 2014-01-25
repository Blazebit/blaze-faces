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

/**
 *
 * @author Christian
 */
@JsfComponent
public interface FileUpload {
    
    @JsfAttribute(description = @JsfDescription(description = "A comma-separated list of allowed file extensions. By default no restriction is applied."))
    public String getAllowedExtensions();
    public void setAllowedExtensions(String allowedExtensions);
    
    @JsfAttribute(defaultValue = "0", description = @JsfDescription(description = "The maximum allowed file size. By default no restriction is applied to the file size."))
    public long getMaxFileSize();
    public void setMaxFileSize(long maxFileSize);
    
}
