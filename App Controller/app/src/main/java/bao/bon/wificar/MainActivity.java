package bao.bon.wificar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import bao.bon.wificar.JoyStick.JoyStickClass;

public class MainActivity extends AppCompatActivity {

    WebView webViewsend;
    TextView textView1, textView2, textView3, textView4, textView5, txtSpeed , txtServo;
    JoyStickClass js;
    //Init Seekbar
    SeekBar seekBarSpeed,seekBarServo;
    RelativeLayout layout_joystick;
    Common commonCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initView();
        commonCar = new Common("50", "9","90");
        txtSpeed.setText("Car Speed : "+commonCar.getSpeed());
        txtServo.setText("Angle : " +commonCar.getAngle());
        setupJoystick();
        seebarChanged();
        sendData();
        hideSystemUI();

    }

    private String converttoString() {
        return "Speed=" + commonCar.getSpeed() +
                ",Controller=" + commonCar.getDirection() +
                ",Servo="+commonCar.getAngle() +
                ",";

    }

    private void sendData() {
        webViewsend.setWebViewClient(new WebViewClient());
        webViewsend.loadUrl("http://192.168.4.1/?Makerlab=" + converttoString());
    }

    private void initView() {
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.txtDerec);
        txtServo = findViewById(R.id.textViewServo);
        seekBarServo = findViewById(R.id.seekbarServo);
        txtSpeed = findViewById(R.id.TextViewCarSpeed);
        layout_joystick = findViewById(R.id.layout_joystick);
        seekBarSpeed = findViewById(R.id.seekbarSpeed);
        webViewsend = findViewById(R.id.webview);
    }



    private void seebarChanged() {

        seekBarServo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtServo.setText("Angle : "+i);
                commonCar.setAngle(180-i+"");
                sendData();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtSpeed.setText("Car Speed : " + progress);
                commonCar.setSpeed(progress + "");
                sendData();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendData();
            }
        });

    }

    private void setupJoystick() {
        js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.circlegrey);
        js.setStickSize(150, 150);
        js.setLayoutSize(600, 600);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);
        layout_joystick.setOnTouchListener(onTouchListener());
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                js.drawStick(event);
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_MOVE) {
                    textView1.setText("X : " + String.valueOf(js.getX()));
                    textView2.setText("Y : " + String.valueOf(js.getY()));
                    textView3.setText("Angle : " + String.valueOf(js.getAngle()));
                    textView4.setText("Distance : " + String.valueOf(js.getDistance()));
                    int direction = js.get8Direction();
                    if (direction == JoyStickClass.STICK_UP) {
                        commonCar.setDirection("1");
                        textView5.setText("Direction : UP");
                        sendData();
                    } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                        textView5.setText("Direction : Up Right");
                        commonCar.setDirection("6");
                        sendData();
                    } else if (direction == JoyStickClass.STICK_RIGHT) {
                        textView5.setText("Direction : Right");
                        commonCar.setDirection("3");
                        sendData();
                    } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textView5.setText("Direction : Down Right");
                        commonCar.setDirection("8");
                        sendData();
                    } else if (direction == JoyStickClass.STICK_DOWN) {
                        textView5.setText("Direction : Down");
                        commonCar.setDirection("2");
                        sendData();
                    } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                        commonCar.setDirection("7");
                        sendData();
                    } else if (direction == JoyStickClass.STICK_LEFT) {
                        textView5.setText("Direction : Left");
                        commonCar.setDirection("4");
                        sendData();
                    } else if (direction == JoyStickClass.STICK_UPLEFT) {
                        textView5.setText("Direction : Up Left");
                        commonCar.setDirection("5");
                        sendData();
                    } else if (direction == JoyStickClass.STICK_NONE) {
                        textView5.setText("Direction : Center");
                        commonCar.setDirection("9");
                        sendData();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    commonCar.setDirection("9");
                    sendData();
                    textView5.setText("Direction: Center");
                    textView1.setText("X : " + String.valueOf(js.getX()));
                    textView2.setText("Y : " + String.valueOf(js.getY()));
                    textView3.setText("Angle : " + String.valueOf(js.getAngle()));
                    textView4.setText("Distance : " + String.valueOf(js.getDistance()));
                }
                return true;
            }
        };
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
