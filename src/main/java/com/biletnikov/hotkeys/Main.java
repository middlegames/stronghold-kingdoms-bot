package com.biletnikov.hotkeys;

import java.awt.event.KeyEvent;

/**
 * Testing!
 * @author Sergei.Biletnikov
 */
public class Main{

    public static void main(String[] argv) {
        GlobalKeyboardHook hook = new GlobalKeyboardHook();
        // Let me define the following hotkeys: CTRL + ALT + H
        int vitrualKey = KeyEvent.VK_H;
        boolean CTRL_Key = true;
        boolean ALT_Key = true;
        boolean SHIFT_Key = false;
        boolean WIN_Key = false;
        //
        hook.setHotKey(vitrualKey, ALT_Key, CTRL_Key, SHIFT_Key, WIN_Key);
        hook.startHook();
        // waiting for the event
        hook.addGlobalKeyboardListener(new GlobalKeyboardListener() {
            public void onGlobalHotkeysPressed() {
                System.out.println("CTRL + ALT + H    was pressed");
            }
        });
        System.out.println("The program waiting for CTRL+ALT+H hotkey...");
    }
}
