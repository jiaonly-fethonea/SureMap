package com.sure.mapnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.sure.mapnote.RepoStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class RepositoryActivity extends BaseActivity {
    private TextView emptyView;
    private LinearLayout listContainer;
    private EditText search;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(buildUi());
        refreshList();
    }

    private View buildUi() {
        LinearLayout root = root();
        root.addView(topBar(Lang.get("title_repo"), true));
        Button primaryButton = primaryButton(Lang.get("add_group"), new View.OnClickListener() { // from class: com.sure.mapnote.RepositoryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RepositoryActivity.this.promptAddGroup();
            }
        });
        margin(primaryButton, 0, 12);
        root.addView(primaryButton);
        EditText input = input(Lang.get("search_hint"));
        this.search = input;
        input.addTextChangedListener(new TextWatcher() { // from class: com.sure.mapnote.RepositoryActivity.2
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                RepositoryActivity.this.refreshList();
            }
        });
        margin(this.search, 0, 12);
        root.addView(this.search);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        this.listContainer = linearLayout;
        linearLayout.setOrientation(1);
        scrollView.addView(this.listContainer);
        TextView textView = new TextView(this);
        this.emptyView = textView;
        textView.setText(Lang.get("no_entries"));
        this.emptyView.setTextSize(15.0f);
        this.emptyView.setTextColor(color(R.color.textSub));
        this.emptyView.setGravity(17);
        this.emptyView.setPadding(0, Util.dp(this, 24.0f), 0, 0);
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(1);
        linearLayout2.addView(scrollView, new LinearLayout.LayoutParams(-1, -2, 1.0f));
        linearLayout2.addView(this.emptyView);
        root.addView(linearLayout2, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        return root;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshList() {
        this.listContainer.removeAllViews();
        EditText editText = this.search;
        String lowerCase = editText == null ? "" : editText.getText().toString().trim().toLowerCase();
        List<RepoStore.Entry> loadEntries = RepoStore.loadEntries(this);
        ArrayList arrayList = new ArrayList();
        for (RepoStore.Entry entry : loadEntries) {
            if (lowerCase.isEmpty() || (entry.name != null && entry.name.toLowerCase().contains(lowerCase))) {
                arrayList.add(entry);
            }
        }
        this.emptyView.setVisibility(arrayList.isEmpty() ? 0 : 8);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.listContainer.addView(entryRow((RepoStore.Entry) it.next()));
        }
    }

    private View entryRow(final RepoStore.Entry entry) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setBackgroundResource(R.drawable.bg_card);
        linearLayout.setPadding(Util.dp(this, 14.0f), Util.dp(this, 12.0f), Util.dp(this, 8.0f), Util.dp(this, 12.0f));
        boolean equals = RepoStore.TYPE_PROJECT.equals(entry.type);
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(1);
        linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(0, -2, 1.0f));
        TextView textView = new TextView(this);
        textView.setText(entry.name);
        textView.setTextSize(16.0f);
        textView.setTextColor(color(R.color.textPrimary));
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        linearLayout2.addView(textView);
        TextView textView2 = new TextView(this);
        textView2.setText(Lang.get(equals ? "entry_type_project" : "entry_type_material") + "  ·  " + entry.group);
        textView2.setTextSize(12.0f);
        textView2.setTextColor(color(R.color.textSub));
        textView2.setPadding(0, Util.dp(this, 4.0f), 0, 0);
        linearLayout2.addView(textView2);
        TextView textView3 = new TextView(this);
        textView3.setText("⋯");
        textView3.setTextSize(22.0f);
        textView3.setTextColor(color(R.color.textSub));
        textView3.setGravity(17);
        textView3.setPadding(Util.dp(this, 10.0f), 0, Util.dp(this, 6.0f), 0);
        textView3.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.RepositoryActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RepositoryActivity.this.showEntryOptions(entry);
            }
        });
        linearLayout.addView(textView3);
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.RepositoryActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (RepoStore.TYPE_PROJECT.equals(entry.type)) {
                    RepositoryActivity.this.openProject(entry);
                } else {
                    RepositoryActivity.this.showEntryOptions(entry);
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = Util.dp(this, 4.0f);
        layoutParams.bottomMargin = Util.dp(this, 4.0f);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openProject(RepoStore.Entry entry) {
        Intent intent = new Intent(this, (Class<?>) MapActivity.class);
        intent.putExtra("project_id", entry.id);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEntryOptions(final RepoStore.Entry entry) {
        boolean equals = RepoStore.TYPE_PROJECT.equals(entry.type);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (equals) {
            arrayList.add(Lang.get("open"));
            arrayList2.add(0);
        }
        arrayList.add(Lang.get("rename"));
        arrayList2.add(1);
        arrayList.add(Lang.get("move"));
        arrayList2.add(2);
        arrayList.add(Lang.get("delete_entry"));
        arrayList2.add(3);
        String[] strArr = (String[]) arrayList.toArray(new String[0]);
        int size = arrayList2.size();
        final int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = ((Integer) arrayList2.get(i)).intValue();
        }
        new AlertDialog.Builder(this).setTitle(entry.name).setItems(strArr, new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.RepositoryActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                int i3 = iArr[i2];
                if (i3 == 0) {
                    RepositoryActivity.this.openProject(entry);
                    return;
                }
                if (i3 == 1) {
                    RepositoryActivity.this.promptRename(entry);
                } else if (i3 == 2) {
                    RepositoryActivity.this.promptMove(entry);
                } else {
                    if (i3 != 3) {
                        return;
                    }
                    RepositoryActivity.this.confirmDelete(entry);
                }
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void promptAddGroup() {
        final EditText input = input(Lang.get("group_name_hint"));
        new AlertDialog.Builder(this).setTitle(Lang.get("add_group")).setView(pad(input)).setPositiveButton(Lang.get("confirm"), new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.RepositoryActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                String trim = input.getText().toString().trim();
                if (trim.isEmpty()) {
                    return;
                }
                if (!RepoStore.addGroup(RepositoryActivity.this, trim)) {
                    Util.toast(RepositoryActivity.this, Lang.get("group_exists"));
                } else {
                    Util.toast(RepositoryActivity.this, Lang.get("confirm"));
                }
            }
        }).setNegativeButton(Lang.get("cancel"), (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void promptRename(final RepoStore.Entry entry) {
        final EditText input = input(Lang.get("new_name"));
        input.setText(entry.name);
        new AlertDialog.Builder(this).setTitle(Lang.get("rename")).setView(pad(input)).setPositiveButton(Lang.get("confirm"), new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.RepositoryActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                String trim = input.getText().toString().trim();
                if (trim.isEmpty()) {
                    return;
                }
                RepoStore.renameEntry(RepositoryActivity.this, entry.id, trim);
                RepositoryActivity.this.refreshList();
            }
        }).setNegativeButton(Lang.get("cancel"), (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void promptMove(final RepoStore.Entry entry) {
        final String[] strArr = (String[]) RepoStore.loadGroups(this).toArray(new String[0]);
        new AlertDialog.Builder(this).setTitle(Lang.get("select_group_move")).setItems(strArr, new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.RepositoryActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                RepoStore.moveEntry(RepositoryActivity.this, entry.id, strArr[i]);
                RepositoryActivity.this.refreshList();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmDelete(final RepoStore.Entry entry) {
        new AlertDialog.Builder(this).setMessage(Lang.get("confirm_delete_entry")).setPositiveButton(Lang.get("delete"), new DialogInterface.OnClickListener() { // from class: com.sure.mapnote.RepositoryActivity.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                RepoStore.deleteEntry(RepositoryActivity.this, entry.id);
                RepositoryActivity.this.refreshList();
            }
        }).setNegativeButton(Lang.get("cancel"), (DialogInterface.OnClickListener) null).show();
    }

    private LinearLayout pad(View view) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setPadding(Util.dp(this, 16.0f), Util.dp(this, 8.0f), Util.dp(this, 16.0f), 0);
        linearLayout.addView(view, new LinearLayout.LayoutParams(-1, -2));
        return linearLayout;
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        refreshList();
    }
}
