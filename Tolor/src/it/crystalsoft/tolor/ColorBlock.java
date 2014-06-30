package it.crystalsoft.tolor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

public class ColorBlock
{
	private RectF myRect = null;
	private Paint paint = null;

	private int width = 0;
	private int height = 0;

	private int strokeWidth = 5;
	private int borderWidth = 0;
	
	public ColorBlock(int width, int height)
	{
		myRect = new RectF();
		paint = new Paint(Paint.DITHER_FLAG);

		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(true);
		paint.setDither(true);

		this.width = width;
		this.height = height;

		borderWidth = (width / 14);
	}

	public void draw(Canvas canvas, int color, int x, int y, int padding, float time, float currentTime)
	{
		int darkColor = Utils.DarkerColor(color);
		//int brighColor = Utils.BrighterColor(color);

		paint.setColor(darkColor);

		int borderRadius = (width / 10);

		myRect.set(x - (padding / 2), y - (padding / 2), (padding / 2) + x + width, (padding / 2) + y + height);

		paint.setShader(null);
		paint.setShader(new RadialGradient(x + (width / 2), y + (height / 2), height, Utils.DarkerColor(darkColor), darkColor, Shader.TileMode.CLAMP));

		canvas.drawRoundRect(myRect, borderRadius, borderRadius, paint);

		if (time >= 0)
		{
			paint.setShader(null);
			paint.setColor(Utils.DarkerColor(darkColor));

			int dynPadding = (padding / 2) - (int)(time * ((padding / 2) / currentTime));

			myRect.set(x - dynPadding, y - dynPadding, dynPadding + x + width, dynPadding + y + height);

			canvas.drawRoundRect(myRect, borderRadius, borderRadius, paint);
		}

		paint.setColor(color);

		paint.setShader(null);
		//paint.setShader(new RadialGradient(x + (width / 2) + (borderWidth / 2), y + height, (width / 1.5f), brighColor, color, Shader.TileMode.CLAMP));

		borderRadius -= (borderWidth / 2);

		myRect.set(x + borderWidth, y + borderWidth, x + width - borderWidth, y + height - borderWidth);

		canvas.drawRoundRect(myRect, borderRadius, borderRadius, paint);
	}

	public boolean contains(int x, int y)
	{
		return myRect.contains(x, y);
	}

	public RectF getRectF()
	{
		return myRect;
	}

	public Rect getRect()
	{
		return new Rect((int)myRect.left, (int)myRect.top, (int)myRect.right, (int)myRect.bottom);
	}
}