# Add project specific ProGuard rules here.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.jeremylakeyjr.lanbulab.**$$serializer { *; }
-keepclassmembers class com.jeremylakeyjr.lanbulab.** {
    *** Companion;
}
-keepclasseswithmembers class com.jeremylakeyjr.lanbulab.** {
    kotlinx.serialization.KSerializer serializer(...);
}
