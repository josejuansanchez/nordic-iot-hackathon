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

import org.apache.commons.lang3.RandomStringUtils;

public class StrUtils {

    public static String generateRandomString() {
        String id = RandomStringUtils.randomAlphabetic(8);

        return id;
    }

    public static String bytetostring(byte[] b) {
        int j = 0, in = 0;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (byte element : b) {
            in = element & 0x0f;
            j = (in >> 4) & 0x0f;
            out += hex[j];
            j = in & 0xf;
            out += hex[j];

        }

        return out;
    }

}
