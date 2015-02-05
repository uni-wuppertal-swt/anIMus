package org.watzlawek.views.activities;

import org.watzlawek.R;

import android.app.Activity;
import android.os.Bundle;

public class MultiUserChatActivity extends Activity {

	/**
	 * Initilize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmanager);
	}
}