# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Optimizations: If you don't want to optimize, use the
# proguard-android.txt configuration file instead of this one, which
# turns off the optimization flags.  Adding optimization introduces
# certain risks, since for example not all optimizations performed by
# ProGuard works on all versions of Dalvik.  The following flags turn
# off various optimizations known to have issues, but the list may not
# be complete or up to date. (The "arithmetic" optimization can be
# used if you are only targeting Android 2.0 or later.)  Make sure you
# test thoroughly if you go this route.

-optimizations !code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify


# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
### Android support
-dontwarn org.apache.http.**
-dontwarn android.support.**
-keep class android.support.** {*;}
-keep class android.webkit.** {*;}

### keep options, system default, from android example
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes *Annotation*,InnerClasses
#-keepattributes SourceFile,LineNumberTable

-keep class com.tzw.noah.models.** {*;}

### APP 3rd party jars
-dontwarn com.amap.**
-keep class com.amap.** {*;}
-dontwarn com.autonavi.amap.**
-keep class com.autonavi.amap.** {*;}
-dontwarn com.alibaba.**
-keep class com.alibaba.fastjson.** {*;}

### APP 3rd party jars(xiaomi push)
-dontwarn com.xiaomi.push.**
-keep class com.xiaomi.** {*;}

### APP 3rd party jars(glide)
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

### NIM SDK
-dontwarn com.netease.**
-keep class com.netease.** {*;}
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}
-keep class net.sqlcipher.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}

-keep class **.R$* { *;}

##第三方引用
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.nineoldandroids.animation.** {*;}
## okhttp
-dontwarn okhttp3.**
-keep class okhttp3.** {*;}
-dontwarn okio.**
-keep class okio.** {*;}
-dontwarn com.vondear.rxtools.**
-keep class com.vondear.rxtools.** {*;}

##
-dontwarn tv.danmaku.ijk.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.**
-keep class com.shuyu.gsyvideoplayer.** { *; }

-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}

-dontwarn me.xiaopan.**
-keep class me.xiaopan.**{*;}

-dontwarn org.greenrobot.eventbus.**
-keep class org.greenrobot.eventbus.**{*;}

-dontwarn org.greenrobot.eventbus.**
-keep class org.greenrobot.eventbus.**{*;}

-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}

-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*