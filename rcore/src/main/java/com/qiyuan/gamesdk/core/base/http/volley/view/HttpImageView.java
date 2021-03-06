package com.qiyuan.gamesdk.core.base.http.volley.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley1.VolleyError;
import com.android.volley1.toolbox.ImageLoader;
import com.qiyuan.gamesdk.core.base.http.volley.listener.ProgressListener;
import com.qiyuan.gamesdk.core.ui.widget.ScalableImageView;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.base.http.volley.listener.ProgressListener;
import com.qiyuan.gamesdk.core.ui.widget.ScalableImageView;

/**
 * Created by chenshuide on 15/6/12.
 * @see {@link com.android.volley1.toolbox.NetworkImageView}
 */
public class HttpImageView extends ScalableImageView implements ProgressListener {


    /**
     * The URL of the network image to load
     */
    private String mUrl;

    /**
     * Resource ID of the image to be used as a placeholder until the network image is loaded.
     */
    private int mDefaultImageId;

    /**
     * Resource ID of the image to be used if the network response fails.
     */
    private int mErrorImageId;

    /**
     * Local copy of the ImageLoader.
     */
    private ImageLoader mImageLoader;

    /**
     * Current ImageContainer. (either in-flight or finished)
     */
    private ImageLoader.ImageContainer mImageContainer;


    private int width, height;

    private static final int STATE_PROGRESS_START = 1001;
    private static final int STATE_PROGRESS_FINISH = 1002;
    private static final int STATE_PROGRESS_LOADING = 1003;
    private int mCurState;
    private Paint ratioPaint,textPaint;
    private int progress;

    private boolean needProgress = true;

    public boolean isNeedProgress() {
        return needProgress;
    }

    public void setNeedProgress(boolean needProgress) {
        this.needProgress = needProgress;
    }

    public HttpImageView(Context context) {
        this(context, null);
    }

    public HttpImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HttpImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        ratioPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ratioPaint.setColor(getResources().getColor(R.color.dk_color_999999));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getResources().getColor(R.color.dk_color_dddddd));
    }

    /**
     * Sets the default image resource ID to be used for this view until the attempt to load it
     * completes.
     */
    public void setDefaultImageResId(int defaultImage) {
        mDefaultImageId = defaultImage;
    }

    /**
     * Sets the error image resource ID to be used for this view in the event that the image
     * requested fails to load.
     */
    public void setErrorImageResId(int errorImage) {
        mErrorImageId = errorImage;
    }


    /**
     * Sets URL of the image that should be loaded into this view. Note that calling this will
     * immediately either set the cached image (if available) or the default image specified by
     * {@link HttpImageView#setDefaultImageResId(int)} on the view.
     * <p/>
     * NOTE: If applicable, {@link HttpImageView#setDefaultImageResId(int)} and
     * {@link HttpImageView#setErrorImageResId(int)} should be called prior to calling
     * this function.
     *
     * @param url         The URL that should be loaded into this ImageView.
     * @param imageLoader ImageLoader that will be used to make the request.
     */
    public void setImageUrl(String url, ImageLoader imageLoader) {
        mUrl = url;
        mImageLoader = imageLoader;
        // The URL has potentially changed. See if we need to load it.
        loadImageIfNecessary(false);
    }


    private void setDefaultImageOrNull() {
        if (mDefaultImageId != 0) {
            setImageResource(mDefaultImageId);
        } else {
            setImageBitmap(null);
        }
    }

    /**
     * Loads the image for the view if it isn't already loaded.
     *
     * @param isInLayoutPass True if this was invoked from a layout pass, false otherwise.
     */
    private void loadImageIfNecessary(final boolean isInLayoutPass) {
        width = getWidth();
        height = getHeight();
        ScaleType scaleType = getScaleType();

        boolean wrapWidth = false, wrapHeight = false;
        if (getLayoutParams() != null) {
            wrapWidth = getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;
            wrapHeight = getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        // if the view's bounds aren't known yet, and this is not a wrap-content/wrap-content
        // view, hold off on loading the image.
        boolean isFullyWrapContent = wrapWidth && wrapHeight;
        if (width == 0 && height == 0 && !isFullyWrapContent) {
            return;
        }

        // if the URL to be loaded in this view is empty, cancel any old requests and clear the
        // currently loaded image.
        if (TextUtils.isEmpty(mUrl)) {
            if (mImageContainer != null) {
                mImageContainer.cancelRequest();
                mImageContainer = null;
            }
            setDefaultImageOrNull();
            return;
        }

        // if there was an old request in this view, check if it needs to be canceled.
        if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
            if (mImageContainer.getRequestUrl().equals(mUrl)) {
                // if the request is from the same URL, return.
                return;
            } else {
                // if there is a pre-existing request, cancel it if it's fetching a different URL.
                mImageContainer.cancelRequest();
                setDefaultImageOrNull();
            }
        }

        // Calculate the max image width / height to use while ignoring WRAP_CONTENT dimens.
        int maxWidth = wrapWidth ? 0 : width;
        int maxHeight = wrapHeight ? 0 : height;

        // The pre-existing content of this view didn't match the current URL. Load the new image
        // from the network.
        ImageLoader.ImageContainer newContainer = mImageLoader.get(mUrl,
                new ImageLoader.ImageListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mErrorImageId != 0) {
                            setImageResource(mErrorImageId);
                        }
                    }

                    @Override
                    public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {
                        // If this was an immediate response that was delivered inside of a layout
                        // pass do not set the image immediately as it will trigger a requestLayout
                        // inside of a layout. Instead, defer setting the image by posting back to
                        // the main thread.
                        if (isImmediate && isInLayoutPass) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    onResponse(response, false);
                                }
                            });
                            return;
                        }

                        Bitmap bitmap = response.getBitmap();

                        if (bitmap != null) {

                            setImageBitmap(bitmap);

                            // 渐变显示图片
                            if (!isImmediate) {
                                startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                            }


                        } else if (mDefaultImageId != 0) {
                            setImageResource(mDefaultImageId);
                        }
                    }
                }, maxWidth, maxHeight, scaleType, needProgress? this : null);

        // update the ImageContainer to be the new bitmap container.
        mImageContainer = newContainer;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mCurState) {

            case STATE_PROGRESS_START:
                canvas.drawRect(0, 0, width, height, ratioPaint);

                break;

            case STATE_PROGRESS_LOADING:

                float cur = height - (float)(height*progress/100);
                canvas.drawRect(0, 0, width, cur, ratioPaint);

                canvas.drawText(progress + "%", width / 2, height / 2, textPaint);

                break;

            case STATE_PROGRESS_FINISH:

                break;


        }

    }

    @Override
    public void onStart(long total) {
        mCurState = STATE_PROGRESS_START;
        postInvalidate();
    }

    @Override
    public void onProgress(long curPos, long total) {
        mCurState = STATE_PROGRESS_LOADING;
        float ratio = (float) (curPos * 100) / total;
        int newprogress = Math.round(ratio);

        if (newprogress > 100)
            newprogress = 100;

        if (newprogress > progress) {
            progress = newprogress;
            postInvalidate();
        }


    }

    @Override
    public void onFinish(long total) {
        mCurState = STATE_PROGRESS_FINISH;
        postInvalidate();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mImageContainer != null) {
            // If the view was bound to an image request, cancel it and clear
            // out the image from the view.
            mImageContainer.cancelRequest();
            setImageBitmap(null);
            // also clear out the container so we can reload the image if necessary.
            mImageContainer = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

}
