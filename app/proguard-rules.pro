# Add project specific ProGuard rules here.
# You can control the default rules by using the -dontobfuscate, -dontoptimize,
# -dontshrink, and -dontwarn options.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep all classes in the Hilt generated packages.
-keep class com.androidforge.streakhappit.core.ads.** { *; }
-keep class com.androidforge.streakhappit.core.broadcast.** { *; }
-keep class com.androidforge.streakhappit.core.notifications.** { *; }

# Hilt
-keep class dagger.hilt.android.HiltAndroidApp { *; }
-keep class dagger.hilt.android.internal.managers.ApplicationComponentManager
-keep class dagger.hilt.android.internal.managers.ActivityComponentManager
-keep class dagger.hilt.android.internal.managers.FragmentComponentManager
-keep class dagger.hilt.android.internal.managers.ViewComponentManager
-keep class dagger.hilt.android.internal.managers.ServiceComponentManager
-keep class dagger.hilt.android.internal.managers.BroadcastReceiverComponentManager
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponentManager { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponentManagerHolder { *; }
-keep class * implements dagger.hilt.android.internal.builders.ActivityComponentBuilder { *; }
-keep class * implements dagger.hilt.android.internal.builders.FragmentComponentBuilder { *; }
-keep class * implements dagger.hilt.android.internal.builders.ServiceComponentBuilder { *; }
-keep class * implements dagger.hilt.android.internal.builders.ViewComponentBuilder { *; }
-keep class * implements dagger.hilt.android.internal.builders.BroadcastReceiverComponentBuilder { *; }

# Room Persistence Library
-keep class androidx.room.RoomDatabase { *; }
-keep class androidx.room.Entity { *; }
-keep class androidx.room.PrimaryKey { *; }
-keep class androidx.room.ForeignKey { *; }
-keep class androidx.room.Index { *; }
-keep class androidx.room.Dao { *; }
-keep class androidx.room.TypeConverter { *; }
-keep class androidx.room.Database { *; }
-keep class androidx.room.ColumnInfo { *; }
-keep class androidx.room.Query { *; }
-keep class androidx.room.Insert { *; }
-keep class androidx.room.Update { *; }
-keep class androidx.room.Delete { *; }
-keep class androidx.room.RawQuery { *; }
-keep class androidx.room.Transaction { *; }
-keep class androidx.room.Embedded { *; }
-keep class androidx.room.Relation { *; }
-keep class androidx.room.Ignore { *; }
-keep class androidx.room.Junction { *; }

# For Room's generated code
-keep class * extends androidx.room.RoomDatabase { *
    <init>(...);
}
-keep class * implements androidx.room.IMultiInstanceInvalidationService { *; }
-keep class androidx.room.InvalidationTracker { *; }
-keep class androidx.room.RoomWarnings { *; }

# Jetpack Compose
-keep class * extends androidx.compose.ui.tooling.preview.PreviewParameterProvider { *; }
-keep class androidx.compose.ui.tooling.ComposeViewAdapter {
    <init>(...);
    void setView(...);
    void generateAndSetView(...);
}

# AdMob
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.mediation.** { *; }
-keep public class com.google.android.gms.common.GooglePlayServicesUtil {
    public static java.lang.String getErrorString(int);
    public static int isGooglePlayServicesAvailable(android.content.Context);
    public static int GooglePlayServicesRepairableException(int, java.lang.String, android.content.Intent);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient { *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info { *; }

# Coroutines
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }

# Timber
-dontwarn timber.log.**
-keep class timber.log.** { *; }

# General Kotlin reflection/serialization (if applicable)
-keepattributes Signature
-keepattributes *Annotation*
-keep class kotlin.Metadata { *; }