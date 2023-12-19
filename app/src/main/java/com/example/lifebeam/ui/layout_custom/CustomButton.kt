package com.example.lifebeam.ui.layout_custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.lifebeam.R

class CustomButton: AppCompatButton {
    private lateinit var customButton: Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = customButton
    }

    private fun init(){
        customButton = ContextCompat.getDrawable(context, R.drawable.custom_activity_button) as Drawable
    }
}