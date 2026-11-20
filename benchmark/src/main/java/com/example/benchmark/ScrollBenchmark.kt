package com.example.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScrollBenchmark {
    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun scrollStaticList() {
        val targetPackage = InstrumentationRegistry.getArguments().getString("targetPackage")
            ?: error("Pass -Pandroid.testInstrumentationRunnerArguments.targetPackage=<package>")

        rule.measureRepeated(
            packageName = targetPackage,
            metrics = listOf(FrameTimingMetric()),
            compilationMode = CompilationMode.Full(),
            iterations = 5,
            setupBlock = {
                pressHome()
                startActivityAndWait()
            }
        ) {
            val list = checkNotNull(device.wait(Until.findObject(By.res("asset_list")), 5_000)) {
                "asset_list not found in $targetPackage"
            }
            list.setGestureMargin(device.displayWidth / 5)
            repeat(3) {
                list.fling(Direction.DOWN)
                device.waitForIdle()
            }
            repeat(3) {
                list.fling(Direction.UP)
                device.waitForIdle()
            }
        }
    }
}