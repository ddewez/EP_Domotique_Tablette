package trebon.landry.timernotifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class TimerTask extends AsyncTask<Object, Void, Void> {
    private int timeNumber = 0;
    private boolean stop = false;
    private int notifID;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected Void doInBackground(Object... params) {
        timeNumber = (int) params[0];
        Notification.Builder notifBuilder = (Notification.Builder) params[1];
        final MainActivity main = (MainActivity) params[2];
        notifID = (int) System.currentTimeMillis();
        NotificationManager notifManager = (NotificationManager) main.getSystemService(main.NOTIFICATION_SERVICE);
        Vibrator mVibrator = (Vibrator) main.getSystemService(main.VIBRATOR_SERVICE);

        Log.i("TimerApp", "doInBackground: Notification ID : " + notifID);

        Intent addIntent = new Intent("ADD_CMD_"+notifID);
        PendingIntent addPendingIntent = PendingIntent.getBroadcast(main, 0, addIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        notifBuilder.addAction(R.drawable.ic_circle_fill, "Add 20s", addPendingIntent);

        Intent stopIntent = new Intent("STOP_CMD_"+notifID);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(main, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        notifBuilder.addAction(R.drawable.ic_circle_fill, "Stop", stopPendingIntent);

        IntentFilter filter = new IntentFilter();
        filter.addAction("ADD_CMD_"+notifID);
        filter.addAction("STOP_CMD_"+notifID);
        main.registerReceiver(new TimerReceiver(), filter);

        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("TimerApp", "onClick: You are killing time!");
                Toast.makeText(main, "You are killing time!", Toast.LENGTH_LONG).show();
            }
        });

        for (int i = 0; i < timeNumber && !stop; i++) {
            String text = "You are killing time since: "+ (i+1) +"s!\n"+ (timeNumber-i-1) +" seconds left!";

            notifBuilder.setSmallIcon(R.drawable.ic_circle_fill);
            notifBuilder.setContentTitle(main.getString(R.string.notif_title));
            notifBuilder.setContentText(text);
            notifBuilder.setProgress(timeNumber, i + 1, false);

            notifManager.notify(notifID, notifBuilder.build());

            //mVibrator.vibrate(100);

            try { Thread.sleep(1000); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        notifManager.cancel(notifID);

        return null;
    }


    private class TimerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Récupération de l'action
            String cmd = intent.getAction();
            Log.i("TimerApp", "onReceive: Command received : " + cmd);

            if(cmd.equals("ADD_CMD_"+notifID)) timeNumber += 20;
            else if(cmd.equals("STOP_CMD_"+notifID)) stop = true;
        }
    }
}
