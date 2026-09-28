package com.mocklocation.aa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var latitudeEditText: EditText
    private lateinit var longitudeEditText: EditText
    private lateinit var accuracyEditText: EditText
    private lateinit var setLocationButton: Button
    private lateinit var selectFromMapButton: Button
    private lateinit var startServiceButton: Button
    private lateinit var stopServiceButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var locationManager: LocationManager

    private val PERMISSION_REQUEST_CODE = 100
    private val MAP_SELECT_REQUEST_CODE = 200
    private var isServiceRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        requestPermissions()
    }

    private fun initViews() {
        latitudeEditText = findViewById(R.id.latitudeEditText)
        longitudeEditText = findViewById(R.id.longitudeEditText)
        accuracyEditText = findViewById(R.id.accuracyEditText)
        setLocationButton = findViewById(R.id.setLocationButton)
        selectFromMapButton = findViewById(R.id.selectFromMapButton)
        startServiceButton = findViewById(R.id.startServiceButton)
        stopServiceButton = findViewById(R.id.stopServiceButton)
        statusTextView = findViewById(R.id.statusTextView)

        // 设置默认值
        latitudeEditText.setText("39.90469")
        longitudeEditText.setText("116.40516")
        accuracyEditText.setText("10")

        setLocationButton.setOnClickListener { setMockLocation() }
        selectFromMapButton.setOnClickListener { openMapSelection() }
        startServiceButton.setOnClickListener { startMockLocationService() }
        stopServiceButton.setOnClickListener { stopMockLocationService() }
    }

    private fun openMapSelection() {
        val intent = Intent(this, MapActivity::class.java)
        startActivityForResult(intent, MAP_SELECT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAP_SELECT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val latitude = data.getDoubleExtra("latitude", 0.0)
            val longitude = data.getDoubleExtra("longitude", 0.0)
            latitudeEditText.setText(latitude.toString())
            longitudeEditText.setText(longitude.toString())
            setMockLocation()
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_MOCK_LOCATION
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun setMockLocation() {
        try {
            val latitude = latitudeEditText.text.toString().toDoubleOrNull()
            val longitude = longitudeEditText.text.toString().toDoubleOrNull()
            val accuracy = accuracyEditText.text.toString().toFloatOrNull() ?: 10f

            if (latitude == null || longitude == null) {
                Toast.makeText(this, "请输入有效的经纬度", Toast.LENGTH_SHORT).show()
                return
            }

            // 检查是否有模拟位置权限
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_MOCK_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "缺少模拟位置权限", Toast.LENGTH_SHORT).show()
                return
            }

            // 添加模拟位置提供者
            if (!locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
                locationManager.addTestProvider(
                    LocationManager.GPS_PROVIDER,
                    false,
                    false,
                    false,
                    false,
                    true,
                    true,
                    true,
                    0,
                    5
                )
            }

            locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)

            // 创建位置对象
            val location = Location(LocationManager.GPS_PROVIDER).apply {
                this.latitude = latitude
                this.longitude = longitude
                this.accuracy = accuracy
                this.time = System.currentTimeMillis()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.elapsedRealtimeNanos = System.nanoTime()
                }
            }

            // 设置模拟位置
            locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location)

            statusTextView.text =
                "位置已设置\n纬度: $latitude\n经度: $longitude\n精度: $accuracy m"
            Toast.makeText(this, "虚拟位置已设置", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this, "错误: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMockLocationService() {
        val intent = Intent(this, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        isServiceRunning = true
        Toast.makeText(this, "虚拟定位服务已启动", Toast.LENGTH_SHORT).show()
    }

    private fun stopMockLocationService() {
        val intent = Intent(this, LocationService::class.java)
        stopService(intent)
        isServiceRunning = false
        Toast.makeText(this, "虚拟定位服务已停止", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限已获取", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
