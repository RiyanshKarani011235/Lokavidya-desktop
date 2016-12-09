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

package Hotkey;

import gui.Call;
import gui.RecordingFrame;
import gui.Workspace;

import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.KeyStroke;

import Hotkey.common.HotKey;
import Hotkey.common.HotKeyListener;
import Hotkey.common.Provider;

/**
 * Author: Denis Tulskiy
 * Date: 6/13/11
 */
public class Hotkey {
	static int toggle =0;
    public  Hotkey() {
        final Provider provider = Provider.getCurrentProvider(false);
        
        provider.register(KeyStroke.getKeyStroke("control alt D"), new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {
                System.out.println(hotKey);
                provider.reset();
                provider.stop();
            }
        });
        
        provider.register(KeyStroke.getKeyStroke("control P"), new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {
                System.out.println(hotKey);
                System.out.println("P called");
              	RecordingFrame.w.dispose();
            }
        });
        
       /* provider.register(KeyStroke.getKeyStroke("RIGHT"), new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {
            	if(Call.workspace.btnNext.isEnabled())
            	{
            		Workspace.changeSlideRight();
            	}
            }
        });
        
        provider.register(KeyStroke.getKeyStroke("LEFT"), new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {
          //  Workspace.changeSlideLeft();
            }
        });
       
       */
        
        provider.register(KeyStroke.getKeyStroke("control R"), new HotKeyListener() {
            
            public void onHotKey(HotKey hotKey) {
                System.out.println(hotKey);
                System.out.println("called hotkey");
                if(Call.workspace.isActive()){
                    try {
                        // presenter.tray.add(presenter.trayIcon);
                        Call.workspace.setState(Frame.ICONIFIED);
                        // if(Call.workspace.screenRecordingFlag)
                        // Call.workspace.recFrame.showFrame();
                        // frame.showFrame();
                    } catch (Exception ex) {
                        Logger.getLogger(Hotkey.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else { 
                	//frame.hideFrame();
                	try {
                		if(Call.workspace.screenRecordingFlag)
//		                	Call.workspace.recFrame.hideFrame();
		                	Call.workspace.setState(Frame.NORMAL);
//		                	Call.workspace.toFront();  
                	 }
                	 catch (Exception ex) {
                         Logger.getLogger(Hotkey.class.getName()).log(Level.SEVERE, null, ex);
                     }
                }
            }
        });

    }
}
