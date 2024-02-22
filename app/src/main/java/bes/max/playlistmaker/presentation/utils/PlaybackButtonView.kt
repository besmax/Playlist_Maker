package bes.max.playlistmaker.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
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
    private var contentWidth: Int = minimumWidth
    private var contentHeight: Int = minimumHeight

    private var state = STATE_PAUSED

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

    fun setState(newState: Int) {
        if (newState != state) {
            state = newState
        }
        invalidate()
    }

    private fun changeState() {
        if (state == STATE_PAUSED) {
            state = STATE_PLAYING
        } else {
            state = STATE_PAUSED
        }
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect.set(0f, 0f, measuredHeight.toFloat(), measuredWidth.toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        contentWidth = when (widthMode) {
            MeasureSpec.UNSPECIFIED -> minimumWidth
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> widthSize
            else -> error(
                context.resources.getString(
                    R.string.error_view_width_mode,
                    widthMode.toString()
                )
            )
        }

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        contentHeight = when (heightMode) {
            MeasureSpec.UNSPECIFIED -> minimumHeight
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> heightSize
            else -> error(
                context.resources.getString(
                    R.string.error_view_height_mode,
                    heightMode.toString()
                )
            )
        }

        val size = min(contentWidth, contentHeight)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        if (state == STATE_PLAYING) {
            if (pauseImage != null) {
                canvas.drawBitmap(pauseImage!!, null, imageRect, null)
            }
        } else {
            if (playImage != null) {
                canvas.drawBitmap(playImage!!, null, imageRect, null)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                callOnClick()
                return true
            }

            MotionEvent.ACTION_UP -> {
                changeState()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    companion object {
        const val STATE_PLAYING = 0
        const val STATE_PAUSED = 1
    }

}