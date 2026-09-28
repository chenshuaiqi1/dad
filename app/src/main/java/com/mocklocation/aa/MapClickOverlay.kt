package com.mocklocation.aa

import android.content.Context
import android.view.MotionEvent
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.util.GeoPoint

class MapClickOverlay(
    private val context: Context,
    private val onLocationSelected: (latitude: Double, longitude: Double) -> Unit
) : Overlay() {

    override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView): Boolean {
        val projection = mapView.projection
        val geoPoint = projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint

        onLocationSelected(geoPoint.latitude, geoPoint.longitude)
        return true
    }
}
