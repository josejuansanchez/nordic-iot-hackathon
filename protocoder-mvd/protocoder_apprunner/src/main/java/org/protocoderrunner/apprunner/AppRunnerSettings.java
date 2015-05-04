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

package org.protocoderrunner.apprunner;

import org.mozilla.javascript.Scriptable;
import org.protocoderrunner.project.Project;

import java.io.File;

public class AppRunnerSettings {

    private static AppRunnerSettings instance;
    public AppRunnerInterpreter interp;
    public Project project;
    public boolean hasUi = false;
    public boolean hasCustomJSInterpreter = true;


    public static AppRunnerSettings get() {
        if (instance == null)
            instance = new AppRunnerSettings();

        return instance;
    }

    public Scriptable newArray() {
        return interp.interpreter.mainScriptContext.newArray(interp.interpreter.scope, 0);
    }


    public Scriptable newArray(File[] files) {
        return interp.interpreter.mainScriptContext.newArray(interp.interpreter.scope, files);
    }

}
