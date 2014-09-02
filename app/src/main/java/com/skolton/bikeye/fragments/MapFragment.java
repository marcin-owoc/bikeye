package com.skolton.bikeye.fragments;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.SKAnnotation;
import com.skobbler.ngx.map.SKAnnotationText;
import com.skobbler.ngx.map.SKAnnotationView;
import com.skobbler.ngx.map.SKCircle;
import com.skobbler.ngx.map.SKCoordinateRegion;
import com.skobbler.ngx.map.SKMapCustomPOI;
import com.skobbler.ngx.map.SKMapPOI;
import com.skobbler.ngx.map.SKMapSettings;
import com.skobbler.ngx.map.SKMapSurfaceListener;
import com.skobbler.ngx.map.SKMapSurfaceView;
import com.skobbler.ngx.map.SKMapViewHolder;
import com.skobbler.ngx.map.SKPOICluster;
import com.skobbler.ngx.map.SKPolygon;
import com.skobbler.ngx.map.SKPolyline;
import com.skobbler.ngx.map.SKScreenPoint;
import com.skobbler.ngx.map.realreach.SKRealReachListener;
import com.skobbler.ngx.navigation.SKNavigationListener;
import com.skobbler.ngx.navigation.SKNavigationState;
import com.skobbler.ngx.poitracker.SKDetectedPOI;
import com.skobbler.ngx.poitracker.SKPOITrackerListener;
import com.skobbler.ngx.poitracker.SKTrackablePOIType;
import com.skobbler.ngx.positioner.SKCurrentPositionListener;
import com.skobbler.ngx.positioner.SKCurrentPositionProvider;
import com.skobbler.ngx.positioner.SKPosition;
import com.skobbler.ngx.routing.SKRouteListener;
import com.skobbler.ngx.routing.SKRouteManager;
import com.skobbler.ngx.routing.SKRouteSettings;
import com.skobbler.ngx.versioning.SKMapUpdateListener;
import com.skolton.bikeye.BikeyeApplication;
import com.skolton.bikeye.R;
import com.skolton.bikeye.animations.ResizeAnimation;
import com.skolton.bikeye.util.DemoUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by x220 on 2014-08-26.
 */
public class MapFragment extends Fragment implements SKMapSurfaceListener, SKRouteListener, SKNavigationListener,
        SKRealReachListener, SKPOITrackerListener, SKCurrentPositionListener, SensorEventListener, SKMapUpdateListener {

    BikeyeApplication app;

    /**
     * Surface view for displaying the map
     */
    private SKMapSurfaceView mapView;


    /**
     * Currently elected annotation
     */
    private SKAnnotation selectedAnnotation;

    /**
     * Tells if a navigation is ongoing
     */
    private boolean navigationInProgress;


    /**
     * Current position provider
     */
    private SKCurrentPositionProvider currentPositionProvider;

    /**
     * Current position
     */
    private SKPosition currentPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        app = (BikeyeApplication) getActivity().getApplication();

        currentPositionProvider = new SKCurrentPositionProvider(getActivity());
        currentPositionProvider.setCurrentPositionListener(this);

        if (DemoUtils.hasGpsModule(getActivity())) {
            currentPositionProvider.requestLocationUpdates(true, true, true);
        }

        SKMapViewHolder mapViewGroup = (SKMapViewHolder) rootView.findViewById(R.id.map_surface_holder);
        mapView = mapViewGroup.getMapSurfaceView();
        mapView.setMapSurfaceListener(this);
        mapView.getMapSettings().setFollowerMode(SKMapSettings.SKMapFollowerMode.NONE);

        mapView.getMapSettings().setCompassPosition(new SKScreenPoint(10,70));
        mapView.getMapSettings().setCompassShown(true);
        //add an annotation

        //annotation.setImagePath(app.getMapResourcesDirPath() + "bike.png");
        //annotation.setImageSize(40);
        /*//add a polyline
        SKPolyline polyline = new SKPolyline();
        ArrayList nodes = new ArrayList();
        nodes.add(new SKCoordinate(17.3303, 51.1167));
        nodes.add(new SKCoordinate(17.3503, 51.1267));
        nodes.add(new SKCoordinate(17.3803, 51.1567));
        polyline.setNodes(nodes);
        polyline.setColor(new float[] { 0f, 0f, 1f, 1f });
        polyline.setOutlineColor(new float[] { 0f, 0f, 1f, 1f });
        polyline.setOutlineSize(4);
        polyline.setOutlineDottedPixelsSolid(3);
        polyline.setOutlineDottedPixelsSkip(3);
        mapView.addPolyline(polyline);
        //add a polygon
        SKPolygon polygon = new SKPolygon();
        List nodes1 = new ArrayList();
        nodes1.add(new SKCoordinate(17.3303, 51.1167));
        nodes1.add(new SKCoordinate(17.3333, 51.1187));
        nodes1.add(new SKCoordinate(17.3353, 51.1190));
        polygon.setNodes(nodes1);
        polygon.setOutlineSize(3);
        polygon.setOutlineColor(new float[] { 1f, 0f, 0f, 1f });
        polygon.setColor(new float[] { 1f, 0f, 0f, 0.2f });
        mapView.addPolygon(polygon);*/


       /* //add a circle overlay
        SKCircle circleMask = new SKCircle();
        circleMask.setMaskedObjectScale(1.3f);
        circleMask.setColor(new float[] { 1f, 1f, 0.5f, 0.67f });
        circleMask.setOutlineColor(new float[] { 0f, 0f, 0f, 1f });
        circleMask.setOutlineSize(3);
        circleMask.setCircleCenter(new SKCoordinate(17.0333, 51.1167));
        circleMask.setRadius(300);

       /* mapView.addCircle(circleMask);*/
        ArrayList annotations = new ArrayList();
        SKAnnotation annotationStart = new SKAnnotation();
        annotationStart.setUniqueID(12);
/*        SKAnnotationText text = new SKAnnotationText();
        text.setText("Start");
        annotationStart.setText(text);*/
        /*ImageView imgView = new ImageView(getActivity());
        imgView.setImageResource( R.drawable.ic_launcher);
        SKAnnotationView annView = new SKAnnotationView();
        annView.setView(imgView);
        annotationStart.setAnnotationView(annView);
        annView.setProperSize(32);*/
        annotationStart.setLocation(new SKCoordinate(17.3303, 51.1167));
        SKAnnotation annotationEnd = new SKAnnotation();
        annotationEnd.setUniqueID(14);
        annotationEnd.setLocation(new SKCoordinate(17.3403, 51.1367));
        annotations.add(annotationStart);
        annotations.add(annotationEnd);

        //annotation.setMininumZoomLevel(1);
        annotationStart.setAnnotationType(SKAnnotation.SK_ANNOTATION_TYPE_MARKER);
        Iterator<SKAnnotation> it = annotations.iterator();
        while(it.hasNext())
        {
            mapView.addAnnotation(it.next());
        }

        // get a route settings object and populate it with the desired properties
        SKRouteSettings route = new SKRouteSettings();
        // set start and destination points
        route.setStartCoordinate(new SKCoordinate(17.3303, 51.1167));
        route.setDestinationCoordinate(new SKCoordinate(17.3403, 51.1367));
        // set the number of routes to be calculated
        route.setNoOfRoutes(1);
        // set the route mode
        route.setRouteMode(SKRouteSettings.SKROUTE_CAR_FASTEST);
        // set whether the route should be shown on the map after it's computed
        route.setRouteExposed(true);
        // set the route listener to be notified of route calculation
        // events
        SKRouteManager.getInstance().setRouteListener(this);
        // pass the route to the calculation routine
        SKRouteManager.getInstance().calculateRoute(route);


//        mapPopup = (CustomCalloutView) findViewById(R.id.map_popup);
        //centers the map on position
       // mapView.centerMapOnPosition(new SKCoordinate(52.2167, 21.033));




        applySettingsOnMapView();
        app.setMapView(mapView);
        // LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

       /* Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastKnownLocation!=null)
            mapView.centerMapOnPosition(new SKCoordinate(lastKnownLocation.getLongitude(),lastKnownLocation.getLatitude()));*/


        return rootView;
    }

    /**
     * Customize the map view
     */
    private void applySettingsOnMapView() {
        mapView.getMapSettings().setMapRotationEnabled(true);
        mapView.getMapSettings().setMapZoomingEnabled(true);
        mapView.getMapSettings().setMapPanningEnabled(true);
        mapView.getMapSettings().setZoomWithAnchorEnabled(true);
        mapView.getMapSettings().setInertiaRotatingEnabled(true);
        mapView.getMapSettings().setInertiaZoomingEnabled(true);
        mapView.getMapSettings().setInertiaPanningEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();


        EditText searchField = (EditText) getActivity().findViewById(R.id.searchField);
        searchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.results_container);
                ResizeAnimation animation=null;

                if(linearLayout.getHeight()>0)
                    animation = new ResizeAnimation(linearLayout, linearLayout.getHeight(), 0, true);
                else
                    animation = new ResizeAnimation(linearLayout, 0, 400, true);

                animation.setDuration(200);
                linearLayout.startAnimation(animation);
            }
        });

    }
    @Override
    public void onActionPan() {}

    @Override
    public void onActionZoom() {}

    @Override
    public void onAnimationsFinished(SKMapSurfaceView.SKAnimationType animationType, boolean inertial) {}

    @Override
    public void onAnnotationSelected(SKAnnotation annotation) {

        Toast.makeText(getActivity(),"Pin clicked !", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompassSelected() {}

    @Override
    public void onCustomPOISelected(SKMapCustomPOI customPoi) {}

    @Override
    public void onDoubleTap(SKScreenPoint point) {}

    @Override
    public void onInternationalisationCalled(int result) {}

    @Override
    public void onInternetConnectionNeeded() {}

    @Override
    public void onLongPress(SKScreenPoint point) {}

    @Override
    public void onMapActionDown(SKScreenPoint point) {}

    @Override
    public void onMapActionUp(SKScreenPoint point) {}

    @Override
    public void onMapPOISelected(SKMapPOI mapPOI) {}

    @Override
    public void onMapRegionChangeEnded(SKCoordinateRegion mapRegion) {}

    @Override
    public void onMapRegionChangeStarted(SKCoordinateRegion mapRegion) {}

    @Override
    public void onMapRegionChanged(SKCoordinateRegion mapRegion) {}

    @Override
    public void onOffportRequestCompleted(int requestId) {}

    @Override
    public void onPOIClusterSelected(SKPOICluster poiCluster) {}

    @Override
    public void onRotateMap() {}

    @Override
    public void onScreenOrientationChanged() {}

    @Override
    public void onSingleTap(SKScreenPoint point) {}

    @Override
    public void onSurfaceCreated() {

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                View chessBackground = getActivity().findViewById(R.id.chess_board_background);
                chessBackground.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCurrentPositionUpdate(SKPosition skPosition) {

    }

    @Override
    public void onNewVersionDetected(int i) {

    }

    @Override
    public void onMapVersionSet() {

    }

    @Override
    public void onVersionFileDownloadTimeout() {

    }

    @Override
    public void onNoNewVersionDetected() {

    }

    @Override
    public void onDestinationReached() {

    }

    @Override
    public void onSignalNewAdvice(String[] strings, boolean b) {

    }

    @Override
    public void onSpeedExceeded(String[] strings, boolean b) {

    }

    @Override
    public void onUpdateNavigationState(SKNavigationState skNavigationState) {

    }

    @Override
    public void onReRoutingStarted() {

    }

    @Override
    public void onFreeDriveUpdated(String s, String s2, int i, double v, double v2) {

    }

    @Override
    public void onVisualAdviceChanged(boolean b, boolean b2, SKNavigationState skNavigationState) {

    }

    @Override
    public void onTunnelEvent(boolean b) {

    }

    @Override
    public void onUpdatePOIsInRadius(double v, double v2, int i) {

    }

    @Override
    public void onReceivedPOIs(SKTrackablePOIType skTrackablePOIType, List<SKDetectedPOI> skDetectedPOIs) {

    }

    @Override
    public void onRealReachCalculationCompleted(int i, int i2, int i3, int i4) {

    }

    @Override
    public void onRouteCalculationCompleted(int i, int i2, int i3, boolean b, int i4) {

    }

    @Override
    public void onAllRoutesCompleted() {

    }

    @Override
    public void onServerLikeRouteCalculationCompleted(int i) {

    }

    @Override
    public void onOnlineRouteComputationHanging(int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
