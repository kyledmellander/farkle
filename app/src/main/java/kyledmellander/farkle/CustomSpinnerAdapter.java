package kyledmellander.farkle;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kyle on 5/11/2016.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Typeface font;

    private CustomSpinnerAdapter(Context context, int resource, int textViewResId) {
        super(context,resource,textViewResId);
        font = Typeface.createFromAsset(context.getAssets(),"fonts/minecraft.ttf");
    }

    //Affects default (closed) state of spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }

    //Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }
}

