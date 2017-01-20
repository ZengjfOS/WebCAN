LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)


LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under,src)

LOCAL_STATIC_JAVA_LIBRARIES := hyphenatechat gson android-support-v4 

LOCAL_JNI_SHARED_LIBRARIES := libaplex libhyphenate libhyphenate_av 


#LOCAL_SRC_FILES := $(call all-subdir-java-files) \ 

LOCAL_PACKAGE_NAME := WebCan
LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)
##################引用第三方jar包###########################
include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := hyphenatechat:libs/hyphenatechat_3.1.4.jar 
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += gson:libs/gson_2.2.4.jar 
include $(BUILD_MULTI_PREBUILT)
##################引用第三方os库############################
include $(CLEAR_VARS)
LOCAL_STATIC_LIBRARIES := libhyphenate_av
LOCAL_STATIC_LIBRARIES += libhyphenate

LOCAL_PREBUILT_LIBS :=libhyphenate_av:libs/armeabi/libhyphenate_av.so
LOCAL_PREBUILT_LIBS +=libhyphenate:libs/armeabi/libhyphenate.so
include $(BUILD_MULTI_PREBUILT)
###########################################################
# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
LOCAL_PATH:= $(call my-dir)
