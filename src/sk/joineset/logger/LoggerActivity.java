package sk.joineset.logger;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoggerActivity extends Activity
{
    private static final String TAG = "LoggerActivity";
    private static final String SMS_FILTER_FILE = "SmsFilter.txt";
    private IntentFilter intentFilter;
    private SmsReceiver smsReceiver;

    public LoggerActivity() {
        smsReceiver = new SmsReceiver();
        smsReceiver.setFilter( readFilterFile() );

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
    }


    @Override
    public void onResume() {
    super.onResume();  // Always call the superclass method first

        registerReceiver(smsReceiver, intentFilter);

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);

        Toast.makeText(
                getApplicationContext(),
                getResources().getText(R.string.logger_start),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();

        unregisterReceiver(smsReceiver);

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);

        Toast.makeText(
                getApplicationContext(),
                getResources().getText(R.string.logger_stop),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();    
        android.os.Debug.stopMethodTracing();
    }

    private List<String> readFilterFile() {
        
        File file = null;
        BufferedReader br = null;
        List<String> filter = null;
        
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(""), SMS_FILTER_FILE);
            br = new BufferedReader(new FileReader(file));
            filter = new ArrayList<String>();
            String line;
            while ((line = br.readLine()) != null) {
                filter.add(line);
            }

            br.close();
            
        } catch (FileNotFoundException fnfex) {
            Log.w(TAG, fnfex.getMessage(), fnfex);
        } catch (IOException ioex) {
            Log.w(TAG, ioex.getMessage(), ioex);
        }

        return filter;
    }
}
