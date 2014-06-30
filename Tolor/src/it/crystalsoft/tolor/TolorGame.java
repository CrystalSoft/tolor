package it.crystalsoft.tolor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

public class TolorGame
{
	private static SplashActivity splash = null;
	private static TolorView Tolor = null;
	private static List<String> colors = null;

	private static Timer gameTimer = null;
	private static TimerTask gameTask = null;
	private final int time = 100;

	private static Handler handler = null;

	private static boolean started = false;
	private static boolean finished = false;
	private static boolean countDown = true;
	private static boolean paused = false;

	private static int units = 0;
	private static float difficulty = 0;
	private static int points = 0;
	private final int pointsUnit = 8;
	private static int seconds = 0;
	private static int countDownSeconds = 0;
	private final int jollyTime = 10000;
	private static int jollyTimeLeft = 0;
	private static int stentTime = 0;
	private static boolean stenting = true;
	private static int perfects = 0;
	private static int goods = 0;
	private static int currentBlock = -1;
	private static boolean[] clickingBlock = null;
	private static boolean wrong = false;
	private static boolean perfect = true;

	private static final int countDownTime = 3;
	//private static final int countDownTime = 0;
	private static float currentMatchSeconds = 0;
	private static float matchSeconds = 0;

	private static int tutorial = 0;
	private static int guiding = 0;

	public TolorGame(final TolorView Tolor, SplashActivity splash)
	{
		TolorGame.splash = splash;
		TolorGame.Tolor = Tolor;

		colors = new ArrayList<String>();

		gameTimer = new Timer();

		gameTask = new GameTask();

		gameTimer.scheduleAtFixedRate(gameTask, 0, time);

		if (handler == null)
		{
			handler = new Handler()
			{
				@Override
	            public void handleMessage(Message msg)
	            {
					switch (msg.what)
					{
						case 0: //Set color block
						{
							Tolor.SetBlockColor(currentBlock, Tolor.getBlocksNum(), Color.parseColor(colors.get(currentBlock)));

							if ((guiding == 3) || (guiding == 4) || (guiding == 5))
							{
								tutorial = (guiding > 4) ? 6 : 5;
								guiding = 0;
							}

							break;
						}

						case 1: //Finish Game
						{
							FinishGame(true);

							break;
						}

						case 2: //Tutorial
						{
							Tolor.WriteTutorial(tutorial);

							break;
						}

						case 3: //Text
						{
							Tolor.WriteText((String)msg.obj);

							break;
						}
					}
	            }
			};
		}

		clickingBlock = new boolean[Tolor.getBlocksNum()];

		Tolor.setOnBlockTouchListener(new TolorView.OnBlockTouchListener()
		{
			@Override
			public void ClickedBlock(int pos)
			{
				if (started && !finished && !countDown && !paused)
				{
					if ((tutorial == 0) || (tutorial == 3) || (tutorial == 4) || (tutorial == 5))
					{
						if ((tutorial != 0) && (pos == currentBlock))
						{
							guiding = tutorial;

							tutorial = 0;

							Tolor.WriteTutorial(tutorial);
						}

						clickingBlock[pos] = true;

						Tolor.SetBlockState(pos, clickingBlock[pos]);
					}
				}
			}
		});

		Tolor.setOnUpTouchListener(new TolorView.OnUpTouchListener()
		{
			@Override
			public void UpBlock(int pos)
			{
				clickingBlock[pos] = false;

				Tolor.SetBlockState(pos, clickingBlock[pos]);

				if ((guiding == 3) && (pos == currentBlock))
				{
					tutorial = guiding + 1;
					guiding = 0;
				}
			}
		});

		Tolor.setOnPauseListener(new TolorView.OnPauseListener()
		{
			@Override
			public void OnPause()
			{
				if (paused || (started && !finished && !countDown && (tutorial < 1)))
				{
					PauseGame();
				}
			}
		});

		Tolor.setOnTutorialListener(new TolorView.OnTutorialListener()
		{
			@Override
			public void OnTutorial()
			{
				if (started && !finished && !countDown && (tutorial > 0))
				{
					if ((tutorial != 3) && (tutorial != 4))
					{
						if (tutorial == 6)
						{
							guiding = tutorial;
							tutorial = 0;

							Tolor.WriteTutorial(tutorial);
						}

						else
						{
							tutorial++;
						}
					}
				}
			}
		});
	}

	private void Initialize()
	{
		colors.clear();

		//for (int i = 30 * (int)Math.floor(difficulty); i < 30 * ((int)Math.floor(difficulty) + 1); i++)
		for (int i = 0; i < 30; i++)
		{
			colors.add(TolorColors.colors[i]);
		}

		Collections.shuffle(colors);

		for (int i = 0; i < Tolor.getBlocksNum(); i++)
		{
			Tolor.SetBlockColor(currentBlock, i, Color.parseColor(colors.get(i)));
		}

		for (int i = 0; i < Tolor.getBlocksNum(); i++)
		{
			clickingBlock[i] = false;
		}

		SplashActivity.HideFinish();
	}

	public void NewGame()
	{
		if (started && countDown)
		{
			return;
		}

		Initialize();

		units = 0;
		difficulty = 0.0f;
		points = 0;
		seconds = 0;
		jollyTimeLeft = jollyTime;
		stentTime = 0;
		stenting = true;
		perfects = 0;
		goods = 0;
		matchSeconds = -1;
		tutorial = 0;
		guiding = 0;

		started = true;
		finished = false;

		wrong = false;
		perfect = true;

		Countdown();

		paused = false;

		Tolor.SetBlockColor(currentBlock, Tolor.getBlocksNum(), Color.parseColor("#000000"));
		Tolor.SetLifeLeft(0);
		Tolor.SetMatchSeconds(0.0f, 0.0f);
		Tolor.SetPastSeconds(0);
		Tolor.SetScore(0);
		Tolor.SetGoods(0);
		Tolor.SetPoints(0);
		Tolor.SetPerfects(0);
		Tolor.WriteTutorial(tutorial);

		Tolor.Finish(finished);
		Tolor.Pause(paused);
	}

	public void PauseGame()
	{
		paused = !paused;

		if (!paused)
		{
			Countdown();

			SplashActivity.MPlayer.start();
		}

		else
		{
			SplashActivity.MPlayer.pause();
		}

		Tolor.Pause(paused);
	}

	public void FinishGame(boolean result)
	{
		started = false;
		finished = true;
		paused = false;

		for (int i = 0; i < Tolor.getBlocksNum(); i++)
		{
			clickingBlock[i] = false;

			Tolor.SetBlockState(i, clickingBlock[i]);
		}

		if ((tutorial >= 6) || (guiding >= 6))
		{
			SplashActivity.configuration.setTutorial(false);
		}

		if (result)
		{
			int finalPoints = Utils.calculateGolds(points, goods, (int)(seconds / 10));

			Tolor.SetPoints(finalPoints);

			SplashActivity.configuration.setPoints(SplashActivity.configuration.getPoints() + finalPoints);

			SplashActivity.ShowFinish();

			splash.submitScore(finalPoints);
		}

		Tolor.Finish(finished);
	}

	public void Countdown()
	{
		countDownSeconds = 0;

		countDown = true;
	}

	public boolean IsPaused()
	{
		return paused;
	}

	public boolean IsFinished()
	{
		return finished;
	}

	public void Clear()
	{
		handler = null;

		colors.clear();
		colors = null;

		difficulty = 0;

		gameTimer.cancel();
		gameTimer = null;

		gameTask.cancel();
		gameTask = null;

		started = false;
		finished = false;
		paused = false;
		countDown = true;
		stenting = true;
		tutorial = 0;
		guiding = 0;
	}

	public void setTolorView(TolorView Tolor)
	{
		TolorGame.Tolor = Tolor;
	}

	private int GenerateRandomBlock()
	{
		units++;

		return Utils.Random(0, Tolor.getBlocksNum() - 1);
	}

	private float GenerateMatchTime()
	{
		return (Utils.Random(5400, 6500) - (difficulty * (time * 10)));
	}

	class GameTask extends TimerTask
	{
		float oneSecond = 0;
		float realSeconds = 0;

		public void run()
		{
			if (started && !finished && !paused)
			{
				if (countDown)
				{
					realSeconds = countDownSeconds / 10;

					if ((countDownSeconds == 0) || (oneSecond >= 1.0f))
					{
						oneSecond = 0;

						if (realSeconds > (countDownTime - 1))
						{
							countDown = false;

							Tolor.CountdownClear();
						}

						else
						{
							Tolor.CountdownTime(countDownTime - (int)realSeconds);
						}
					}

					countDownSeconds++;
				}

				else
				{
					realSeconds = seconds / 10;

					if (jollyTimeLeft > 0)
					{
						if (matchSeconds >= time)
						{
							if (tutorial == 0)
							{
								if (!stenting)
								{
									matchSeconds -= time;
								}
							}
						}

						else
						{
							if (matchSeconds >= 0)
							{
								if (!wrong && (points > 0.0))
								{
									jollyTimeLeft += time;

									/*Message msg = new Message();
									msg.what = 3;
									msg.obj = SplashActivity.Language.get("playGoods");

									handler.sendMessage(msg);*/

									goods++;

									Tolor.SetGoods(goods);
								}
	
								if (perfect)
								{
									/*Message msg = new Message();
									msg.what = 3;
									msg.obj = SplashActivity.Language.get("playPerfect");

									handler.sendMessage(msg);*/

									perfects++;

									Tolor.SetPerfects(perfects);
								}
							}

							matchSeconds = GenerateMatchTime();
							currentMatchSeconds = matchSeconds;

							currentBlock = GenerateRandomBlock();

							perfect = true;
							wrong = false;

							stentTime = 0;

							Message msg = new Message();
							msg.what = 0;

							handler.sendMessage(msg);

							if (SplashActivity.configuration.getTutorial() && (tutorial == 0) && (guiding == 0))
							{
								tutorial++;
							}
						}

						if ((tutorial >= 1) && !stenting)
						{
							Message msg = new Message();
							msg.what = 2;

							handler.sendMessage(msg);
						}

						else
						{
							if (clickingBlock[currentBlock])
							{
								points += pointsUnit;
	
								Tolor.SetScore(points);
							}
	
							else
							{
								if (stentTime >= (5 - difficulty)) //Steinting (no clicking blocks!)
								{
									jollyTimeLeft -= time;
									//jollyTimeLeft -= (time * 10);
	
									Tolor.SetLifeLeft(((jollyTime - jollyTimeLeft) * 100) / jollyTime);

									stenting = false;

									Tolor.SetStenting(false);

									perfect = false;
								}
	
								else //Stent standby time
								{
									stenting = true;

									Tolor.SetStenting(true);
	
									stentTime++;
								}
	
								for (int i = 0; i < Tolor.getBlocksNum(); i++)
								{
									if (clickingBlock[i])
									{
										points -= pointsUnit;
	
										if (points < 0)
										{
											points = 0;
										}
	
										Tolor.SetScore(points);

										wrong = true;
	
										break;
									}
								}
							}
						}

						if ((seconds == 0) || (oneSecond >= 1.0f))
						{
							oneSecond = 0;

							Tolor.SetMatchSeconds((int)Math.floor(matchSeconds / 1000), currentMatchSeconds / 1000);
							Tolor.SetPastSeconds((int)Math.ceil(realSeconds));
						}
					}

					else
					{

						Message msg = new Message();
						msg.what = 1;

						handler.sendMessage(msg);
					}

					seconds++;
				}

				if ((tutorial == 0) || countDown)
				{
					oneSecond += 0.1f;
				}

				if (tutorial == 0)
				{
					difficulty += 0.002f;
				}
			}
		}
	}
}
