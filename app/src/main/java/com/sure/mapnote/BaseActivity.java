package com.sure.mapnote;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/* loaded from: classes.dex */
public class BaseActivity extends Activity {
    /* JADX INFO: Access modifiers changed from: protected */
    public int color(int i) {
        return getResources().getColor(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LinearLayout root() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(color(R.color.cream));
        linearLayout.setPadding(Util.dp(this, 16.0f), Util.dp(this, 12.0f), Util.dp(this, 16.0f), Util.dp(this, 12.0f));
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LinearLayout topBar(String str, boolean z) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(0, Util.dp(this, 4.0f), 0, Util.dp(this, 12.0f));
        if (z) {
            TextView textView = new TextView(this);
            textView.setText("‹");
            textView.setTextSize(34.0f);
            textView.setTextColor(color(R.color.blue));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setPadding(0, 0, Util.dp(this, 16.0f), 0);
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.sure.mapnote.BaseActivity.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    BaseActivity.this.onBackPressed();
                }
            });
            linearLayout.addView(textView);
        }
        TextView textView2 = new TextView(this);
        textView2.setText(str);
        textView2.setTextSize(22.0f);
        textView2.setTextColor(color(R.color.textPrimary));
        textView2.setTypeface(Typeface.DEFAULT_BOLD);
        linearLayout.addView(textView2, new LinearLayout.LayoutParams(0, -2, 1.0f));
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Button primaryButton(String str, View.OnClickListener onClickListener) {
        Button button = new Button(this);
        button.setText(str);
        button.setTextSize(16.0f);
        button.setTextColor(-1);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setBackgroundResource(R.drawable.sel_btn_blue);
        button.setPadding(Util.dp(this, 16.0f), Util.dp(this, 12.0f), Util.dp(this, 16.0f), Util.dp(this, 12.0f));
        if (onClickListener != null) {
            button.setOnClickListener(onClickListener);
        }
        return button;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Button ghostButton(String str, View.OnClickListener onClickListener) {
        Button button = new Button(this);
        button.setText(str);
        button.setTextSize(15.0f);
        button.setTextColor(color(R.color.blue));
        button.setAllCaps(false);
        button.setBackgroundResource(R.drawable.sel_btn_ghost);
        button.setPadding(Util.dp(this, 16.0f), Util.dp(this, 10.0f), Util.dp(this, 16.0f), Util.dp(this, 10.0f));
        if (onClickListener != null) {
            button.setOnClickListener(onClickListener);
        }
        return button;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EditText input(String str) {
        EditText editText = new EditText(this);
        editText.setHint(str);
        editText.setTextSize(16.0f);
        editText.setTextColor(color(R.color.textPrimary));
        editText.setHintTextColor(color(R.color.textSub));
        editText.setBackgroundResource(R.drawable.bg_edit);
        editText.setPadding(Util.dp(this, 14.0f), Util.dp(this, 10.0f), Util.dp(this, 14.0f), Util.dp(this, 10.0f));
        editText.setSingleLine(true);
        return editText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TextView label(String str) {
        TextView textView = new TextView(this);
        textView.setText(str);
        textView.setTextSize(14.0f);
        textView.setTextColor(color(R.color.textSub));
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void margin(View view, int i, int i2) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LinearLayout.LayoutParams(-1, -2);
        }
        layoutParams.topMargin = Util.dp(this, i);
        layoutParams.bottomMargin = Util.dp(this, i2);
        view.setLayoutParams(layoutParams);
    }
}
