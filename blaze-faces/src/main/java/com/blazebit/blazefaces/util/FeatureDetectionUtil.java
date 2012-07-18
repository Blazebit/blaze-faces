/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.util;

import com.blazebit.blazefaces.json.JSONException;
import com.blazebit.blazefaces.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Christian Beikov
 */
public class FeatureDetectionUtil {

    private static final String FEATURES_SUBMITTED = "FEATURES_SUBMITTED";
    private static final String RESOURCE_LIBRARY = "blazefaces";
    private static final String MODERNIZER_RESOURCE_NAME = "core/modernizr.js";
    private static final String DETECT_RESOURCE_NAME = "core/detect.js";

    public static void retrieveFeatures(FacesContext fc) {
        ExternalContext ec = fc.getExternalContext();
        Map<String, Object> sessionMap = ec.getSessionMap();

        if (!sessionMap.containsKey(FEATURES_SUBMITTED)) {
            try {
                String features = ec.getRequestParameterMap().get("features");

                if (features == null) {
                    return;
                }

                JSONObject json = new JSONObject(features);
                Map<String, Boolean> featureMap = new HashMap<String, Boolean>();
                Iterator keys = json.keys();

                while (keys.hasNext()) {
                    String name = keys.next().toString();
                    JSONObject obj = json.optJSONObject(name);

                    if (obj != null) {
                        Iterator keys2 = obj.keys();

                        while (keys2.hasNext()) {
                            String name2 = keys2.next().toString();
                            String support = obj.optString(name2, null);

                            if (support != null && !support.isEmpty() && !"false".equalsIgnoreCase(support)) {
                                featureMap.put(name + "." + name2, Boolean.TRUE);
                            } else {
                                featureMap.put(name + "." + name2, obj.optBoolean(name2, false));
                            }
                        }
                    } else {
                        featureMap.put(name, json.getBoolean(name));
                    }
                }

                sessionMap.put(FEATURES_SUBMITTED, featureMap);
            } catch (JSONException ex) { /* ignore */ }
        }
    }

    public static Map<String, Boolean> getFeatures() {
        return getFeatures(FacesContext.getCurrentInstance());
    }

    public static Map<String, Boolean> getFeatures(FacesContext context) {
        return (Map<String, Boolean>) context.getExternalContext().getSessionMap().get(FEATURES_SUBMITTED);
    }

    public static boolean isSupported(String feature) {
        return isSupported(getFeatures(), feature);
    }

    public static boolean isSupported(FacesContext context, String feature) {
        return isSupported(getFeatures(context), feature);
    }

    private static boolean isSupported(Map<String, Boolean> features, String feature) {
        if(features == null)
            return false;
        Boolean support = features.get(feature);
        return support != null && support.booleanValue();
    }

    public static void encodeFeatures(FacesContext context) throws IOException {
        if (!context.getExternalContext().getSessionMap().containsKey(FEATURES_SUBMITTED)) {
            RendererUtil.encodeScript(context, RESOURCE_LIBRARY, MODERNIZER_RESOURCE_NAME);
            RendererUtil.encodeScript(context, RESOURCE_LIBRARY, DETECT_RESOURCE_NAME);
        }
    }
}
