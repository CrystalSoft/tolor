package it.crystalsoft.tolor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class RoundRect
{
	private RectF myRect = null;
	private Paint paint = null;

	private int width = 0;
	private int height = 0;

	private int strokeWidth = 3;
	
	public RoundRect(int width, int height)
	{
		myRect = new RectF();
		paint = new Paint(Paint.DITHER_FLAG);

		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);

		this.width = width;
		this.height = height;
	}

	public void draw(Canvas canvas, int color, int x, int y)
	{
		paint.setColor(color);

		int borderRadius = (width / 20);
 
		myRect.set(x, y, x + width, y + height);

		canvas.drawRoundRect(myRect, borderRadius, borderRadius, paint);
		//canvas.drawRect(myRect, paint);
	}

	public boolean contains(int x, int y)
	{
		return myRect.contains(x, y);
	}
}