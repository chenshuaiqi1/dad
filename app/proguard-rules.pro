# This file contains the ProGuard configuration for the application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

# Keep all classes in the app package
-keep class com.mocklocation.aa.** { *; }
