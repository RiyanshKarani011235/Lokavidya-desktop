/*
 * Copyright (c) 2011 Denis Tulskiy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package Hotkey.common;

import com.sun.jna.Platform;

import Hotkey.osx.CarbonProvider;
import Hotkey.windows.WindowsProvider;
import Hotkey.x11.X11Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory.*;
import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main interface to global hotkey providers
 * <br>
 * Author: Denis Tulskiy
 * Date: 6/12/11
 */
public abstract class Provider {
    private static final Logger LOGGER = LoggerFactory.getLogger(Provider.class);

    static {
        System.setProperty("jna.nosys", "true");
    }

    private boolean useSwingEventQueue;

    /**
     * Get global hotkey provider for current platform
     *
     * @param useSwingEventQueue whether the provider should be using Swing Event queue or a regular thread
     * @return new instance of Provider, or null if platform is not supported
     * @see X11Provider
     * @see WindowsProvider
     * @see CarbonProvider
     */
    public static Provider getCurrentProvider(boolean useSwingEventQueue) {
        Provider provider;
        if (Platform.isX11()) {
        	System.out.println("Hotkey.common.provider : platform is X11");
            provider = new X11Provider();
        } else if (Platform.isWindows()) {
        	System.out.println("Hotkey.common.provider : platform in Windows");
            provider = new WindowsProvider();
        } else if (Platform.isMac()) {
        	System.out.println("Hotkey.common.provider : platform is Macosx");
            provider = new CarbonProvider();
        } else {
        	System.out.println("Hotkey.common.provider : No suitable provider for " + System.getProperty("os.name"));
            LOGGER.warn("No suitable provider for " + System.getProperty("os.name"));
            return null;
        }
        provider.setUseSwingEventQueue(useSwingEventQueue);
        provider.init();
        return provider;

    }

    private ExecutorService eventQueue;


    /**
     * Initialize provider. Starts main thread that will listen to hotkey events
     */
    protected abstract void init();

    /**
     * Stop the provider. Stops main thread and frees any resources.
     * <br>
     * all hotkeys should be reset before calling this method
     *
     * @see Provider#reset()
     */
    public void stop() {
        if (eventQueue != null)
            eventQueue.shutdown();
    }

    /**
     * Reset all hotkey listeners
     */
    public abstract void reset();

    /**
     * Register a global hotkey. Only keyCode and modifiers fields are respected
     *
     * @param keyCode  KeyStroke to register
     * @param listener listener to be notified of hotkey events
     * @see KeyStroke
     */
    public abstract void register(KeyStroke keyCode, HotKeyListener listener);

    /**
     * Register a media hotkey. Currently supported media keys are:
     * <br>
     * <ul>
     * <li>Play/Pause</li>
     * <li>Stop</li>
     * <li>Next track</li>
     * <li>Previous Track</li>
     * </ul>
     *
     * @param mediaKey media key to register
     * @param listener listener to be notified of hotkey events
     * @see MediaKey
     */
    public abstract void register(MediaKey mediaKey, HotKeyListener listener);

    /**
     * Helper method fro providers to fire hotkey event in a separate thread
     *
     * @param hotKey hotkey to fire
     */
    protected void fireEvent(HotKey hotKey) {
        HotKeyEvent event = new HotKeyEvent(hotKey);
        if (useSwingEventQueue) {
            SwingUtilities.invokeLater(event);
        } else {
            if (eventQueue == null) {
                eventQueue = Executors.newSingleThreadExecutor();
            }
            eventQueue.execute(event);
        }
    }

    public void setUseSwingEventQueue(boolean useSwingEventQueue) {
        this.useSwingEventQueue = useSwingEventQueue;
    }

    private class HotKeyEvent implements Runnable {
        private HotKey hotKey;

        private HotKeyEvent(HotKey hotKey) {
            this.hotKey = hotKey;
        }

        public void run() {
            hotKey.listener.onHotKey(hotKey);
        }
    }

}
