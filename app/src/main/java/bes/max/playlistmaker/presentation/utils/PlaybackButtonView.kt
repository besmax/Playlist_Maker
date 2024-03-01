package bes.max.playlistmaker.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import bes.max.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val imageRect = RectF()
    private var playImage: Bitmap? = null
    private var pauseImage: Bitmap? = null

    private var state = PlaybackButtonViewState.STATE_PAUSED

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playImage = getDrawable(R.styleable.PlaybackButtonView_playImageResId)?.toBitmap()
                pauseImage = getDrawable(R.styleable.PlaybackButtonView_pauseImageResId)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    fun setState(newState: PlaybackButtonViewState) {
        if (newState != state) {
            state = newState
        }
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect.set(0f, 0f, measuredHeight.toFloat(), measuredWidth.toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec, minimumWidth)
        val heightSize = getSize(heightMeasureSpec, minimumHeight)
        val size = min(widthSize, heightSize)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        if (state == PlaybackButtonViewState.STATE_PLAYING) {
            if (pauseImage != null) {
                canvas.drawBitmap(pauseImage!!, null, imageRect, null)
            }
        } else {
            if (playImage != null) {
                canvas.drawBitmap(playImage!!, null, imageRect, null)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                changeState()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun changeState() {
        if (state == PlaybackButtonViewState.STATE_PAUSED) {
            setState(PlaybackButtonViewState.STATE_PLAYING)
        } else {
            setState(PlaybackButtonViewState.STATE_PAUSED)
        }
    }

    private fun getSize(measureSpec: Int, minimumSize: Int): Int {
        val size = MeasureSpec.getSize(measureSpec)
        val sizeMode = MeasureSpec.getMode(measureSpec)
        return when (sizeMode) {
            MeasureSpec.UNSPECIFIED -> {
                if (minimumSize > 0) {
                    minimumSize
                } else {
                    size
                }
            }

            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> Log.e(
                TAG,
                context.resources.getString(
                    R.string.error_view_size_mode,
                    sizeMode.toString()
                )
            )
        }
    }

    companion object {
        private const val TAG = "PlaybackButtonView"

        enum class PlaybackButtonViewState {
            STATE_PLAYING,
            STATE_PAUSED
        }
    }

}