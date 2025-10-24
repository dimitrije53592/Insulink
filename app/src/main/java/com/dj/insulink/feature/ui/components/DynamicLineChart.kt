package com.dj.insulink.feature.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.clearCompositionErrors
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.MaterialTheme
import com.dj.insulink.R
import com.dj.insulink.feature.ui.viewmodel.GlucoseReadingTimespan
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DynamicLineChart(
    xValues: List<Long>,
    yValues: List<Int>,
    modifier: Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val scrollState = rememberVicoScrollState(
        initialScroll = Scroll.Absolute.End
    )

    LaunchedEffect(xValues, yValues) {
        if (xValues.isNotEmpty() && yValues.isNotEmpty()) {
            val baseTime = xValues.minOrNull() ?: 0L
            val normalisedXValues = xValues.map { (it - baseTime) / 3600000 }

            modelProducer.runTransaction {
                lineSeries {
                    series(
                        x = normalisedXValues.map { it.toFloat() },
                        y = yValues.map { it.toFloat() })
                }
            }

            scrollState.scroll(Scroll.Absolute.Start)
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(
                title = stringResource(R.string.new_reading_text_field_label),
                titleComponent = rememberTextComponent(
                    color = MaterialTheme.colorScheme.onBackground
                )
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = CartesianValueFormatter { context, x, _ ->
                    val baseTime = xValues.minOrNull() ?: 0L
                    val realTime = baseTime + (x.toLong() * 3600000)
                    SimpleDateFormat("d MMM", Locale.getDefault()).format(Date(realTime))
                },
                title = stringResource(R.string.glucose_screen_date_axis_title),
                titleComponent = rememberTextComponent(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        ),
        modelProducer = modelProducer,
        modifier = modifier,
        scrollState = scrollState,
        zoomState = rememberVicoZoomState(zoomEnabled = true)
    )
}