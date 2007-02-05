/*
 * %W% %E%
 *
 * Copyright  1990-2006 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version
 * 2 only, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included at /legal/license.txt).
 *
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa
 * Clara, CA 95054 or visit www.sun.com if you need additional
 * information or have any questions.
 */

package com.sun.jumpimpl.executive;

import com.sun.jump.presentation.JUMPLauncher;
import com.sun.jump.executive.JUMPExecutive;
import com.sun.jump.executive.JUMPUserInputManager;
import com.sun.jump.executive.JUMPIsolateProxy;
import com.sun.jump.executive.JUMPApplicationProxy;
import com.sun.jump.message.JUMPMessagingService;
import com.sun.jump.message.JUMPMessageDispatcher;
import com.sun.jump.message.JUMPOutgoingMessage;
import com.sun.jump.message.JUMPMessage;
import com.sun.jump.common.JUMPAppModel;

import com.sun.jump.module.lifecycle.JUMPLifeCycleModuleFactory;
import com.sun.jump.module.lifecycle.JUMPLifeCycleModule;

import com.sun.jumpimpl.process.JUMPProcessProxyImpl;
import com.sun.jumpimpl.process.JUMPModulesConfig;

import com.sun.jump.os.JUMPOSInterface;

import com.sun.jump.common.JUMPContent;
import com.sun.jump.common.JUMPApplication;
import com.sun.jump.module.installer.JUMPInstallerModuleFactory;
import com.sun.jump.module.installer.JUMPInstallerModule;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

public class JUMPExecutiveImpl extends JUMPExecutive {
    private JUMPProcessProxyImpl pp;
    private JUMPOSInterface os;
    
    // name of a propoerty which points to name of a propoerty file which
    // overrides default modules configuration
    private final static String PROPERTY_FILE_NAME_PROP 
        = "runtime-properties-file";

    private void
    handleCommandLine(String[] args) {
        // remove default value (if any)
        JUMPModulesConfig.getProperties().remove(PROPERTY_FILE_NAME_PROP);

        for(int i = 0; i < args.length; ++i) {
            if("--config-file".equals(args[i])) {
                if(!(++i < args.length) || args[i] == null) {
                    throw new IllegalArgumentException(
                        "configuration file not specified");
                }
                JUMPModulesConfig.overrideDefaultConfig(args[i]);

                // put name of a property file overriding default properties
                // in the configuration map hopeing JUMPLifeCycleModule 
                // implementation will hook it up
                JUMPModulesConfig.getProperties().put(
                    PROPERTY_FILE_NAME_PROP, args[i]);
            }
        }
    }
    
    public JUMPExecutiveImpl() {
        super();
    }
    
    /*
     * Main entry point to the executive
     */
    public static void main(String[] args) {
        JUMPExecutiveImpl jei = new JUMPExecutiveImpl();
        
        jei.handleCommandLine(args);
        
        // Initialize os interface
        new com.sun.jumpimpl.os.JUMPOSInterfaceImpl();
        
        // Get critical objects
        jei.os = JUMPOSInterface.getInstance();
        jei.pp = JUMPProcessProxyImpl.createProcessProxyImpl(jei.os.getProcessID());
        
        JUMPFactories.init(JUMPModulesConfig.getProperties());
        
        if (false) {
            // Sample code to create blank isolate upon startup
            JUMPLifeCycleModuleFactory lcmf =
                    JUMPLifeCycleModuleFactory.getInstance();
            JUMPLifeCycleModule lcm = lcmf.getModule();
            JUMPIsolateProxy ip = lcm.newIsolate(JUMPAppModel.XLET);
            System.err.println("New isolate created="+ip);
            
            JUMPInstallerModuleFactory imf =
                    JUMPInstallerModuleFactory.getInstance();
            JUMPInstallerModule xletInstaller =
                    imf.getModule(JUMPAppModel.XLET);
            JUMPContent[] content = xletInstaller.getInstalled();
            if (content != null) {
                for (int i = 0; i < content.length; i++) {
                    JUMPApplication app = (JUMPApplication)content[i];
                    System.err.println("App["+i+"] = "+app);
                }
                JUMPApplicationProxy appProxy = ip.startApp((JUMPApplication)content[0], null);
                System.err.println("Executive started app="+appProxy);
            } else {
                System.err.println("No content available");
                System.exit(1);
            }
        }
        
        // Take it away, someone -- presentation mode?
        try {
            JUMPLauncher l = getLauncher();
            if (l != null) {
                l.start();
            }
            Thread.sleep(0L);
        } catch(Throwable e) {
        }
    }
    
    public static JUMPLauncher getLauncher() {
        JUMPLauncher launcher = null;
        String launcherClass = (String)JUMPModulesConfig.getProperties().get("jump.launcher");
        if (launcherClass != null) {
            try {
                Class c = Class.forName(launcherClass);
                Object obj = c.newInstance();
                if (obj instanceof com.sun.jump.presentation.JUMPLauncher) {
                    launcher = (JUMPLauncher) c.newInstance();
                } else {
                    System.err.println("Could not instantiate jump.launcher class: " + launcherClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return launcher;
    }
    
    public int
            getProcessId() {
        return os.getProcessID();
    }
    
    public JUMPMessageDispatcher
            getMessageDispatcher() {
        return pp.getMessageDispatcher();
    }
    
    public JUMPOutgoingMessage
            newOutgoingMessage(String mesgType) {
        return pp.newOutgoingMessage(mesgType);
    }
    
    public JUMPOutgoingMessage
            newOutgoingMessage(JUMPMessage requestMessage) {
        return pp.newOutgoingMessage(requestMessage);
    }
    
    public JUMPMessage
            newMessage(byte[] rawData) {
        return pp.newMessage(rawData);
    }
    
    public JUMPUserInputManager getUserInputManager() {
        return null;
    }
}
