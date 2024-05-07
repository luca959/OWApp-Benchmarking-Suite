package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

public class MyAppMonitorService extends AccessibilityService {
    private WindowManager windowManager;
    private View overlayView;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        configureService(); // Example method for configuring the service
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

    }

    private void configureService() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED; // Listen for window state changes
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK; // Provide feedback for all types of events
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS; // Report view IDs in accessibility events
        setServiceInfo(info); // Apply the service configuration
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // Check if the package name of the launched app matches your target app
            String packageName = String.valueOf(event.getPackageName());
            Log.d("AppMonitor", "app"+packageName+" is launched");
            // Check if the launched app's package name matches your target app
            if ("com.example.mastg_test0011".equals(packageName)) {
                // Show overlay
                showOverlay();
            }
        }
    }
    private void showOverlay() {
        if (windowManager == null) {
            Log.e("AppMonitor", "WindowManager is null. Cannot add overlay.");
            return;
        }

        // Inflate overlay layout
        LayoutInflater inflater = LayoutInflater.from(this);
        overlayView = inflater.inflate(R.layout.activity_overlay, null);

        // Add overlay view to window manager
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                android.graphics.PixelFormat.TRANSLUCENT
        );

        windowManager.addView(overlayView, params);
    }

    @Override
    public void onInterrupt() {

    }

}
