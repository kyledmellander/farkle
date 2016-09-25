package kyledmellander.farkle;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Random;

public class Die implements View.OnTouchListener {
    public enum Status {
        selected, used, ready
    }
    private static final String TAG = Game.class.getSimpleName();
    private Status status = Status.ready;
    private int value = 0;
    private final ImageView image;
    private final int[] imagesArray = {R.drawable.ic_1,
            R.drawable.ic_2,
            R.drawable.ic_3,
            R.drawable.ic_4,
            R.drawable.ic_5,
            R.drawable.ic_6};

    final Handler handler = new Handler();

    //Handle long clicks with no movement to select die
    Runnable mLongPressed = new Runnable() {
        public void run() {
            switch (status) {
                case ready:
                    status = Status.selected;
                    image.setColorFilter(Color.BLACK, PorterDuff.Mode.LIGHTEN);
                    break;
                case selected:
                    status = Status.ready;
                    image.setColorFilter(null);
                    break;
                case used:
                    return;
            }
            image.playSoundEffect(SoundEffectConstants.CLICK);
            image.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
    };

    public Die(ImageView dieImage, DisplayMetrics m) {
        super();
        Random r = new Random();
        int minX = m.widthPixels / 6;
        int maxX = m.widthPixels / 2; //This plus minW
        int minY = m.heightPixels / 6;
        int maxY = m.heightPixels / 3;
        value = r.nextInt(6);
        image = dieImage;
        image.setImageResource(imagesArray[value]);
        image.setX(minX + r.nextInt(maxX));
        image.setY(minY + r.nextInt(maxY));
        image.setRotation(r.nextInt(360));
        image.setOnTouchListener(this);
    }

    public boolean onTouch(View view, MotionEvent event) {
        final int w = (int) ((double) image.getWidth() / 1.5);
        final int h = (int) ((double) image.getHeight() / 1.5);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                handler.postDelayed(mLongPressed, 150);
                break;
            case MotionEvent.ACTION_MOVE:
                if (status != Status.used) {
                    int x_cord = (int) event.getRawX();
                    int y_cord = (int) event.getRawY();
                    handler.removeCallbacks(mLongPressed);
                    image.bringToFront();
                    image.setX(x_cord - w);
                    image.setY(y_cord - h);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(mLongPressed);
                break;
        }
        return true;
    }

    public void rollDie(DisplayMetrics m, boolean force) {
        if (force) {
            status = Status.ready;
            image.setColorFilter(null);
            image.setImageAlpha(255);
        }

        if (status == Status.ready) {
            int minW = m.widthPixels / 6;
            int maxW = m.widthPixels / 2; //This plus minW
            int minH = m.heightPixels / 6;
            int maxH = m.heightPixels / 3; //This plus minHH
            Random r = new Random();
            value = r.nextInt(6);
            image.setImageResource(imagesArray[r.nextInt(6)]);
            image.bringToFront();
            image.animate()
                    .rotation(r.nextInt(360))
                    .x(minW + r.nextInt(maxW))
                    .y(minH + r.nextInt(maxH))
                    .setDuration(300 + r.nextInt(800))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            image.setImageResource(imagesArray[value]);
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
        } else {
            image.setImageAlpha(50);
            image.setColorFilter(null);
            status = Status.used;
        }
    }

    public int getValue() {
        return value + 1;
    }

    public Status getStatus() {
        return status;
    }
}
