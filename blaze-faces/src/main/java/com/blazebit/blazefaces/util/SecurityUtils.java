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
package com.blazebit.blazefaces.util;

import java.security.Principal;

import javax.faces.context.FacesContext;

import com.blazebit.blazefaces.apt.JsfFunction;

public class SecurityUtils {

	private SecurityUtils() {}
	
	@JsfFunction
	public static boolean ifGranted(String role) {
		return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role);
	}

	@JsfFunction
	public static boolean ifAllGranted(String value) {
		String[] roles = value.split(",");
		boolean isAuthorized = false;
		
		for(String role : roles) {
			if(ifGranted(role.trim())) {
				isAuthorized = true;
			} else {
				isAuthorized = false;
				break;
			}
		}
		
		return isAuthorized;
	}

	@JsfFunction
	public static boolean ifAnyGranted(String value) {
		String[] roles = value.split(",");
		boolean isAuthorized = false;
		
		for(String role : roles) {
			if(ifGranted(role.trim())) {
				isAuthorized = true;
				break;
			}
		}
		
		return isAuthorized;
	}

	@JsfFunction
	public static boolean ifNoneGranted(String value) {
		String[] roles = value.split(",");
		boolean isAuthorized = false;
		
		for(String role : roles) {
			if(ifGranted(role.trim())) {
				isAuthorized = false;
				break;
			} else {
				isAuthorized = true;
			}
		}
		
		return isAuthorized;
	}
	
	@JsfFunction
	public static String remoteUser() {
		return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
	}

	@JsfFunction
	public static Principal userPrincipal() {
		return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
	}
}