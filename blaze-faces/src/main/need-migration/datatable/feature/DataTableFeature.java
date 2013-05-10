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
package com.blazebit.blazefaces.component.datatable.feature;

import java.io.IOException;
import javax.faces.context.FacesContext;
import com.blazebit.blazefaces.component.datatable.DataTable;
import com.blazebit.blazefaces.component.datatable.DataTableRenderer;

public interface DataTableFeature {
    
    public boolean shouldDecode(FacesContext context, DataTable table);
    
    public boolean shouldEncode(FacesContext context, DataTable table);
    
    public void decode(FacesContext context, DataTable table);
    
    public void encode(FacesContext context, DataTableRenderer renderer, DataTable table) throws IOException;
}