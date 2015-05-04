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

package org.protocoderrunner.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import org.protocoderrunner.utils.MLog;

import java.util.Vector;

@SuppressLint("NewApi")
public class VideoPlayerFragment extends VideoView {

    private View v;
    private VideoView mVideoView;
    Vector<VideoListener> listeners = new Vector<VideoListener>();
    Runnable r;
    protected Handler handler;
    protected MediaPlayer mp_;
    private final Context c;

    public interface VideoListener {

        public void onReady(boolean ready);

        public void onFinish(boolean finished);

        public void onTimeUpdate(int ms, int totalDuration);
    }

    /**
     * Called when the activity is first created.
     *
     * @return
     */

    public VideoPlayerFragment(Context context) {
        super(context);
        this.c = context;

        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // fl.animate().scaleX(0.5f).scaleY(0.5f).setDuration(5000);
            }
        });
        MLog.d("mm", "onCreateView");

        handler = new Handler();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        for (VideoListener l : listeners) {
            l.onReady(true);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // super.onDestroy();

        // mp_.stop();
        for (VideoListener l : listeners) {
            l = null;
        }
        handler.removeCallbacks(r);
    }

    public void loadExternalVideo(String path) {
        loadVideo(path);
    }

    public void loadResourceVideo(String videoFile) {
        String path = "android.resource://" + c.getPackageName() + videoFile;
        loadVideo(path);
    }

    public void loadVideo(final String path) {
        /*
		 * Alternatively,for streaming media you can use
		 * mVideoView.setVideoURI(Uri.parse(URLstring));
		 */

        mVideoView.setVideoPath(path);
        MediaController mediaController = new MediaController(c);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(null);

        mVideoView.requestFocus();
        mVideoView.setKeepScreenOn(true);

        mVideoView.start();

        mVideoView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                close();
            }
        });

        mVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp_ = mp;
                mp_.setLooping(true);

                // mVideoView.animate().rotation(200).alpha((float) 0.5)
                // .scaleX(0.2f).scaleY(0.2f).setDuration(2000);

                r = new Runnable() {

                    @Override
                    public void run() {
                        for (VideoListener l : listeners) {
                            try {
                                l.onTimeUpdate(mp_.getCurrentPosition(), mp_.getDuration());

                            } catch (Exception e) {
                            }

                        }
                        handler.postDelayed(this, 1000);
                    }
                };

                handler.post(r);
            }
        });

        // mp_.setO

        mVideoView.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                // finish();
                for (VideoListener l : listeners) {
                    l.onFinish(true);
                }

            }
        });

    }

    public void setVolume(float volume) {
        if (mp_ != null) {
            mp_.setVolume(volume, volume);

        }
    }

    public void setLoop(boolean b) {
        mp_.setLooping(b);
    }

    public void close() {
        handler.removeCallbacks(r);
        // mVideoView.stopPlayback();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

        }
        return true;
    }

    public void addListener(VideoListener videoListener) {
        listeners.add(videoListener);
    }

    public void removeListener(VideoListener videoListener) {
        listeners.remove(videoListener);
    }

    @Override
    public void seekTo(int ms) {
        mp_.seekTo(ms);
    }

    @Override
    public int getDuration() {
        return mp_.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mp_.getCurrentPosition();
    }

    public void getVideoWidth() {
        mp_.getVideoWidth();
    }

    public void getVideoHeight() {
        mp_.getVideoHeight();
    }

    public void play() {
        mp_.start();
    }

    @Override
    public void pause() {
        mp_.pause();
    }

    public void stop() {
        mp_.stop();
    }

}
