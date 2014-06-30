package it.crystalsoft.tolor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

public class TolorView extends View
{
	//private Context context = null;

	private static int width = 0;
	private static int height = 0;

	private static int salt = 0;
	private static int salt2 = 0;
	private final int portraitNum = 3;
	private final int landscapeNum = 6;

	private static OnBlockTouchListener listener = null;
	private static OnUpTouchListener upListener = null;
	private static OnPauseListener pauseListener = null;
	private static OnTutorialListener tutorialListener = null;

	private static int blockWidth = 0;
	private static int blockHeight = 0;
	private static int blockPadding = 0;
	private static int topPadding = 0;
	private static int bottomPadding = 0;
	private static int leftPadding = 0;
	private static int separatorPos = 0;

	private final static int blocksNum = 18;
	private ColorBlock[] block = new ColorBlock[blocksNum + 1];
	private static int[] color = new int[blocksNum + 1];
	private static boolean[] pressed = new boolean[blocksNum];

	private static int currentBlock = 0;

	private final String textColor = "#AAAAAA";

	private TextBlock text = null;
	private Rectangle rect = null;

	private RoundRect pause = null;
	private Separator separator = null;

	private ImageBlock image = null;
	private Bitmap imagePlay = null;

	private static boolean paused = false;
	private static boolean finished = false;

	private final int lifeWarning = 65;
	private static int lifeLeft = 0;
	private static boolean stenting = false;
	private GradientBar lifeBar = null;

	private static boolean countDown = false;
	private static boolean gameCountDown = true;
	private static int countDownTime = 0;

	private static int seconds = 0;
	private static int score = 0;
	private static int points = 0;
	private static int perfects = 0;
	private static int goods = 0;
	private static float currentMatchSeconds = 0;
	private static float matchSeconds = 0;

	private static String pastTime = "";

	private static Handler handler = null;

	private TutorialBox tutorialBox = null;
	private static int tutorial = 0;
	private static String[] tutorialText = null;

	private static String animText = "";

	private int alphing = 0;
	private int alphaSign = 0;

	public TolorView(Context context)
	{
		super(context);  

		InitiateView(context);          
	}

	public TolorView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		InitiateView(context);
	}

	public TolorView(final Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		InitiateView(context);
	}

	private void InitiateView(final Context cont)
	{
		countDown = false;
		countDownTime = 0;

		if (handler == null)
		{
			handler = new Handler()
			{
				@Override
	            public void handleMessage(Message msg)
	            {
					switch (msg.what)
					{
						case 0: //???
						{
							

							break;
						}
					}
	            }
			};
		}

		getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
		{
			@Override
			public void onGlobalLayout()
			{
				if (block[0] == null)
				{
					width = getWidth();
					height = getHeight();

					salt = ((width < height) ? portraitNum : landscapeNum);
					salt2 = ((width < height) ? landscapeNum : portraitNum);

					blockWidth = (int)(width / salt);
					blockWidth = blockWidth - (int)(blockWidth / 4);

					blockPadding = (int)(width / 21.0f);

					blockHeight = (int)(height / (salt2 + portraitNum));
					blockHeight = blockHeight - (int)(blockPadding / 1.2);

					topPadding = blockHeight + (int)(blockPadding * 3f);
					leftPadding = (blockWidth / 4);
					//bottomPadding = (blockPadding / 2);
					bottomPadding = blockPadding;

					separatorPos = (int)(blockHeight / 1.5f) + (int)(blockPadding * 3.2f);

					for (int i = 0; i < blocksNum + 1; i++)
					{
						block[i] = new ColorBlock(blockWidth, blockHeight);
					}

					pause = new RoundRect((blockWidth / 3), (blockHeight / 3));
					text = new TextBlock(width, height);
					rect = new Rectangle();
					separator = new Separator(width, 10);
					lifeBar = new GradientBar(width, (int)(blockHeight / 3));
					image = new ImageBlock(width, height);

					imagePlay = BitmapFactory.decodeResource(cont.getResources(), R.drawable.play);

					tutorialBox = new TutorialBox(cont, (int)(width / 2), (int)(width / 1.8));

					tutorialText = new String[]
					{
							SplashActivity.Language.get("playTutWelcome"),
							"&uarr;<br/>" + SplashActivity.Language.get("playTutColor"),
							SplashActivity.Language.get("playTutPress"),
							SplashActivity.Language.get("playTutReleased"),
							SplashActivity.Language.get("playTutTime"),
							SplashActivity.Language.get("playTutFinish")
					};
				}
			}
		});
	}

	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		if (alphing >= 130)
		{
			alphaSign = -5;
		}

		else if (alphing <= 0)
		{
			alphaSign = 5;
		}

		alphing += alphaSign;

		if (!paused && (block[0] != null))
		{
			if (lifeLeft >= lifeWarning)
			{
				if (lifeLeft >= 100)
				{
					alphing = 130;

					if (SplashActivity.configuration.getSoundEnabled())
					{
						SplashActivity.Player.stopSounds();
					}
				}

				else if (lifeLeft >= 65)
				{
					if (SplashActivity.configuration.getSoundEnabled())
					{
						if (!SplashActivity.Player.isPlaying(4))
						{
							SplashActivity.Player.playLoopedSound(4);
						}
					}
				}

				canvas.drawARGB(alphing, 200, 0, 0);
			}

			if (!finished)
			{
				int currentBlockX = (width / 2) - (blockWidth / 2), currentBlockY = (int)(blockPadding * 1.5);
				int finalX = 0, finalY = 0, correctX = 0, correctY = 0;
	
				for (int i = 0, y = 0, z = 0; i < blocksNum; i++, y++)
				{
					finalX = (y * blockWidth) + (leftPadding * (y + 1));
					finalY = (topPadding + (z * blockHeight) + (bottomPadding * (z + 1)));

					if (currentBlock == i)
					{
						correctX = finalX;
						correctY = finalY;
					}

					block[i].draw(canvas, pressed[i] ? ((currentBlock == i) ? Utils.DarkerColor(color[i]) : Utils.ComplimentColor(color[i])) : color[i], finalX, finalY, pressed[i] ? (int)(blockPadding / 1.5) : 0, pressed[i] && (currentBlock == i) ? matchSeconds : -1, currentMatchSeconds);

					if (pressed[i])
					{
						boolean correct = (currentBlock == i);

						if (SplashActivity.configuration.getSoundEnabled() && !finished)
						{
							if (SplashActivity.Player.isPlaying(correct ? 3 : 2))
							{
								SplashActivity.Player.stopSound(correct ? 3 : 2);
							}

							if (!SplashActivity.Player.isPlaying(correct ? 2 : 3))
							{
								SplashActivity.Player.playLoopedSound(correct ? 2 : 3);
							}
						}
					}

					if (y >= (salt - 1))
					{
						y = -1;
	
						z++;
					}
				}

				//Rectangle for Pause
				if (!gameCountDown)
				{
					pause.draw(canvas, Color.parseColor(textColor), (width - (blockPadding / 2)) - (int)(blockWidth / 3.3f), (int)(blockPadding / 3.0f));
					text.draw(canvas, "II", (blockWidth / 7), Color.parseColor(textColor), blockPadding, (int)(blockPadding * 1.2f), true, false, 0, false);
				}

				if ((tutorial == 4) || (tutorial == 5))
				{
					canvas.drawARGB(180, 0, 0, 0);
				}

				//Game score
				text.draw(canvas, SplashActivity.Language.get("playScore"), width / 12.0f, Utils.DarkerColor(Color.parseColor(textColor)), blockPadding, (int)(blockPadding * 2.7f), false, false, 0, false);
				text.draw(canvas, String.format("%05d", score), width / 12.0f, Color.parseColor(textColor), blockPadding, (int)(blockPadding * 4.2f), false, false, 0, false);

				//Separator & Life Bar
				separator.draw(canvas, Color.parseColor(textColor), 0, separatorPos);
				separator.draw(canvas, Color.parseColor(textColor), 0, separatorPos + (int)(blockHeight / 3));
				lifeBar.draw(canvas, stenting ? -1 : lifeLeft, Color.parseColor("#FF0000"), Color.parseColor("#00FF00"), 0, separatorPos, 0);

				if (tutorial == 3)
				{
					canvas.drawARGB(180, 0, 0, 0);
				}

				//Game seconds
				text.draw(canvas, String.format("%02.0fs", matchSeconds), width / 8.0f, Color.parseColor(textColor), blockPadding, (int)(blockPadding * 4.2f), true, false, 0, false);

				//Game countdown
				if (countDown)
				{
					text.draw(canvas, String.format("%d", countDownTime), width / 1.5f, Color.parseColor("#FFFFFF"), 0, 0, false, false, 2, true);
	
					invalidate();
				}

				if (animText.length() > 0)
				{
					text.draw(canvas, animText, width / 1.5f, Color.parseColor("#FFFFFF"), 0, 0, false, false, 2, true);
				}
	
				if (lifeLeft >= 65)
				{
					if (lifeLeft < 100)
					{
						invalidate();
					}
				}

				if (tutorial > 0)
				{
					float tutTextSize = (width / 21.0f);

					if (!gameCountDown)
					{
						if ((tutorial >= 1) && (tutorial <= 2) || (tutorial == 6))
						{
							canvas.drawARGB(180, 0, 0, 0);
						}

						if ((tutorial == 1) || (tutorial == 6)) //Welcome and messages
						{
							tutorialBox.draw(canvas, tutorialText[tutorial - 1], SplashActivity.Language.get("playTutOK"), tutTextSize, Color.parseColor("#666666"), (width / 4), (height / 2) - (width / 4));
						}

						else if (tutorial == 2) //Indicare blocco e spiegazione generale
						{
							tutorialBox.draw(canvas, tutorialText[tutorial - 1], SplashActivity.Language.get("playTutOK2"), tutTextSize, Color.parseColor("#666666"), (width / 4), (int)(blockHeight * 1.7f));
						}

						else if ((tutorial == 3) || (tutorial == 4) || (tutorial == 5)) //Premi questo colore
						{
							block[currentBlock].draw(canvas, color[currentBlock], correctX, correctY, 0, -1, currentMatchSeconds);

							if (correctX <= leftPadding)
							{
								correctX = leftPadding;
							}

							else if ((correctX + (width / 2)) >= width)
							{
								correctX = width - (width / 2) - leftPadding;
							}

							else
							{
								correctX -= (width / 8); 
							}

							correctY += blockHeight + bottomPadding;

							if (correctY <= bottomPadding)
							{
								correctY = bottomPadding;
							}

							else if ((correctY + (int)(width / 1.8f)) >= height)
							{
								//correctY = height - (int)(width / 1.8f) - bottomPadding;
								correctY -= (int)(width / 1.8f) + (bottomPadding * 2) + blockHeight;
							}

							tutorialBox.draw(canvas, tutorialText[tutorial - 1], "", tutTextSize, Color.parseColor("#666666"), correctX, correctY);
						}
					}
				}

				//Current Block & Past time
				if (!gameCountDown && (tutorial != 1))
				{
					block[blocksNum].draw(canvas, color[blocksNum], currentBlockX, currentBlockY, 0, -1, 0);

					text.draw(canvas, pastTime, width / 20.0f, (color[blocksNum] != Color.parseColor("#000000")) ? Utils.ComplimentColor(color[blocksNum]) : 0, 0, currentBlockY + (int)(blockHeight / 1.5), false, false, 1, false);
				}
			}

			else //Finish Game / Game over
			{
				text.draw(canvas, SplashActivity.Language.get("playFinishGame"), (width / 6f), Color.parseColor("#FFFFFF"), 0, (height / 15f) * 1.7f, false, false, 1, false);

				rect.draw(canvas, Color.parseColor("#222222"), width, (int)(height / 3f), 0, (int)((height / 15f) * 3));

				text.draw(canvas, SplashActivity.Language.get("playTime") + ": " + pastTime, (width / 8f), Color.parseColor("#018b0a"), (int)(((float)width / (float)100) * 5), (height / 15f) * 4.3f, false, false, 0, false);
				text.draw(canvas, Utils.Titleize(SplashActivity.Language.get("playScore")) + ": " + score, (width / 8f), Color.parseColor("#89030a"), (int)(((float)width / (float)100) * 5), (height / 11f) * 4.3f, false, false, 0, false);
				text.draw(canvas, Utils.Titleize(SplashActivity.Language.get("playGoods")) + ": " + goods, (width / 8f), Color.parseColor("#010392"), (int)(((float)width / (float)100) * 5), (height / 11f) * 5.3f, false, false, 0, false);

				//text.draw(canvas, SplashActivity.Language.get("playFinishGolds"), (width / 9f), Color.parseColor("#DDDDDD"), 0, (height / 11f) * 7f, false, false, 1, false);
				//text.draw(canvas, String.format("%d", Utils.calculateGolds(score, goods, seconds)), (width / 8f), Color.parseColor("#ffe400"), 0, (height / 11f) * 8f, false, false, 1, false);

				text.draw(canvas, SplashActivity.Language.get("playFinishPoints"), (width / 9f), Color.parseColor("#DDDDDD"), 0, (height / 11f) * 7f, false, false, 1, false);
				text.draw(canvas, String.format("%d", points), (width / 8f), Color.parseColor("#ffe400"), 0, (height / 11f) * 8f, false, false, 1, false);
			}
		}

		else if (paused)
		{
			canvas.drawRGB(100, 100, 100);
			canvas.drawARGB(alphing, 0, 0, 0);

			//text.draw(canvas, "II", width / 1.5f, Color.parseColor("#000000"), 0, 0, false, false, 2, false);
			image.draw(canvas, imagePlay, (int)(width / 2.0f), (int)(width / 2.0f), 0, 0, false, false, 2);

			invalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		super.onTouchEvent(event);

		int x = (int)event.getX();
		int y = (int)event.getY();

		for (int i = 0; i < blocksNum; i++)
		{
			//if (block[i].contains(x, y) && !paused)
			if (block[i].contains(x, y))
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
					{
						ClickedBlock(i);

						break;
					}

					case MotionEvent.ACTION_UP:
					{
						UpBlock(i);

						break;
					}
				}

				break;
			}

			else if (event.getAction() == MotionEvent.ACTION_MOVE)
			{
				UpBlock(i);
			}
		}

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			{
				if (pause.contains(x, y) || paused)
				{
					OnPause();
				}

				else if (tutorialBox.contains(x, y))
				{
					OnTutorial();
				}

				break;
			}
		}

		return true;
	}

	private void ClickedBlock(int pos)
	{
		if (listener != null)
		{
			listener.ClickedBlock(pos);
		}
	}

	private void UpBlock(int pos)
	{
		if (upListener != null)
		{
			upListener.UpBlock(pos);
		}
	}

	private void OnPause()
	{
		if (pauseListener != null)
		{
			pauseListener.OnPause();
		}
	}

	private void OnTutorial()
	{
		if (tutorialListener != null)
		{
			tutorialListener.OnTutorial();
		}
	}

	public int getBlocksNum()
	{
		return blocksNum;
	}

	public void CountdownTime(int seconds)
	{
		if (!countDown)
		{
			countDown = true;
		}

		if (text != null)
		{
			text.Clear();
		}

		countDownTime = seconds;

		postInvalidate();

		if (SplashActivity.configuration.getSoundEnabled())
		{
			SplashActivity.Player.playSound(0, 1.0f + ((seconds * 0.4f) / 3));
		}
	}

	public void CountdownClear()
	{
		countDown = false;
		gameCountDown = false;

		postInvalidate();
	}

	public void SetScore(int score)
	{
		TolorView.score = score;

		postInvalidate();
	}

	public void SetGoods(int goods)
	{
		TolorView.goods = goods;
	}

	public void SetPerfects(int perfects)
	{
		TolorView.perfects = perfects;
	}

	public void SetPoints(int points)
	{
		TolorView.points = points;
	}

	public void SetMatchSeconds(float matchSeconds, float currentMatchSeconds)
	{
		if (matchSeconds != TolorView.matchSeconds)
		{
			TolorView.currentMatchSeconds = currentMatchSeconds;
			TolorView.matchSeconds = matchSeconds;
		}

		postInvalidate();
	}

	public void SetPastSeconds(int seconds)
	{
		this.seconds = seconds;

		String pastTime = String.format("%d:%02d", seconds / 60, seconds % 60);

		if (!pastTime.equals(TolorView.pastTime))
		{
			TolorView.pastTime = pastTime;

			postInvalidate();
		}
	}

	public void SetLifeLeft(int percentage)
	{
		lifeLeft = percentage;

		postInvalidate();
	}

	public void SetStenting(boolean stenting)
	{
		TolorView.stenting = stenting;

		postInvalidate();
	}

	public void SetBlockColor(int currentBlock, int pos, int color)
	{
		TolorView.currentBlock = currentBlock;
		TolorView.color[pos] = color;

		invalidate();
	}

	public void SetBlockState(int pos, boolean pressed)
	{
		if (SplashActivity.configuration.getSoundEnabled())
		{
			if (!TolorView.pressed[pos] && pressed)
			{
				SplashActivity.Player.playSound(1, 0);
			}

			if (TolorView.pressed[pos] && !pressed)
			{
				SplashActivity.Player.stopSound(2);
				SplashActivity.Player.stopSound(3);
			}
		}

		TolorView.pressed[pos] = pressed;

		invalidate();
	}

	public void WriteTutorial(int tutorial)
	{
		if (TolorView.tutorial != tutorial)
		{
			TolorView.tutorial = tutorial;

			invalidate();
		}
	}

	public void WriteText(String text)
	{
		this.text.Clear();

		animText = text;

		invalidate();
	}

	public void Pause(boolean pause)
	{
		paused = pause;

		gameCountDown = true;

		if (SplashActivity.configuration.getSoundEnabled())
		{
			if (paused)
			{
				SplashActivity.Player.stopSounds();
			}
	
			else
			{
				if (lifeLeft >= lifeWarning)
				{
					SplashActivity.Player.playLoopedSound(4);
				}
			}
		}

		invalidate();
	}

	public void Finish(boolean finish)
	{
		finished = finish;

		

		invalidate();
	}

	/*
	*	Listeners manage
	*/
	public interface OnBlockTouchListener
	{
	    public void ClickedBlock(int pos); 
	}

	public void setOnBlockTouchListener(OnBlockTouchListener listener)
	{
		TolorView.listener = listener;
	}

	public interface OnUpTouchListener
	{
	    public void UpBlock(int pos); 
	}

	public void setOnUpTouchListener(OnUpTouchListener listener)
	{
		upListener = listener;
	}

	public interface OnPauseListener
	{
	    public void OnPause(); 
	}

	public void setOnPauseListener(OnPauseListener listener)
	{
		pauseListener = listener;
	}

	public interface OnTutorialListener
	{
	    public void OnTutorial(); 
	}

	public void setOnTutorialListener(OnTutorialListener listener)
	{
		tutorialListener = listener;
	}
}
