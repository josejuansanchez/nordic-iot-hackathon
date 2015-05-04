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

package org.protocoderrunner.apprunner.api.media;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.protocoderrunner.apidoc.annotation.ProtoMethod;
import org.protocoderrunner.apidoc.annotation.ProtoMethodParam;
import org.protocoderrunner.apprunner.AppRunnerActivity;
import org.protocoderrunner.apprunner.AppRunnerSettings;
import org.protocoderrunner.apprunner.PInterface;
import org.protocoderrunner.apprunner.api.other.WhatIsRunning;
import org.protocoderrunner.media.AudioServicePd;
import org.protocoderrunner.utils.MLog;
import org.puredata.android.service.PdService;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.PdDispatcher;

import java.io.File;
import java.util.Arrays;

public class PPureData extends PInterface {

    private String TAG = "PPureData";

    public PPureData(Context c) {
        super(c);
        //setActivity(c);
    }


    // --------- initPDPatch ---------//
    public interface initPDPatchCB {
        void event(PDReturn o);
    }

    class PDReturn {
        String type;
        protected String source;
        protected Object data;

    }

    public void initPatch(String fileName) {
        // create and install the dispatcher
        PdDispatcher dispatcher = new PdUiDispatcher() {

            @Override
            public void print(String s) {
                Log.i("Pd print", s);
            }

        };

        PdBase.setReceiver(dispatcher);
        PdBase.subscribe("android");

        String filePath = AppRunnerSettings.get().project.getStoragePath() + File.separator + fileName;
        AudioServicePd.file = filePath;

        Intent intent = new Intent(getContext(), PdService.class);
        (getContext()).bindService(intent, AudioServicePd.pdConnection, Context.BIND_AUTO_CREATE);

        initSystemServices();

        WhatIsRunning.getInstance().add(this);
        WhatIsRunning.getInstance().add(AudioServicePd.pdConnection);
    }

    public PPureData onNewData(final initPDPatchCB callbackfn) {

        PdUiDispatcher receiver = new PdUiDispatcher() {

            @Override
            public void print(String s) {
                MLog.d(TAG, "pd >>" + s);

                PDReturn o = new PDReturn();
                o.type = "print";
                o.data = s;

                callbackfn.event(o);
            }

            @Override
            public void receiveBang(String source) {
                MLog.d(TAG, "bang");

                PDReturn o = new PDReturn();
                o.type = "bang";
                o.source = source;

                callbackfn.event(o);
            }

            @Override
            public void receiveFloat(String source, float x) {
                MLog.d(TAG, "float: " + x);

                PDReturn o = new PDReturn();
                o.type = "float";
                o.source = source;
                o.data = x;

                callbackfn.event(o);
            }

            @Override
            public void receiveList(String source, Object... args) {
                MLog.d(TAG, "list: " + Arrays.toString(args));

                JSONArray jsonArray = new JSONArray();
                for (Object arg : args) {
                    jsonArray.put(arg);
                }

                PDReturn o = new PDReturn();
                o.type = "list";
                o.source = source;
                o.data = jsonArray;

                callbackfn.event(o);
            }

            @Override
            public void receiveMessage(String source, String symbol, Object... args) {
                MLog.d(TAG, "message: " + Arrays.toString(args));

                JSONArray jsonArray = new JSONArray();
                for (Object arg : args) {
                    jsonArray.put(arg);
                }

                PDReturn o = new PDReturn();
                o.type = "message";
                o.source = source;
                o.data = jsonArray;

                callbackfn.event(o);
            }

            @Override
            public void receiveSymbol(String source, String symbol) {
                MLog.d(TAG, "symbol: " + symbol);

                PDReturn o = new PDReturn();
                o.type = "symbol";
                o.source = source;
                o.data = symbol;

                callbackfn.event(o);
            }

        };


        //PdBase.setReceiver(receiver);
        // start pure data sound engine


        return this;
    }


    @ProtoMethod(description = "Sends a message to PdLib", example = "")
    @ProtoMethodParam(params = {"message", "value"})
    public void sendMessage(String message, String value) {
        if (value.isEmpty()) {
            PdBase.sendBang(message);
        } else if (value.matches("[0-9]+")) {
            PdBase.sendFloat(message, Float.parseFloat(value));
        } else {
            PdBase.sendSymbol(message, value);
        }
    }


    @ProtoMethod(description = "Sends a bang to PdLib", example = "")
    @ProtoMethodParam(params = {"name"})
    public void sendBang(String name) {
        PdBase.sendBang(name);
    }


    @ProtoMethod(description = "Sends a float number to PdLib", example = "")
    @ProtoMethodParam(params = {"name", "value"})
    public void sendFloat(String name, int value) {
        PdBase.sendFloat(name, value);
    }


    @ProtoMethod(description = "Sends a note to PdLib", example = "")
    @ProtoMethodParam(params = {"channel", "pitch, velocity"})
    public void sendNoteOn(int channel, int pitch, int velocity) {
        PdBase.sendNoteOn(channel, pitch, velocity);
    }


    @ProtoMethod(description = "Sends a midibyte to PdLib", example = "")
    @ProtoMethodParam(params = {"port", "value"})
    public void sendMidiByte(int port, int value) {
        PdBase.sendMidiByte(port, value);
    }


    @ProtoMethod(description = "Gets an array from PdLib", example = "")
    @ProtoMethodParam(params = {"name", "size"})
    public float[] getArray(String source, int n) {
        // public void getArray(float[] destination, int destOffset, String
        // source, int srcOffset, int n) {
        // PdBase.readArray(destination, destOffset, source, srcOffset, n);

        float[] destination = new float[n];
        PdBase.readArray(destination, 0, source, 0, n);

        return destination;
    }


    @ProtoMethod(description = "Sends and array of floats to PdLib", example = "")
    @ProtoMethodParam(params = {"name", "array", "size"})
    public void sendArray(String destination, float[] source, int n) {
        PdBase.writeArray(destination, 0, source, 0, n);
    }

    public void stop() {
        PdBase.release();
    }


    private void initSystemServices() {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (AudioServicePd.pdService == null) {
                    return;
                }
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    AudioServicePd.start();
                } else {
                    AudioServicePd.pdService.stopAudio();
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

}
