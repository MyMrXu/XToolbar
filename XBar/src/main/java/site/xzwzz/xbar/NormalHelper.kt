package site.xzwzz.xbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

public class NormalHelper constructor(val mContext: Context) {
    private lateinit var rootView: RelativeLayout
    private lateinit var mCenterView: LinearLayout
    private var backButton: View? = null
    private val mLeftLayout by lazy {
        val linearLayout = LinearLayout(mContext);
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        linearLayout.layoutParams = layoutParams
        linearLayout.id = SizeUtils.generateViewId()
        linearLayout.setPadding(SizeUtils.dp2px(8f), 0, 0, 0)
        linearLayout.setVerticalGravity(Gravity.CENTER)
        linearLayout
    }
    private val mRightLayout by lazy {
        val linearLayout = LinearLayout(mContext);
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        linearLayout.layoutParams = layoutParams
        linearLayout.id = SizeUtils.generateViewId()
        linearLayout.gravity = Gravity.END
        linearLayout.setVerticalGravity(Gravity.CENTER)
        linearLayout.setPadding(0, 0, SizeUtils.dp2px(8f), 0)
        linearLayout
    }
    private val mTitleView: TextView by lazy {
        val view = TextView(mContext)
        val layoutparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = layoutparams
        view
    }
    private val mSubTitleView: TextView by lazy {
        val view = TextView(mContext)
        val layoutparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = layoutparams
        view
    }

    internal var title = ""
    internal var titleSize = 18f
    internal var titleColor = Color.parseColor("#242424")
    internal var titleMarquee = false

    internal var subtitle = ""
    internal var subTitleSize = 12f
    internal var subTitleColor = Color.parseColor("#8d908d")

    internal var backRes = 0
    internal var backListener: ((View) -> Unit)? = null

    internal var customView: View? = null

    init {
        rootView = RelativeLayout(mContext)
        rootView.addView(mLeftLayout)
        rootView.addView(mRightLayout)
        mCenterView = LinearLayout(mContext)
        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        mCenterView.orientation = LinearLayout.VERTICAL
        mCenterView.gravity = Gravity.CENTER
        mCenterView.setPadding(SizeUtils.dp2px(8f), 0, SizeUtils.dp2px(8f), 0)
        rootView.addView(mCenterView, layoutParams)
        setParams()
    }

    private fun setParams() {
        if (backButton == null) {
            backButton = addToolbarButton(
                Gravity.LEFT,
                resourceDrawable = if (backRes == 0) R.drawable.ic_baseline_arrow_back_ios_24 else backRes,
                clickListener = backListener
            )
        } else {
            backButton?.setOnClickListener {
                backListener?.invoke(it)
            }
        }
        if (title.isNotEmpty()) {
            mTitleView.setText(title)
            mTitleView.setTextSize(
                SizeUtils.px2sp(SizeUtils.dp2px(titleSize).toFloat()).toFloat()
            )
            mTitleView.setTextColor(titleColor)
            mTitleView.setGravity(Gravity.CENTER);
            mTitleView.setSingleLine(true);
            if (titleMarquee) {
                mTitleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                mTitleView.setMarqueeRepeatLimit(-1);
                mTitleView.requestFocus();
                mTitleView.setSelected(true);
            }
            if (mCenterView.indexOfChild(mTitleView) == -1) {
                mCenterView.addView(mTitleView)
            }
        } else {
            mCenterView.removeView(mTitleView)
        }
        if (subtitle.isNotEmpty()) {
            val layoutparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mSubTitleView.setText(subtitle)
            mSubTitleView.setTextColor(subTitleColor)
            mSubTitleView.setTextSize(
                SizeUtils.px2sp(SizeUtils.dp2px(subTitleSize).toFloat()).toFloat()
            )
            mSubTitleView.setSingleLine()

            mSubTitleView.setPadding(0, SizeUtils.dp2px(4f), 0, 0)
            if (mCenterView.indexOfChild(mSubTitleView) == -1) {
                mCenterView.addView(mSubTitleView)
            }
        } else {
            mCenterView.removeView(mSubTitleView)
        }
    }

    private fun calcuCenterViewWidth() {
        if (mCenterView == null) {
            return
        }
        val layoutParams = mCenterView.layoutParams

        layoutParams.width = SizeUtils.getScreenWidth(mContext) - (Math.max(
            SizeUtils.getMeasuredWidth(mLeftLayout),
            SizeUtils.getMeasuredWidth(mRightLayout)
        ) * 2)
        mCenterView.layoutParams = layoutParams
    }

    /**
     * add a button at left or right of view
     */
    public fun addToolbarButton(
        gravity: Int,
        stringres: CharSequence = "",
        @DrawableRes resourceDrawable: Int = 0,
        clickListener: ((View) -> Unit)? = null
    ): TextView {
        return try {
            val drawable = ContextCompat.getDrawable(mContext, resourceDrawable)
                ?: throw IllegalArgumentException("unavaible resource!")
            drawable.setBounds(0, 0, SizeUtils.dp2px(16f), SizeUtils.dp2px(16f))
            addToolbarButton(gravity, stringres, drawable, clickListener)
        } catch (e: Exception) {
            addToolbarButton(gravity, stringres, null, clickListener)
        }
    }

    public fun addToolbarButton(
        gravity: Int,
        stringres: CharSequence = "",
        resourceDrawable: Drawable?,
        clickListener: ((View) -> Unit)? = null
    ): TextView {
        if (gravity != Gravity.LEFT && gravity != Gravity.RIGHT) {
            throw IllegalArgumentException("the gravity must be LEFT or RIGHT")
        }
        val textView = TextView(mContext)
        if (resourceDrawable != null) {
            if (resourceDrawable is StateListDrawable) {
                textView.setCompoundDrawables(null, null, resourceDrawable, null)
            } else {
                textView.text = SpanUtils(mContext).appendImage(resourceDrawable).create()
            }
        }
        if (stringres.isNotEmpty()) {
            textView.text = stringres
            textView.setPadding(0, 0, 0, 0)
        }
        textView.gravity = Gravity.CENTER
        try {
            val typedValue = TypedValue()
            val attrbackground = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                android.R.attr.selectableItemBackgroundBorderless
            } else {
                android.R.attr.selectableItemBackground
            }

            mContext.theme.resolveAttribute(
                attrbackground,
                typedValue,
                true
            )
            val attribute = intArrayOf(attrbackground)
            val typedArray: TypedArray =
                mContext.theme.obtainStyledAttributes(typedValue.resourceId, attribute)
            textView.background = typedArray.getDrawable(0);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        textView.id = SizeUtils.generateViewId();
        textView.setOnClickListener(clickListener)
        val params = if (stringres.isNotEmpty()) {
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        } else {
            LinearLayout.LayoutParams(SizeUtils.dp2px(48f), SizeUtils.dp2px(48f))
        }
        if (gravity == Gravity.LEFT) {
            mLeftLayout.addView(textView, params)
        } else if (gravity == Gravity.RIGHT) {
            mRightLayout.addView(textView, params)
        }
        calcuCenterViewWidth()
        return textView
    }

    internal fun detch(toolbar: XToolbar) {
        toolbar.removeView(rootView)
    }

    internal fun attach(toolbar: XToolbar): NormalHelper {
        toolbar.addView(
            rootView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        return this
    }

    /**
     * set a listener for title view
     */
    fun titleOnClickListener(listener: (View) -> Unit): NormalHelper {
        mTitleView.setOnClickListener {
            listener(it)
        }
        return this
    }

    /**
     * open the navigator Button to do something
     */
    fun openNaviButton(
        @DrawableRes res: Int = this.backRes,
        listener: ((View) -> Unit)? = this.backListener
    ): NormalHelper {
        if (this.backListener != null) {
            throw IllegalArgumentException("back button only can set once")
        }
        this.backRes = res;
        this.backListener = listener
        return this
    }

    /**
     * set some params for title view
     */
    fun title(
        title: String = this.title,
        titleSize: Float = this.titleSize,
        titleColor: Int = this.titleColor,
        marquee: Boolean = this.titleMarquee
    ): NormalHelper {
        this.title = title
        this.titleColor = titleColor
        this.titleSize = titleSize
        this.titleMarquee = marquee
        return this
    }

    /**
     * set some params of view which subtitle
     */
    fun subTitle(
        subtitle: String = this.subtitle,
        subTitleSize: Float = this.subTitleSize,
        subTitleColor: Int = this.subTitleColor
    ): NormalHelper {
        this.subtitle = subtitle
        this.subTitleSize = subTitleSize
        this.subTitleColor = subTitleColor
        return this
    }

    fun commit() {
        setParams()
    }
}