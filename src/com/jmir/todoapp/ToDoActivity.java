package com.jmir.todoapp;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class ToDoActivity extends ActionBarActivity {
	
	ArrayList<String> todoItems;
	ArrayAdapter<String> todoAdapter; // linked to xml form
	ListView lvItems;
	EditText etNewItem;
	int editresult = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		etNewItem = (EditText) findViewById(R.id.etNewItem);

		lvItems = (ListView) findViewById(R.id.lvItems);

		//populateArrayItems();
		readItems();
		todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
		lvItems.setAdapter(todoAdapter);
		
		setupListviewLongListener();
		setupListviewClickListener();

	}

	private void readItems(){
		File filesDir = getFilesDir();
		File todofile = new File(filesDir, "todo_list.txt");
		try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todofile));
		
		} catch (IOException e) {
			if (todoItems.isEmpty()) {
			  // no need to warn, first-time usage also has no file
			}
			else {
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, "ToDo Read error", duration);
				toast.show();
				todoItems = new ArrayList<String>();
				e.printStackTrace();
			}
		}
		
	}
	
	private void saveItems(){
		File filesDir = getFilesDir();
		File todofile = new File(filesDir, "todo_list.txt");
		try{ 
			FileUtils.writeLines(todofile, todoItems);
		} catch (IOException e ){
			if (todoItems.isEmpty()) {
			  // no need to warn
			}
			else {
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, "ToDo Write error", duration);
				toast.show();
				e.printStackTrace();
			}
		}
	}
	
	private void setupListviewLongListener(){
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) 
			{
				todoItems.remove(pos);
				todoAdapter.notifyDataSetChanged();
				//todoAdapter.remove(pos); doesn't work, requires string arg not int
				saveItems();
				return true; // why always return true?
			}
		});
	}
	
	private void setupListviewClickListener(){
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
				String changeitem = todoItems.get(pos); 
				
				// time to change some stuff. Call edit activity
				
				Intent i = new Intent(ToDoActivity.this, EditItemActivity.class);
				i.putExtra("changethis",changeitem); 
				i.putExtra("itemnumber", pos);
				editresult = 0;
				startActivityForResult(i,editresult); // brings up the second activity
				
				if (editresult == RESULT_OK) {
					// hopefully more interesting todo
					//todoAdapter.notifyDataSetChanged();
					//saveItems();
					// why does Click return void but LongClick doesn't?
				}
				else {
					// handle some problem here
				}
			}
		});
	}
	
	private void populateArrayItems() {
		todoItems = new ArrayList<String>();
		todoItems.add("item 1");
		todoItems.add("item 2");
		todoItems.add("item 3");
	}
	
	public void onAddedItem(View v)
	{
		String newitem = etNewItem.getText().toString();
		todoAdapter.add(newitem);
		etNewItem.setText("");  // clear out the text field for next time
		saveItems();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	       editresult = resultCode;
	       		if (editresult == RESULT_OK) {
	       		String updatedtext = data.getExtras().getString("changedit");
	       		int position = data.getExtras().getInt("position");
	       		todoItems.set(position, updatedtext);
	       		todoAdapter.notifyDataSetChanged();
				saveItems();
	       }
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do, menu);
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
