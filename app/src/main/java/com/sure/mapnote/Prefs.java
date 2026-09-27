package com.sure.mapnote;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public final class Prefs {
    private static final String NAME = "sure_mapnote";

    private Prefs() {
    }

    public static SharedPreferences sp(Context context) {
        return context.getSharedPreferences(NAME, 0);
    }

    public static String lang(Context context) {
        return sp(context).getString("lang", Lang.ZH);
    }

    public static void setLang(Context context, String str) {
        sp(context).edit().putString("lang", str).apply();
    }

    public static String lastGroup(Context context) {
        return sp(context).getString("last_group", "");
    }

    public static void setLastGroup(Context context, String str) {
        sp(context).edit().putString("last_group", str).apply();
    }

    public static boolean isSeeded(Context context) {
        return sp(context).getBoolean("defaults_seeded", false);
    }

    public static void setSeeded(Context context, boolean z) {
        sp(context).edit().putBoolean("defaults_seeded", z).apply();
    }
}
