package it.crystalsoft.tolor;

import android.graphics.Canvas;
import android.graphics.Paint;

@SuppressWarnings("unused")
public class Separator
{
	private Paint paint = null;

	private int width = 0;
	private int height = 0;

	private int strokeWidth = 3;
	
	public Separator(int width, int height)
	{
		paint = new Paint(Paint.DITHER_FLAG);

		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.FILL);

		this.width = width;
		this.height = height;
	}

	public void draw(Canvas canvas, int color, int x, int y)
	{
		paint.setColor(color);

		canvas.drawLine(x, y, x + width, y, paint);
	}
}