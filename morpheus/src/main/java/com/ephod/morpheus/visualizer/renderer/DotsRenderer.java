/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package com.ephod.morpheus.visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.ephod.morpheus.visualizer.AudioData;
import com.ephod.morpheus.visualizer.FFTData;

public class DotsRenderer extends Renderer
{
  private int mDivisions;
  private Paint mPaint;
  private boolean mTop;

  /**
   * Renders the FFT data as a series of lines, in histogram form
   * @param divisions - must be a power of 2. Controls how many lines to draw
   * @param paint - Paint to draw lines with
   * @param top - whether to draw the lines at the top of the canvas, or the bottom
   */
  public DotsRenderer(int divisions,
                      Paint paint,
                      boolean top)
  {
    super();
    mDivisions = divisions;
    mPaint = paint;
    mTop = top;
  }

  @Override
  public void onRender(Canvas canvas, AudioData data, Rect rect)
  {
    // Do nothing, we only display FFT data
  }

  @Override
  public void onRender(Canvas canvas, FFTData data, Rect rect)
  {
      int mWidth = canvas.getWidth();
      int mHeight = canvas.getHeight();
      int numHoriBars = (mWidth/14) + 2;
      int barX = 7;

    for(int y = 0; y < numHoriBars; y++){

        int rectSize = 14;
        barX = barX + y;
        int finalRectSize = rectSize - 8;
        int lowpoint = -128;
        int rawValue = data.bytes[y];
        int topvalue = 127 - rawValue;
        int bottomValue = rawValue - (lowpoint);
        int lhs = (bottomValue * 61);
        int rhs = topvalue + bottomValue;

        int base = rectSize * 20;
        float numOfBar = (float)(lhs)/(rhs);
        /*Log.e("XC", "start");
        Log.e("XC", String.valueOf(numOfBar));
        Log.e("XC", "end");*/

        for (int z = 0; z < numOfBar; z++){
            canvas.drawCircle(y * rectSize, (((z  - 30) * rectSize)), finalRectSize, mPaint);
        }

        //Log.e("Width Of Box", String.valueOf(rectSize));
    }

      /*
      * for(int y = 0; y < 52; y++){
            int mWidth = canvas.getWidth();
            int mHeight = canvas.getHeight();
            int rectSize = mWidth/50;
            int finalRectSize = rectSize - 8;
            int lowpoint = -128;
            int rawValue = data.bytes[y];
            int topvalue = 127 - rawValue;
            int bottomValue = rawValue - (lowpoint);
            int lhs = (bottomValue * 61);
            int rhs = topvalue + bottomValue;

            int base = rectSize * 20;
            float numOfBar = (float)(lhs)/(rhs);

            for (int z = 0; z < numOfBar; z++){
                canvas.drawCircle(y * rectSize, (((z  - 30) * rectSize)), finalRectSize, mPaint);
            }

            Log.e("Width Of Box", String.valueOf(rectSize));
          }
    */

    /*for (int i = 0; i < data.bytes.length / mDivisions; i++) {
      mFFTPoints[i * 4] = i * 4 * mDivisions;
      mFFTPoints[i * 4 + 2] = i * 4 * mDivisions;
      byte rfk = data.bytes[mDivisions * i];
      byte ifk = data.bytes[mDivisions * i + 1];
      float magnitude = (rfk * rfk + ifk * ifk);
      int dbValue = (int) (10 * Math.log10(magnitude));

      if(mTop)
      {
        mFFTPoints[i * 4 + 1] = 0;
        mFFTPoints[i * 4 + 3] = (dbValue * 2 - 10);
      }
      else
      {
        mFFTPoints[i * 4 + 1] = rect.height();
        mFFTPoints[i * 4 + 3] = rect.height() - (dbValue * 2 - 10);
      }

      //canvas.drawCircle();
        //canvas.drawLines(mFFTPoints, mPaint);

    }


    canvas.drawPoints(mFFTPoints, mPaint);*/
  }
}
