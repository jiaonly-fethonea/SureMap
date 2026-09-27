package com.sure.mapnote;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class OfficialActivity extends BaseActivity {
    private TextView contentView;
    private TextView pathView;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(buildUi());
        loadContent();
    }

    private File configFile() {
        return new File(getExternalFilesDir(null), "official.txt");
    }

    private void ensureConfig() {
        File configFile = configFile();
        if (configFile.exists()) {
            return;
        }
        try {
            if (configFile.getParentFile() != null) {
                configFile.getParentFile().mkdirs();
            }
            InputStream open = getAssets().open("official.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(configFile);
            byte[] bArr = new byte[8192];
            while (true) {
                int read = open.read(bArr);
                if (read <= 0) {
                    open.close();
                    fileOutputStream.close();
                    return;
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (Exception unused) {
        }
    }

    private String readConfig() {
        ensureConfig();
        try {
            return new String(RepoStore.readBytes(configFile()), "UTF-8");
        } catch (Exception unused) {
            return Lang.get("unsaved_msg");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadContent() {
        String readConfig = readConfig();
        if (readConfig == null || readConfig.trim().isEmpty()) {
            readConfig = " ";
        }
        this.contentView.setText(readConfig);
        this.pathView.setText(Lang.get("official_path") + "\n" + configFile().getAbsolutePath());
    }

    private View buildUi() {
        LinearLayout root = root();
        root.addView(topBar(Lang.get("title_official"), true));
        Button ghostButton = ghostButton(Lang.get("reload"), new View.OnClickListener() { // from class: com.sure.mapnote.OfficialActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                OfficialActivity.this.loadContent();
            }
        });
        margin(ghostButton, 0, 8);
        root.addView(ghostButton);
        TextView textView = new TextView(this);
        this.pathView = textView;
        textView.setTextSize(12.0f);
        this.pathView.setTextColor(color(R.color.textSub));
        this.pathView.setTypeface(Typeface.MONOSPACE);
        this.pathView.setPadding(0, 0, 0, Util.dp(this, 8.0f));
        root.addView(this.pathView);
        ScrollView scrollView = new ScrollView(this);
        TextView textView2 = new TextView(this);
        this.contentView = textView2;
        textView2.setTextSize(15.0f);
        this.contentView.setTextColor(color(R.color.textPrimary));
        this.contentView.setLineSpacing(Util.dp(this, 4.0f), 1.0f);
        this.contentView.setGravity(8388659);
        scrollView.addView(this.contentView, new ViewGroup.LayoutParams(-1, -2));
        root.addView(scrollView, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        return root;
    }
}
