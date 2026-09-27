package com.sure.mapnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sure.mapnote.Project;
import com.sure.mapnote.RepoStore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MapActivity extends BaseActivity {
    private Button areaBtn;
    private Button mapBtn;
    private Button recordBtn;
    private Button redoBtn;
    private Button undoBtn;
    private WebView webView;
    private Project project = new Project();
    private RepoStore.Entry entry = null;
    private String geojson = "";
    private String materialFile = "";
    private String materialLabel = "";
    private String mapType = "world";
    private String projectName = "";
    private boolean pageReady = false;
    private boolean dirty = false;
    private double viewLat = 20.0d;
    private double viewLng = 0.0d;
    private double viewZoom = 2.0d;
    private String toolMode = "pan";
    private String areaMode = "black";
    private final ArrayList<String> undoStack = new ArrayList<>();
    private final ArrayList<String> redoStack = new ArrayList<>();

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        Lang.setLang(Prefs.lang(this));
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("project_id");
        this.projectName = intent.getStringExtra("name");
        this.mapType = intent.getStringExtra("map_type");
        this.materialFile = intent.getStringExtra("material_file");
        this.materialLabel = intent.getStringExtra("material_label");
        if (stringExtra != null && !stringExtra.isEmpty()) {
            RepoStore.Entry findEntry = RepoStore.findEntry(this, stringExtra);
            this.entry = findEntry;
            if (findEntry != null) {
                Project fromJson = Project.fromJson(RepoStore.readEntryContent(this, findEntry));
                this.project = fromJson;
                if (fromJson.id == null || this.project.id.isEmpty()) {
                    this.project.id = stringExtra;
                }
                this.geojson = this.project.geojson;
            }
        }
        String str2 = this.geojson;
        if ((str2 == null || str2.isEmpty()) && RepoStore.TYPE_MATERIAL.equals(this.mapType) && (str = this.materialFile) != null && !str.isEmpty()) {
            this.geojson = RepoStore.readRawFile(this, this.materialFile);
        }
        String str3 = this.geojson;
        if (str3 == null || str3.isEmpty()) {
            this.geojson = loadAsset("regions/world-countries.geojson");
            this.project.mapType = "world";
        }
        if (this.project.name == null || this.project.name.isEmpty()) {
            Project project = this.project;
            String str4 = this.projectName;
            project.name = (str4 == null || str4.isEmpty()) ? Lang.get("new_project") : this.projectName;
        }
        if (this.project.id == null || this.project.id.isEmpty()) {
            this.project.id = Util.uuid();
        }
        if (this.project.mapType == null || this.project.mapType.isEmpty()) {
            Project project2 = this.project;
            String str5 = this.mapType;
            project2.mapType = str5 != null ? str5 : "world";
        }
        if (this.project.materialLabel == null || this.project.materialLabel.isEmpty()) {
            Project project3 = this.project;
            String str6 = this.materialLabel;
            if (str6 == null) {
                str6 = "";
            }
            project3.materialLabel = str6;
        }
        if (this.project.createdAt == 0) {
            this.project.createdAt = System.currentTimeMillis();
        }
        this.project.geojson = this.geojson;
        this.viewLat = this.project.viewLat;
        this.viewLng = this.project.viewLng;
        this.viewZoom = this.project.viewZoom;
        setContentView(buildUi());
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        this.webView.addJavascriptInterface(new Bridge(), "Android");
        this.webView.setWebViewClient(new WebViewClient() { // from class: com.sure.mapnote.MapActivity.1
            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str7) {
                MapActivity.this.pageReady = true;
                MapActivity.this.injectInit();
            }
        });
        this.webView.loadUrl("file:///android_asset/map.html");
    }

    private View buildUi() {
        LinearLayout root = root();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(0, Util.dp(this, 2.0f), 0, Util.dp(this, 8.0f));
        TextView textView = new TextView(this);
        textView.setText("‹");
        textView.setTextSize(34.0f);
        textView.setTextColor(color(R.color.blue));
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setPadding(0, 0, Util.dp(this, 12.0f), 0);
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.MapActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MapActivity.this.onBackPressed();
            }
        });
        linearLayout.addView(textView);
        TextView textView2 = new TextView(this);
        textView2.setText(this.project.name);
        textView2.setTextSize(18.0f);
        textView2.setTextColor(color(R.color.textPrimary));
        textView2.setTypeface(Typeface.DEFAULT_BOLD);
        textView2.setSingleLine(true);
        linearLayout.addView(textView2, new LinearLayout.LayoutParams(0, -2, 1.0f));
        Button button = new Button(this);
        button.setText(Lang.get("save"));
        button.setTextSize(14.0f);
        button.setTextColor(-1);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setBackgroundResource(R.drawable.sel_btn_blue);
        button.setPadding(Util.dp(this, 14.0f), Util.dp(this, 6.0f), Util.dp(this, 14.0f), Util.dp(this, 6.0f));
        button.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.MapActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MapActivity.this.save();
            }
        });
        linearLayout.addView(button);
        root.addView(linearLayout);
        this.webView = new WebView(this);
        root.addView(this.webView, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(0);
        linearLayout2.setPadding(0, Util.dp(this, 8.0f), 0, 0);
        Button button2 = toolButton(Lang.get("tool_record"));
        this.recordBtn = button2;
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.MapActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MapActivity mapActivity = MapActivity.this;
                mapActivity.setToolMode("record".equals(mapActivity.toolMode) ? "pan" : "record");
            }
        });
        linearLayout2.addView(this.recordBtn);
        Button button3 = toolButton(Lang.get("tool_area"));
        this.areaBtn = button3;
        button3.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.MapActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MapActivity.this.showAreaDialog();
            }
        });
        linearLayout2.addView(this.areaBtn);
        Button button4 = toolButton("↶");
        this.undoBtn = button4;
        button4.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.MapActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MapActivity.this.undo();
            }
        });
        linearLayout2.addView(this.undoBtn);
        Button button5 = toolButton("↷");
        this.redoBtn = button5;
        button5.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.MapActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MapActivity.this.redo();
            }
        });
        linearLayout2.addView(this.redoBtn);
        Button button6 = toolButton(Lang.get("map_satellite"));
        this.mapBtn = button6;
        button6.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.MapActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MapActivity mapActivity = MapActivity.this;
                mapActivity.setTileMode("satellite".equals(mapActivity.project.tileMode) ? "standard" : "satellite");
            }
        });
        linearLayout2.addView(this.mapBtn);
        root.addView(linearLayout2);
        refreshToolbar();
        return root;
    }

    private Button toolButton(String str) {
        Button button = new Button(this);
        button.setText(str);
        button.setTextSize(13.0f);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setGravity(17);
        button.setSingleLine(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.leftMargin = Util.dp(this, 2.0f);
        layoutParams.rightMargin = Util.dp(this, 2.0f);
        button.setLayoutParams(layoutParams);
        return button;
    }

    private void styleTool(Button button, boolean z) {
        if (z) {
            button.setBackgroundResource(R.drawable.sel_btn_blue);
            button.setTextColor(-1);
        } else {
            button.setBackgroundResource(R.drawable.sel_btn_ghost);
            button.setTextColor(color(R.color.blue));
        }
    }

    private void refreshToolbar() {
        styleTool(this.recordBtn, "record".equals(this.toolMode));
        styleTool(this.areaBtn, "area".equals(this.toolMode));
        styleTool(this.undoBtn, !this.undoStack.isEmpty());
        styleTool(this.redoBtn, !this.redoStack.isEmpty());
        this.mapBtn.setText(Lang.get("satellite".equals(this.project.tileMode) ? "map_standard" : "map_satellite"));
        styleTool(this.mapBtn, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void injectInit() {
        if (this.pageReady) {
            this.webView.evaluateJavascript("window.__initData(" + JSONObject.quote(this.geojson) + "," + JSONObject.quote(this.project.markersJson()) + "," + JSONObject.quote(this.project.regionsJson()) + "," + JSONObject.quote(this.project.tileMode) + "," + ("{\"lat\":" + this.viewLat + ",\"lng\":" + this.viewLng + ",\"zoom\":" + this.viewZoom + "}") + ");window.__setTool(" + JSONObject.quote(this.toolMode) + ");window.__setArea(" + JSONObject.quote(this.areaMode) + ");", null);
        }
    }

    private void refreshJs() {
        if (this.pageReady) {
            this.webView.evaluateJavascript("window.__updateAll(" + JSONObject.quote(this.project.regionsJson()) + "," + JSONObject.quote(this.project.markersJson()) + ");", null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTileMode(String str) {
        this.project.tileMode = str;
        if (this.pageReady) {
            this.webView.evaluateJavascript("window.__setTile(" + JSONObject.quote(str) + ");", null);
        }
        refreshToolbar();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setToolMode(String str) {
        this.toolMode = str;
        if (this.pageReady) {
            this.webView.evaluateJavascript("window.__setTool(" + JSONObject.quote(str) + ");", null);
        }
        refreshToolbar();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAreaMode(String str) {
        this.areaMode = str;
        this.toolMode = "area";
        if (this.pageReady) {
            this.webView.evaluateJavascript("window.__setTool('area');window.__setArea(" + JSONObject.quote(str) + ");", null);
        }
        refreshToolbar();
    }

    private String snapshot() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("markers", new JSONArray(this.project.markersJson()));
            jSONObject.put("regions", new JSONArray(this.project.regionsJson()));
            return jSONObject.toString();
        } catch (Exception unused) {
            return "{}";
        }
    }

    private void applySnapshot(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.project.markers.clear();
            JSONArray optJSONArray = jSONObject.optJSONArray("markers");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        Project.Marker marker = new Project.Marker();
                        marker.id = optJSONObject.optString("id");
                        marker.lat = optJSONObject.optDouble("lat");
                        marker.lng = optJSONObject.optDouble("lng");
                        marker.title = optJSONObject.optString("title");
                        marker.content = optJSONObject.optString("content");
                        marker.ts = optJSONObject.optLong("ts");
                        this.project.markers.add(marker);
                    }
                }
            }
            this.project.regions.clear();
            JSONArray optJSONArray2 = jSONObject.optJSONArray("regions");
            if (optJSONArray2 != null) {
                for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                    JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i2);
                    if (optJSONObject2 != null) {
                        Project.Region region = new Project.Region();
                        region.key = optJSONObject2.optString("key");
                        region.name = optJSONObject2.optString("name");
                        region.state = optJSONObject2.optString("state");
                        this.project.regions.add(region);
                    }
                }
            }
        } catch (Exception unused) {
        }
        refreshJs();
        refreshToolbar();
    }

    private void recordUndo() {
        this.undoStack.add(snapshot());
        if (this.undoStack.size() > 50) {
            this.undoStack.remove(0);
        }
        this.redoStack.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void undo() {
        if (this.undoStack.isEmpty()) {
            return;
        }
        this.redoStack.add(snapshot());
        ArrayList<String> arrayList = this.undoStack;
        applySnapshot(arrayList.remove(arrayList.size() - 1));
        this.dirty = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void redo() {
        if (this.redoStack.isEmpty()) {
            return;
        }
        this.undoStack.add(snapshot());
        ArrayList<String> arrayList = this.redoStack;
        applySnapshot(arrayList.remove(arrayList.size() - 1));
        this.dirty = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addMarker(double d, double d2, String str, String str2) {
        recordUndo();
        Project.Marker marker = new Project.Marker();
        marker.id = Util.uuid();
        marker.lat = d;
        marker.lng = d2;
        marker.title = str;
        marker.content = str2;
        marker.ts = System.currentTimeMillis();
        this.project.markers.add(marker);
        this.dirty = true;
        refreshJs();
        refreshToolbar();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMarker(String str, String str2, String str3) {
        Project.Marker findMarker = findMarker(str);
        if (findMarker == null) {
            return;
        }
        recordUndo();
        findMarker.title = str2;
        findMarker.content = str3;
        this.dirty = true;
        refreshJs();
        refreshToolbar();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteMarker(String str) {
        Project.Marker findMarker = findMarker(str);
        if (findMarker == null) {
            return;
        }
        recordUndo();
        this.project.markers.remove(findMarker);
        this.dirty = true;
        refreshJs();
        refreshToolbar();
    }

    private Project.Marker findMarker(String str) {
        for (Project.Marker marker : this.project.markers) {
            if (marker.id.equals(str)) {
                return marker;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleRegion(String str, String str2, String str3) {
        Project.Region region;
        recordUndo();
        Iterator<Project.Region> it = this.project.regions.iterator();
        while (true) {
            if (!it.hasNext()) {
                region = null;
                break;
            }
            region = it.next();
            if (region.key != null && region.key.equals(str)) {
                break;
            }
        }
        if (region != null) {
            if (region.state != null && region.state.equals(str3)) {
                this.project.regions.remove(region);
            } else {
                region.state = str3;
                region.name = str2;
            }
        } else {
            Project.Region region2 = new Project.Region();
            region2.key = str;
            region2.name = str2;
            region2.state = str3;
            this.project.regions.add(region2);
        }
        this.dirty = true;
        refreshJs();
        refreshToolbar();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showRecordDialog(final double d, final double d2) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(Util.dp(this, 8.0f), Util.dp(this, 4.0f), Util.dp(this, 8.0f), 0);
        final EditText input = input(Lang.get("marker_title"));
        final EditText input2 = input(Lang.get("marker_content_hint"));
        input2.setSingleLine(false);
        input2.setMinLines(3);
        input2.setGravity(48);
        linearLayout.addView(input);
        linearLayout.addView(input2);
        new AlertDialog.Builder(this).setTitle(Lang.get("record_title")).setView(linearLayout).setPositiveButton(Lang.get("add_record"), new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.MapActivity.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                MapActivity.this.addMarker(d, d2, input.getText().toString().trim(), input2.getText().toString().trim());
            }
        }).setNegativeButton(Lang.get("cancel"), (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMarkerOptions(String str) {
        final Project.Marker findMarker = findMarker(str);
        if (findMarker == null) {
            return;
        }
        new AlertDialog.Builder(this).setTitle((findMarker.title == null || findMarker.title.isEmpty()) ? Lang.get("marker_options") : findMarker.title).setItems(new String[]{Lang.get("edit"), Lang.get("delete")}, new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.MapActivity.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    MapActivity.this.showEditMarker(findMarker);
                } else {
                    MapActivity.this.confirmDeleteMarker(findMarker);
                }
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEditMarker(final Project.Marker marker) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(Util.dp(this, 8.0f), Util.dp(this, 4.0f), Util.dp(this, 8.0f), 0);
        final EditText input = input(Lang.get("marker_title"));
        input.setText(marker.title);
        final EditText input2 = input(Lang.get("marker_content_hint"));
        input2.setText(marker.content);
        input2.setSingleLine(false);
        input2.setMinLines(3);
        input2.setGravity(48);
        linearLayout.addView(input);
        linearLayout.addView(input2);
        new AlertDialog.Builder(this).setTitle(Lang.get("edit")).setView(linearLayout).setPositiveButton(Lang.get("save"), new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.MapActivity.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                MapActivity.this.updateMarker(marker.id, input.getText().toString().trim(), input2.getText().toString().trim());
            }
        }).setNegativeButton(Lang.get("cancel"), (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmDeleteMarker(final Project.Marker marker) {
        new AlertDialog.Builder(this).setMessage(Lang.get("confirm_delete_marker")).setPositiveButton(Lang.get("delete"), new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.MapActivity.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                MapActivity.this.deleteMarker(marker.id);
            }
        }).setNegativeButton(Lang.get("cancel"), (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAreaDialog() {
        new AlertDialog.Builder(this).setTitle(Lang.get("area_mode_title")).setItems(new String[]{Lang.get("area_black"), Lang.get("area_white"), Lang.get("cancel")}, new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.MapActivity.13
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    MapActivity.this.setAreaMode("black");
                } else if (i == 1) {
                    MapActivity.this.setAreaMode("white");
                } else {
                    MapActivity.this.setToolMode("pan");
                }
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void save() {
        this.project.viewLat = this.viewLat;
        this.project.viewLng = this.viewLng;
        this.project.viewZoom = this.viewZoom;
        this.project.updatedAt = System.currentTimeMillis();
        this.project.geojson = this.geojson;
        RepoStore.Entry entry = this.entry;
        if (entry != null) {
            RepoStore.updateProject(this, entry, this.project);
            this.dirty = false;
            Util.toast(this, Lang.get("saved"));
            return;
        }
        chooseGroupAndSave();
    }

    private void chooseGroupAndSave() {
        List<String> loadGroups = RepoStore.loadGroups(this);
        final String[] strArr = (String[]) loadGroups.toArray(new String[0]);
        int indexOf = loadGroups.indexOf(Prefs.lastGroup(this));
        int i = indexOf >= 0 ? indexOf : 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Lang.get("select_group"));
        builder.setSingleChoiceItems(strArr, i, (DialogInterface.OnClickListener) null);
        builder.setPositiveButton(Lang.get("save"), new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.MapActivity.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                String str;
                int checkedItemPosition = ((AlertDialog) dialogInterface).getListView().getCheckedItemPosition();
                if (checkedItemPosition >= 0) {
                    String[] strArr2 = strArr;
                    if (checkedItemPosition < strArr2.length) {
                        str = strArr2[checkedItemPosition];
                        Prefs.setLastGroup(MapActivity.this, str);
                        MapActivity mapActivity = MapActivity.this;
                        mapActivity.entry = RepoStore.addProject(mapActivity, mapActivity.project.name, str, MapActivity.this.project.toJson());
                        MapActivity.this.dirty = false;
                        Util.toast(MapActivity.this, Lang.get("saved"));
                    }
                }
                str = RepoStore.DEFAULT_GROUP;
                Prefs.setLastGroup(MapActivity.this, str);
                MapActivity mapActivity2 = MapActivity.this;
                mapActivity2.entry = RepoStore.addProject(mapActivity2, mapActivity2.project.name, str, MapActivity.this.project.toJson());
                MapActivity.this.dirty = false;
                Util.toast(MapActivity.this, Lang.get("saved"));
            }
        });
        builder.setNegativeButton(Lang.get("cancel"), (DialogInterface.OnClickListener) null);
        builder.show();
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (!this.dirty) {
            super.onBackPressed();
        } else {
            new AlertDialog.Builder(this).setMessage(Lang.get("unsaved_msg")).setPositiveButton(Lang.get("save"), new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.MapActivity.16
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    MapActivity.this.save();
                }
            }).setNegativeButton(Lang.get("discard"), new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.MapActivity.15
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    MapActivity.this.dirty = false;
                    MapActivity.this.onBackPressed();
                }
            }).setNeutralButton(Lang.get("cancel"), (DialogInterface.OnClickListener) null).show();
        }
    }

    private String loadAsset(String str) {
        try {
            return new String(readAll(getAssets().open(str)), "UTF-8");
        } catch (Exception unused) {
            return "{\"type\":\"FeatureCollection\",\"features\":[]}";
        }
    }

    private byte[] readAll(InputStream inputStream) throws IOException {
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
    }

    /* loaded from: classes.dex */
    private class Bridge {
        private Bridge() {
        }

        @JavascriptInterface
        public void onViewChanged(double d, double d2, double d3) {
            MapActivity.this.viewLat = d;
            MapActivity.this.viewLng = d2;
            MapActivity.this.viewZoom = d3;
        }

        @JavascriptInterface
        public void onLongPress(final double d, final double d2) {
            MapActivity.this.runOnUiThread(new Runnable() { // from class: com.sure.mapnote.MapActivity.Bridge.1
                @Override // java.lang.Runnable
                public void run() {
                    MapActivity.this.showRecordDialog(d, d2);
                }
            });
        }

        @JavascriptInterface
        public void onMarkerTap(final String str) {
            MapActivity.this.runOnUiThread(new Runnable() { // from class: com.sure.mapnote.MapActivity.Bridge.2
                @Override // java.lang.Runnable
                public void run() {
                    MapActivity.this.showMarkerOptions(str);
                }
            });
        }

        @JavascriptInterface
        public void onRegionTap(final String str, final String str2, final String str3) {
            MapActivity.this.runOnUiThread(new Runnable() { // from class: com.sure.mapnote.MapActivity.Bridge.3
                @Override // java.lang.Runnable
                public void run() {
                    MapActivity.this.toggleRegion(str, str2, str3);
                }
            });
        }
    }
}
