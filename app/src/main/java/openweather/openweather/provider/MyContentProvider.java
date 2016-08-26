package openweather.openweather.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import openweather.openweather.MyDBHandler;

public class MyContentProvider extends ContentProvider {

    private MyDBHandler myDB;

    private static final String AUTHORITY = "openweather.openweather.provider.MyContentProvider";
    private static final String WEATHER_TABLE = "WEATHER";
    public static final Uri CONTENT_URI = Uri.parse ("content://" + AUTHORITY + "/" +
            WEATHER_TABLE);

    public static final int WEATHER = 1;
    public static final int WEATHER_ID = 2;

    private static final UriMatcher sURIMatcher =
            new UriMatcher (UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, WEATHER_TABLE, WEATHER);
        sURIMatcher.addURI(AUTHORITY, WEATHER_TABLE + "/#", WEATHER_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType)
        {
            case WEATHER:
                rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_OPENWEATHER,
                        selection,
                        selectionArgs);
                break;
            case WEATHER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                {
                    rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_OPENWEATHER,
                            MyDBHandler.WEATHER_ID + "=" + id,
                            null);
                }
                else
                {
                    rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_OPENWEATHER,
                            MyDBHandler.WEATHER_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public boolean onCreate() {
        myDB = new MyDBHandler(getContext(), null, null, 1);
        return false;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        int uriType = sURIMatcher.match((uri));

        SQLiteDatabase sqlDB = myDB.getWritableDatabase();

        long id = 0;

        switch(uriType)
        {
            case WEATHER:
                //Log.i("Log_Tag","db is"+MyDBHandler.TABLE_OPENWEATHER);
                id = sqlDB.insert(MyDBHandler.TABLE_OPENWEATHER, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse (WEATHER_TABLE + "/" + id);

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables (MyDBHandler.TABLE_OPENWEATHER);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case WEATHER_ID:
                queryBuilder.appendWhere(MyDBHandler.WEATHER_ID + "="
                        + uri.getLastPathSegment());
                break;
            case WEATHER:

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }



    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqldb = myDB.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType)
        {
            case WEATHER:
                rowsUpdated = sqldb.update(MyDBHandler.TABLE_OPENWEATHER,
                        values,
                        selection,
                        selectionArgs);
                break;

            case WEATHER_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection))
                {
                    rowsUpdated = sqldb.update(MyDBHandler.TABLE_OPENWEATHER,
                            values,
                            MyDBHandler.WEATHER_ID + "=" + id,
                            null);

                }
                else
                {
                    rowsUpdated = sqldb.update(MyDBHandler.TABLE_OPENWEATHER,
                            values,
                            MyDBHandler.WEATHER_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;

    }


}
