package it.crystalsoft.tolor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.RectF;
import android.text.Html;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

public class TutorialBox
{
	private Paint paint = null;
	private TextPaint tPaint = null;
	private RectF boxRect = null;
	private RectF textRect = null;
	private RectF buttonRect = null;

	private int width = 0;
	private int height = 0;

	private int strokeWidth = 1;

	private StaticLayout textLayout = null;
	private StaticLayout buttonTextLayout = null;
	
	public TutorialBox(Context context, int width, int height)
	{
		paint = new Paint(Paint.DITHER_FLAG);
		tPaint = new TextPaint(TextPaint.DITHER_FLAG);
		boxRect = new RectF();
		textRect = new RectF();
		buttonRect = new RectF();

		this.width = width;
		this.height = height;

		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(true);
		paint.setDither(true);

		tPaint.setAntiAlias(true);
		tPaint.setDither(true);
	}

	public void draw(Canvas canvas, String text, String buttonText, float size, int color, float x, float y)
	{
		//int borderRadius = (width / 10);
		int borderWidth = (width / 10);

		//Rectangle
		paint.setColor(Color.parseColor("#FFFFFF"));

		boxRect.set((int)x, (int)y, (int)(x + width), (int)(y + height));
		
		paint.setShader(new RadialGradient(x + (width / 2), y + (height / 2), height, Utils.DarkerColor(color), color, Shader.TileMode.CLAMP));

		canvas.drawRect(boxRect, paint);

		//Text
		tPaint.setColor(Color.parseColor("#FFFFFF"));
		tPaint.setTextSize(size);

		textRect.set((int)x + borderWidth, (int)y + borderWidth, (int)(x + width) - borderWidth, (int)(y + height) - borderWidth);

		textLayout = new StaticLayout(Html.fromHtml(text), tPaint, (int)textRect.width(), Layout.Alignment.ALIGN_CENTER, 1, 1, false);

		canvas.save();
		canvas.translate(textRect.left, textRect.top);

		textLayout.draw(canvas);

		canvas.restore();

		if (buttonText.length() > 0)
		{
			//Button
			paint.setShader(null);
			paint.setColor(Color.parseColor("#CCCCCC"));

			buttonRect.set((int)x + borderWidth, (int)y + borderWidth + (int)(height / 1.5f), (int)(x + width) - borderWidth, (int)(y + height) - borderWidth);

			canvas.drawRect(buttonRect, paint);

			//Button Text
			tPaint.setColor(Color.parseColor("#000000"));
			tPaint.setTextSize(size);
	
			buttonTextLayout = new StaticLayout(buttonText, tPaint, (int)buttonRect.width(), Layout.Alignment.ALIGN_CENTER, 1, 1, false);
	
			canvas.save();
			canvas.translate(buttonRect.left, buttonRect.top + (int)(size / 3));
	
			buttonTextLayout.draw(canvas);
	
			canvas.restore();
		}
	}

	public boolean contains(int x, int y)
	{
		return buttonRect.contains(x, y);
	}
};