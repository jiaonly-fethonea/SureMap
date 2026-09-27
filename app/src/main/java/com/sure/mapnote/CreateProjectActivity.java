package com.sure.mapnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sure.mapnote.RepoStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class CreateProjectActivity extends BaseActivity {
    private TextView mapLabel;
    private EditText nameInput;
    private Button pickMapBtn;
    private RepoStore.Entry selectedMap;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        RepoStore.seedDefaults(this);
        setContentView(buildUi());
        preselectDefault();
    }

    private List<RepoStore.Entry> materials() {
        ArrayList arrayList = new ArrayList();
        for (RepoStore.Entry entry : RepoStore.loadEntries(this)) {
            if (RepoStore.TYPE_MATERIAL.equals(entry.type)) {
                arrayList.add(entry);
            }
        }
        return arrayList;
    }

    private void preselectDefault() {
        List<RepoStore.Entry> materials = materials();
        this.selectedMap = null;
        Iterator<RepoStore.Entry> it = materials.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            RepoStore.Entry next = it.next();
            if ("世界地图".equals(next.name)) {
                this.selectedMap = next;
                break;
            }
        }
        if (this.selectedMap == null && !materials.isEmpty()) {
            this.selectedMap = materials.get(0);
        }
        updateMapUi();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMapUi() {
        TextView textView = this.mapLabel;
        if (textView == null) {
            return;
        }
        RepoStore.Entry entry = this.selectedMap;
        if (entry != null) {
            textView.setText(entry.name);
            this.pickMapBtn.setText(Lang.get("pick_map"));
        } else {
            textView.setText(Lang.get("no_material"));
            this.pickMapBtn.setText(Lang.get("pick_map"));
        }
    }

    private View buildUi() {
        LinearLayout root = root();
        root.addView(topBar(Lang.get("title_create"), true));
        root.addView(label(Lang.get("project_name")));
        EditText input = input(Lang.get("name_hint"));
        this.nameInput = input;
        margin(input, 4, 16);
        root.addView(this.nameInput);
        root.addView(label(Lang.get("map_mode")));
        TextView textView = new TextView(this);
        this.mapLabel = textView;
        textView.setTextSize(15.0f);
        this.mapLabel.setTextColor(color(R.color.textPrimary));
        this.mapLabel.setTypeface(Typeface.DEFAULT_BOLD);
        this.mapLabel.setPadding(0, Util.dp(this, 6.0f), 0, Util.dp(this, 6.0f));
        root.addView(this.mapLabel);
        Button ghostButton = ghostButton(Lang.get("pick_map"), new View.OnClickListener() { // from class: com.sure.mapnote.CreateProjectActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CreateProjectActivity.this.pickMap();
            }
        });
        this.pickMapBtn = ghostButton;
        margin(ghostButton, 4, 16);
        root.addView(this.pickMapBtn);
        Button primaryButton = primaryButton(Lang.get("create_btn"), new View.OnClickListener() { // from class: com.sure.mapnote.CreateProjectActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CreateProjectActivity.this.doCreate();
            }
        });
        margin(primaryButton, 8, 0);
        root.addView(primaryButton);
        return root;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pickMap() {
        List<RepoStore.Entry> materials = materials();
        if (materials.isEmpty()) {
            Util.toast(this, Lang.get("no_material"));
            return;
        }
        String[] strArr = new String[materials.size()];
        final RepoStore.Entry[] entryArr = (RepoStore.Entry[]) materials.toArray(new RepoStore.Entry[0]);
        for (int i = 0; i < entryArr.length; i++) {
            strArr[i] = entryArr[i].name;
        }
        new AlertDialog.Builder(this).setTitle(Lang.get("pick_map")).setItems(strArr, new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.CreateProjectActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                CreateProjectActivity.this.selectedMap = entryArr[i2];
                CreateProjectActivity.this.updateMapUi();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCreate() {
        String trim = this.nameInput.getText().toString().trim();
        if (trim.isEmpty()) {
            Util.toast(this, Lang.get("name_required"));
            return;
        }
        if (this.selectedMap == null) {
            Util.toast(this, Lang.get("no_material"));
            return;
        }
        Intent intent = new Intent(this, (Class<?>) MapActivity.class);
        intent.putExtra("name", trim);
        intent.putExtra("map_type", RepoStore.TYPE_MATERIAL);
        intent.putExtra("material_file", this.selectedMap.file);
        intent.putExtra("material_label", this.selectedMap.name);
        startActivity(intent);
    }
}
