package yeeaoo.frist2016_06_15;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * 获取位置、传感器(光照、加速度)
 */
public class MainActivity extends AppCompatActivity {
    private TextView positionTextView, lightLevelTextView;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private String provider;
    private ImageView compass_img;
    private Button pass;
    private SensorEventListener listener = new SensorEventListener() {
        float[] accValues = new float[3];
        float[] magValues = new float[3];
        private float lastRotateDegree;

        @Override
        public void onSensorChanged(SensorEvent event) {
            //values 数组中的第一个小标对应的值就是光照强度
//            float value = event.values[0];
//            lightLevelTextView.setText("Current light level is "+value);
            //获取加速度
//            float xValues = Math.abs(event.values[0]);
//            float yValues = Math.abs(event.values[1]);
//            float zValues = Math.abs(event.values[2]);
//            if(xValues>15||yValues>15||zValues>15){
//                Toast.makeText(MainActivity.this, "恭喜您！摇到了100万大奖", Toast.LENGTH_SHORT).show();
//            }
            //制作的指南针
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {//这是加速度
                accValues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {//这是地磁
                magValues = event.values.clone();
            }
            float[] R = new float[9];
            float[] values = new float[3];
            SensorManager.getRotationMatrix(R, null, accValues, magValues);
            SensorManager.getOrientation(R, values);
            Log.d("data", "values[0] is " + Math.toDegrees(values[0]));
            //计算出旋转角度取反值 来旋转背景图
            float rotateDegree = -(float) Math.toDegrees(values[0]);
            if (Math.abs(rotateDegree - lastRotateDegree) > 1) {
                RotateAnimation rotateA = new RotateAnimation(lastRotateDegree, rotateDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateA.setFillAfter(true);
                compass_img.startAnimation(rotateA);
                lastRotateDegree = rotateDegree;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        positionTextView = (TextView) findViewById(R.id.position_text_view);
        lightLevelTextView = (TextView) findViewById(R.id.light_level);
        compass_img = (ImageView) findViewById(R.id.compass_img);
        pass = (Button) findViewById(R.id.pass);
        //位置 经纬度
        if (getAddress()) return;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //光照传感器
//        getLight();
        //加速度传感器
//        getAccelerate();
        //获取方向传感器
        getOrientation();
        //使用parcel传值
        pass();
    }

    /**
     * 传值
     */
    private void pass() {
        findViewById(R.id.pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = new Person();
                person.setName("lin");
                person.setAge(18);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AnotherActivity.class);
                intent.putExtra("person", person);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取手机方向传感器(通过加速度传感器和地磁传感器)
     */
    private void getOrientation() {
        Sensor sensor_acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor sensor_mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(listener, sensor_acc, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener, sensor_mag, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * 获取手机加速度
     */
    private void getAccelerate() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 获取光照强度
     */
    private void getLight() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 获取经纬度
     *
     * @return
     */
    private boolean getAddress() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            //当前没有可用的位置提供器时,弹出Toast提示用户
            Toast.makeText(MainActivity.this, "No Location Provider to use", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return true;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //显示设置的位置信息
            showLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(locationListener);
            if (sensorManager != null) {
                sensorManager.unregisterListener(listener);
            }
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //更新当前设备的位置信息
            showLocation(location);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void showLocation(Location location) {
        String currentPosition = "latitude is " + location.getLatitude() + "\n"
                + "longitude is " + location.getLongitude();
        positionTextView.setText(currentPosition);
    }
}
