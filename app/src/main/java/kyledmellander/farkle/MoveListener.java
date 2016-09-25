package kyledmellander.farkle;

import android.os.Handler;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Kyle on 5/8/2016.
 */
public class MoveListener implements View.OnTouchListener {
    private int windowwidth;
    private int windowheight;
    private ImageView image = null;
    final Handler handler = new Handler();

    //Handle long clicks with no movement
    Runnable mLongPressed = new Runnable() {
        public void run() {
            image.playSoundEffect(SoundEffectConstants.CLICK);
            image.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
    };

    public MoveListener(ImageView image) {
        super();
        this.image = image;
    }

    public boolean onTouch(View view, MotionEvent event)  {
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        final int w = (int) ((double)image.getWidth()/1.5);
        final int h = (int) ((double)image.getHeight()/1.5);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                handler.postDelayed(mLongPressed,750);
                break;
            case MotionEvent.ACTION_MOVE:
                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                handler.removeCallbacks(mLongPressed);
                image.setX(x_cord-w);
                image.setY(y_cord-h);
                break;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(mLongPressed);
                break;
        }
        return true;
    }
}
