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
package com.blazebit.blazefaces.component.paginator;

import java.io.IOException;
import javax.faces.context.FacesContext;
import com.blazebit.blazefaces.component.UIData;

public class FirstPageLinkRenderer extends PageLinkRenderer implements PaginatorElementRenderer {

    public void render(FacesContext context, UIData uidata) throws IOException {
        boolean disabled = uidata.getPage() == 0;
       
        super.render(context, uidata, UIData.PAGINATOR_FIRST_PAGE_LINK_CLASS, UIData.PAGINATOR_FIRST_PAGE_ICON_CLASS, disabled);
    }
}