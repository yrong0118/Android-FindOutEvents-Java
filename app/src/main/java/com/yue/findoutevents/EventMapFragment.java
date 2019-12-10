package com.yue.findoutevents;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventMapFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener  {

    private MapView mMapView;
    private View mView;

    private DatabaseReference database;
    private List<Event> events;

    private GoogleMap mGoogleMap;


    public EventMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_event_map, container,
                false);

        database = FirebaseDatabase.getInstance().getReference();
        events = new ArrayList<Event>();

        return mView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.event_map_view);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();// needed to get the map to display immediately
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
//        MapsInitializer.initialize(getContext());
//        double latitude = 17.385044;
//        double longitude = 78.486671;
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(latitude, longitude)).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory
//                .newCameraPosition(cameraPosition));

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        mGoogleMap.setOnInfoWindowClickListener(this);


        final LocationTracker locationTracker = new LocationTracker(getActivity());
        locationTracker.getLocation();

        double curLatitude = locationTracker.getLatitude();
        double curLongitude = locationTracker.getLongitude();

        // Set up camera configuration, set camera to latitude = 17.385044, longitude = 78.486671, and set Zoom to 12
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(curLatitude, curLongitude)).zoom(12).build();
        // Animate the zoom process
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        setUpMarkersCloseToCurLocation(googleMap, curLatitude, curLongitude);



//        // Create marker on google map
//        MarkerOptions marker = new MarkerOptions().position(
//                new LatLng(latitude, longitude)).title("This is your focus");
//
//        // Change marker Icon on google map
//        marker.icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
//
//        // Add marker to google map
//        googleMap.addMarker(marker);

    }

    private void setUpMarkersCloseToCurLocation(final GoogleMap googleMap,
                                                final double curLatitude,
                                                final double curLongitude) {
        events.clear();
        database.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get all available events
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Event event = noteDataSnapshot.getValue(Event.class);
                    double destLatitude = event.getLatitude();
                    double destLongitude = event.getLongitude();
                    int distance = Utils.distanceBetweenTwoLocations(curLatitude, curLongitude,
                            destLatitude, destLongitude);
                    if (distance <= 10) {
                        events.add(event);
                    }
                }
                // Set up every events
                for (Event event : events) {
                    // create marker
                    MarkerOptions marker = new MarkerOptions().position(
                            new LatLng(event.getLatitude(), event.getLongitude())).
                            title(event.getTitle());

                    // Changing marker icon
                    marker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    // adding marker
                    Marker mker = googleMap.addMarker(marker);
                    mker.setTag(event);


                    // adding marker
                    googleMap.addMarker(marker);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: do something
            }
        });
    }

    private void sendIntent(String eventId){
        Intent intent = new Intent(getContext(),CommentActivity.class);
        intent.putExtra("EventID", eventId);
        startActivity(intent);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Event event = (Event)marker.getTag();
        Intent intent = new Intent(getContext(), CommentActivity.class);
        String eventId = event.getId();
        intent.putExtra("EventID", eventId);
        getContext().startActivity(intent);

    }
}
