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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.protocoderrunner.apidoc.annotation.ProtoMethod;
import org.protocoderrunner.apidoc.annotation.ProtoMethodParam;
import org.protocoderrunner.apprunner.PInterface;
import org.protocoderrunner.project.ProjectManager;

import java.util.ArrayList;

public class PSqLite extends PInterface {

    String TAG = "PSqlite";
    private SQLiteDatabase db;

    public PSqLite(Context a, String dbName) {
        super(a);

        open(dbName);
        WhatIsRunning.getInstance().add(this);
    }


    @ProtoMethod(description = "Open a SQLite ", example = "")
    @ProtoMethodParam(params = {"dirName"})
    public void open(String dbName) {
        db = getContext().openOrCreateDatabase(
                ProjectManager.getInstance().getCurrentProject().getStoragePath() + "/" + dbName, getContext().MODE_PRIVATE,
                null);

    }


    @ProtoMethod(description = "Executes a SQL sentence", example = "")
    @ProtoMethodParam(params = {"sql"})
    public void execSql(String sql) {
        db.execSQL(sql);
    }

    // http://stackoverflow.com/questions/8830753/android-sqlite-which-query-query-or-rawquery-is-faster

    @ProtoMethod(description = "Querys the database in the given array of columns and returns a cursor", example = "")
    @ProtoMethodParam(params = {"table", "colums[]"})
    public Cursor query(String table, String[] columns) {
        for (String column : columns) {
            //MLog.d("qq", column);

        }
        Cursor c = db.query(table, columns, null, null, null, null, null);

        return c;
    }


    @ProtoMethod(description = "Close the database connection", example = "")
    @ProtoMethodParam(params = {})
    public void close() {
        db.close();
    }


    @ProtoMethod(description = "Deletes a table in the database", example = "")
    @ProtoMethodParam(params = {"tableName"})
    public void delete(String table) {
        this.db.delete(table, null, null);

    }


    @ProtoMethod(description = "Exectutes SQL sentence in the database", example = "")
    @ProtoMethodParam(params = {"table", "fields"})
    public void insert(String table, ArrayList<DBDataType> fields) {

        String names = "";
        String values = "";

        for (int i = 0; i < fields.size(); i++) {
            if (i != fields.size() - 1) {
                names = names + " " + fields.get(i).name + ",";
                values = values + " " + fields.get(i).obj.toString();
            } else {
                names = names + "" + fields.get(i).name;
                values = values + " " + fields.get(i).obj.toString();
            }
        }

        Log.d(TAG, " " + names + " " + values);

        db.execSQL("INSERT INTO " + table + " (" + names + ")" + " VALUES (" + values + ")");

    }

    public void stop() {
        if (db != null) {
            db.close();
        }
    }

    public class DBDataType {

        String name;
        Object obj;

        public DBDataType(String name, Object obj) {
            this.name = name;
            this.obj = obj;

        }
    }

}
