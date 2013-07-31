/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.joineset.logger;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jozefovovic
 */
public class SmsLogs {

    private static final String SMS_LOG_FILE = "SmsLog.txt";

    private List<String> logs;

    public void readLogs() {
        File file = null;
        BufferedReader br = null;

        try {
            file = new File(Environment.getExternalStoragePublicDirectory(""), SMS_LOG_FILE);
            br = new BufferedReader(new FileReader(file));
            logs = new ArrayList<String>();
            String line;
            String item = "";
            while ((line = br.readLine()) != null) {
                item = item + line;
                if(line.isEmpty()) {
                    logs.add(item);
                    item = "";
                }
            }

            br.close();

        } catch (FileNotFoundException fnfex) {
            Log.w(fnfex.getMessage(), fnfex);
        } catch (IOException ioex) {
            Log.w(ioex.getMessage(), ioex);
        }
    }

    public List<String> getLogs(){
        return logs;
    }

    public List<String> getLogTitles(){

        List<String> titles = new ArrayList<String>(logs);

        for(int i = 0; i < titles.size(); i++) {
            titles.set(i, titles.get(i).substring(0, 40) );
        }

        return titles;
    }
}
