package trebon.landry.timernotifications;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class RandomValueService extends Service {
    private Random random;
    private RandomValueBinder randomValueBinder;
    private IBinder mBinder;
    private int value;
    private boolean generate;

    public int getValue() {
        return value;
    }

    private void produceRandomInt() {
        // Permet de modifier le nombre en parallèle de son utilisation
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (generate) {
                    value = random.nextInt(61);
                    try { Thread.sleep(1000); }
                    catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }).start();
    }

    public RandomValueService() {
        random = new Random();
        mBinder = new RandomValueBinder();
        generate = true;
    }

    @Override
    public void onCreate() {
        produceRandomInt();
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        // Plus besoin de générer de nombre random quand le service est détruit
        generate = false;
    }

    public class RandomValueBinder extends Binder {
        RandomValueService getService() {
            return RandomValueService.this;
        }
    }
}
