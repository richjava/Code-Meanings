package com.richjavalabs.codemeanings;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.mikpenz.iconics.IconicsDrawable;
import com.mikpenz.iconics.typeface.FontAwesome;
//use the codeItemApi CodeItem here, not the POJO
import com.richjavalabs.backend.codeItemApi.model.CodeItem;
import com.richjavalabs.codemeanings.tasks.InsertCodeItemTask;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_add_edit)
public class AddEditActivity extends RoboActionBarActivity {

    @InjectView (R.id.toolbar)Toolbar toolbar;
    @InjectView (R.id.fab_button)ImageButton fabButton;
    @InjectView (R.id.edit_code)EditText codeEdit;
    @InjectView (R.id.edit_meaning)EditText meaningEdit;
    @InjectView (R.id.spinner_type)Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fab Button
        fabButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_plus).color(Color.WHITE).actionBarSize());
        fabButton.setOnClickListener(fabClickListener);

        //Type Spinner
        String[] units = getResources().getStringArray(
                R.array.array_type);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, units);
        typeSpinner.setAdapter(adapter);
    }

    public CodeItem getCodeItem(){
        CodeItem codeItem = new CodeItem();
        codeItem.setCode(codeEdit.getText().toString());
        codeItem.setMeaning(meaningEdit.getText().toString());
        codeItem.setType(typeSpinner.getSelectedItem().toString());
        return codeItem;
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new InsertCodeItemTask(getCodeItem(),AddEditActivity.this).execute();
            Intent intent = new Intent(AddEditActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
