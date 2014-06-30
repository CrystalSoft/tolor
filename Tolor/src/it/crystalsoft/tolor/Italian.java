package it.crystalsoft.tolor;

import java.util.HashMap;

public class Italian
{
	public static HashMap<String, String> Language = null;

	public Italian()
	{
		Language = new HashMap<String, String>();

		Language.put("locale", "it_IT");
		Language.put("localeN", "0");

		//Splash & Menu
		Language.put("menuStart", "Nuova partita".toUpperCase());
		Language.put("menuStore", "Negozio".toUpperCase());
		Language.put("menuAchievements", "Obiettivi".toUpperCase());
		Language.put("menuHighscores", "Classifica".toUpperCase());
		Language.put("menuExit", "Esci".toUpperCase());

		//Play
		Language.put("playTutWelcome", "<strong>Benvenuto in Tolor!</strong><br/><br/>Ti mostrerò brevemente come si gioca!");
		Language.put("playTutColor", "Qui verrà visualizzato il colore che dovrai tenere premuto.");
		Language.put("playTutPress", "Tieni premuto il colore evidenziato con il tocco, fino allo scadere dei secondi.");
		Language.put("playTutReleased", "Hai rilasciato il colore prima della scadenza del tempo, mantieni premuto il colore o la vita diminuirà!");
		Language.put("playTutTime", "Come vedi è scaduto il tempo di pressione ed il colore è cambiato, adesso premi il nuovo colore.");
		Language.put("playTutFinish", "Ci stai prendendo la mano?<br/><br/>Adesso, prova tu da solo!");
		Language.put("playTutOK", "OK");
		Language.put("playTutOK2", "Ricevuto!");
		Language.put("playScore", "punti");
		Language.put("playFinishGame", "Game Over!");
		Language.put("playTime", "Tempo");
		Language.put("playMenu", "Menù");
		Language.put("playRetry", "Riprova");
		Language.put("playScores", "Punteggi");
		Language.put("playFinishScore", "Punteggio finale");
		Language.put("playPerfect", "Perfetto!!!");
		Language.put("playGoods", "Corretti");
		Language.put("playPerfects", "Perfetti");
		Language.put("playFinishGolds", "Monete guadagnate");
		Language.put("playFinishPoints", "Punteggio finale");

		//Credits
		Language.put("creditsClose", "Chiudi");

		//Highscores
		Language.put("highscoreTitle", "Classifica");

		//Settings
		Language.put("settingsTitle", "Opzioni");
		Language.put("settingsMusic", "Abilita Musica");
		Language.put("settingsSound", "Abilita effetti sonori");
		Language.put("settingsClose", "Chiudi");
	}
}