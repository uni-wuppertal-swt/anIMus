package org.watzlawek;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * Class of the activity for displaying the chatlist.
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-08
 */
public class ChatlistActivity extends ListActivity{
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlistlight);
	}
}
