/*
package com.rowaad.utils;


import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

public class TouchableWrapper extends FrameLayout {

    private GoogleMap mGoogleMap = null;

    public TouchableWrapper(Context context) {
        super(context);


    }

    public void setGoogleMap(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);

                long thisTime = System.currentTimeMillis();
                long mLastTouchTime = 0;
                if (thisTime - mLastTouchTime < ViewConfiguration.getDoubleTapTimeout()) {

                    if (mGoogleMap != null) {
                        LatLng zoomCenter = mGoogleMap.getProjection().fromScreenLocation(new Point((int) event.getX(), (int) event.getY()));
                        float currentZoom = mGoogleMap.getCameraPosition().zoom;

                        int mapViewHeight = getHeight();
                        int mapViewWidth = getWidth();

                        Projection projection = mGoogleMap.getProjection();

                        LatLng geographicalPosition = projection.fromScreenLocation(new Point(mapViewWidth / 2, mapViewHeight / 2));

                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(geographicalPosition.latitude, geographicalPosition.longitude), currentZoom + 1));

                    }
                    mLastTouchTime = -1;
                } else {
                    mLastTouchTime = thisTime;
                    mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
                mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
                break;

            case MotionEvent.ACTION_UP:
                mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + (event.getAction() & MotionEvent.ACTION_MASK));
        }

        return super.dispatchTouchEvent(event);
    }




    ScaleGestureDetector mScaleDetector = new ScaleGestureDetector(getContext(),
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {

                private float scaleFactor = 1f;

                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    // store scale factor for detect zoom "direction" on end
                    scaleFactor = detector.getScaleFactor();
                    float currentZoom = mGoogleMap.getCameraPosition().zoom;
                    int mapViewHeight = getHeight();
                    int mapViewWidth = getWidth();
                    LatLng geographicalPosition;
                    if (scaleFactor > 1) {
                        // zoom in detected
                        geographicalPosition = mGoogleMap.getProjection().fromScreenLocation(new Point(mapViewWidth / 2, mapViewHeight / 2));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geographicalPosition, currentZoom + 0.05f));

                    } else if (scaleFactor < 1) {
                        // zoom out detected
                        geographicalPosition = mGoogleMap.getProjection().fromScreenLocation(new Point(mapViewWidth / 2, mapViewHeight / 2));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geographicalPosition, currentZoom - 0.05f));
                    }
                    return true;
                }

                @Override
                public void onScaleEnd(ScaleGestureDetector detector) {

                    super.onScaleEnd(detector);
                }
            });

}

*/
