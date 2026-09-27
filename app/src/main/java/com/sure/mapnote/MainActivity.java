package com.sure.mapnote;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/* loaded from: classes.dex */
public class MainActivity extends BaseActivity {
    private final String[] langCodes = {Lang.ZH, Lang.EN, Lang.JA};

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Lang.setLang(Prefs.lang(this));
        RepoStore.seedDefaults(this);
        setContentView(buildHome());
    }

    private View buildHome() {
        LinearLayout root = root();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(0, Util.dp(this, 4.0f), 0, Util.dp(this, 16.0f));
        TextView textView = new TextView(this);
        textView.setText(Lang.get("app_name"));
        textView.setTextSize(22.0f);
        textView.setTextColor(color(R.color.textPrimary));
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(0, -2, 1.0f));
        TextView textView2 = new TextView(this);
        textView2.setText(Lang.get("lang_label"));
        textView2.setTextSize(13.0f);
        textView2.setTextColor(color(R.color.textSub));
        textView2.setPadding(0, 0, Util.dp(this, 6.0f), 0);
        linearLayout.addView(textView2);
        Spinner spinner = new Spinner(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{Lang.langName(Lang.ZH), Lang.langName(Lang.EN), Lang.langName(Lang.JA)});
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((SpinnerAdapter) arrayAdapter);
        String lang = Lang.getLang();
        int i = 0;
        int i2 = 0;
        while (true) {
            String[] strArr = this.langCodes;
            if (i >= strArr.length) {
                spinner.setSelection(i2);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.sure.mapnote.MainActivity.1
                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }

                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long j) {
                        String str = MainActivity.this.langCodes[i3];
                        if (str.equals(Lang.getLang())) {
                            return;
                        }
                        Prefs.setLang(MainActivity.this, str);
                        Lang.setLang(str);
                        MainActivity.this.recreate();
                    }
                });
                linearLayout.addView(spinner);
                root.addView(linearLayout);
                TextView textView3 = new TextView(this);
                textView3.setText(Lang.get("home_sub"));
                textView3.setTextSize(14.0f);
                textView3.setTextColor(color(R.color.textSub));
                textView3.setGravity(17);
                textView3.setPadding(0, Util.dp(this, 4.0f), 0, Util.dp(this, 24.0f));
                root.addView(textView3);
                root.addView(homeButton(Lang.get("create_new"), new View.OnClickListener() { // from class: com.sure.mapnote.MainActivity.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, (Class<?>) CreateProjectActivity.class));
                    }
                }));
                root.addView(homeButton(Lang.get("repository"), new View.OnClickListener() { // from class: com.sure.mapnote.MainActivity.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, (Class<?>) RepositoryActivity.class));
                    }
                }));
                root.addView(homeButton(Lang.get("import_export"), new View.OnClickListener() { // from class: com.sure.mapnote.MainActivity.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, (Class<?>) ImportExportActivity.class));
                    }
                }));
                root.addView(homeButton(Lang.get("official"), new View.OnClickListener() { // from class: com.sure.mapnote.MainActivity.5
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, (Class<?>) OfficialActivity.class));
                    }
                }));
                return root;
            }
            if (strArr[i].equals(lang)) {
                i2 = i;
            }
            i++;
        }
    }

    private Button homeButton(String str, View.OnClickListener onClickListener) {
        Button button = new Button(this);
        button.setText(str);
        button.setTextSize(17.0f);
        button.setTextColor(-1);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setBackgroundResource(R.drawable.sel_btn_blue);
        button.setMinHeight(Util.dp(this, 64.0f));
        button.setGravity(17);
        button.setOnClickListener(onClickListener);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = Util.dp(this, 6.0f);
        layoutParams.bottomMargin = Util.dp(this, 6.0f);
        button.setLayoutParams(layoutParams);
        return button;
    }
}
