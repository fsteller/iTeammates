package com.fsteller.mobile.android.teammatesapp.activities.events;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.activities.base.FragmentMaintenancePageBase;
import com.fsteller.mobile.android.teammatesapp.model.base.IEventsEntity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Project: iTeammates
 * Package: com.fsteller.mobile.android.teammatesapp.activities.events
 * <p/>
 * Description:
 * Created by fsteller on 4/17/14.
 */
public class EventsMaintenancePage3 extends FragmentMaintenancePageBase implements GoogleMap.OnMapLongClickListener {


    //<editor-fold desc="Constants">

    private final static String TAG = EventsMaintenancePage3.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    // Google Map
    private GoogleMap googleMap = null;
    private IEventsEntity mEventEntity = null;
    private Marker[] mMarkers = new Marker[2];
    private int markersCount = 0;

    //</editor-fold>

    //<editor-fold desc="Static">

    //</editor-fold>
    //<editor-fold desc="Overridden">

    //<editor-fold desc="Fragment">

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity mActivity = getActivity();

        if (mActivity != null)
            imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.mEventEntity = (IEventsEntity) mEntity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_events_maintenance_page3, container, false);
        if (rootView != null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.event_map)).getMap();
            if (googleMap != null) {
                googleMap.setBuildingsEnabled(false);
                googleMap.setMyLocationEnabled(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setOnMapLongClickListener(this);

            } else // check if map is created successfully or not
                Toast.makeText(getActivity(), getResources().getString(R.string.loading_maps_error), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    //</editor-fold>
    //<editor-fold desc="GoogleMap.OnMapClickListener">


    @Override
    public void onMapLongClick(LatLng latLng) {
        markersCount++;
        if (markersCount < 2) {
            final String markerTitle = getResources()
                    .getString(markersCount > 1 ? R.string.maps_from : R.string.maps_to);
            final Marker mMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title(markerTitle));

            // Save the marker in order to draw a route between places
            mMarkers[markersCount] = mMarker;

            // Move the camera instantly to hamburg with a zoom of 15.
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }

    }


    //</editor-fold>

    @Override
    protected int getFragmentDefaultImage() {
        return 0;
    }


    //</editor-fold>


}
