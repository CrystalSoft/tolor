package it.crystalsoft.tolor;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class ColouredTextView extends TextView
{
	private final int[] colors = new int[] { Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA };

	private int width = -1;
	private int height = -1;

	private LinearGradient gradient = null;
	private BlurMaskFilter shadow = null;
	private Matrix matrix = null;
	private int offset = 0;
	private int angle = 0;

	public ColouredTextView(Context context)
	{
		super(context);

		InitiateView(context);
	}

	public ColouredTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		InitiateView(context);
	}

	public ColouredTextView(final Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		InitiateView(context);
	}

	private void InitiateView(Context context)
	{
		matrix = new Matrix();

		getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
		{
			@Override
			public void onGlobalLayout()
			{
				if ((width == -1) || (height == -1))
				{
					width = getWidth();
					height = getHeight();

					gradient = new LinearGradient(0, 0, width, 0, colors, null, Shader.TileMode.MIRROR);
					shadow = new BlurMaskFilter((width / 50), Blur.INNER);
				}
			}
		});
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		if (gradient != null)
		{
			if (offset >= (width * 2))
			{
				offset = 0;
			}
	
			else
			{
				offset += (width / 100);
			}
	
			if (angle > 360)
			{
				angle = 0;
			}
	
			matrix.setTranslate(offset, 0);
			//matrix.setRotate(angle++);
	
			gradient.setLocalMatrix(matrix);
	
			getPaint().setAntiAlias(true);
			getPaint().setShader(gradient);
			//getPaint().setShadowLayer(100.0f, 0.0f, 0.0f, 0xFFFFFFFF);
			getPaint().setMaskFilter(shadow);
	
			super.onDraw(canvas);
	
			invalidate();
		}
	}
}