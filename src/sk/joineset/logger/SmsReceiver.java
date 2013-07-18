/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.joineset.logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jozefovovic
 */
public class SmsReceiver extends BroadcastReceiver{

    private static final String TAG = "SmsReceiver";
    private static final String OUT_FILE = "SmsLog.txt";
    private static final String OUT_DIR = "";
    private List<String> filter;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            SmsMessage[] msgs = null;
            try{
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for(int i=0; i<msgs.length; i++){
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    if(!isInFilter(msgs[i].getDisplayOriginatingAddress())){
                        logSms(msgs[i]);
                    }
                }
            }catch(Exception e){
                Log.w(TAG, e.getMessage(), e);
            }
        }
    }

    private boolean isInFilter(String number){

        if(filter != null){
            for(String filterNumber : filter){
                if(PhoneNumberUtils.compare(filterNumber, number)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void logSms(SmsMessage sms) {
        if(isExternalStorageWritable()){
            try{
                File outFile = new File(Environment.getExternalStoragePublicDirectory(OUT_DIR), OUT_FILE);
                BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, true));

                CharSequence now_seq = DateFormat.format("yyyy-MM-dd hh:mm >", (new Date()).getTime());
                writer.write(now_seq.toString());

                writer.write("From: " + sms.getDisplayOriginatingAddress());
                writer.newLine();

                writer.write("Text: " + sms.getDisplayMessageBody() + "\n");
                writer.newLine();
                
                writer.flush();
                writer.close();
            }
            catch(IOException e)
            {
                Log.w(TAG, e.getMessage(), e);
            }
        } else {
            Log.w(TAG, "Unable to write to external storage: \"//SmsLog.txt\"");
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void setFilter(List<String> fil){
        filter = fil;
    }
}
