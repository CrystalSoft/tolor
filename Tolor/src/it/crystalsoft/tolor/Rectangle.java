package it.crystalsoft.tolor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Rectangle
{
	private Rect myRect = null;
	private Paint paint = null;

	private int strokeWidth = 3;
	
	public Rectangle()
	{
		myRect = new Rect();
		paint = new Paint(Paint.DITHER_FLAG);

		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.FILL);
	}

	public void draw(Canvas canvas, int color, int width, int height, int x, int y)
	{
		paint.setColor(color);

		myRect.set(x, y, x + width, y + height);

		canvas.drawRect(myRect, paint);
	}

	public boolean contains(int x, int y)
	{
		return myRect.contains(x, y);
	}
}