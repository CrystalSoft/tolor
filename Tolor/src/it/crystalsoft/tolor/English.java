package it.crystalsoft.tolor;

import java.util.HashMap;

public class English
{
	public static HashMap<String, String> Language = null;

	public English()
	{
		Language = new HashMap<String, String>();

		Language.put("locale", "en_US");
		Language.put("localeN", "1");

		//Splash & Menu
		Language.put("menuStart", "New game".toUpperCase());
		Language.put("menuStore", "Store".toUpperCase());
		Language.put("menuAchievements", "Achievements".toUpperCase());
		Language.put("menuHighscores", "Highscores".toUpperCase());
		Language.put("menuExit", "Exit".toUpperCase());

		//Play
		Language.put("playTutWelcome", "<strong>Welcome in Tolor!</strong><br/><br/>I will show you how to play!");
		Language.put("playTutColor", "This is the color that you will must keep pressed.");
		Language.put("playTutPress", "Keep pressed highlighted color until the end of time.");
		Language.put("playTutReleased", "You have released the color before time expiration, keep pressed or life will go down!");
		Language.put("playTutTime", "Time has been expired and the color has changed, now press the new color.");
		Language.put("playTutFinish", "Feeling friendly?<br/>Now, try yourself!");
		Language.put("playTutOK", "OK");
		Language.put("playTutOK2", "Got it!");
		Language.put("playScore", "score");
		Language.put("playFinishGame", "Game Over!");
		Language.put("playTime", "Time");
		Language.put("playMenu", "Menu");
		Language.put("playRetry", "Retry");
		Language.put("playScores", "Scores");
		Language.put("playFinishScore", "Final score");
		Language.put("playPerfect", "Perfect!!!");
		Language.put("playGoods", "Goods");
		Language.put("playPerfects", "Perfects");
		Language.put("playFinishGolds", "Earned Golds");
		Language.put("playFinishPoints", "Final score");

		//Credits
		Language.put("creditsClose", "Close");

		//Highscores
		Language.put("highscoreTitle", "Highscores");

		//Settings
		Language.put("settingsTitle", "Settings");
		Language.put("settingsMusic", "Enable Music");
		Language.put("settingsSound", "Enable sound effects");
		Language.put("settingsClose", "Close");
	}
}