package kyledmellander.farkle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Kyle on 5/11/2016.
 */
public class FontTextView extends TextView {
    public FontTextView (Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
            "fonts/minecraft.ttf");
        this.setTypeface(face);
    }

    public FontTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
            "fonts/minecraft.ttf");
        this.setTypeface(face);
    }

    public FontTextView (Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
            "fonts/minecraft.ttf");
        this.setTypeface(face);
    }
}
