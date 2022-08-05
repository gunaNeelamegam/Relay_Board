// Native methods implementation of
// /home/user/NetBeansProjects/AsteriskCall/src/testNative/testNative.java

#include <stdlib.h>

#include "testNative.h"

jintArray JNICALL Java_testNative_testNative_callingSystem
(JNIEnv * env, jclass object) {

}

jintArray JNICALL Java_testNative_testNative_CallingSystem
(JNIEnv * env, jobject object, jintArray arr1) {

    jint len = (*env)-> GetArrayLength(env, arr1);
    jint carray[len];
    jint jarray[len];
    (*env)->SetIntArrayRegion(env, arr1, 0, len, carray);
    for (int i = 0; i < len; i++) {
        if (carray[i] % 2 == 0) {
            carray[i] = 0;
            jarray[i] = carray[i];
            EXIT_SUCCESS;
        } else {
            puts("Number is Not the Even Number :");
        }
    }
    jintArray jar =(*env)->NewIntArray(env,len);
    (*env)->SetIntArrayRegion(env,jar,0,len,jarray);
    return jar;
}
