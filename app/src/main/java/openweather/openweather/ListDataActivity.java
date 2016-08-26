package openweather.openweather;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import openweather.openweather.provider.MyContentProvider;


public class ListDataActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LIST_LOADER= 0;
    private SimpleCursorAdapter mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list);

        String[] mFromColumns=
                {
                        MyDBHandler.WEATHER_ID,
                        //  MyDBHandler.WEATHER_TIME,
                        MyDBHandler.WEATHER_TEMP,
                        MyDBHandler.WEATHER_HUMIDITY,
                        MyDBHandler.WEATHER_DESCRIPTION

                };

        int[] mToFields =
                {
                        R.id.srno,
                        // R.id.time,
                        R.id.temp,
                        //  R.id.pressure,
                        R.id.humidity,
                        R.id.description,
                };
        mAdaptor = new SimpleCursorAdapter(this,R.layout.item_row,null,mFromColumns,mToFields,0);
        ListView mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdaptor);

        getLoaderManager().initLoader(LIST_LOADER,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id,Bundle args)
    {
        switch(id)
        {
            case LIST_LOADER:
                return new CursorLoader(this, MyContentProvider.CONTENT_URI,MyDBHandler.PROJECTION,null,null,null);
            //don't need break because of return statement above
            //break;
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data!=null)
        {
            mAdaptor.changeCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdaptor.changeCursor(null);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
