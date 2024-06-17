package com.example.appcuriosity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PieChartView : View {

    private var countConosciuti: Float = 0f
    private var countSconosciuti: Float = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    fun setCounts(countConosciuti: Float, countSconosciuti: Float) {
        this.countConosciuti = countConosciuti
        this.countSconosciuti = countSconosciuti
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (canvas == null) return

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = Math.min(width, height) / 2

        val centerX = width / 2
        val centerY = height / 2

        val pennello  = Paint(Paint.ANTI_ALIAS_FLAG)
        pennello .style = Paint.Style.FILL
        val total = countConosciuti + countSconosciuti

        // Draw known segment - Blu
        pennello .color = Color.rgb(100, 149, 237)
        val knownAngle = 360 * (countConosciuti / total)
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, -90f, knownAngle, true, pennello )

        // Draw percentage for known segment
        pennello .color = Color.WHITE
        pennello .textSize = 36f
        val knownPercent = (countConosciuti / total * 100).toInt()
        val knownPercentText = "$knownPercent%"
        val textWidthKnown = pennello .measureText(knownPercentText)
        val textHeightKnown = pennello .descent() - pennello .ascent() // calculate text height to center vertically

        // Calculate position for text in the center of the known segment
        val angleRad = Math.toRadians((-90 + knownAngle / 2).toDouble())
        val xKnown = centerX + (radius / 2 * Math.cos(angleRad)).toFloat() - textWidthKnown / 2
        val yKnown = centerY + (radius / 2 * Math.sin(angleRad)).toFloat() + textHeightKnown / 2

        canvas.drawText(knownPercentText, xKnown, yKnown, pennello )

        // Draw unknown segment - Rosso
        pennello .color = Color.rgb(255, 128, 128)
        val unknownAngle = 360 * (countSconosciuti / total)
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, -90f + knownAngle, unknownAngle, true, pennello)

        // Draw percentage for unknown segment
        pennello .color = Color.WHITE
        val unknownPercent = (countSconosciuti / total * 100).toInt()
        val unknownPercentText = "$unknownPercent%"
        val textWidthUnknown = pennello .measureText(unknownPercentText)
        val textHeightUnknown = pennello .descent() - pennello .ascent() // calculate text height to center vertically

        // Calculate position for text in the center of the unknown segment
        val angleRadUnknown = Math.toRadians((-90 + knownAngle + unknownAngle / 2).toDouble())
        val xUnknown = centerX + (radius / 2 * Math.cos(angleRadUnknown)).toFloat() - textWidthUnknown / 2
        val yUnknown = centerY + (radius / 2 * Math.sin(angleRadUnknown)).toFloat() + textHeightUnknown / 2

        canvas.drawText(unknownPercentText, xUnknown, yUnknown, pennello )

    }
}