package kyledmellander.farkle;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Kyle on 5/11/2016.
 */
public class FontButton extends Button {
    public FontButton(Context context) {
        super (context);
    }

    public FontButton(Context context, AttributeSet attrs) {
        super(context,attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
            "fonts/minecraft.ttf");
        this.setTypeface(face);
    }

    public FontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
            "fonts/minecraft.ttf");
        this.setTypeface(face);
    }
}
