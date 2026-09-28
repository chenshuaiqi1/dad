package com.mocklocation.aa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.util.GeoPoint

class MapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var confirmButton: Button
    private var selectedLatitude = 39.90469
    private var selectedLongitude = 116.40516

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)
        confirmButton = findViewById(R.id.confirmButton)

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(GeoPoint(selectedLatitude, selectedLongitude))

        // 地图点击事件监听
        mapView.overlays.add(MapClickOverlay(this) { latitude, longitude ->
            selectedLatitude = latitude
            selectedLongitude = longitude
        })

        confirmButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("latitude", selectedLatitude)
            resultIntent.putExtra("longitude", selectedLongitude)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
