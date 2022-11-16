package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.view.SurfaceView.*;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private List<JoystickListener> joystickCallback;

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int source);
    }


    private void setupDimensions() {
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        baseRadius = Math.min(getWidth(), getHeight()) / 3f;
        hatRadius = Math.min(getWidth(), getHeight()) / 5f;
    }
    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = new ArrayList<>();
            joystickCallback.add((JoystickListener) context);
        }

    }


    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener){
            joystickCallback = new ArrayList<>();
            joystickCallback.add((JoystickListener) context);
        }
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener){
            joystickCallback = new ArrayList<>();
            joystickCallback.add((JoystickListener) context);
        }
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener){
            joystickCallback = new ArrayList<>(2);
            joystickCallback.add((JoystickListener) context);
        }
    }














    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void drawJoystick(float newX, float newY) {
        if(getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint color = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // To clear the canvas.

            color.setARGB(255, 50, 50, 50); // Light gray, solid (not transparent)
            myCanvas.drawCircle(centerX, centerY, baseRadius, color);
            color.setARGB(255, 0, 0, 255);
            myCanvas.drawCircle(newX, newY, hatRadius, color);
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.equals(this)) { // accepts only touches that touch the joystick, this surfaceView.
            float curr_x = motionEvent.getX(), curr_y = motionEvent.getY();
            float displacement = (float) Math.sqrt(Math.pow(curr_x - centerX, 2) + Math.pow(curr_y - centerY, 2));
            if(motionEvent.getAction() != MotionEvent.ACTION_UP) { // as long as it's not lifting finger from the screen (not touching anymore), and it's currently touching our surfaceview.
                if (displacement < baseRadius) {
                    drawJoystick(curr_x, curr_y);
                    for (JoystickListener jl : joystickCallback) {
                        jl.onJoystickMoved((curr_x - centerX) / baseRadius, (curr_y - centerY) / baseRadius, getId());
                    }
                }
                else { // Out of bounds of base circle.. calculate ratio as similar triangles
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (curr_x - centerX) * ratio;
                    float constrainedY = centerY + (curr_y - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    for (JoystickListener jl : joystickCallback) {
                        jl.onJoystickMoved((constrainedX - centerX) / baseRadius,
                                (constrainedY - centerY) / baseRadius, getId());
                    }
                }
            } else {
                drawJoystick(centerX, centerY);
                for (JoystickListener jl : joystickCallback) {
                    jl.onJoystickMoved(0, 0, getId());
                }
            }
        }
        return true;
    }

    public JoystickView addJoystickListener(JoystickListener jl) {
        if (jl != null)
            joystickCallback.add(jl);
        for(JoystickListener joy : joystickCallback) {
            Log.d("Joysticklistener", joy.toString());
        }
        return this;
    }
}
