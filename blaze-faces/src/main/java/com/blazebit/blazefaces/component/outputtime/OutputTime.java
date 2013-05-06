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
package com.blazebit.blazefaces.component.outputtime;

import com.blazebit.blazefaces.apt.JsfAttribute;
import com.blazebit.blazefaces.apt.JsfComponent;
import com.blazebit.blazefaces.apt.JsfDescription;
import com.blazebit.blazefaces.component.BaseUIOutput;
import com.blazebit.blazefaces.component.Styleable;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;

import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Christian
 */
@JsfComponent(
		parent = UIOutput.class,
		renderer = OutputTimeRenderer.class,
		description = @JsfDescription(
				displayName = "OutputTime",
				description = "OutputTime is an output component for date/time values. It makes use of the HTML5 time-Tag and can also print pretty times like \"5 minutes ago\" based on the given locale."
		),
                attributes = {
                    @JsfAttribute(name = "pubdate", type = Boolean.class, defaultValue = "false", description = @JsfDescription(description = "If the time element is the publication date of the document or of an article element that contains it, set this to true.")),
                    @JsfAttribute(name = "value", description = @JsfDescription(description = "The date time value to be displayed.")),
                    @JsfAttribute(name = "pretty", type = Boolean.class, defaultValue = "false", description = @JsfDescription(description = "Indicates whether the time should be printed as human readable text or as normal date.")),
                    @JsfAttribute(name = "pattern", type = String.class, description = 
                        @JsfDescription(description = "The pattern attribute sets the custom formatting pattern which determines how the date/time string should be formatted and parsed. \n" +
                                                    "                <table border=\"1\">\n" +
                                                    "                        <tbody>\n" +
                                                    "                        <tr>\n" +
                                                    "                                <th align=\"left\">Pattern</th>\n" +
                                                    "                                <th align=\"left\">Result</th>\n" +
                                                    "                        </tr>\n" +
                                                    "                                <tr><td>yyyy.MM.dd 'at' HH:mm:ss z</td><td>2006.01.01  at 10:05:30 EST </td></tr>\n" +
                                                    "                                <tr bgcolor=\"#eeeeff\"><td>EEE, MMM d, ''yy</td><td>Sun, Jan 1, '06 </td></tr>\n" +
                                                    "                                <tr><td>h:mm a</td><td>10:05 AM </td></tr><tr bgcolor=\"#eeeeff\"><td>hh 'o''clock' a, zzzz</td><td>10 o'clock AM, Eastern Standard Time </td></tr>\n" +
                                                    "                                <tr><td>EEE, d MMM yyyy HH:mm:ss Z </td><td>Sun, 1 Jan 2006 10:05:30 -0500 </td></tr>\n" +
                                                    "                        </tbody>\n" +
                                                    "                </table>\n" +
                                                    "                For more examples of date formatting patterns, please see the  Java API documentation for java.text.SimpleDateFormat.")),
                    @JsfAttribute(name = "timeZone", type = TimeZone.class, description = 
                        @JsfDescription(description = "This attribute sets the time zone for which to interpret date/time information.\n" +
                                                    "The value must be either a value-binding expression that evaluates to a java.util.TimeZone instance, " +
                                                    "or a String that is a timezone ID as per the Java API documentation for java.util.TimeZone.getTimeZone().")),
                    @JsfAttribute(name = "locale", type = Locale.class, description = 
                        @JsfDescription(description = "The locale attribute sets the language, country, and variant for formatting locale-sensitive data such as numbers and dates.\n" +
                                                    "If not specified, the Locale returned by FacesContext.getViewRoot().getLocale() will be used. Valid expressions must evaluate to a java.util.Locale.")),
                    @JsfAttribute(name = "type", type = String.class, defaultValue = "date", description = 
                        @JsfDescription(description = "This attributes specifies what type of information the string to be formatted or parsed will contain.\n" +
                                                    "The valid values for this attribute are \"date\", \"time\", and \"both\". The default value is \"date\".")),
                    @JsfAttribute(name = "dateStyle", type = String.class, defaultValue="default", description = 
                        @JsfDescription(description = "This attribute sets a predefined formatting style which determines how a date string is to be formatted. It only applies the type attribute is \"date\" or \"both\".\n" +
                                                    "                    Valid values for this attribute are:\n" +
                                                    "                    \n" +
                                                    "                    <table width=\"350\" border=\"1\">\n" +
                                                    "                        <tbody>\n" +
                                                    "                            <tr>\n" +
                                                    "                                <th align=\"left\">Date Style </th>\n" +
                                                    "                                <th align=\"left\">Example</th>\n" +
                                                    "                            </tr>\n" +
                                                    "                            <tr>\n" +
                                                    "                                <td align=\"left\">default</td>\n" +
                                                    "                                <td nowrap=\"\" align=\"left\"><span>Jan 1, 2006 1:20:45 PM</span></td>\n" +
                                                    "                            </tr>\n" +
                                                    "                            <tr>\n" +
                                                    "                                <td align=\"left\">short</td>\n" +
                                                    "                                <td align=\"left\"><span>1/1/06 1:20:45 PM</span></td>\n" +
                                                    "                            </tr>\n" +
                                                    "                            <tr>\n" +
                                                    "                                <td align=\"left\">medium</td>\n" +
                                                    "                                <td align=\"left\"><span>Jan 1, 2006 1:20:45 PM</span></td>\n" +
                                                    "                            </tr>\n" +
                                                    "                            <tr>\n" +
                                                    "                                <td align=\"left\">long</td>\n" +
                                                    "                                <td align=\"left\"><span>January 1, 2006 1:20:45 PM</span></td>\n" +
                                                    "                            </tr>\n" +
                                                    "                            <tr>\n" +
                                                    "                                <td align=\"left\">full</td>\n" +
                                                    "                                <td align=\"left\"><span>Sunday, January 1, 2006 1:20:45 PM</span></td>\n" +
                                                    "                            </tr>\n" +
                                                    "                        </tbody>\n" +
                                                    "                    </table>")),
                    @JsfAttribute(name = "timeStyle", type = String.class, defaultValue="default", description = 
                        @JsfDescription(description = "The timeStyle attribute sets a predefined formatting style which determines how a time string is to be formatted and parsed. Applied only if the type attribute is \"time\" or \"both\".\n" +
                                                    "                Valid values are:\n" +
                                                    "\n" +
                                                    "                <table width=\"350\" border=\"1\">\n" +
                                                    "                        <tbody>\n" +
                                                    "                                <tr>\n" +
                                                    "                                        <th align=\"left\">Date Style </th>\n" +
                                                    "                                        <th align=\"left\">Example</th>\n" +
                                                    "                                </tr>\n" +
                                                    "                                <tr>\n" +
                                                    "                                        <td align=\"left\">default</td>\n" +
                                                    "                                        <td nowrap=\"\" align=\"left\"><span id=\"time7\">Jan 1, 2006 10:05:30 AM</span></td>\n" +
                                                    "                                </tr>\n" +
                                                    "                                <tr>\n" +
                                                    "                                        <td align=\"left\">short</td>\n" +
                                                    "                                        <td align=\"left\"><span id=\"time8\">1/1/06 10:05:30 AM</span></td>\n" +
                                                    "                                </tr>\n" +
                                                    "                                <tr>\n" +
                                                    "                                        <td align=\"left\">medium</td>\n" +
                                                    "                                        <td align=\"left\"><span id=\"time9\">Jan 1, 2006 10:05:30 AM</span></td>\n" +
                                                    "                                </tr>\n" +
                                                    "                                <tr>\n" +
                                                    "                                        <td align=\"left\">long</td>\n" +
                                                    "                                        <td align=\"left\"><span id=\"time10\">January 1, 2006 10:05:30 AM</span></td>\n" +
                                                    "                                </tr>\n" +
                                                    "                                <tr>\n" +
                                                    "                                        <td align=\"left\">full</td>\n" +
                                                    "                                        <td align=\"left\"><span id=\"time11\">Sunday, January 1, 2006 10:05:30 AM</span></td>\n" +
                                                    "                                </tr>\n" +
                                                    "                        </tbody>\n" +
                                                    "                </table>\n" +
                                                    "                The default value is \"default\"."))
                }
)
@ResourceDependencies({
    @ResourceDependency(name = "core/html5.js", target = "head_lt_ie9"),
    @ResourceDependency(name = "core/innershiv.js")
})
public class OutputTime extends OutputTimeBase implements BaseUIOutput, Styleable, ClientBehaviorHolder{
        
}
