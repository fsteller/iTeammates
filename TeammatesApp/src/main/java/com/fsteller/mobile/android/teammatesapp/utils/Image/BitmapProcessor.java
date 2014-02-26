package com.fsteller.mobile.android.teammatesapp.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Project: iTeammates
 * Subpackage: utils.image
 * <p/>
 * Description:
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public interface BitmapProcessor {
    public Bitmap loadImage(final Context context, final Uri imageUri, final int imageSize);

    public Bitmap loadImage(final Context context, final String imageData, final int imageSize);
}
