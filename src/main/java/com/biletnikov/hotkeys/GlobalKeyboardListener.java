package com.biletnikov.hotkeys;

import java.util.EventListener;

/**
 * Hotkeys listener.
 * @author Sergei.Biletnikov
 */
public interface GlobalKeyboardListener extends EventListener {
    
    void onGlobalHotkeysPressed();

}
