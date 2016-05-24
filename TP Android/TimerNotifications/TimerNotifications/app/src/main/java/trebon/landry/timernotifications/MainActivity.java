package trebon.landry.timernotifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RandomValueService randomValueService;
    private boolean mBound = false;
    private static int timeHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static int timeMinute = Calendar.getInstance().get(Calendar.MINUTE);
    AlarmManager alarmManager;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RandomValueService.RandomValueBinder binder = (RandomValueService.RandomValueBinder) service;
            randomValueService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, RandomValueService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, RandomValueService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Définition de l'action lors du click sur le bouton
        Button actionBtn = (Button) findViewById(R.id.action_btn);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText timeEdit = (EditText) findViewById(R.id.time_number);
                int time;

                if(timeEdit.getText().toString().matches("")) time = randomValueService.getValue();
                else time = Integer.parseInt(timeEdit.getText().toString());

                Log.i("TimerApp", "onClick: Time : " + time + "s");
                Notification.Builder notifBuilder = new Notification.Builder(MainActivity.this);

                TimerTask task = new TimerTask();
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, time, notifBuilder, MainActivity.this);
            }
        });

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Définition de l'action lors du click sur le bouton
        Button alarmBtn = (Button) findViewById(R.id.alarm_btn);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText timeAlarm = (EditText) findViewById(R.id.time_alarm);
                timeAlarm.getText();
                setAlarm();

            }
        });


    }

    private void setAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
