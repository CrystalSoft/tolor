package it.crystalsoft.tolor;

import java.util.HashMap;
import java.util.Locale;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.analytics.tracking.android.EasyTracker;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends BaseGameActivity
{
	//Local App Variables
	private static SplashActivity Splash = null;
	private static boolean firstTime = true;
	private static boolean waiting = false;
	public static HashMap<String,String> Language = null;
	public static MediaPlayer MPlayer = null;
	public static SoundManager Player = null;
	public static Typeface Font = null;
	public static Configuration configuration = null;
	public static String WebURL = null;
	public static String Vers = "";
	private static AdRequest adRequest = null;
	private static AdRequest adRequest2 = null;

	//Controls
	public static ActionBar actionBar = null;
	private static RelativeLayout SplashLayout = null;
	private static ImageView Logo = null;
	private static TextView Version = null;
	private static ColouredTextView LogoTolor = null;
	private static ImageView LogoCrystal = null;
	private static Button Start = null;
	private static Button Store = null;
	private static Button Achievements = null;
	private static Button Highscores = null;
	private static Button Exit = null;
	private static View Separator = null;
	private static ImageView Credits = null;
	private static ImageView PlayStoreGames = null;
	private static ImageView Settings = null;
	private static RelativeLayout CreditsLayout = null;
	private static TextView CreditsText = null;
	private static Button CloseCredits = null;
	private static ImageView Facebook = null;
	private static ImageView PlayStore = null;
	private static RelativeLayout SettingsLayout = null;
	private static TextView SettingsTitle = null;
	private static CheckBox SettingsMusic = null;
	private static CheckBox SettingsSound = null;
	private static Button CloseSettings = null;
	private static RelativeLayout FinishLayout = null;
	private static Button Menu = null;
	private static Button Retry = null;
	private static Button Scores = null;
	private static TolorView Tolor = null;
	private static AdView Ad = null;
	private static AdView Ad2 = null;

	//Private Game variables
	private static TolorGame game = null;
	private static boolean inGame = false;

	private final int /*RC_RESOLVE = 5000, */RC_UNUSED = 5001;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			actionBar = getActionBar();
		}

		catch (NoSuchMethodError e)
		{
			actionBar = null;

			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		setContentView(R.layout.activity_splash);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Splash = this;

		if (configuration == null)
		{
			configuration = new Configuration(this);
		}

		String loc = null;

		if (configuration.getLocale().length() <= 0)
		{
			loc = Locale.getDefault().getDisplayLanguage().toLowerCase();

			configuration.setLocale(Locale.getDefault().getLanguage());
		}

		else
		{
			loc = configuration.getLocale();
		}

		Font = Typeface.createFromAsset(getAssets(), "grinched.ttf"); 

		Player = new SoundManager();
		Player.initSounds(getBaseContext());

		Player.addSound(0, R.raw.countdown);
		Player.addSound(1, R.raw.block_click);
		Player.addSound(2, R.raw.block_loop);
		Player.addSound(3, R.raw.block_loop2);
		Player.addSound(4, R.raw.warning_loop);
		Player.addSound(5, R.raw.click);

		SplashLayout = (RelativeLayout)findViewById(R.id.splash_layout);
		Logo = (ImageView)findViewById(R.id.logo);
		Version = (TextView)findViewById(R.id.version);
		LogoTolor = (ColouredTextView)findViewById(R.id.logoTolor);
		LogoCrystal = (ImageView)findViewById(R.id.logo_crystal);
		Start = (Button)findViewById(R.id.start);
		Store = (Button)findViewById(R.id.store);
		Achievements = (Button)findViewById(R.id.achievements);
		Highscores = (Button)findViewById(R.id.highscores);
		Exit = (Button)findViewById(R.id.exit);
		Separator = (View)findViewById(R.id.separator);
		Credits = (ImageView)findViewById(R.id.credits);
		PlayStoreGames = (ImageView)findViewById(R.id.playstoregames);
		Settings = (ImageView)findViewById(R.id.settings);
		CreditsLayout = (RelativeLayout)findViewById(R.id.credits_layout);
		CreditsText = (TextView)findViewById(R.id.creditsText);
		CloseCredits = (Button)findViewById(R.id.closeCredits);
		Facebook = (ImageView)findViewById(R.id.facebook);
		PlayStore = (ImageView)findViewById(R.id.playstore);
		SettingsLayout = (RelativeLayout)findViewById(R.id.settings_layout);
		SettingsTitle = (TextView)findViewById(R.id.settingsTitle);
		SettingsMusic = (CheckBox)findViewById(R.id.settingsMusic);
		SettingsSound = (CheckBox)findViewById(R.id.settingsSound);
		CloseSettings = (Button)findViewById(R.id.closeSettings);
		FinishLayout = (RelativeLayout)findViewById(R.id.finish_layout);
		Menu = (Button)findViewById(R.id.menu);
		Retry = (Button)findViewById(R.id.retry);
		Scores = (Button)findViewById(R.id.scores);
		Tolor = (TolorView)findViewById(R.id.tolor);
		Ad = (AdView)findViewById(R.id.ad);
		Ad2 = (AdView)findViewById(R.id.ad2);

		adRequest = new AdRequest.Builder().build();
		adRequest2 = new AdRequest.Builder().build();
		Ad.loadAd(adRequest);
		Ad2.loadAd(adRequest2);
		Ad.setVisibility(View.INVISIBLE);

		//Start.setTypeface(Font);
		//Version.setTypeface(Font);
		LogoTolor.setTypeface(Font);

		LoadLanguage(loc);

		LogoTolor.setText("Tolor");

		Start.setSoundEffectsEnabled(false);
		Start.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					Start.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press2));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					Start.setBackgroundDrawable(getResources().getDrawable(R.drawable.button2));
				}

				return false;
			}
		});
		Start.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				NewGame();
			}
		});

		Store.setSoundEffectsEnabled(false);
		Store.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					Store.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press3));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					Store.setBackgroundDrawable(getResources().getDrawable(R.drawable.button3));
				}

				return false;
			}
		});
		Store.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				
			}
		});

		Achievements.setSoundEffectsEnabled(false);
		Achievements.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					Achievements.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press3));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					Achievements.setBackgroundDrawable(getResources().getDrawable(R.drawable.button3));
				}

				return false;
			}
		});
		Achievements.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), RC_UNUSED);
			}
		});

		Highscores.setSoundEffectsEnabled(false);
		Highscores.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					Highscores.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press3));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					Highscores.setBackgroundDrawable(getResources().getDrawable(R.drawable.button3));
				}

				return false;
			}
		});
		Highscores.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				ShowScores();
			}
		});

		Exit.setSoundEffectsEnabled(false);
		Exit.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					Exit.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press4));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					Exit.setBackgroundDrawable(getResources().getDrawable(R.drawable.button4));
				}

				return false;
			}
		});
		Exit.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				onExit(false);
			}
		});

		CreditsLayout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				return;
			}
		});

		Credits.setSoundEffectsEnabled(false);
		Credits.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				CreditsText.setText(Html.fromHtml("Copyright &copy; 2014 CrystalSoft<br/><br/><b>Development</b><br/>Dario Cancelliere<br/><br/><b>Design</b><br/>Dario Cancelliere<br/><br/><b>Music</b><br/>Dario Cancelliere"));

				CreditsLayout.setVisibility(View.VISIBLE);
			}
		});

		CloseCredits.setSoundEffectsEnabled(false);
		CloseCredits.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					CloseCredits.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					CloseCredits.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
				}

				return false;
			}
		});
		CloseCredits.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				CreditsLayout.setVisibility(View.INVISIBLE);
			}
		});

		Facebook.setSoundEffectsEnabled(false);
		Facebook.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					//Player.playSound(1);
				}

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/CrystalSoft"));
				
				startActivity(i);
			}
		});

		PlayStore.setSoundEffectsEnabled(false);
		PlayStore.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=CrystalSoft")));
				}

				catch (android.content.ActivityNotFoundException anfe)
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=CrystalSoft&hl=it")));
				}
			}
		});

		PlayStoreGames.setSoundEffectsEnabled(false);
		PlayStoreGames.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				if (!isSignedIn())
				{
					beginUserInitiatedSignIn();
				}

				else
				{
					ShowScores();
				}
			}
		});

		SettingsLayout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				return;
			}
		});

		Settings.setSoundEffectsEnabled(false);
		Settings.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				SettingsLayout.setVisibility(View.VISIBLE);
			}
		});

		SettingsMusic.setSoundEffectsEnabled(false);
		SettingsMusic.setChecked(configuration.getMusicEnabled());
		SettingsMusic.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				configuration.setMusicEnabled(((CheckBox)v).isChecked());

				if (configuration.getMusicEnabled())
				{
					if (MPlayer == null)
					{
						MPlayer = MediaPlayer.create(Splash, R.raw.theme_splash);
			
						if (MPlayer != null)
						{
							MPlayer.setVolume(0.2f, 0.2f);
							MPlayer.setLooping(true);
							MPlayer.start();
						}
					}
				}

				else
				{
					Player.stopSounds();

					if (MPlayer != null)
					{
						if (MPlayer.isPlaying())
						{
							MPlayer.stop();
						}

						MPlayer.reset();
						MPlayer.release();

						MPlayer = null;
					}
				}
			}
		});

		SettingsSound.setSoundEffectsEnabled(false);
		SettingsSound.setChecked(configuration.getSoundEnabled());
		SettingsSound.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				configuration.setSoundEnabled(((CheckBox)v).isChecked());
			}
		});

		CloseSettings.setSoundEffectsEnabled(false);
		CloseSettings.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					CloseSettings.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					CloseSettings.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
				}

				return false;
			}
		});
		CloseSettings.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				SettingsLayout.setVisibility(View.INVISIBLE);
			}
		});

		Menu.setSoundEffectsEnabled(false);
		Menu.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					Menu.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press5));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					Menu.setBackgroundDrawable(getResources().getDrawable(R.drawable.button5));
				}

				return false;
			}
		});
		Menu.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				onBackPressed();
			}
		});

		Retry.setSoundEffectsEnabled(false);
		Retry.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					Retry.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press5));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					Retry.setBackgroundDrawable(getResources().getDrawable(R.drawable.button5));
				}

				return false;
			}
		});
		Retry.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				NewGame();
			}
		});

		Scores.setSoundEffectsEnabled(false);
		Scores.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					Scores.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_press5));
				}

				else if (event.getAction() != MotionEvent.ACTION_MOVE)
				{
					Scores.setBackgroundDrawable(getResources().getDrawable(R.drawable.button5));
				}

				return false;
			}
		});
		Scores.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (configuration.getSoundEnabled())
				{
					Player.playSound(5, 0);
				}

				ShowScores();
			}
		});

		if (LogoCrystal != null)
		{
			LogoCrystal.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.crystalsoft.it/"));
	
					startActivity(i);
				}
			});
		}

		try
		{
			Vers = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

			Version.setText(Vers);
		}

		catch (NameNotFoundException e)
		{
			//e.printStackTrace();
		}

		Initialize(firstTime);

		firstTime = false;

		getGameHelper().setMaxAutoSignInAttempts(0);
	}

	private void Initialize(final boolean firstTime)
	{
		//Prima volta
		if (firstTime)
		{
			game = new TolorGame(Tolor, this);

			if (!waiting)
			{
				waiting = true;

				new Handler().postDelayed(new Runnable()
				{
					public void run()
					{
						ShowInit(!firstTime);
					}
				}, firstTime ? 3000 : 0);
				//}, 0);
			}
		}

		else
		{
			game.setTolorView(Tolor);

			if (!waiting)
			{
				if (inGame)
				{
					HideMenu();
				}
		
				else
				{
					ShowMenu();
				}
			}
		}
	}

	private static void RefreshLocale()
	{
		Start.setText(Language.get("menuStart"));
		Store.setText(Language.get("menuStore"));
		Achievements.setText(Language.get("menuAchievements"));
		Highscores.setText(Language.get("menuHighscores"));
		Exit.setText(Language.get("menuExit"));

		CloseCredits.setText(Language.get("creditsClose"));
		SettingsTitle.setText(Language.get("settingsTitle"));
		SettingsMusic.setText(Language.get("settingsMusic"));
		SettingsSound.setText(Language.get("settingsSound"));
		CloseSettings.setText(Language.get("settingsClose"));

		Menu.setText(Language.get("playMenu"));
		Retry.setText(Language.get("playRetry"));
		Scores.setText(Language.get("playScores"));
	}

	public static void LoadLanguage(String loc)
	{
		loc = loc.toLowerCase();

		if (loc.contains("en"))
		{
			new English();

			Language = English.Language;
		}

		else
		{
			new Italian();

			Language = Italian.Language;
		}

		RefreshLocale();
	}

	private void ShowInit(final boolean fast)
	{
		final Runnable code = new Runnable()
		{
			@Override
			public void run()
			{
				waiting = false;

				ShowMenu();
			}
		};

		if (!fast)
		{
			Animation Fader = AnimationUtils.loadAnimation(Splash, R.anim.fade_effect3);
	
			SplashLayout.startAnimation(Fader);
	
			Fader.setAnimationListener(new AnimationListener()
			{
				@Override
				public void onAnimationEnd(Animation arg0)
				{
					code.run();

					SplashLayout.clearAnimation();
					SplashLayout.startAnimation(AnimationUtils.loadAnimation(Splash, R.anim.fade_effect));
				}
	
				@Override
				public void onAnimationRepeat(Animation arg0)
				{
					//
				}
	
				@Override
				public void onAnimationStart(Animation arg0)
				{
					//
				}
			});
		}

		else
		{
			code.run();
		}
	}

	@SuppressWarnings("deprecation")
	public void ShowMenu()
	{
		SplashLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));

		Logo.setVisibility(View.INVISIBLE);

		Version.setVisibility(View.VISIBLE);
		LogoTolor.setVisibility(View.VISIBLE);
		LogoCrystal.setVisibility(View.VISIBLE);
		Start.setVisibility(View.VISIBLE);
		//Store.setVisibility(View.VISIBLE);

		if (isSignedIn())
		{
			ShowHighscoresButton();
		}

		Exit.setVisibility(View.VISIBLE);
		Separator.setVisibility(View.VISIBLE);
		Credits.setVisibility(View.VISIBLE);
		PlayStoreGames.setVisibility(View.VISIBLE);
		Settings.setVisibility(View.VISIBLE);

		Tolor.setVisibility(View.INVISIBLE);
		Ad.setVisibility(View.INVISIBLE);

		FinishLayout.setVisibility(View.INVISIBLE);

		Start.startAnimation((AnimationSet)AnimationUtils.loadAnimation(Splash, R.anim.button_animation));

		if (Store.getVisibility() == View.VISIBLE)
		{
			Store.startAnimation((AnimationSet)AnimationUtils.loadAnimation(Splash, R.anim.button_animation));
		}

		if (Achievements.getVisibility() == View.VISIBLE)
		{
			Achievements.startAnimation((AnimationSet)AnimationUtils.loadAnimation(Splash, R.anim.button_animation));
		}

		if (Highscores.getVisibility() == View.VISIBLE)
		{
			Highscores.startAnimation((AnimationSet)AnimationUtils.loadAnimation(Splash, R.anim.button_animation));
		}

		Exit.startAnimation((AnimationSet)AnimationUtils.loadAnimation(Splash, R.anim.button_animation));

		if (configuration.getMusicEnabled())
		{
			if (MPlayer == null)
			{
				MPlayer = MediaPlayer.create(Splash, R.raw.theme_splash);
	
				if (MPlayer != null)
				{
					MPlayer.setVolume(0.2f, 0.2f);
					//MPlayer.setVolume(0.0f, 0.0f);
					MPlayer.setLooping(true);
					MPlayer.start();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void HideMenu()
	{
		SplashLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));

		Logo.setVisibility(View.INVISIBLE);

		Version.setVisibility(View.INVISIBLE);
		LogoTolor.setVisibility(View.INVISIBLE);
		LogoCrystal.setVisibility(View.INVISIBLE);
		Start.setVisibility(View.INVISIBLE);
		//Store.setVisibility(View.INVISIBLE);

		if (isSignedIn())
		{
			HideAchievementsButton();
		}

		Exit.setVisibility(View.INVISIBLE);
		Separator.setVisibility(View.INVISIBLE);
		Credits.setVisibility(View.INVISIBLE);
		PlayStoreGames.setVisibility(View.INVISIBLE);
		Settings.setVisibility(View.INVISIBLE);

		Tolor.setVisibility(View.VISIBLE);

		NewGameAd();
	}

	public static void ShowFinish()
	{
		FinishLayout.setVisibility(View.VISIBLE);

		FinishGameAd();
	}

	public static void HideFinish()
	{
		FinishLayout.setVisibility(View.INVISIBLE);
	}

	public void ShowScores()
	{
		if (isSignedIn())
		{
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), getString(R.string.leaderboard_highscores)), RC_UNUSED);
		}

		else
		{
			beginUserInitiatedSignIn();
		}
	}

	private void ShowAchievementsButton()
	{
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)Exit.getLayoutParams();
		params.addRule(RelativeLayout.BELOW, R.id.achievements);

		Exit.setLayoutParams(params);

		Achievements.setVisibility(View.VISIBLE);
	}

	private void HideAchievementsButton()
	{
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)Exit.getLayoutParams();
		params.addRule(RelativeLayout.BELOW, R.id.start);

		Exit.setLayoutParams(params);

		Achievements.setVisibility(View.INVISIBLE);
	}

	private void ShowHighscoresButton()
	{
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)Exit.getLayoutParams();
		params.addRule(RelativeLayout.BELOW, R.id.highscores);

		Exit.setLayoutParams(params);

		Highscores.setVisibility(View.VISIBLE);
	}

	private void HideHighscoresButton()
	{
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)Exit.getLayoutParams();
		params.addRule(RelativeLayout.BELOW, R.id.start);

		Exit.setLayoutParams(params);

		Highscores.setVisibility(View.INVISIBLE);
	}

	private static void NewGameAd()
	{
		Ad.setVisibility(View.VISIBLE);
	}

	private static void FinishGameAd()
	{
		Ad.setVisibility(View.INVISIBLE);
	}

	private void NewGame()
	{
		inGame = true;

		if (configuration.getMusicEnabled())
		{
			if (MPlayer != null)
			{
				if (MPlayer.isPlaying())
				{
					MPlayer.stop();
				}

				MPlayer.reset();
				MPlayer.release();
	
				MPlayer = null;
			}

			if (MPlayer == null)
			{
				MPlayer = MediaPlayer.create(Splash, R.raw.theme);
	
				if (MPlayer != null)
				{
					MPlayer.setVolume(0.2f, 0.2f);
					//MPlayer.setVolume(0.0f, 0.0f);
					MPlayer.setLooping(true);
				}
			}
	
			if (MPlayer != null)
			{
				MPlayer.start();
			}
		}

		HideMenu();

		game.NewGame();
	}

	private void PauseGame()
	{
		game.PauseGame();
	}

	private void FinishGame()
	{
		if (configuration.getMusicEnabled())
		{
			Player.stopSounds();

			if (MPlayer != null)
			{
				if (MPlayer.isPlaying())
				{
					MPlayer.stop();
				}

				MPlayer.reset();
				MPlayer.release();
	
				MPlayer = null;
			}
		}

		inGame = false;

		ShowMenu();

		game.FinishGame(false);
	}

	private void Clear()
	{
		firstTime = true;
		waiting = false;

		if (MPlayer != null)
		{
			if (MPlayer.isPlaying())
			{
				MPlayer.stop();
			}

			MPlayer.reset();
			MPlayer.release();

			MPlayer = null;
		}

		game.Clear();

		Player.clear();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		if (!waiting)
		{
			
		}

		return super.onTouchEvent(e);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onRestart()
	{
		super.onRestart();
	}

	@Override
	public void onPause()
	{
		if (!waiting)
		{
			if (inGame && !game.IsPaused() && !game.IsFinished())
			{
				PauseGame();
			}

			else if ((MPlayer != null) && MPlayer.isPlaying())
			{
				MPlayer.pause();
			}
		}

		super.onPause();
	}

	@Override
	public void onResume()
	{
		if (!waiting)
		{
			if (inGame)
			{
				//PauseGame(); //Request feedback
			}

			else if ((MPlayer != null) && configuration.getMusicEnabled())
			{
				MPlayer.start();
			}
		}

		super.onResume();
	}

	@Override
	public void onStop()
	{
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	public void onDestroy()
	{
		

		super.onDestroy();
	}

	//Exit confirmation
	@Override
	public void onBackPressed()
	{
		if (inGame)
		{
			if (game.IsPaused())
			{
				game.PauseGame();
			}

			else
			{
				FinishGame();
			}
		}

		else if (CreditsLayout.getVisibility() == View.VISIBLE)
		{
			CreditsLayout.setVisibility(View.INVISIBLE);
		}

		else
		{
			onExit(false);
		}
	}

	private void onExit(boolean confirm)
	{
		if (confirm)
		{
			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Closing Activity")
			.setMessage("Are you sure you want to close this activity?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					exit();
				}
			}).setNegativeButton("No", null).show();
		}

		else
		{
			exit();
		}
	}

	private void exit()
	{
		Clear();

		finish();
	}

	@Override
	public void onSignInFailed()
	{
		//HideAchievementsButton();
		HideHighscoresButton();
	}

	@Override
	public void onSignInSucceeded()
	{
		if (!waiting)
		{
			//ShowAchievementsButton();
			ShowHighscoresButton();
		}
	}

	public void unlockAchievement(int achievementId, String fallbackString)
	{
		if (isSignedIn())
		{
			Games.Achievements.unlock(getApiClient(), getString(achievementId));
		}
	}

	public void submitScore(long score)
	{
		if (isSignedIn())
		{
			Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_highscores), score);
		}
	}
}