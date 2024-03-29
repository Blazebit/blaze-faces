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
package com.blazebit.blazefaces.model;

import java.util.List;
import javax.faces.component.UIComponent;

//import com.blazebit.blazefaces.component.menuitem.MenuItem;
//import com.blazebit.blazefaces.component.submenu.Submenu;

public interface MenuModel {

	public List<UIComponent> getContents();
	
//	public void addSubmenu(Submenu submenu);
//	
//	public void addMenuItem(MenuItem menuItem);
}