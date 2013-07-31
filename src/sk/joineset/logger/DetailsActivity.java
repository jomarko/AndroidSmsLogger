/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.joineset.logger;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 *
 * @author Jozefovovic
 */
public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            DetailsFragment details = DetailsFragment.newInstance(getIntent().getExtras().getInt("index"), getIntent().getExtras().getString("detail"));
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }


}
