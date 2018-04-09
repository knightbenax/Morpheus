package com.ephod.morpheus;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewBold extends android.support.v7.widget.AppCompatTextView {

	public CustomTextViewBold(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	
	public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public CustomTextViewBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		if(!isInEditMode()){ //this is to prevent, the Viewer from trying to render the XML view
			//Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OmnesSemibold.ttf");
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Heavy.ttf");
			setTypeface(tf);
		}
	}
	
}