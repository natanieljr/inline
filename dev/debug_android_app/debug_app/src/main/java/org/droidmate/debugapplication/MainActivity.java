package org.droidmate.debugapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings)
    {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void openGooglePlay(View view)
  {

    Log.i("debug_app", "Opening Google Play");

    // Reference: http://stackoverflow.com/questions/28180430/chrome-not-opening-from-android-app
    String url = "http://play.google.com/store/apps";
    try
    {

      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setPackage("com.android.chrome");
      i.setData(Uri.parse(url));
      startActivity(i);

      startActivity(i);
    }
    catch(ActivityNotFoundException e) {
      // Chrome is not installed
      Log.e("debug_app", "Opening Google Play: Chrome not installed:", e);
      Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      startActivity(i);
    }
  }
}
