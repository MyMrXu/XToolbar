package site.xzwzz.xbar

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.ColorUtils
import site.xzwzz.xbar.statusbar.StatusBarUtil


class XToolbar : Toolbar {
    private var immersive = false
    private var showType = 0//0正常的  1搜索框   2google官方
    private var statusBarColor = Color.TRANSPARENT
    private var statusBarAlpha = 1.0f
    private var mNormalHelper: NormalHelper? = null;

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        loadAttr(context, attrs)
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        loadAttr(context, attrs)
        initView(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == View.MeasureSpec.AT_MOST) {
            heightSize = StatusBarUtil.getActionBarHeight(context);
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    private fun loadAttr(context: Context, attrs: AttributeSet?) {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.XToolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // notice 未引入沉浸式标题栏之前,隐藏标题栏撑起布局
            immersive = array.getBoolean(R.styleable.XToolbar_immersive, true)
        }
        showType = array.getInt(R.styleable.XToolbar_showtype, 0);
        statusBarColor = array.getColor(R.styleable.XToolbar_statusBarColor, Color.TRANSPARENT)
        statusBarAlpha = array.getFloat(R.styleable.XToolbar_statusBarAlpha, 1.0f)
    }

    private fun initView(context: Context) {
        if (immersive && context is Activity) {
            StatusBarUtil.immersive(context, statusBarColor, statusBarAlpha)
            calcuHeight()
        }

    }


    private fun calcuHeight() {
        if (layoutParams == null) {
            postDelayed({
                calcuHeight()
            }, 5)
        } else {
            StatusBarUtil.setPaddingSmart(context, this)
            StatusBarUtil.darkMode(context as Activity, getMainColor(statusBarColor))
        }
    }

    private fun getMainColor(colorInt: Int): Boolean {
//        val gray = (Color.red(colorInt) * 0.299 + Color.green(colorInt) * 0.587 + Color.blue(colorInt) * 0.114).toInt()
//        return gray < 192
        val calculateLuminance = ColorUtils.calculateLuminance(colorInt)
        Log.e("xzwzz", "current bar color luminance: " + calculateLuminance)
        return calculateLuminance >= 0.5
    }

    fun getHelper(): NormalHelper {
        if (showType == 0 && mNormalHelper != null) {
            return mNormalHelper!!
        } else {
            mNormalHelper = NormalHelper(context)
            mNormalHelper!!.attach(this)
            return mNormalHelper!!
        }
    }
}