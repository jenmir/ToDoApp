package com.jmir.todoapp;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class EditItemActivity extends ActionBarActivity {

	EditText changeobj;
	int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edititem);
		String changethis = getIntent().getStringExtra("changethis");
		position = getIntent().getIntExtra("itemnumber", 0); //default is 0
		changeobj = (EditText) findViewById(R.id.etChange);
		changeobj.setText(changethis);
		changeobj.requestFocus();
		changeobj.setCursorVisible(true);
	}

	public void onSave(View v) {
		  // closes the activity and returns to first screen
		  Intent data = new Intent();
		  // Pass relevant data back as a result
		  data.putExtra("changedit", changeobj.getText().toString());
		  data.putExtra("position", position);
		  // Activity finished ok, return the data
		  setResult(RESULT_OK, data); // set result code and bundle data for response
		  //finish(); // closes the activity, pass data to parent
		
		  this.finish(); 
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
