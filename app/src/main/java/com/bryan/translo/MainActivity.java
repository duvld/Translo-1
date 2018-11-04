package com.bryan.translo;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {

    private static String API_KEY = "46213302";
    private static String SESSION_ID = "1_MX40NjIxMzMwMn5-MTU0MDkxNDc0NjY2Nn5NdXdaNC9BRjYwb3A4MHFsNGxhWkMvZHB-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjIxMzMwMiZzaWc9YWZmZjUxNmNmN2U4ZTU0OWM3OWNkYjUzMDM3NjJjNjEyYzQ2MjRiNjpzZXNzaW9uX2lkPTFfTVg0ME5qSXhNek13TW41LU1UVTBNRGt4TkRjME5qWTJObjVOZFhkYU5DOUJSall3YjNBNE1IRnNOR3hoV2tNdlpIQi1mZyZjcmVhdGVfdGltZT0xNTQwOTE0ODA0Jm5vbmNlPTAuNTQwODk5NDk5NDk1NTE5MiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTQwOTM2NDAzJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS = 123;
    private Session session;
    private FrameLayout publisherContainer;
    private FrameLayout subscriberContainer;
    private Publisher publisher;
    private Subscriber subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        publisherContainer = (FrameLayout)findViewById(R.id.publisher_container);
        subscriberContainer = (FrameLayout)findViewById(R.id.subscriber_container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_SETTINGS)
    private void requestPermissions() {
        String[] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perm)) {
            session = new Session.Builder(this, API_KEY, SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);
        }
        else {
            EasyPermissions.requestPermissions(this, "Translo needs to access your camera and microphone", RC_SETTINGS, perm);
        }
    }

    @Override
    public void onConnected(Session session) {
        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);
        publisherContainer.addView(publisher.getView());
        session.publish(publisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (subscriber == null) {
            subscriber = new Subscriber.Builder(this, stream).build();
            session.subscribe(subscriber);
            subscriberContainer.addView(subscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if (subscriber != null) {
            subscriber = null;
            subscriberContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
