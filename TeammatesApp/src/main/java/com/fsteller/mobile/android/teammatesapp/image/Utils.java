package com.fsteller.mobile.android.teammatesapp.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.database.Contract;
import com.fsteller.mobile.android.teammatesapp.handlers.DatabaseHandler;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidParameterException;

/**
 * Project: iTeammates
 * Subpackage: image
 * <p/>
 * Description:
 * <p/>
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class Utils {

    //<editor-fold desc="Constants">

    private static final String TAG = Utils.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Constructor">

    /**
     * Private constructor used in order to prevent instantiation of this class.
     */
    private Utils() {
    }

    //</editor-fold>

    //<editor-fold desc="Public">

    public static Loader setupImageLoader(final Activity activity, final int defaultImageRefId) {
        return setupImageLoader(activity, defaultImageRefId, ImageProcessor.getInstance());
    }

    public static Loader setupImageLoader(final Activity activity, final int defaultImageRefId, final BitmapProcessor callback) {

        final Loader mLoader = new Loader(activity, getListPreferredItemHeight(activity)) {
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

        mLoader.addImageCache(activity.getFragmentManager(), 0.1f);
        mLoader.setLoadingImage(defaultImageRefId);
        mLoader.setImageFadeIn(false);

        return mLoader;
    }

    public static byte[] getImageAsByteArray(final String url) {
        try {
            final URL imageUrl = new URL(url);
            final URLConnection ucon = imageUrl.openConnection();

            final InputStream is = ucon.getInputStream();
            final ByteArrayBuffer baf = new ByteArrayBuffer(500);
            final BufferedInputStream bis = new BufferedInputStream(is);

            int current;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            return baf.toByteArray();
        } catch (final Exception e) {
            Log.d(TAG, "Error: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static void PickImage(final Activity mActivity) {
        Log.d(TAG, "Raising intent to pick  and crop image...");

        final String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {

            final Intent pickCropImageIntent =
                    new Intent(Intent.ACTION_PICK, TC.MediaStore.MEDIA_CONTENT_URI);

            pickCropImageIntent.setType("image/*");
            pickCropImageIntent.putExtra("crop", "true");
            pickCropImageIntent.putExtra("scale", "true");
            pickCropImageIntent.putExtra("return-data", "true");
            pickCropImageIntent.putExtra("outputX", TC.MediaStore.ImageOutputY);
            pickCropImageIntent.putExtra("outputY", TC.MediaStore.ImageOutputX);
            pickCropImageIntent.putExtra("aspectX", TC.MediaStore.ImageOutputY);
            pickCropImageIntent.putExtra("aspectY", TC.MediaStore.ImageOutputX);
            pickCropImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG);
            pickCropImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, TC.MediaStore.URI_TMP_FILE);
            mActivity.startActivityForResult(pickCropImageIntent, TC.MediaStore.Pick_Image);
        }
    }

    //</editor-fold>
    //<editor-fold desc="private">

    /**
     * Gets the preferred height for each item in the ListView, in pixels, after accounting for
     * screen density. ImageProcessor uses this value to resize thumbnail images to match the ListView
     * item height.
     *
     * @return The preferred height in pixels, based on the current theme.
     */
    private static int getListPreferredItemHeight(final Activity activity) {

        final TypedValue typedValue = new TypedValue();

        // Resolve list item preferred height theme attribute into typedValue
        Resources.Theme mTheme = activity.getTheme();
        if (mTheme != null)
            mTheme.resolveAttribute(android.R.attr.listPreferredItemHeight, typedValue, true);

        // AddItem a new DisplayMetrics object
        final DisplayMetrics metrics = new DisplayMetrics();

        // Populate the DisplayMetrics
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Return theme value based on DisplayMetrics
        return (int) typedValue.getDimension(metrics);
    }

    //</editor-fold>
    //<editor-fold desc="Inner Classes">

    public static final class PickImage implements View.OnClickListener {

        private Activity mActivity = null;

        public PickImage(final Activity activity) {
            if (activity == null)
                throw new InvalidParameterException("Parameter context can not be null");

            this.mActivity = activity;
        }

        @Override
        public void onClick(final View v) {
            PickImage(mActivity);
        }

    }

    private static final class ImageProcessor implements BitmapProcessor {

        //<editor-fold desc="Constants">

        private static final String TAG = ImageProcessor.class.getSimpleName();
        private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        private static final int IMAGE_FROM_FILE = 1001;
        private static final int IMAGE_FROM_CONTACTS = 1002;
        private static final int IMAGE_FROM_DATABASE = 1003;

        //</editor-fold>
        //<editor-fold desc="Static">

        static {
            // List of uris supported to be processed as image
            //URI_MATCHER.addURI("", "*", IMAGE_FROM_FILE);
            URI_MATCHER.addURI(ContactsContract.AUTHORITY, "contacts/#/*", IMAGE_FROM_CONTACTS);
            URI_MATCHER.addURI(Contract.AUTHORITY, Contract.MediaContent.PATH_ID, IMAGE_FROM_DATABASE);
        }

        //</editor-fold>

        //<editor-fold desc="Variables">

        private static ImageProcessor instance = null;

        //</editor-fold>
        //<editor-fold desc="Constructor">

        private ImageProcessor() {
        }

        //</editor-fold>

        //<editor-fold desc="Overrides">

        @Override
        public final Bitmap loadImage(final Context context, final Uri imageUri, final int imageSize) {

            // Ensures the Fragment is still added to an activity. As this method is called in a
            // background thread, there's the possibility the Fragment is no longer attached and
            // added to an activity. If so, no need to spend resources loading the contact photo.
            if (context == null)
                return null;

            switch (URI_MATCHER.match(imageUri)) {
                case IMAGE_FROM_FILE:
                case IMAGE_FROM_CONTACTS:
                    return loadImageUri(context, imageUri, imageSize);
                case IMAGE_FROM_DATABASE:
                    return loadImageFromDb(context, imageUri, imageSize);
                default:
                    Log.e(TAG, String.format("Load image from uri: %s is not supported", imageUri));
            }

            return null;
        }

        @Override
        public final Bitmap loadImage(final Context context, final String imageData, final int imageSize) {
            return loadImage(context, Uri.parse(imageData), imageSize);
        }

        //</editor-fold>
        //<editor-fold desc="Public">

        public static ImageProcessor getInstance() {
            if (instance == null)
                instance = new ImageProcessor();
            return instance;
        }

        //</editor-fold>
        //<editor-fold desc="Private">

        private static Bitmap loadImageFromDb(final Context context, final Uri imageUri, final int imageSize) {
            final byte[] imageData = DatabaseHandler.getMediaContent(context, imageUri);
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        }

        private static Bitmap loadImageUri(final Context context, final Uri imageUri, final int imageSize) {

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
                        image = Loader.decodeSampledBitmapFromDescriptor(fileDescriptor, imageSize, imageSize);
                }
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Photo thumbnail not found at Uri: " + imageUri + ", error: " + e.toString());
            } finally {
                // If an AssetFileDescriptor was returned, try to close it
                if (afd != null)
                    try {
                        afd.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
            }
            // If the decoding failed, returns null
            return image;
        }

        //</editor-fold>
    }

//</editor-fold>
}
