package com.sure.mapnote;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.sure.mapnote.RepoStore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ImportExportActivity extends BaseActivity {
    private static final int RC_EXPORT = 1003;
    private static final int RC_IMPORT_MATERIAL = 1002;
    private static final int RC_IMPORT_PROJECT = 1001;
    private LinearLayout exportList;
    private LinearLayout exportPanel;
    private final ArrayList<String> exportQueue = new ArrayList<>();
    private Button exportTabBtn;
    private LinearLayout importPanel;
    private Button importTabBtn;
    private RepoStore.Entry pendingExportEntry;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(buildUi());
        showTab(true);
    }

    private View buildUi() {
        LinearLayout root = root();
        root.addView(topBar(Lang.get("title_ie"), true));
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        this.importTabBtn = tabButton(Lang.get("import_tab"));
        this.exportTabBtn = tabButton(Lang.get("export_tab"));
        this.importTabBtn.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.ImportExportActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ImportExportActivity.this.showTab(true);
            }
        });
        this.exportTabBtn.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.ImportExportActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ImportExportActivity.this.showTab(false);
            }
        });
        linearLayout.addView(this.importTabBtn);
        linearLayout.addView(this.exportTabBtn);
        root.addView(linearLayout);
        this.importPanel = buildImportPanel();
        this.exportPanel = buildExportPanel();
        root.addView(this.importPanel, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        root.addView(this.exportPanel, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        return root;
    }

    private Button tabButton(String str) {
        Button button = new Button(this);
        button.setText(str);
        button.setTextSize(15.0f);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.rightMargin = Util.dp(this, 4.0f);
        button.setLayoutParams(layoutParams);
        return button;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTab(boolean z) {
        this.importPanel.setVisibility(z ? 0 : 8);
        this.exportPanel.setVisibility(z ? 8 : 0);
        styleTab(this.importTabBtn, z);
        styleTab(this.exportTabBtn, !z);
        if (z) {
            return;
        }
        buildExportList();
    }

    private void styleTab(Button button, boolean z) {
        if (z) {
            button.setBackgroundResource(R.drawable.sel_btn_blue);
            button.setTextColor(-1);
        } else {
            button.setBackgroundResource(R.drawable.sel_btn_ghost);
            button.setTextColor(color(R.color.blue));
        }
    }

    private LinearLayout buildImportPanel() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, Util.dp(this, 12.0f), 0, 0);
        TextView textView = new TextView(this);
        textView.setText(Lang.get("import_hint"));
        textView.setTextSize(13.0f);
        textView.setTextColor(color(R.color.textSub));
        linearLayout.addView(textView);
        Button primaryButton = primaryButton(Lang.get("import_project_btn"), new View.OnClickListener() { // from class: com.sure.mapnote.ImportExportActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ImportExportActivity.this.openDocument("application/json", ImportExportActivity.RC_IMPORT_PROJECT);
            }
        });
        margin(primaryButton, 16, 8);
        linearLayout.addView(primaryButton);
        Button primaryButton2 = primaryButton(Lang.get("import_material_btn"), new View.OnClickListener() { // from class: com.sure.mapnote.ImportExportActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ImportExportActivity.this.openDocument("*/*", ImportExportActivity.RC_IMPORT_MATERIAL);
            }
        });
        margin(primaryButton2, 0, 0);
        linearLayout.addView(primaryButton2);
        return linearLayout;
    }

    private LinearLayout buildExportPanel() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, Util.dp(this, 12.0f), 0, 0);
        TextView textView = new TextView(this);
        textView.setText(Lang.get("export_hint"));
        textView.setTextSize(13.0f);
        textView.setTextColor(color(R.color.textSub));
        linearLayout.addView(textView);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout linearLayout2 = new LinearLayout(this);
        this.exportList = linearLayout2;
        linearLayout2.setOrientation(1);
        scrollView.addView(this.exportList);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
        layoutParams.topMargin = Util.dp(this, 8.0f);
        linearLayout.addView(scrollView, layoutParams);
        Button primaryButton = primaryButton(Lang.get("export_btn"), new View.OnClickListener() { // from class: com.sure.mapnote.ImportExportActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ImportExportActivity.this.startExport();
            }
        });
        margin(primaryButton, 8, 0);
        linearLayout.addView(primaryButton);
        return linearLayout;
    }

    private void buildExportList() {
        this.exportList.removeAllViews();
        ArrayList<RepoStore.Entry> arrayList = new ArrayList();
        for (RepoStore.Entry entry : RepoStore.loadEntries(this)) {
            if (RepoStore.TYPE_PROJECT.equals(entry.type)) {
                arrayList.add(entry);
            }
        }
        if (arrayList.isEmpty()) {
            TextView textView = new TextView(this);
            textView.setText(Lang.get("no_projects_in_repo"));
            textView.setTextColor(color(R.color.textSub));
            textView.setPadding(0, Util.dp(this, 12.0f), 0, 0);
            this.exportList.addView(textView);
            return;
        }
        for (RepoStore.Entry entry2 : arrayList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(entry2.name);
            checkBox.setTextSize(15.0f);
            checkBox.setTextColor(color(R.color.textPrimary));
            checkBox.setTag(entry2);
            this.exportList.addView(checkBox);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openDocument(String str, int i) {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType(str);
        startActivityForResult(intent, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startExport() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.exportList.getChildCount(); i++) {
            View childAt = this.exportList.getChildAt(i);
            if ((childAt instanceof CheckBox) && ((CheckBox) childAt).isChecked()) {
                arrayList.add((RepoStore.Entry) childAt.getTag());
            }
        }
        if (arrayList.isEmpty()) {
            Util.toast(this, Lang.get("export_none"));
            return;
        }
        this.exportQueue.clear();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.exportQueue.add(((RepoStore.Entry) it.next()).id);
        }
        exportNext();
    }

    private void exportNext() {
        if (this.exportQueue.isEmpty()) {
            Util.toast(this, Lang.get("export_ok"));
            return;
        }
        RepoStore.Entry findEntry = RepoStore.findEntry(this, this.exportQueue.remove(0));
        this.pendingExportEntry = findEntry;
        if (findEntry == null) {
            exportNext();
            return;
        }
        Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("application/json");
        intent.putExtra("android.intent.extra.TITLE", sanitize(this.pendingExportEntry.name) + ".json");
        startActivityForResult(intent, RC_EXPORT);
    }

    private String sanitize(String str) {
        return str == null ? RepoStore.TYPE_PROJECT : str.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            this.exportQueue.clear();
            return;
        }
        Uri data = intent == null ? null : intent.getData();
        if (data == null) {
            this.exportQueue.clear();
            return;
        }
        if (i == RC_IMPORT_PROJECT) {
            importProject(data);
        } else if (i == RC_IMPORT_MATERIAL) {
            importMaterial(data);
        } else if (i == RC_EXPORT) {
            exportOne(data);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0029 A[Catch: Exception -> 0x005f, TryCatch #0 {Exception -> 0x005f, blocks: (B:3:0x0002, B:5:0x0013, B:7:0x001b, B:12:0x0029, B:15:0x0031, B:17:0x0039, B:19:0x0041, B:20:0x004c, B:22:0x0044), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0031 A[Catch: Exception -> 0x005f, TryCatch #0 {Exception -> 0x005f, blocks: (B:3:0x0002, B:5:0x0013, B:7:0x001b, B:12:0x0029, B:15:0x0031, B:17:0x0039, B:19:0x0041, B:20:0x004c, B:22:0x0044), top: B:2:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void importProject(android.net.Uri r5) {
        /*
            r4 = this;
            java.lang.String r0 = "import_fail"
            java.lang.String r1 = r4.readText(r5)     // Catch: java.lang.Exception -> L5f
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch: java.lang.Exception -> L5f
            r2.<init>(r1)     // Catch: java.lang.Exception -> L5f
            java.lang.String r3 = "markers"
            boolean r3 = r2.has(r3)     // Catch: java.lang.Exception -> L5f
            if (r3 != 0) goto L26
            java.lang.String r3 = "geojson"
            boolean r3 = r2.has(r3)     // Catch: java.lang.Exception -> L5f
            if (r3 != 0) goto L26
            java.lang.String r3 = "mapType"
            boolean r2 = r2.has(r3)     // Catch: java.lang.Exception -> L5f
            if (r2 == 0) goto L24
            goto L26
        L24:
            r2 = 0
            goto L27
        L26:
            r2 = 1
        L27:
            if (r2 != 0) goto L31
            java.lang.String r5 = com.sure.mapnote.Lang.get(r0)     // Catch: java.lang.Exception -> L5f
            com.sure.mapnote.Util.toast(r4, r5)     // Catch: java.lang.Exception -> L5f
            return
        L31:
            com.sure.mapnote.Project r1 = com.sure.mapnote.Project.fromJson(r1)     // Catch: java.lang.Exception -> L5f
            java.lang.String r2 = r1.name     // Catch: java.lang.Exception -> L5f
            if (r2 == 0) goto L44
            java.lang.String r2 = r1.name     // Catch: java.lang.Exception -> L5f
            boolean r2 = r2.isEmpty()     // Catch: java.lang.Exception -> L5f
            if (r2 != 0) goto L44
            java.lang.String r5 = r1.name     // Catch: java.lang.Exception -> L5f
            goto L4c
        L44:
            java.lang.String r5 = r4.displayName(r5)     // Catch: java.lang.Exception -> L5f
            java.lang.String r5 = r4.baseName(r5)     // Catch: java.lang.Exception -> L5f
        L4c:
            java.lang.String r2 = "素材"
            java.lang.String r1 = r1.toJson()     // Catch: java.lang.Exception -> L5f
            com.sure.mapnote.RepoStore.addProject(r4, r5, r2, r1)     // Catch: java.lang.Exception -> L5f
            java.lang.String r5 = "import_ok"
            java.lang.String r5 = com.sure.mapnote.Lang.get(r5)     // Catch: java.lang.Exception -> L5f
            com.sure.mapnote.Util.toast(r4, r5)     // Catch: java.lang.Exception -> L5f
            goto L66
        L5f:
            java.lang.String r5 = com.sure.mapnote.Lang.get(r0)
            com.sure.mapnote.Util.toast(r4, r5)
        L66:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sure.mapnote.ImportExportActivity.importProject(android.net.Uri):void");
    }

    private void importMaterial(Uri uri) {
        try {
            byte[] readBytes = readBytes(uri);
            String displayName = displayName(uri);
            if (displayName == null || displayName.isEmpty()) {
                displayName = RepoStore.TYPE_MATERIAL;
            }
            RepoStore.addImported(this, displayName, RepoStore.DEFAULT_GROUP, readBytes);
            Util.toast(this, Lang.get("import_ok"));
        } catch (Exception unused) {
            Util.toast(this, Lang.get("import_fail"));
        }
    }

    private void exportOne(Uri uri) {
        if (this.pendingExportEntry == null) {
            return;
        }
        try {
            OutputStream openOutputStream = getContentResolver().openOutputStream(uri);
            if (openOutputStream != null) {
                openOutputStream.write(RepoStore.readBytes(RepoStore.entryFile(this, this.pendingExportEntry)));
                openOutputStream.close();
            }
        } catch (Exception unused) {
        }
        exportNext();
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0039, code lost:
    
        if (r10 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0028, code lost:
    
        if (r10 != null) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x002a, code lost:
    
        r10.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x003c, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String displayName(android.net.Uri r10) {
        /*
            r9 = this;
            r0 = 0
            android.content.ContentResolver r1 = r9.getContentResolver()     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L38
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r2 = r10
            android.database.Cursor r10 = r1.query(r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L38
            if (r10 == 0) goto L28
            java.lang.String r1 = "_display_name"
            int r1 = r10.getColumnIndex(r1)     // Catch: java.lang.Throwable -> L24 java.lang.Exception -> L26
            if (r1 < 0) goto L28
            boolean r2 = r10.moveToFirst()     // Catch: java.lang.Throwable -> L24 java.lang.Exception -> L26
            if (r2 == 0) goto L28
            java.lang.String r0 = r10.getString(r1)     // Catch: java.lang.Throwable -> L24 java.lang.Exception -> L26
            goto L28
        L24:
            r0 = move-exception
            goto L32
        L26:
            goto L39
        L28:
            if (r10 == 0) goto L3c
        L2a:
            r10.close()
            goto L3c
        L2e:
            r10 = move-exception
            r8 = r0
            r0 = r10
            r10 = r8
        L32:
            if (r10 == 0) goto L37
            r10.close()
        L37:
            throw r0
        L38:
            r10 = r0
        L39:
            if (r10 == 0) goto L3c
            goto L2a
        L3c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sure.mapnote.ImportExportActivity.displayName(android.net.Uri):java.lang.String");
    }

    private String baseName(String str) {
        if (str == null) {
            return RepoStore.TYPE_PROJECT;
        }
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf > 0 ? str.substring(0, lastIndexOf) : str;
    }

    private String readText(Uri uri) throws IOException {
        return new String(readBytes(uri), "UTF-8");
    }

    private byte[] readBytes(Uri uri) throws IOException {
        InputStream openInputStream = getContentResolver().openInputStream(uri);
        if (openInputStream == null) {
            return new byte[0];
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[8192];
        while (true) {
            int read = openInputStream.read(bArr);
            if (read <= 0) {
                openInputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }
}
