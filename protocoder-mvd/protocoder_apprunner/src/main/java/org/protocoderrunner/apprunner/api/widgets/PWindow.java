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

package org.protocoderrunner.apprunner.api.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.protocoderrunner.R;
import org.protocoderrunner.apidoc.annotation.ProtoMethod;
import org.protocoderrunner.apidoc.annotation.ProtoMethodParam;
import org.protocoderrunner.utils.MLog;

public class PWindow extends RelativeLayout implements PViewInterface {

    private static final String TAG = "PWindow";
    private final int currentColor;
    private final int viewCount = 0;
    private final Context c;
    private final RelativeLayout mBar;
    private final LinearLayout mMainContainer;
    private final PWindow mWindow;
    private Button mBtnClose;
    private TextView mTitle;

    public PWindow(Context context) {
        super(context);
        c = context;
        currentColor = Color.argb(255, 255, 255, 255);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pwidget_window, this, true);

        mWindow = this;
        mBar = (RelativeLayout) findViewById(R.id.pWidgetWindowBar);
        mTitle = (TextView) findViewById(R.id.pWidgetWindowTitle);
        mBtnClose = (Button) findViewById(R.id.pWidgetWindowClose);
        mMainContainer = (LinearLayout) findViewById(R.id.pWidgetWindowMainContainer);

        //setOnTouchListener(this);

        mBar.setOnTouchListener(onMoveListener);

    }


    @ProtoMethod(description = "Adds a new view", example = "")
    @ProtoMethodParam(params = {"view"})
    public PWindow addWidget(View v) {
        v.setAlpha(0);
        v.animate().alpha(1).setDuration(500).setStartDelay(100 * (1 + viewCount));

        mMainContainer.addView(v);

        return mWindow;
    }


    @ProtoMethod(description = "Show/hides the window bar", example = "")
    @ProtoMethodParam(params = {"boolean"})
    public PWindow showBar(boolean b) {
        if (b) {
            mBar.setVisibility(View.VISIBLE);
        } else {
            mBar.setVisibility(View.INVISIBLE);
        }

        return mWindow;

    }


    @ProtoMethod(description = "Sets the window title", example = "")
    @ProtoMethodParam(params = {"text"})
    public PWindow setTitle(String text) {
        mTitle.setText(text);

        return mWindow;

    }


    @ProtoMethod(description = "Sets the title color", example = "")
    @ProtoMethodParam(params = {"colorHext"})
    public PWindow setTitleColor(String color) {
        mTitle.setTextColor(Color.parseColor(color));

        return mWindow;
    }


    @ProtoMethod(description = "Sets the bar background color", example = "")
    @ProtoMethodParam(params = {"colorHex"})
    public PWindow setBarBackgroundColor(String color) {
        mBar.setBackgroundColor(Color.parseColor(color));

        return mWindow;
    }


    @ProtoMethod(description = "Sets the background color", example = "")
    @ProtoMethodParam(params = {"colorHex"})
    public PWindow setWindowBackgroundColor(String color) {
        mWindow.setBackgroundColor(Color.parseColor(color));

        return mWindow;
    }


    OnTouchListener onMoveListener = new OnTouchListener() {

        public int x_init;
        public int y_init;

        @Override
        public boolean onTouch(View v, MotionEvent e) {

            //FixedLayout.LayoutParams layoutParams = (Rel.LayoutParams) v.getLayoutParams();

            int action = e.getActionMasked();

            switch (action) {

                case MotionEvent.ACTION_DOWN:
                    //DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    //v.startDrag(null, shadowBuilder, v, 0);
                    //v.setVisibility(View.INVISIBLE);
                    x_init = (int) e.getRawX() - (int) mWindow.getX();
                    MLog.network(getContext(), TAG, "" + x_init + " " + (int) e.getRawX() + " " + (int) mWindow.getX() + " " + (int) mWindow.getLeft());
                    y_init = (int) e.getRawY() - (int) mWindow.getY();

                    break;

                case MotionEvent.ACTION_MOVE:

                    int x_cord = (int) e.getRawX();
                    int y_cord = (int) e.getRawY();

                    mWindow.setX(x_cord - x_init);
                    mWindow.setY(y_cord - y_init);

                    break;
            }

            return true;
        }

    };

}
