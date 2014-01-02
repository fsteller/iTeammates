package com.fsteller.mobile.android.teammatesapp.utils.Image;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.fsteller.mobile.android.teammatesapp.BuildConfig;
import com.fsteller.mobile.android.teammatesapp.utils.VersionTools;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    private ImageUtils() {
    }

    public static ImageLoader setupImageLoader(final Activity activity, final int defaultImageRefId) {
        return setupImageLoader(activity, defaultImageRefId, ImageProcessor.getInstance());
    }

    public static ImageLoader setupImageLoader(final Activity activity, final int defaultImageRefId, final BitmapProcessor callback) {

        final ImageLoader mImageLoader = new ImageLoader(activity, getListPreferredItemHeight(activity)) {

            @Override
            protected Bitmap processBitmap(final Context context, final Object data) {
                Bitmap result = null;
                if (callback != null && data != null) {
                    if (data instanceof String)
                        result = callback.loadImage(context, (String) data, getImageSize());
                    else if (data instanceof Uri)
                        result = callback.loadImage(context, (Uri) data, getImageSize());
                }
                return result;
            }
        };

        mImageLoader.addImageCache(activity.getFragmentManager(), 0.1f);
        mImageLoader.setLoadingImage(defaultImageRefId);
        mImageLoader.setImageFadeIn(false);

        return mImageLoader;
    }

    /**
     * Gets the preferred height for each item in the ListView, in pixels, after accounting for
     * screen density. ImageProcessor uses this value to resize thumbnail images to match the ListView
     * item height.
     *
     * @return The preferred height in pixels, based on the current theme.
     */
    protected static int getListPreferredItemHeight(final Activity activity) {

        final TypedValue typedValue = new TypedValue();

        // Resolve list item preferred height theme attribute into typedValue
        activity.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, typedValue, true);

        // AddItem a new DisplayMetrics object
        final DisplayMetrics metrics = new DisplayMetrics();

        // Populate the DisplayMetrics
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Return theme value based on DisplayMetrics
        return (int) typedValue.getDimension(metrics);
    }

    public static final class ImageProcessor implements BitmapProcessor {

        private static ImageProcessor instance = null;

        private ImageProcessor() {
        }

        public static ImageProcessor getInstance() {
            if (instance == null)
                instance = new ImageProcessor();
            return instance;
        }

        @Override
        public final Bitmap loadImage(final Context context, final String imageData, final int imageSize) {

            Uri thumbUri = null;
            if (context != null)
                //   Ensures the Fragment is still added to an activity. As this method is called in a
                //   background thread, there's the possibility the Fragment is no longer attached and
                //   added to an activity. If so, no need to spend resources loading the contact photo.
                if (VersionTools.hasHoneycomb())
                    // If Android 3.0 or later, converts the Uri passed as a string to a Uri object.
                    thumbUri = Uri.parse(imageData);
                else {
                    // For versions prior to Android 3.0, appends the string argument to the content
                    // Uri for the teams table.
                    final Uri contentUri = ContactsContract.Contacts.CONTENT_URI;
                    if (contentUri != null) {
                        final Uri contactUri = Uri.withAppendedPath(contentUri, imageData);
                        if (contactUri != null)
                            thumbUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                    }
                }
            return loadImage(context, thumbUri, imageSize);
        }

        @Override
        public final Bitmap loadImage(final Context context, final Uri imageUri, final int imageSize) {

            // Ensures the Fragment is still added to an activity. As this method is called in a
            // background thread, there's the possibility the Fragment is no longer attached and
            // added to an activity. If so, no need to spend resources loading the contact photo.
            if (context == null)
                return null;

            // Instantiates an AssetFileDescriptor. Given a content Uri pointing to an image file, the
            // ContentResolver can return an AssetFileDescriptor for the file.
            Bitmap image = null;
            AssetFileDescriptor afd = null;

            try {
                // Retrieves a file descriptor from the teams Provider. To learn more about this
                // feature, read the reference documentation for
                // ContentResolver#openAssetFileDescriptor.
                afd = context.getContentResolver().openAssetFileDescriptor(imageUri, "r");

                // Gets a FileDescriptor from the AssetFileDescriptor. A BitmapFactory object can
                // decode the contents of a file pointed to by a FileDescriptor into a Bitmap.
                if (afd != null) {
                    final FileDescriptor fileDescriptor = afd.getFileDescriptor();
                    if (fileDescriptor != null)
                        // Decodes a Bitmap from the image pointed to by the FileDescriptor, and scales it
                        // to the specified width and height
                        image = ImageLoader.decodeSampledBitmapFromDescriptor(fileDescriptor, imageSize, imageSize);
                }
            } catch (FileNotFoundException e) {
                // If the file pointed to by the thumbnail URI doesn't exist, or the file can't be
                // opened in "read" mode, ContentResolver.openAssetFileDescriptor throws a
                // FileNotFoundException.
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "Contact photo thumbnail not found for contact " + imageUri + ": " + e.toString());

            } finally { // If an AssetFileDescriptor was returned, try to close it
                if (afd != null)
                    try {
                        afd.close();
                    } catch (IOException ignored) {
                    }
            }

            // If the decoding failed, returns null
            return image;
        }

    }

}
