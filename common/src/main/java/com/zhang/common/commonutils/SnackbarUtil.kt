package com.zhang.common.commonutils

import android.annotation.SuppressLint
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference


@SuppressLint("StaticFieldLeak")
/**
 * Created by zhang .
 * DATA: 2018/8/8 .
 * Description : Snackbar_Utils
 */
class SnackbarUtil constructor(parent: View){
    val LENGTH_INDEFINITE = -2
    val LENGTH_SHORT = -1
    val LENGTH_LONG = 0

    annotation class Duration

    private val COLOR_DEFAULT = -0x1000001
    private val COLOR_SUCCESS = -0xd44a00
    private val COLOR_WARNING = -0x3f00
    private val COLOR_ERROR = -0x10000
    private val COLOR_MESSAGE = -0x1

    private var sReference: WeakReference<Snackbar>? = null

    private var view: View? = null
    private var message: CharSequence? = null
    private var messageColor: Int = 0
    private var bgColor: Int = 0
    private var bgResource: Int = 0
    private var duration: Int = 0
    private var actionText: CharSequence? = null
    private var actionTextColor: Int = 0
    private var actionListener: View.OnClickListener? = null
    private var bottomMargin: Int = 0

    init {
        setDefault()
        view = parent
    }

    private fun setDefault() {
        message = ""
        messageColor = COLOR_DEFAULT
        bgColor = COLOR_DEFAULT
        bgResource = -1
        duration = LENGTH_SHORT
        actionText = ""
        actionTextColor = COLOR_DEFAULT
        bottomMargin = 0
    }

    /**
     * Set the view to find a parent from.
     *
     * @param view The view to find a parent from.
     * @return the single [SnackbarUtil] instance
     */
    fun with(@NonNull view: View): SnackbarUtil {
        return SnackbarUtil(view)
    }

    /**
     * Set the message.
     *
     * @param msg The message.
     * @return the single [SnackbarUtil] instance
     */
    fun setMessage(@NonNull msg: CharSequence): SnackbarUtil {
        this.message = msg
        return this
    }

    /**
     * Set the color of message.
     *
     * @param color The color of message.
     * @return the single [SnackbarUtil] instance
     */
    fun setMessageColor(@ColorInt color: Int): SnackbarUtil {
        this.messageColor = color
        return this
    }

    /**
     * Set the color of background.
     *
     * @param color The color of background.
     * @return the single [SnackbarUtil] instance
     */
    fun setBgColor(@ColorInt color: Int): SnackbarUtil {
        this.bgColor = color
        return this
    }

    /**
     * Set the resource of background.
     *
     * @param bgResource The resource of background.
     * @return the single [SnackbarUtil] instance
     */
    fun setBgResource(@DrawableRes bgResource: Int): SnackbarUtil {
        this.bgResource = bgResource
        return this
    }

    /**
     * Set the duration.
     *
     * @param duration The duration.
     *
     *  * [Duration.LENGTH_INDEFINITE]
     *  * [Duration.LENGTH_SHORT]
     *  * [Duration.LENGTH_LONG]
     *
     * @return the single [SnackbarUtil] instance
     */
    fun setDuration(@Duration duration: Int): SnackbarUtil {
        this.duration = duration
        return this
    }

    /**
     * Set the action.
     *
     * @param text     The text.
     * @param listener The click listener.
     * @return the single [SnackbarUtil] instance
     */
    fun setAction(@NonNull text: CharSequence,
                  @NonNull listener: View.OnClickListener): SnackbarUtil {
        return setAction(text, COLOR_DEFAULT, listener)
    }

    /**
     * Set the action.
     *
     * @param text     The text.
     * @param color    The color of text.
     * @param listener The click listener.
     * @return the single [SnackbarUtil] instance
     */

    fun setAction(@NonNull text: CharSequence,
                  @ColorInt color: Int,
                  @NonNull listener: View.OnClickListener): SnackbarUtil {
        this.actionText = text
        this.actionTextColor = color
        this.actionListener = listener
        return this
    }

    /**
     * Set the bottom margin.
     *
     * @param bottomMargin The size of bottom margin, in pixel.
     */
    fun setBottomMargin(bottomMargin: Int): SnackbarUtil {
        this.bottomMargin = bottomMargin
        return this
    }

    /**
     * Show the snackbar.
     */
    fun show() {
        val view = this.view ?: return
        if (messageColor != COLOR_DEFAULT) {
            val spannableString = SpannableString(message)
            val colorSpan = ForegroundColorSpan(messageColor)
            spannableString.setSpan(
                    colorSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            sReference = WeakReference(Snackbar.make(view, spannableString, duration))
        } else {
            sReference = WeakReference(Snackbar.make(view, message!!, duration))
        }
        val snackbar = sReference!!.get()
        val snackbarView = snackbar?.getView()
        if (bgResource != -1) {
            snackbarView?.setBackgroundResource(bgResource)
        } else if (bgColor != COLOR_DEFAULT) {
            snackbarView?.setBackgroundColor(bgColor)
        }
        if (bottomMargin != 0) {
            val params = snackbarView?.getLayoutParams() as ViewGroup.MarginLayoutParams
            params.bottomMargin = bottomMargin
        }
        if (actionText!!.length > 0 && actionListener != null) {
            if (actionTextColor != COLOR_DEFAULT) {
                snackbar?.setActionTextColor(actionTextColor)
            }
            snackbar?.setAction(actionText, actionListener)
        }
        snackbar?.show()
    }

    /**
     * Show the snackbar with success style.
     */
    fun showSuccess() {
        bgColor = COLOR_SUCCESS
        messageColor = COLOR_MESSAGE
        actionTextColor = COLOR_MESSAGE
        show()
    }

    /**
     * Show the snackbar with warning style.
     */
    fun showWarning() {
        bgColor = COLOR_WARNING
        messageColor = COLOR_MESSAGE
        actionTextColor = COLOR_MESSAGE
        show()
    }

    /**
     * Show the snackbar with error style.
     */
    fun showError() {
        bgColor = COLOR_ERROR
        messageColor = COLOR_MESSAGE
        actionTextColor = COLOR_MESSAGE
        show()
    }

    /**
     * Dismiss the snackbar.
     */
    fun dismiss() {
        if (sReference != null && sReference!!.get() != null) {
            sReference!!.get()?.dismiss()
            sReference = null
        }
    }

    /**
     * Return the view of snackbar.
     *
     * @return the view of snackbar
     */
    fun getView(): View? {
        val snackbar = sReference!!.get() ?: return null
        return snackbar.getView()
    }

    /**
     * Add view to the snackbar.
     *
     * Call it after [.show]
     *
     * @param layoutId The id of layout.
     * @param params   The params.
     */
    fun addView(@LayoutRes layoutId: Int,
                params: ViewGroup.LayoutParams) {
        val view = getView()
        if (view != null) {
            view!!.setPadding(0, 0, 0, 0)
            val layout = view as Snackbar.SnackbarLayout?
            val child = LayoutInflater.from(view!!.getContext()).inflate(layoutId, null)
            layout!!.addView(child, -1, params)
        }
    }

    /**
     * Add view to the snackbar.
     *
     * Call it after [.show]
     *
     * @param child  The child view.
     * @param params The params.
     */
    fun addView(child: View,
                params: ViewGroup.LayoutParams) {
        val view = getView()
        if (view != null) {
            view!!.setPadding(0, 0, 0, 0)
            val layout = view as Snackbar.SnackbarLayout?
            layout!!.addView(child, params)
        }
    }

}