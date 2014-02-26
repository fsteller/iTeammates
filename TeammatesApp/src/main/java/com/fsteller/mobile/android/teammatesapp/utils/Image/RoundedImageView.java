package com.fsteller.mobile.android.teammatesapp.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Project: iTeammates
 * Subpackage: utils.image
 * <p/>
 * Description:
 * Created by fsteller on 12/30/13.
 */
final class RoundedImageView extends ImageView {

    public RoundedImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(final Canvas canvas) {

        final Drawable drawable = getDrawable();

        if (drawable == null)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        final int w = getWidth(), h = getHeight();
        final Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        final Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap roundBitmap = getCroppedBitmap(bitmap, w > h ? h : w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    private static Bitmap getCroppedBitmap(final Bitmap bmp, final int radius) {
        Bitmap mBitmap;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            mBitmap = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            mBitmap = bmp;

        final Bitmap output = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final int color = 0xffa19774;//Color.parseColor("#BAB399")
        final Rect rect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(mBitmap.getWidth() / 2 + 0.7f, mBitmap.getHeight() / 2 + 0.7f, mBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBitmap, rect, rect, paint);

        return output;
    }

}
