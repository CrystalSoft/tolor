package it.crystalsoft.tolor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

public class GradientBar
{
	private RectF myRect = null;
	private RectF myRect2 = null;
	private Paint paint = null;

	private int width = 0;
	private int height = 0;

	private int strokeWidth = 1;
	
	public GradientBar(int width, int height)
	{
		myRect = new RectF();
		myRect2 = new RectF();
		paint = new Paint(Paint.DITHER_FLAG);

		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(true);
		paint.setDither(true);

		this.width = width;
		this.height = height;
	}

	public void draw(Canvas canvas, int percentage, int color, int color2, int x, int y, int padding)
	{
		paint.setColor(color);

		if (percentage >= 0)
		{
			paint.setShader(new LinearGradient(0, 0, x + width, 0, color, color2, Shader.TileMode.MIRROR));
		}

		else
		{
			paint.setColor(Utils.DarkerColor(Utils.ComplimentColor(color)));
		}

		myRect.set(x, y, x + width, y + height);

		canvas.drawRect(myRect, paint);

		if (percentage >= 0)
		{
			paint.setColor(Color.parseColor("#000000"));
			paint.setShader(null);

			myRect2.set(x + (width - ((width * percentage) / 100)), y, x + width, y + height);

			canvas.drawRect(myRect2, paint);
		}
	}
}