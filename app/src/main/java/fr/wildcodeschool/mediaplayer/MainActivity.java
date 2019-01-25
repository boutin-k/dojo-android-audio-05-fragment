package fr.wildcodeschool.mediaplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fr.wildcodeschool.mediaplayer.notification.MediaNotification;
import fr.wildcodeschool.mediaplayer.notification.NotificationReceiver;
import fr.wildcodeschool.mediaplayer.player.WildPlayer;
import fr.wildcodeschool.mediaplayer.service.MediaService;

@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity implements ServiceConnection {
  // Fragments
  private ControllerFragment mControllerFragment;

  // Bound service
  MediaService mService;
  boolean mBound = false;

  // Notification
  private MediaNotification mNotification = null;

  /**
   * Application context accessor
   * https://possiblemobile.com/2013/06/context/
   */
  private static Context appContext;
  public  static Context getAppContext() {
    return appContext;
  }

  /**
   * Called when the activity is starting.
   * @param savedInstanceState Bundle: If the activity is being re-initialized after previously
   * being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Get fragment instance
    mControllerFragment =
      (ControllerFragment) getSupportFragmentManager()
        .findFragmentById(R.id.controller_fragment);

    // Initialization of the application context
    MainActivity.appContext = getApplicationContext();

    // Bind to MediaService
    Intent intent = new Intent(this, MediaService.class);
    bindService(intent, this, Context.BIND_AUTO_CREATE);

    // Create the notification
    mNotification =
      new MediaNotification.Builder(getApplicationContext())
        .addActions(NotificationReceiver.class)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.greenday))
        .setContentTitle(getString(R.string.song_title))
        .setContentText(getString(R.string.song_description))
        .buildNotification();
    mNotification.register();
  }

  /**
   * Perform any final cleanup before an activity is destroyed. This can happen either because
   * the activity is finishing (someone called finish() on it), or because the system is
   * temporarily destroying this instance of the activity to save space.
   * You can distinguish between these two scenarios with the isFinishing() method.
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Release the service
    unbindService(this);
    mBound = false;
    // Release the notification
    if (null != mNotification)
      mNotification.unregister();
  }

  // --------------------------------------------------------------------------
  // Player validity
  // --------------------------------------------------------------------------

  /**
   * Get the validity of mediaPlayer instance
   * @return boolean: Returns the validity of th WildPlayer
   */
  private boolean isPlayerReady() {
    return mBound
      && (null != mService)
      && (null != mService.getPlayer());
  }

  /**
   * Return the instance of the WildPlayer stored in the service
   * @return WildPlayer: The instance of the WildPlayer
   */
  public WildPlayer getPlayer() {
    return isPlayerReady() ? mService.getPlayer() : null;
  }

  // --------------------------------------------------------------------------
  // Buttons onClick
  // --------------------------------------------------------------------------

  /**
   * On play button click
   * Launch the playback of the media
   */
  public void playMedia(View v) {
    mControllerFragment.playMedia(getPlayer());
  }

  /**
   * On pause button click
   * Pause the playback of the media
   */
  public void pauseMedia(View v) {
    mControllerFragment.pauseMedia(getPlayer());
  }

  /**
   * On reset button click
   * Stop the playback of the media
   */
  public void stopMedia(View v) {
    mControllerFragment.stopMedia(getPlayer());
  }

  // --------------------------------------------------------------------------
  // Service interface
  // --------------------------------------------------------------------------

  /**
   * Called when a connection to the Service has been established, with the IBinder of the
   * communication channel to the Service.
   * @param className ComponentName: The concrete component name of the service that has been connected.
   * @param service IBinder: The IBinder of the Service's communication channel, which you can now make calls on.
   */
  @Override
  public void onServiceConnected(ComponentName className, IBinder service) {
    // We've bound to MediaService, cast the IBinder and get MediaService instance
    MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
    mService = binder.getService();
    mBound = true;

    mService.createMediaPlayer(R.string.song, mControllerFragment);
  }

  /**
   * Called when a connection to the Service has been lost. This typically happens when the
   * process hosting the service has crashed or been killed.
   * @param name ComponentName: The concrete component name of the service whose connection has been lost.
   */
  @Override
  public void onServiceDisconnected(ComponentName name) {
    mBound = false;
  }
}
