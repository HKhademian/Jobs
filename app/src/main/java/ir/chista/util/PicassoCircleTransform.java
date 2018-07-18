/**
 * https://stackoverflow.com/questions/26112150/android-create-circular-image-with-picasso
 * https://gist.github.com/julianshen/5829333
 */
package ir.chista.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.squareup.picasso.Transformation;

public class PicassoCircleTransform implements Transformation {
  @Override
  public Bitmap transform(Bitmap source) {
    int size = Math.min(source.getWidth(), source.getHeight());

    int x = (source.getWidth() - size) / 2;
    int y = (source.getHeight() - size) / 2;

    Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
    if (squaredBitmap != source) {
      source.recycle();
    }

    final Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

    final Canvas canvas = new Canvas(bitmap);
    final Paint paint = new Paint();
    final BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
    paint.setShader(shader);
    paint.setAntiAlias(true);

    float r = size / 2f;
    canvas.drawCircle(r, r, r, paint);

    squaredBitmap.recycle();
    return bitmap;
  }

  @Override
  public String key() {
    return "circle";
  }
}
