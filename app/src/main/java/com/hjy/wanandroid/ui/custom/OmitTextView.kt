package com.hjy.wanandroid.ui.custom

import android.content.Context
import android.text.Layout
import android.text.StaticLayout
import android.util.AttributeSet
import android.widget.TextView

class OmitTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        adjustTextWithEllipsis(text.toString())
    }

    private fun adjustTextWithEllipsis(text: String) {
        val paint = paint
        val availableWidth = width - paddingStart - paddingEnd
        var texts = text
        // 创建一个临时静态布局来确定文本是否需要省略
        val layout =
            StaticLayout.Builder.obtain(text, 0, text.length, paint, availableWidth.toInt())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build()

        // 如果文本超过了一行
        if (layout.lineCount > 1) {
            // 计算在当前宽度下文本能完整显示的最后一行
            val lastVisibleLineNumber = getLastVisibleLineNumber(layout, availableWidth.toFloat())

            // 截取完整显示的最后一行之前的文本并添加省略号
            val truncatedText = truncateTextToFitLastLine(layout, lastVisibleLineNumber)

            // 设置已截断并带有省略号的新文本 Val cannot be reassigned
            texts = truncatedText
        }

        // 更新文本
        this.text = texts
    }
    private fun getLastVisibleLineNumber(layout: StaticLayout, availableWidth: Float): Int {
        for (i in layout.lineCount - 1 downTo 0) {
            if (layout.getLineMax(i) <= availableWidth) {
                return i
            }
        }
        return 0
    }


    private fun truncateTextToFitLastLine(
        layout: StaticLayout,
        lastVisibleLineNumber: Int
    ): String {
        val sb = StringBuilder()
        for (i in 0..lastVisibleLineNumber) {
            sb.append(layout.text.subSequence(layout.getLineStart(i), layout.getLineEnd(i)))
            if (i < lastVisibleLineNumber) {
                sb.append('\n')
            } else {
                sb.append("...")
            }
        }
        return sb.toString()
    }
}