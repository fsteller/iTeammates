package com.fsteller.mobile.android.teammatesapp.image;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.BuildConfig;

/**
 * Project: iTeammates
 * Subpackage: image
 * <p/>
 * Description:
 * <p/>
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class Cache {

    private static final String TAG = "Cache";
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     * Private constructor used in order to prevent instantiation of this class.
     * Creating a new Cache object using the specified parameters.
     *
     * @param memCacheSizePercent The cache size as a percent of available app memory.
     */
    private Cache(float memCacheSizePercent) {
        init(memCacheSizePercent);
    }

    /**
     * Find and return an existing Cache stored in a {@link Cache.RetainFragment}, if not found a new
     * one is created using the supplied params and saved to a {@link Cache.RetainFragment}.
     *
     * @param fragmentManager     The fragments manager to use when dealing with the retained fragments.
     * @param memCacheSizePercent The cache size as a percent of available app memory.
     * @return An existing retained Cache object or a new one if one did not exist
     */
    public static Cache getInstance(
            FragmentManager fragmentManager, float memCacheSizePercent) {

        // Search for, or create an instance of the non-UI RetainFragment
        final RetainFragment mRetainFragment = findOrCreateRetainFragment(fragmentManager);

        // See if we already have an Cache stored in RetainFragment
        Cache cache = (Cache) mRetainFragment.getObject();

        // No existing Cache, create one and store it in RetainFragment
        if (cache == null) {
            cache = new Cache(memCacheSizePercent);
            mRetainFragment.setObject(cache);
        }

        return cache;
    }

    /**
     * Get the size in bytes of a bitmap.
     *
     * @param bitmap The bitmap to calculate the size of.
     * @return size of bitmap in bytes.
     */
    public static int getBitmapSize(final Bitmap bitmap) {
        return bitmap.getByteCount();
    }

    /**
     * Calculates the memory cache size based on a percentage of the max available VM memory.
     * Eg. setting percent to 0.2 would set the memory cache to one fifth of the available
     * memory. Throws {@link IllegalArgumentException} if percent is < 0.05 or > .8.
     * memCacheSize is stored in kilobytes instead of bytes as this will eventually be passed
     * to construct a LruCache which takes an int in its constructor.
     * <p/>
     * This value should be chosen carefully based on a number of factors
     * Refer to the corresponding Android Training class for more discussion:
     * http://developer.android.com/training/displaying-bitmaps/
     *
     * @param percent Percent of available app memory to use to size memory cache.
     */
    public static int calculateMemCacheSize(float percent) {
        if (percent < 0.05f || percent > 0.8f) {
            throw new IllegalArgumentException("setMemCacheSizePercent - percent must be "
                    + "between 0.05 and 0.8 (inclusive)");
        }
        return Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
    }

    /**
     * Locate an existing instance of this Fragment or if not found, create and
     * add it using FragmentManager.
     *
     * @param fm The FragmentManager manager to use.
     * @return The existing instance of the Fragment or the new instance if just
     * created.
     */
    public static RetainFragment findOrCreateRetainFragment(final FragmentManager fm) {
        // Check to see if we have retained the worker fragments.
        RetainFragment mRetainFragment = (RetainFragment) fm.findFragmentByTag(TAG);

        // If not retained (or first time running), we need to create and add it.
        if (mRetainFragment == null) {
            mRetainFragment = new RetainFragment();
            fm.beginTransaction().add(mRetainFragment, TAG).commitAllowingStateLoss();
        }

        return mRetainFragment;
    }

    /**
     * Initialize the cache.
     *
     * @param memCacheSizePercent The cache size as a percent of available app memory.
     */
    private void init(float memCacheSizePercent) {
        int memCacheSize = calculateMemCacheSize(memCacheSizePercent);

        // Set up memory cache
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Memory cache created (size = " + memCacheSize + ")");
        }
        mMemoryCache = new LruCache<String, Bitmap>(memCacheSize) {
            /**
             * Measure item size in kilobytes rather than units which is more practical
             * for a bitmap cache
             */
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                final int bitmapSize = getBitmapSize(bitmap) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }
        };
    }

    /**
     * Adds a bitmap to both memory and disk cache.
     *
     * @param data   Unique identifier for the bitmap to store
     * @param bitmap The bitmap to store
     */
    public void addBitmapToCache(final String data, final Bitmap bitmap) {
        if (data == null || bitmap == null) {
            return;
        }

        // AddItem to memory cache
        if (mMemoryCache != null && mMemoryCache.get(data) == null) {
            mMemoryCache.put(data, bitmap);
        }
    }

    /**
     * Get from memory cache.
     *
     * @param data Unique identifier for which item to get
     * @return The bitmap if found in cache, null otherwise
     */
    public Bitmap getBitmapFromMemCache(final String data) {
        if (mMemoryCache != null) {
            final Bitmap memBitmap = mMemoryCache.get(data);
            if (memBitmap != null) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Memory cache hit");
                }
                return memBitmap;
            }
        }
        return null;
    }

    /**
     * A simple non-UI Fragment that stores a single Object and is retained over configuration
     * changes. It will be used to retain the Cache object.
     */
    public static class RetainFragment extends Fragment {
        private Object mObject;

        /**
         * Empty constructor as per the Fragment documentation
         */
        public RetainFragment() {
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure this Fragment is retained over a configuration change
            setRetainInstance(true);
        }

        /**
         * Get the stored object.
         *
         * @return The stored object
         */
        public Object getObject() {
            return mObject;
        }

        /**
         * Store a single object in this Fragment.
         *
         * @param object The object to store
         */
        public void setObject(final Object object) {
            mObject = object;
        }
    }
}

