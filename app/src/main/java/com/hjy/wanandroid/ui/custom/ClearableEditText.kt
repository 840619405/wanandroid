package com.hjy.wanandroid.ui.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.hjy.wanandroid.R

class ClearableEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr) {
    private var watcher: TextWatcher? = null
    private val clearDrawable: Drawable? by lazy {
        ContextCompat.getDrawable(context, R.drawable.baseline_close_24) // 加载清空图标资源
    }

    init {
        // 设置清空图标的大小和位置
        compoundDrawablePadding = 20
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = compoundDrawablesRelative[2]
                if (drawableRight != null && event.rawX >= (right - compoundDrawablePadding - drawableRight.intrinsicWidth)) {
                    watcher?.afterTextChanged(null)
                    setText("")
                    clearFocus()
                    return@setOnTouchListener true
                }
            }
            super.onTouchEvent(event)
        }

        // 添加清空图标到EditText的右边
        setCompoundDrawablesWithIntrinsicBounds(null, null, clearDrawable, null)
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            null,
            null
        )
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                watcher?.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 当文本内容改变时，根据文本长度决定清空图标的显示或隐藏
                val hasText = s?.isNotEmpty() == true
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    if (hasText) clearDrawable else null,
                    null
                )
                watcher?.onTextChanged(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                watcher?.afterTextChanged(s)
            }
        })
    }


    fun addMyTextChangedListener(watcher: TextWatcher) {
        this.watcher = watcher
    }
}