/**
 * Project: Assignment_04_Video
 * File: SimpleVideo.java
 * Date: 2011-03-21
 * Time: 3:12:37 AM
 */
package org.trollop.SimpleVideo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SimpleVideo extends Activity {
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        downloadImageSequence("http://virtual-void.org/", 10, "gif");
    }
    
    /* store image in the buffer and wait if full */
	public void downloadImageSequence(String baseURL, int numImages, String imgType) {
		final String url = baseURL;
		final int seq = numImages;
		final String type = imgType;
		
		new Thread() {
			public void run() {
				InputStream in = null;
				
				try {
					int i = 1;
					
					while (true) {
						in = openHttpConnection(url + Integer.toString(i) + "." + type);
						ImageData id = new ImageData(in.toString().getBytes(), i);
						
						if (i != 10)
							i++;
						else
							i = 1;
						
						in.close();
					}
				}
				catch (IOException e) {
					Log.e("IOException", e.toString());
				}
			}
		}.start();
	}

	private InputStream openHttpConnection(String URL) {
		InputStream in = null;
		int resCode = -1;
		
		try {
			URL url = new URL(URL);
			URLConnection urlConn = url.openConnection();
			
			if (!(urlConn instanceof HttpURLConnection))
				throw new IOException("URL is not an HTTP URL");

			HttpURLConnection httpConn = (HttpURLConnection)urlConn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			
			resCode = httpConn.getResponseCode();
			
			if (resCode == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		}
		catch (MalformedURLException e) {
			Log.e("MalformedURLException", e.toString());
		}
		catch (IOException e) {
			Log.e("IOException", e.toString());
		}
		
		return in;
	}
}