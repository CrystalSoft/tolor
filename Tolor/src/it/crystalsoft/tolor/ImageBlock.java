package it.crystalsoft.tolor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class ImageBlock
{
	private RectF myRect = null;
	private Paint paint = null;

	private int strokeWidth = 1;
	
	public ImageBlock(int width, int height)
	{
		myRect = new RectF();
		paint = new Paint(Paint.DITHER_FLAG);

		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
	}

	public void draw(Canvas canvas, Bitmap bitmap, int width, int height, float x, float y, boolean right, boolean bottom, int centered)
	{
		if (centered == 0)
		{
			if (right)
			{
				x = canvas.getWidth() - x;
			}
	
			if (bottom)
			{
				y = canvas.getHeight() - y;
			}
		}

		else
		{
			if (centered > 0)
			{
				x = (canvas.getWidth() / 2) - (width / 2);
			}

			if (centered > 1)
			{
				y = (canvas.getHeight() / 2) - (height / 2);
			}
		}

		myRect.set(x, y, x + width, y + height);

		canvas.drawBitmap(bitmap, null, myRect, paint);
		
	}
}