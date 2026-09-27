# Add project specific ProGuard rules here.
# This project keeps minify disabled, so no rules are required yet.
# If you enable minification later, keep the WebView JavaScript bridge:
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
