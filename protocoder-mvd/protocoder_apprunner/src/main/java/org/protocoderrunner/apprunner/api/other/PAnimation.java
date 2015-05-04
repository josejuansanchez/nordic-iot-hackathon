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

package org.protocoderrunner.apprunner.api.other;


import android.view.View;

import org.protocoderrunner.apidoc.annotation.ProtoMethod;
import org.protocoderrunner.apidoc.annotation.ProtoMethodParam;

public class PAnimation {
    private final View myView;

    public PAnimation(View view) {
        WhatIsRunning.getInstance().add(this);

        myView = view;
    }


    @ProtoMethod(description = "", example = "")
    @ProtoMethodParam(params = {""})
    public PAnimation move(float x, float y) {
        myView.animate().x(x).y(y);
        return this;
    }


    @ProtoMethod(description = "", example = "")
    @ProtoMethodParam(params = {""})
    public PAnimation moveBy(float x, float y) {
        myView.animate().xBy(x).yBy(y);
        return this;
    }


    @ProtoMethod(description = "", example = "")
    @ProtoMethodParam(params = {""})
    public PAnimation rotate(float x) {
        myView.animate().rotation(x);
        return this;
    }


    @ProtoMethod(description = "", example = "")
    @ProtoMethodParam(params = {""})
    public PAnimation rotate(float x, float y, float z) {
        //return myView.animate().rotation(x).rotationX(y).rotationY(z);
        return this;
    }


    @ProtoMethod(description = "", example = "")
    @ProtoMethodParam(params = {""})
    public void start() {
        // myView.
    }


    @ProtoMethod(description = "", example = "")
    @ProtoMethodParam(params = {""})
    public void stop() {

    }


}
