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

package com.sun.jump.command;

/**
 * <code>JUMPExecutiveLifecycleRequest</code> defines all the lifecycle 
 * related requests that originate from the <Code>JUMPExecutive</code>
 */
public interface JUMPExecutiveLifecycleRequest {
    /** 
     * Initialize the target isolate
     * <ol>
     *   <li>args[0] - {@link com.sun.jump.common.JUMPAppModel}</li>
     * </ol>
     * Synchronous request. expects a 
     * {@link com.sun.jump.command.JUMPResponse#Success} or 
     * {@link com.sun.jump.command.JUMPResponse#Failure}
     */
    public static final String ID_INIT_ISOLATE       = "InitIsolate";
    
    /** 
     * Destroys the target isolate.
     * <ol>
     *   <li>args[0] - <code>true</code> means <b>best effort</b> and 
     *                 <code>false</code> means <b>unconditional</b>
     * </ol>
     * {@link com.sun.jump.command.JUMPResponse#Success} or 
     * {@link com.sun.jump.command.JUMPResponse#Failure}
     */
    public static final String ID_DESTROY_ISOLATE    = "DestroyIsolate";
    
    /** 
     * Start the Application to run on the target Isolate. It returns an
     * unique id within the target isolate that represents the application
     * running.
     * <ol>
     *   <li>args[0] - {@link com.sun.jump.common.JUMPApplication}</li>
     * </ol>
     * Synchronous request. expects a 
     * {@link com.sun.jump.command.JUMPResponse#ID_DATA} with contents
     * of the return value as 
     * <ol>
     *   <li>args[0] - Application Id</li>
     * </ol>
     * or 
     * {@link com.sun.jump.command.JUMPResponse#Failure}
     */
    public static final String ID_START_APP      = "StartApp";
    
    /** 
     * Pause the Application running on target Isolate
     * <ol>
     *   <li>args[0] - App Id</li>
     * </ol>
     * Synchronous request. expects a 
     * {@link com.sun.jump.command.JUMPResponse#Success} or 
     * {@link com.sun.jump.command.JUMPResponse#Failure}
     */
    public static final String ID_PAUSE_APP      = "PauseApp";
    
    /** 
     * Resume the Application paused on target Isolate
     * <ol>
     *   <li>args[0] - App Id</li>
     * </ol>
     * Synchronous request. expects a 
     * {@link com.sun.jump.command.JUMPResponse#Success} or 
     * {@link com.sun.jump.command.JUMPResponse#Failure}
     */
    public static final String ID_RESUME_APP    = "ResumeApp";
    
    /** 
     * Get the Windows for the application.
     * <ol>
     *   <li>args[0] - App Id</li>
     *   
     * </ol>
     * Synchronous request. expects a 
     * {@link com.sun.jump.command.JUMPResponse#ID_DATA} with contents
     * of the return value as 
     * <ol>
     *   <li>args[0] - Number of windows that the application has (N) </li>
     *   <li>args[1] - window id</li>
     *   <li>...</li>
     *   <li>args[N] - windows id</li>
     * </ol>
     * or 
     * {@link com.sun.jump.command.JUMPResponse#Failure}
     */
    public static final String ID_GET_APP_WINDOWS  = "GetAppWindows";
    
    /** 
     * Destroys the Application.
     * <ol>
     *   <li>args[0] - App Id</li>
     *   <li>args[1] - <code>true</code> means <b>best effort</b> and 
     *                 <code>false</code> means <b>unconditional</b>
     * </ol>
     * Synchronous request. expects a
     * {@link com.sun.jump.command.JUMPResponse#Success} or 
     * {@link com.sun.jump.command.JUMPResponse#Failure}
     */
    public static final String ID_DESTROY_APP    = "DestroyApp";
    
    
    public static final JUMPRequest PauseApp   = 
        JUMPRequest.newInstance(ID_PAUSE_APP);
    public static final JUMPRequest ResumeApp  = 
        JUMPRequest.newInstance(ID_RESUME_APP);
    
}
