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

package org.protocoderrunner.apprunner.api.network;

import com.codebutler.android_websockets.SocketIOClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.NativeArray;
import org.protocoderrunner.apidoc.annotation.ProtoMethod;
import org.protocoderrunner.apidoc.annotation.ProtoMethodParam;
import org.protocoderrunner.apprunner.api.other.WhatIsRunning;
import org.protocoderrunner.utils.MLog;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PSocketIOClient {

    private String TAG = "PSocketIOClient";
    private SocketIOClient mSocketIOClient;
    private connectSocketIOCB mCallbackfn;

    // --------- connectSocketIO ---------//
    interface connectSocketIOCB {
        // void event(String string, String reason, String string2);
        void event(String string, String event, JSONArray arguments);
    }


    public PSocketIOClient(String uri) {
        SocketIOClient.Handler handler = new SocketIOClient.Handler() {

            @Override
            public void onMessage(String message) {
                if (mCallbackfn != null) mCallbackfn.event("onMessage", null, null);
                //MLog.d("qq", "onMessage");
            }

            @Override
            public void onJSON(JSONObject json) {

            }

            @Override
            public void onError(Exception error) {
                if (mCallbackfn != null) mCallbackfn.event("error", null, null);
            }

            @Override
            public void onDisconnect(int code, String reason) {
                if (mCallbackfn != null) mCallbackfn.event("disconnect", reason, null);
                // MLog.d("qq", "disconnected");
            }

            @Override
            public void onConnect() {
                if (mCallbackfn != null) mCallbackfn.event("connected", null, null);
                // MLog.d("qq", "connected");
            }

            @Override
            public void onConnectToEndpoint(String s) {

            }

            @Override
            public void on(String event, JSONArray arguments) {
                if (mCallbackfn != null) mCallbackfn.event("on", event, arguments);
                // MLog.d("qq", "onmessage");

            }
        };


        try {
            mSocketIOClient = new SocketIOClient(new URI(uri), handler);
            mSocketIOClient.connect();
            WhatIsRunning.getInstance().add(this);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }


    @ProtoMethod(description = "Sends a JSONObject to the destination", example = "")
    @ProtoMethodParam(params = {"jsonObject"})
    public PSocketIOClient emit(JSONObject jsonMessage) throws JSONException {
        mSocketIOClient.emit(jsonMessage);

        return this;
    }


    @ProtoMethod(description = "Sends an array to the destination", example = "")
    @ProtoMethodParam(params = {"message", "array"})
    public PSocketIOClient emit(String message, NativeArray array) {
        try {
            JSONArray jsonArray = new JSONArray();
            MLog.d(TAG, "mSocketIOClient " + mSocketIOClient);
            try {
                jsonArray.put(array);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSocketIOClient.emit(message, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }


    @ProtoMethod(description = "Sends an array to the destination", example = "")
    @ProtoMethodParam(params = {"message"})
    public PSocketIOClient emit(String message) {
        mSocketIOClient.emit(message);

        return this;
    }


    @ProtoMethod(description = "Receiving callback", example = "")
    @ProtoMethodParam(params = {"function(data)"})
    public PSocketIOClient onNewData(final connectSocketIOCB callbackfn) {
        mCallbackfn = callbackfn;

        return this;
    }

    public void stop() {
        try {
            mSocketIOClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
