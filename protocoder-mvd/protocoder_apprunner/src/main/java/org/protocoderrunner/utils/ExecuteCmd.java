/*
* Part of Protocoder http://www.protocoder.org
* A prototyping platform for Android devices 
*
* Copyright (C) 2013 Victor Diaz Barrales victormdb@gmail.com
* 
* Protocoder is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Protocoder is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with Protocoder. If not, see <http://www.gnu.org/licenses/>.
*/

package org.protocoderrunner.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.protocoderrunner.apidoc.annotation.ProtoMethod;
import org.protocoderrunner.apprunner.api.other.WhatIsRunning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteCmd {

    private static final String TAG = "ExecuteCmd";

    public interface ExecuteCommandCB {
        void event(String buffer);
    }

    private final String cmd;
    private final ExecuteCommandCB callbackfn;

    private Handler mHandler;
    private Thread mThread;

    public ExecuteCmd(final String cmd, final ExecuteCommandCB callbackfn) {
        this.cmd = cmd;
        this.callbackfn = callbackfn;

        WhatIsRunning.getInstance().add(this);
    }

    private void initThread() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
            Looper.prepare();

            int count = 0;
            String str = "";
            try {
                //execute the command
                final Process process = Runtime.getRuntime().exec(cmd);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));

                //handler that can stop the thread
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                    process.destroy();

                    mThread.interrupt();
                    mThread = null;

                    try {
                        process.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    }
                };

                //read the lines
                int i;
                final char[] buffer = new char[4096];
                StringBuffer output = new StringBuffer();

                while ((i = reader.read(buffer)) > 0) {
                    output.append(buffer, 0, i);

                    Handler h = new Handler(Looper.getMainLooper());
                    final int finalI = i;
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            callbackfn.event(finalI + " " + String.valueOf(buffer));
                        }
                    });

                }
                reader.close();

                str = output.toString();
            } catch (IOException e) {
                // Log.d(TAG, "Error");
                e.printStackTrace();
            }
            Looper.loop();
            }

        });
    }

    public ExecuteCmd start() {
        initThread();
        mThread.start();

        return this;
    }

    @ProtoMethod(description = "stop the running command", example = "")
    public ExecuteCmd stop() {
        Message msg = mHandler.obtainMessage();
        msg.arg1 = 0;
        mHandler.dispatchMessage(msg);
        // mHandler.

        return this;
    }
}