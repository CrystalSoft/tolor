package it.crystalsoft.tolor;

/*
 * Utils Class v0.1
 * by Dario Cancelliere
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.Toast;

public class Utils
{
	public static void MessageBox(Activity instance, String Text, int Duration)
	{
		if (Duration == -1)
		{
			Duration = Toast.LENGTH_SHORT;
		}

		Toast.makeText(instance, Text, Duration).show();
	}

	public static void WriteLog(String Text)
	{
		Log.e("WriteLog", Text);
	}

	public static String GetHTTPData(String Address)
	{
		String response = null;

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(Address);

		try
		{
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = httpclient.execute(httpget, responseHandler);
		}

		catch (ClientProtocolException e)
		{
			response = e.getMessage();
		}

		catch (IOException e)
		{
			response = e.getMessage();
		}

		return response;
	}

	public static void GetAsyncHTTPData(String Address)
	{
		AsyncThread asthread = new AsyncThread(Address);
		asthread.start();
	}

	public static class AsyncThread extends Thread
	{
		private String url = new String();

		public AsyncThread(String url)
		{
			this.url = url;
		}

		public void run()
		{
			GetHTTPData(url);
		}
	}

	public static String PostHTTPData(String Address, String Vars[][])
	{
		String response = null;

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Address);

		try
		{
			if (Vars != null)
			{
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(Vars.length);

				for (int i = 0; i < Vars.length; i++)
				{
					nameValuePairs.add(new BasicNameValuePair(Vars[i][0], Vars[i][1]));
				}

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = httpclient.execute(httppost, responseHandler);
		}

		catch (ClientProtocolException e)
		{
			response = e.getMessage();
		}

		catch (IOException e)
		{
			response = e.getMessage();
		}

		return response;
	}

	public static String substringBetween(String str, String open, String close)
	{
		if (str == null || open == null || close == null)
		{
			return null;
		}

		int start = str.indexOf(open);

		if (start != -1)
		{
			int end = str.indexOf(close, start + open.length());

			if (end != -1)
			{
				return str.substring(start + open.length(), end);
			}
		}

		return null;
	}

	private static void doubleDigit(StringBuilder builder, long value)
	{
		builder.insert(0, value);

		if (value < 10)
		{
			builder.insert(0, '0');
		}
	}

	public static String formatTime(int time)
	{
		long seconds = time % 60;

		time /= 60;

		long minutes = time % 60;

		time /= 60;

		long hours = time;

		StringBuilder builder = new StringBuilder(8);
		doubleDigit(builder, seconds);
		builder.insert(0, ':');

		if (hours == 0)
		{
			builder.insert(0, minutes);
		}

		else
		{
			doubleDigit(builder, minutes);
			builder.insert(0, ':');
			builder.insert(0, hours);
		}

		return builder.toString();
	}

	public static String escapebackslashes(String str)
	{
		return str.replace("\\", "\\\\");
	}

	public static String addslashes(String str)
	{
		return str.replace("'", "\\'");
	}

	public static int Random(int Min, int Max)
	{
		/*ArrayList<Integer> v = new ArrayList<Integer>();

		for (int i = Min; i <= (Max+1); i++)
		{
			v.add(i);
		}

		Collections.shuffle(v);

		return v.get(0);*/

		try
		{
			Random num = new Random(System.currentTimeMillis());

			return num.nextInt(Max - Min + 1) + Min;
		}

		catch (IllegalArgumentException e)
		{
			return Min;
		}
	}

	public static boolean RandomBool()
	{
		return (Math.random() < 0.5);
	}

	public static String GetUsername(Context context)
	{
	    AccountManager manager = AccountManager.get(context); 
	    Account[] accounts = manager.getAccountsByType("com.google"); 
	    List<String> possibleEmails = new LinkedList<String>();

	    for (Account account : accounts) {
	      // TODO: Check possibleEmail against an email regex or treat
	      // account.name as an email address only for certain account.type values.
	      possibleEmails.add(account.name);
	    }

	    if(!possibleEmails.isEmpty() && possibleEmails.get(0) != null){
	        String email = possibleEmails.get(0);
	        String[] parts = email.split("@");
	        if(parts.length > 0 && parts[0] != null)
	            return parts[0];
	        else
	            return null;
	    }else
	        return null;
	}

    public static String Titleize(String source)
    {
        boolean cap = true;
        char[]  out = source.toCharArray();
        int i, len = source.length();
        for(i=0; i<len; i++){
            if(Character.isWhitespace(out[i])){
                cap = true;
                continue;
            }
            if(cap){
                out[i] = Character.toUpperCase(out[i]);
                cap = false;
            }
        }
        return new String(out);
    }
   
    public static int DarkerColor(int color)
    {
    	float[] hsv = new float[3];

    	Color.colorToHSV(color, hsv);
    	hsv[2] *= 0.7f;

    	return Color.HSVToColor(hsv);
    }
   
    public static int BrighterColor(int color)
    {
    	float[] hsv = new float[3];

    	Color.colorToHSV(color, hsv);
    	hsv[2] /= 0.5f;

    	return Color.HSVToColor(hsv);
    }
   
    public static int ComplimentColor(int color)
	{
		int alpha = Color.alpha(color);
		int red = Color.red(color);
		int blue = Color.blue(color);
		int green = Color.green(color);

		red = (~red) & 0xff;
		blue = (~blue) & 0xff;
		green = (~green) & 0xff;

		return Color.argb(alpha, red, green, blue);
	}
   
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
		int width = bm.getWidth();
		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		return resizedBitmap;
    }

    public static int calculatePoints(int score, int seconds, int goods)
    {
    	return score + ((score / 10) * goods);
    }

    public static int calculateGolds(int score, int seconds, int goods)
    {
    	return (int)(Math.floor((double)((double)score + (((double)score / 10.0f) * (double)goods) + (((double)score / 10.0f) * (double)seconds))) / 100);
    }
}