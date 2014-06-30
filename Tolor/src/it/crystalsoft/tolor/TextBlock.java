package it.crystalsoft.tolor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

public class TextBlock
{
	private Paint paint = null;
	private Rect textBounds = null;

	private int strokeWidth = 1;

	private int currAlpha = 0;
	private int currAnimPos = 0;
	private int currTextSize = 0;
	private String prevStr = "";
	
	public TextBlock(int width, int height)
	{
		paint = new Paint(Paint.DITHER_FLAG);
		textBounds = new Rect();

		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(true);
		paint.setDither(true);
	}

	public void draw(Canvas canvas, String text, float size, int color, float x, float y, boolean right, boolean bottom, int centered, boolean anim)
	{
		paint.setColor(color);
		paint.setTextSize(size);
		paint.setTypeface(SplashActivity.Font);
		paint.getTextBounds(text, 0, text.length(), textBounds);

		if (centered == 0)
		{
			if (right)
			{
				x = canvas.getWidth() - x;

				paint.setTextAlign(Align.RIGHT);
			}

			else
			{
				paint.setTextAlign(Align.LEFT);
			}
	
			if (bottom)
			{
				y = canvas.getHeight() - textBounds.height() - y;
			}
		}

		else
		{
			if (centered > 0)
			{
				x = (canvas.getWidth() / 2)/* - (textBounds.width() / 2)*/;
			}

			if (centered > 1)
			{
				y = (canvas.getHeight() / 2)/* - (textBounds.height() / 2)*/;
			}

			paint.setTextAlign(Align.CENTER);
		}

		if (anim)
		{
			if (!prevStr.equals(text))
			{
				Clear();

				prevStr = text;
			}

			else
			{
				paint.setColor(Color.argb(255 - currAlpha, Color.red(color), Color.green(color), Color.blue(color)));
				paint.setTextSize(size - currTextSize);
				//paint.setShadowLayer(10.0f, 0.0f, 0.0f, Color.WHITE);

				y += currAnimPos;

				currAlpha += 8;

				if (currAlpha >= 255)
				{
					currAlpha = 255;
				}

				currAnimPos += (canvas.getHeight() / 60);
				currTextSize += (canvas.getWidth() / 50);

				if (currTextSize >= size)
				{
					currTextSize = (int)size;
				}
			}
		}

		canvas.drawText(text, x, y, paint);

		/*paint.setTextAlign(Align.CENTER);

		canvas.drawText(text, 0, 0, paint);*/

		//canvas.drawText(text, canvas.getWidth() / 2, canvas.getHeight() - size, paint);
	}

	public void Clear()
	{
		currAlpha = 0;
		currAnimPos = 0;
		currTextSize = 0;
		prevStr = "";
	}
}