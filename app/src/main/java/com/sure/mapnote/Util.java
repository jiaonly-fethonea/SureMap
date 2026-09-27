package com.sure.mapnote;

import android.content.Context;
import android.widget.Toast;
import java.util.UUID;

/* loaded from: classes.dex */
public final class Util {
    private Util() {
    }

    public static int dp(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int sp(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static void toast(Context context, String str) {
        Toast.makeText(context, str, 0).show();
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
