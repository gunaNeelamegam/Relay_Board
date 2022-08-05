// Native methods implementation of
// /home/user/NetBeansProjects/StringNative/src/stringnative/StringNative.java



#include "StringNative.h"
#include "string.h"

void JNICALL Java_stringnative_StringNative_println
(JNIEnv * env, jclass object, jstring param1) {
    jboolean bool;
    const char* str = (*env)->GetStringUTFChars(env, param1, &bool);
    printf("%s\n ", str);
}

jstring JNICALL Java_stringnative_StringNative_println__
(JNIEnv * env, jclass object) {
    const char* name = "g";
    jstring str = (*env)->NewStringUTF(env, name);
    //     jstring str1=(*env)->NewStringUTF(env,"gunaNeelamegam");
    return str;
}

jstring JNICALL Java_stringnative_StringNative_println__Ljava_lang_String_2Ljava_lang_String_2
(JNIEnv * env, jclass object, jstring str, jstring str1) {
    jboolean bool, bool1;
    const char* no1 = (*env)-> GetStringUTFChars(env, str, &bool);
    const char* no2 = (*env)-> GetStringUTFChars(env, str1, &bool1);
//    char text[100];
//    jint len1 = strlen(no1);
//    jint len2 = strlen(no2);
//    for (int i = 0; i < len1; i++) {
//        text[i] = no1[i];
//    }
//    for (int i = 0; i < len2; i++) {
//        text[len1 + i] = no2[i];
//    }
//    const char *text1 = text;
    jstring jstr = (*env)->NewStringUTF(env, text1);
    jstring jstr1= (*str)
    return jstr;

}