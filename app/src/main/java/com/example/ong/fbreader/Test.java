package com.example.ong.fbreader;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Test extends ActionBarActivity  implements View.OnClickListener, AdapterView.OnItemClickListener{

  //  @Override
    EditText mText;
    Button mAdd;
    ListView mList;

    MyDbHelper mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mText = (EditText)findViewById(R.id.name);
        mAdd = (Button)findViewById(R.id.add);
        mAdd.setOnClickListener(this);
        mList = (ListView)findViewById(R.id.list);
        mList.setOnItemClickListener(this);

        mHelper = new MyDbHelper(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDb = mHelper.getWritableDatabase();
        String[] columns = new String[] {"_id", MyDbHelper.COL_NAME, MyDbHelper.COL_DATE};
        mCursor = mDb.query(MyDbHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        String[] headers = new String[] {MyDbHelper.COL_NAME, MyDbHelper.COL_DATE};
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                mCursor, headers, new int[]{android.R.id.text1, android.R.id.text2});
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mDb.close();
        mCursor.close();
    }
    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues(2);
        cv.put(MyDbHelper.COL_NAME, mText.getText().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put(MyDbHelper.COL_DATE, dateFormat.format(new Date())); //Insert 'now' as the date
        mDb.insert(MyDbHelper.TABLE_NAME, null, cv);
        mCursor.requery();
        mAdapter.notifyDataSetChanged();
        mText.setText(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        mCursor.moveToPosition(position);
        String rowId = mCursor.getString(0); //Column 0 of the cursor is the id
        mDb.delete(MyDbHelper.TABLE_NAME, "_id = ?", new String[]{rowId});
        mCursor.requery();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
