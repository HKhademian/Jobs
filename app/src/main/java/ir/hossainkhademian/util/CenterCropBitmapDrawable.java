package ir.hossainkhademian.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

/**
 * A subclass of bitmap drawable that scales the bitmap to CenterCrop
 * https://stackoverflow.com/questions/37012821/android-how-can-i-centercrop-the-background-of-a-view
 *
 * @author PremNirmal
 */
public class CenterCropBitmapDrawable extends BitmapDrawable {
  private final int width, height;

  public CenterCropBitmapDrawable(final Resources res, final Bitmap bitmap) {
    super(res, bitmap);
    this.width = bitmap.getWidth();
    this.height = bitmap.getHeight();
  }

  @Override
  public void draw(Canvas canvas) {
    final Matrix drawMatrix = new Matrix();
    final int dwidth = getIntrinsicWidth();
    final int dheight = getIntrinsicHeight();

    final int vwidth = width;
    final int vheight = height;

    float scale;
    float dx = 0, dy = 0;
    int saveCount = canvas.getSaveCount();
    canvas.save();
    if (dwidth * vheight > vwidth * dheight) {
      scale = (float) vheight / (float) dheight;
      dx = (vwidth - dwidth * scale) * 0.5f;
    } else {
      scale = (float) vwidth / (float) dwidth;
      dy = (vheight - dheight * scale) * 0.5f;
    }

    drawMatrix.setScale(scale, scale);
    drawMatrix.postTranslate(Math.round(dx), Math.round(dy));
    canvas.concat(drawMatrix);

    canvas.drawBitmap(getBitmap(), 0, 0, getPaint());

    canvas.restoreToCount(saveCount);
  }
}
