package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JoystickView.JoystickListener {
    private List<JoystickView.JoystickListener> additionalListeners = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        JoystickCommunication jc = null;
        try {
            jc = new JoystickCommunication("10.0.2.2", 2020);
            additionalListeners.add(jc);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        SurfaceView sv = new JoystickView(this); //, new ArrayList<JoystickView.JoystickListener>());
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        for (JoystickView.JoystickListener jl : additionalListeners) {
            jl.onJoystickMoved(xPercent, yPercent, source);
        }
    }
}