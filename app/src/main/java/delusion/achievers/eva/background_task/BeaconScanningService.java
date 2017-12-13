package delusion.achievers.eva.background_task;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.List;

import delusion.achievers.eva.model.BeaconValues;
import delusion.achievers.eva.utils.ServiceApi;


public class BeaconScanningService extends Service {
    public int counter = 0;

    public BeaconScanningService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public BeaconScanningService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        startTimer();
        scanBeacon();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("delusion.achievers.eva.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
//        stoptimertask();
        beaconManager.stopRanging(region);
    }

    private BeaconManager beaconManager;
    private BeaconRegion region;

    private void scanBeacon() {
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {

            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if (!beacons.isEmpty()) {
                    ArrayList<BeaconValues> beaconValuesList = new ArrayList<>();
                    for (int i = 0; i < beacons.size(); i++) {
                        Beacon nearestBeacon = beacons.get(i);
                        BeaconValues beaconValues = new BeaconValues();
                        beaconValues.setMajor(nearestBeacon.getMajor());
                        beaconValues.setMinor(nearestBeacon.getMinor());
                        beaconValues.setUniqueKey(nearestBeacon.getUniqueKey());
                        beaconValues.setUUID(nearestBeacon.getProximityUUID().toString());
//                        List<String> places = placesNearBeacon(nearestBeacon);
                        beaconValuesList.add(beaconValues);
                        // TODO: update the UI here
                    }

//                        if (beaconValuesList.size() > 0) {
//                            Utils.showLoading(activity);
//                            StringBuilder URL = new StringBuilder(ServiceApi.GET_NAMES);
//                            for (int i = 0; i < beaconValuesList.size(); i++) {
//                                URL.append("uids=").append(beaconValuesList.get(i).getUUID()).append("&");
//                            }
//                            String url = URL.toString().substring(0, URL.toString().length() - 1);
//                            ModelManager.getInstance().getBeaconsManager().getNames(activity, true, url);
//                        }
//                        is_need = true;
                }

            }
        });
        region = new BeaconRegion("ranged region", null, Integer.parseInt(ServiceApi.MAJOR), Integer.parseInt(ServiceApi.MINOR));

    }
//    private Timer timer;
//    private TimerTask timerTask;
//    long oldTime=0;
//    public void startTimer() {
//        //set a new Timer
//        timer = new Timer();
//
//        //initialize the TimerTask's job
//        initializeTimerTask();
//
//        //schedule the timer, to wake up every 1 second
//        timer.schedule(timerTask, 1000, 1000); //
//    }
//
//    /**
//     * it sets the timer to print the counter every x seconds
//     */
//    public void initializeTimerTask() {
//        timerTask = new TimerTask() {
//            public void run() {
//                Log.i("in timer", "in timer ++++  "+ (counter++));
//            }
//        };
//    }
//
//    /**
//     * not needed
//     */
//    public void stoptimertask() {
//        //stop the timer, if it's not already null
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}