package it.crystalsoft.tolor;

import android.app.Activity;
import android.content.SharedPreferences;

public class Configuration
{
	//Local Variable
	private Activity MyActivity = null;
	public static final String PREFS_NAME = "TolorConfiguration";
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;

	public Configuration(Activity MyActivity)
	{
		this.MyActivity = MyActivity;

		settings = this.MyActivity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
		editor = settings.edit();
	}

	public boolean getMusicEnabled()
	{
		return settings.getBoolean("musicEnabled", true);
	}

	public boolean getSoundEnabled()
	{
		return settings.getBoolean("soundEnabled", true);
	}

	public String getLocale()
	{
		return settings.getString("gameLocale", "");
	}

	public boolean getTutorial()
	{
		return settings.getBoolean("tutorial", true);
	}

	public int getPoints()
	{
		return settings.getInt("points", 0);
	}

	public void setMusicEnabled(boolean enabled)
	{
		editor.putBoolean("musicEnabled", enabled);
		editor.commit();
	}

	public void setSoundEnabled(boolean enabled)
	{
		editor.putBoolean("soundEnabled", enabled);
		editor.commit();
	}

	public void setLocale(String locale)
	{
		editor.putString("gameLocale", locale);
		editor.commit();
	}

	public void setTutorial(boolean tutorial)
	{
		editor.putBoolean("tutorial", tutorial);
		editor.commit();
	}

	public void setPoints(int points)
	{
		editor.putInt("points", points);
		editor.commit();
	}

	public void saveAll()
	{
		editor.commit();
	}
}