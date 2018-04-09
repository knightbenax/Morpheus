package com.ephod.morpheus;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

	public CustomTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	
	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		if(!isInEditMode()){ //this is to prevent, the Viewer from trying to render the XML view
			//Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Omnes.ttf");
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Regular.ttf");
			setTypeface(tf);
		}
	}
	
}