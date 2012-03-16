/*
 *	Global Keyboard hook
 *	
 *	
 *	JNI Interface for setting a Keyboard Hook and monitoring
 *	it Java-side
 *
 */

#include <process.h> 
#include "windows.h"
#include "Winuser.h"
#include "jni.h"
#include "com_biletnikov_hotkeys_GlobalKeyboardHook.h"

#pragma data_seg(".HOOKDATA") //Shared data among all instances.

static HHOOK hotKeyHook = NULL;
static HANDLE g_hModule = NULL;

const int HOT_KEY_ID = 0xBBBC;
static int hotKeyRegisterStatus = 0;

// Hot key settings
static int keyModifiers = 0;
static int virtualKey = 0;
//
static int hotKeyPressedFlag = 0;

HANDLE messageThreadEvent = NULL;
HANDLE messageThread = NULL;
DWORD messageThreadID;

#pragma data_seg() 
 
#pragma comment(linker, "/SECTION:.HOOKDATA,RWS")
 
 
/*
 * Class:     de_alfah_popup_jni_KeyboardHookThread
 * Method:    checkHotKey
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_biletnikov_hotkeys_GlobalKeyboardHook_checkHotKey
(JNIEnv *env, jobject obj)
{
	int pressedFlag = hotKeyPressedFlag;
	hotKeyPressedFlag = 0;
	return pressedFlag != 0 ? JNI_TRUE : JNI_FALSE;
}

DWORD WINAPI HotKeyListener(LPVOID lpParameter)
{
	hotKeyRegisterStatus = RegisterHotKey(NULL, HOT_KEY_ID, keyModifiers, virtualKey);

	SetEvent(messageThreadEvent);
	if (hotKeyRegisterStatus !=0)
	{
		MSG msg = {0};
		while (GetMessage(&msg, NULL, 0, 0) != 0)
		{			
			switch (msg.message)
			{
				case WM_HOTKEY: 
					//MessageBox(NULL,"The thread was shutdown","Message",MB_OK | MB_TOPMOST);
					hotKeyPressedFlag = 1;			
					break;
			}
		} 

	}
	return hotKeyRegisterStatus;;
}

// Reset hot keys by terminating the message thread
static void resetHotKey()
{
	UnregisterHotKey(NULL, HOT_KEY_ID);
	if (messageThread != NULL)
	{		
		PostThreadMessage((DWORD) messageThreadID, (UINT) WM_QUIT, 0, 0);
//		TerminateThread (messageThread, 0);
		CloseHandle(messageThread);
		messageThread = NULL;
		messageThreadID = NULL;
	}
	if (messageThreadEvent != NULL) {
		CloseHandle(messageThreadEvent);
		messageThreadEvent = NULL;
	}
	hotKeyRegisterStatus = 0;
}

// Sets new hot key
static int setNewHotKey()
{
	messageThreadEvent = CreateEvent( NULL, TRUE, TRUE, NULL );
	ResetEvent( messageThreadEvent );

	messageThread = CreateThread(NULL, 0, HotKeyListener, 0,0,&messageThreadID);

	WaitForSingleObject( messageThreadEvent, INFINITE );

	return hotKeyRegisterStatus;
}
 

/*
 * Class:     de_alfah_popup_jni_KeyboardHookThread
 * Method:    setHotKey
 * Signature: (IZZZZ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_biletnikov_hotkeys_GlobalKeyboardHook_setHotKey
  (JNIEnv *, jobject, jint vk, jboolean altKey, jboolean ctrlKey, jboolean shiftKey, jboolean winKey)
{
	// define modifiers
	int modifiers = 0;

	if (altKey == JNI_TRUE)
	{
		modifiers = MOD_ALT;
	} if (ctrlKey == JNI_TRUE) {		
		modifiers = modifiers | MOD_CONTROL;
	} if (shiftKey == JNI_TRUE) {
		modifiers = modifiers | MOD_SHIFT;
	} if (winKey == JNI_TRUE) {
		modifiers = modifiers | MOD_WIN;
	}
	// set key setings
	keyModifiers = modifiers;
	virtualKey = vk;

	// reset previous hot key
	resetHotKey();
	//
	int status = setNewHotKey();

	return status == 0 ? JNI_FALSE : JNI_TRUE;
}

/*
 * Class:     de_alfah_popup_jni_KeyboardHookThread
 * Method:    resetHotKey
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_biletnikov_hotkeys_GlobalKeyboardHook_resetHotKey
  (JNIEnv * env, jobject)
{
	resetHotKey();
}

// The main DLL 
BOOL APIENTRY DllMain( HANDLE hModule, DWORD  ul_reason_for_call, LPVOID lpReserved )
{
	switch(ul_reason_for_call)
	{
		case DLL_PROCESS_ATTACH:
			g_hModule = hModule;
			return TRUE; 
		case DLL_PROCESS_DETACH:			
			resetHotKey();
			return TRUE;
	}
 
    return TRUE;
}
