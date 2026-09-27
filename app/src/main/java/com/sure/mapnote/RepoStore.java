package com.sure.mapnote;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class RepoStore {
    public static final String DEFAULT_GROUP = "素材";
    public static final String TYPE_MATERIAL = "material";
    public static final String TYPE_PROJECT = "project";

    /* loaded from: classes.dex */
    public static class Entry {
        public String id = "";
        public String name = "";
        public String group = RepoStore.DEFAULT_GROUP;
        public String type = RepoStore.TYPE_PROJECT;
        public String file = "";
        public long ts = 0;
    }

    private RepoStore() {
    }

    private static File repoDir(Context context) {
        return new File(context.getExternalFilesDir(null), "repo");
    }

    private static File filesDir(Context context) {
        return new File(repoDir(context), "files");
    }

    private static File indexFile(Context context) {
        return new File(repoDir(context), "index.json");
    }

    private static void ensure(Context context) {
        repoDir(context).mkdirs();
        filesDir(context).mkdirs();
        if (indexFile(context).exists()) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(DEFAULT_GROUP);
            jSONObject.put("groups", jSONArray);
            jSONObject.put("entries", new JSONArray());
            writeString(indexFile(context), jSONObject.toString());
        } catch (Exception unused) {
        }
    }

    public static File entryFile(Context context, Entry entry) {
        return new File(filesDir(context), entry.file);
    }

    public static String readEntryContent(Context context, Entry entry) {
        return readString(entryFile(context, entry));
    }

    public static String readRawFile(Context context, String str) {
        ensure(context);
        return readString(new File(filesDir(context), str));
    }

    public static void seedDefaults(Context context) {
        ensure(context);
        if (Prefs.isSeeded(context)) {
            return;
        }
        String[][] strArr = {new String[]{"世界地图", "regions/world-countries.geojson"}, new String[]{"中国地图", "regions/china-provinces.geojson"}, new String[]{"美国地图", "regions/usa-states.geojson"}, new String[]{"日本地图", "regions/japan-prefectures.geojson"}};
        for (int i = 0; i < 4; i++) {
            String[] strArr2 = strArr[i];
            try {
                addEntryBytes(context, strArr2[0], DEFAULT_GROUP, TYPE_MATERIAL, Util.uuid() + ".geojson", readStream(context.getAssets().open(strArr2[1])));
            } catch (Exception unused) {
            }
        }
        Prefs.setSeeded(context, true);
    }

    private static byte[] readStream(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[8192];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    inputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (Exception unused) {
            return new byte[0];
        }
    }

    public static List<String> loadGroups(Context context) {
        ensure(context);
        ArrayList arrayList = new ArrayList();
        JSONArray optJSONArray = loadIndex(context).optJSONArray("groups");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                String optString = optJSONArray.optString(i);
                if (optString != null && optString.length() > 0 && !arrayList.contains(optString)) {
                    arrayList.add(optString);
                }
            }
        }
        if (!arrayList.contains(DEFAULT_GROUP)) {
            arrayList.add(0, DEFAULT_GROUP);
        }
        return arrayList;
    }

    public static List<Entry> loadEntries(Context context) {
        ensure(context);
        ArrayList arrayList = new ArrayList();
        JSONArray optJSONArray = loadIndex(context).optJSONArray("entries");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    Entry entry = new Entry();
                    entry.id = optJSONObject.optString("id");
                    entry.name = optJSONObject.optString("name");
                    entry.group = optJSONObject.optString("group", DEFAULT_GROUP);
                    entry.type = optJSONObject.optString("type", TYPE_PROJECT);
                    entry.file = optJSONObject.optString("file");
                    entry.ts = optJSONObject.optLong("ts");
                    arrayList.add(entry);
                }
            }
        }
        return arrayList;
    }

    public static Entry findEntry(Context context, String str) {
        for (Entry entry : loadEntries(context)) {
            if (entry.id.equals(str)) {
                return entry;
            }
        }
        return null;
    }

    public static Entry addProject(Context context, String str, String str2, String str3) {
        return addEntry(context, str, str2, TYPE_PROJECT, Util.uuid() + ".json", str3);
    }

    public static Entry addImported(Context context, String str, String str2, byte[] bArr) {
        return addEntryBytes(context, str, str2, TYPE_MATERIAL, Util.uuid() + extOf(str), bArr);
    }

    public static void updateProject(Context context, Entry entry, Project project) {
        writeString(entryFile(context, entry), project.toJson());
        JSONObject loadIndex = loadIndex(context);
        try {
            JSONArray optJSONArray = loadIndex.optJSONArray("entries");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null && optJSONObject.optString("id").equals(entry.id)) {
                        optJSONObject.put("name", project.name == null ? "" : project.name);
                        optJSONObject.put("ts", System.currentTimeMillis());
                    }
                }
            }
            saveIndex(context, loadIndex);
        } catch (Exception unused) {
        }
    }

    public static boolean addGroup(Context context, String str) {
        if (str == null || str.trim().isEmpty() || loadGroups(context).contains(str)) {
            return false;
        }
        JSONObject loadIndex = loadIndex(context);
        try {
            JSONArray optJSONArray = loadIndex.optJSONArray("groups");
            if (optJSONArray == null) {
                optJSONArray = new JSONArray();
                loadIndex.put("groups", optJSONArray);
            }
            optJSONArray.put(str);
            saveIndex(context, loadIndex);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static void renameEntry(Context context, String str, String str2) {
        JSONObject loadIndex = loadIndex(context);
        try {
            JSONArray optJSONArray = loadIndex.optJSONArray("entries");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null && optJSONObject.optString("id").equals(str)) {
                        optJSONObject.put("name", str2);
                    }
                }
            }
            saveIndex(context, loadIndex);
        } catch (Exception unused) {
        }
    }

    public static void moveEntry(Context context, String str, String str2) {
        JSONObject loadIndex = loadIndex(context);
        try {
            JSONArray optJSONArray = loadIndex.optJSONArray("entries");
            boolean z = false;
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null && optJSONObject.optString("id").equals(str)) {
                        optJSONObject.put("group", str2);
                    }
                }
            }
            JSONArray optJSONArray2 = loadIndex.optJSONArray("groups");
            if (optJSONArray2 != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= optJSONArray2.length()) {
                        break;
                    }
                    if (optJSONArray2.optString(i2).equals(str2)) {
                        z = true;
                        break;
                    }
                    i2++;
                }
            } else {
                optJSONArray2 = new JSONArray();
                loadIndex.put("groups", optJSONArray2);
            }
            if (!z) {
                optJSONArray2.put(str2);
            }
            saveIndex(context, loadIndex);
        } catch (Exception unused) {
        }
    }

    public static void deleteEntry(Context context, String str) {
        Entry findEntry = findEntry(context, str);
        if (findEntry != null) {
            File entryFile = entryFile(context, findEntry);
            if (entryFile.exists()) {
                entryFile.delete();
            }
        }
        JSONObject loadIndex = loadIndex(context);
        try {
            JSONArray optJSONArray = loadIndex.optJSONArray("entries");
            JSONArray jSONArray = new JSONArray();
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null && !optJSONObject.optString("id").equals(str)) {
                        jSONArray.put(optJSONObject);
                    }
                }
            }
            loadIndex.put("entries", jSONArray);
            saveIndex(context, loadIndex);
        } catch (Exception unused) {
        }
    }

    private static String extOf(String str) {
        int lastIndexOf;
        return (str == null || (lastIndexOf = str.lastIndexOf(46)) < 0 || (str.length() - lastIndexOf) + (-1) <= 0 || (str.length() - lastIndexOf) + (-1) > 8) ? ".json" : str.substring(lastIndexOf).toLowerCase();
    }

    private static Entry addEntry(Context context, String str, String str2, String str3, String str4, String str5) {
        ensure(context);
        if (str2 == null || str2.trim().isEmpty()) {
            str2 = DEFAULT_GROUP;
        }
        writeString(new File(filesDir(context), str4), str5);
        return registerEntry(context, str, str2, str3, str4);
    }

    private static Entry addEntryBytes(Context context, String str, String str2, String str3, String str4, byte[] bArr) {
        ensure(context);
        if (str2 == null || str2.trim().isEmpty()) {
            str2 = DEFAULT_GROUP;
        }
        writeBytes(new File(filesDir(context), str4), bArr);
        return registerEntry(context, str, str2, str3, str4);
    }

    private static Entry registerEntry(Context context, String str, String str2, String str3, String str4) {
        Entry entry = new Entry();
        entry.id = Util.uuid();
        entry.name = str;
        entry.group = str2;
        entry.type = str3;
        entry.file = str4;
        entry.ts = System.currentTimeMillis();
        JSONObject loadIndex = loadIndex(context);
        try {
            JSONArray optJSONArray = loadIndex.optJSONArray("entries");
            if (optJSONArray == null) {
                optJSONArray = new JSONArray();
                loadIndex.put("entries", optJSONArray);
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", entry.id);
            jSONObject.put("name", str);
            jSONObject.put("group", str2);
            jSONObject.put("type", str3);
            jSONObject.put("file", str4);
            jSONObject.put("ts", entry.ts);
            optJSONArray.put(jSONObject);
            JSONArray optJSONArray2 = loadIndex.optJSONArray("groups");
            if (optJSONArray2 == null) {
                optJSONArray2 = new JSONArray();
                loadIndex.put("groups", optJSONArray2);
            }
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= optJSONArray2.length()) {
                    break;
                }
                if (optJSONArray2.optString(i).equals(str2)) {
                    z = true;
                    break;
                }
                i++;
            }
            if (!z) {
                optJSONArray2.put(str2);
            }
            saveIndex(context, loadIndex);
        } catch (Exception unused) {
        }
        return entry;
    }

    private static JSONObject loadIndex(Context context) {
        ensure(context);
        try {
            return new JSONObject(readString(indexFile(context)));
        } catch (Exception unused) {
            return new JSONObject();
        }
    }

    private static void saveIndex(Context context, JSONObject jSONObject) {
        try {
            writeString(indexFile(context), jSONObject.toString());
        } catch (Exception unused) {
        }
    }

    private static void writeString(File file, String str) {
        try {
            writeBytes(file, str.getBytes("UTF-8"));
        } catch (Exception unused) {
        }
    }

    private static void writeBytes(File file, byte[] bArr) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bArr);
            fileOutputStream.close();
        } catch (Exception unused) {
        }
    }

    private static String readString(File file) {
        try {
            return new String(readBytes(file), "UTF-8");
        } catch (Exception unused) {
            return "";
        }
    }

    public static byte[] readBytes(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[8192];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read <= 0) {
                    fileInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (Exception unused) {
            return new byte[0];
        }
    }
}
