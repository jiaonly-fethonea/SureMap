package com.sure.mapnote;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Project {
    public String id = "";
    public String name = "";
    public String mapType = "world";
    public String materialLabel = "";
    public String tileMode = "standard";
    public String geojson = "";
    public double viewLat = 20.0d;
    public double viewLng = 0.0d;
    public double viewZoom = 2.0d;
    public final List<Marker> markers = new ArrayList();
    public final List<Region> regions = new ArrayList();
    public long createdAt = 0;
    public long updatedAt = 0;

    /* loaded from: classes.dex */
    public static class Marker {
        public double lat;
        public double lng;
        public long ts;
        public String id = "";
        public String title = "";
        public String content = "";
    }

    /* loaded from: classes.dex */
    public static class Region {
        public String key = "";
        public String name = "";
        public String state = "";
    }

    public String toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("v", 1);
            jSONObject.put("id", this.id);
            Object obj = this.name;
            if (obj == null) {
                obj = "";
            }
            jSONObject.put("name", obj);
            Object obj2 = this.mapType;
            if (obj2 == null) {
                obj2 = "world";
            }
            jSONObject.put("mapType", obj2);
            Object obj3 = this.materialLabel;
            if (obj3 == null) {
                obj3 = "";
            }
            jSONObject.put("materialLabel", obj3);
            Object obj4 = this.tileMode;
            if (obj4 == null) {
                obj4 = "standard";
            }
            jSONObject.put("tileMode", obj4);
            Object obj5 = this.geojson;
            if (obj5 == null) {
                obj5 = "";
            }
            jSONObject.put("geojson", obj5);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("lat", this.viewLat);
            jSONObject2.put("lng", this.viewLng);
            jSONObject2.put("zoom", this.viewZoom);
            jSONObject.put("view", jSONObject2);
            JSONArray jSONArray = new JSONArray();
            for (Marker marker : this.markers) {
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("id", marker.id);
                jSONObject3.put("lat", marker.lat);
                jSONObject3.put("lng", marker.lng);
                jSONObject3.put("title", marker.title == null ? "" : marker.title);
                jSONObject3.put("content", marker.content == null ? "" : marker.content);
                jSONObject3.put("ts", marker.ts);
                jSONArray.put(jSONObject3);
            }
            jSONObject.put("markers", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            for (Region region : this.regions) {
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put("key", region.key);
                jSONObject4.put("name", region.name == null ? "" : region.name);
                jSONObject4.put("state", region.state == null ? "" : region.state);
                jSONArray2.put(jSONObject4);
            }
            jSONObject.put("regions", jSONArray2);
            jSONObject.put("createdAt", this.createdAt);
            jSONObject.put("updatedAt", this.updatedAt);
        } catch (Exception unused) {
        }
        return jSONObject.toString();
    }

    public static Project fromJson(String str) {
        Project project = new Project();
        if (str == null) {
            return project;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            project.id = jSONObject.optString("id");
            project.name = jSONObject.optString("name");
            project.mapType = jSONObject.optString("mapType", "world");
            project.materialLabel = jSONObject.optString("materialLabel");
            project.tileMode = jSONObject.optString("tileMode", "standard");
            project.geojson = jSONObject.optString("geojson");
            JSONObject optJSONObject = jSONObject.optJSONObject("view");
            if (optJSONObject != null) {
                project.viewLat = optJSONObject.optDouble("lat", 20.0d);
                project.viewLng = optJSONObject.optDouble("lng", 0.0d);
                project.viewZoom = optJSONObject.optDouble("zoom", 2.0d);
            }
            project.createdAt = jSONObject.optLong("createdAt");
            project.updatedAt = jSONObject.optLong("updatedAt");
            JSONArray optJSONArray = jSONObject.optJSONArray("markers");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
                    if (optJSONObject2 != null) {
                        Marker marker = new Marker();
                        marker.id = optJSONObject2.optString("id");
                        marker.lat = optJSONObject2.optDouble("lat");
                        marker.lng = optJSONObject2.optDouble("lng");
                        marker.title = optJSONObject2.optString("title");
                        marker.content = optJSONObject2.optString("content");
                        marker.ts = optJSONObject2.optLong("ts");
                        project.markers.add(marker);
                    }
                }
            }
            JSONArray optJSONArray2 = jSONObject.optJSONArray("regions");
            if (optJSONArray2 != null) {
                for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                    JSONObject optJSONObject3 = optJSONArray2.optJSONObject(i2);
                    if (optJSONObject3 != null) {
                        Region region = new Region();
                        region.key = optJSONObject3.optString("key");
                        region.name = optJSONObject3.optString("name");
                        region.state = optJSONObject3.optString("state");
                        project.regions.add(region);
                    }
                }
            }
        } catch (Exception unused) {
        }
        return project;
    }

    public String regionsJson() {
        JSONArray jSONArray = new JSONArray();
        try {
            for (Region region : this.regions) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("key", region.key);
                String str = "";
                jSONObject.put("name", region.name == null ? "" : region.name);
                if (region.state != null) {
                    str = region.state;
                }
                jSONObject.put("state", str);
                jSONArray.put(jSONObject);
            }
        } catch (Exception unused) {
        }
        return jSONArray.toString();
    }

    public String markersJson() {
        JSONArray jSONArray = new JSONArray();
        try {
            for (Marker marker : this.markers) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("id", marker.id);
                jSONObject.put("lat", marker.lat);
                jSONObject.put("lng", marker.lng);
                String str = "";
                jSONObject.put("title", marker.title == null ? "" : marker.title);
                if (marker.content != null) {
                    str = marker.content;
                }
                jSONObject.put("content", str);
                jSONObject.put("ts", marker.ts);
                jSONArray.put(jSONObject);
            }
        } catch (Exception unused) {
        }
        return jSONArray.toString();
    }
}
