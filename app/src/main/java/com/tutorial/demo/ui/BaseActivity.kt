package com.tutorial.demo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author 麥光廷
 * @date 2023-03-10
 * Activity的基底。
 */
abstract class BaseActivity : AppCompatActivity() {
    // 當前的Activity。
    lateinit var mActivity: BaseActivity

    /**
     * 當系統記憶體不足時，會呼叫此方法。
     * @param level 系統記憶體不足的等級。
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level <= TRIM_MEMORY_BACKGROUND)
            System.gc()
    }

    /**
     * 當Activity被建立時，會呼叫此方法。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
    }

    /**
     * 可共用方法。
     */
    fun start(next: Class<*>, bundle: Bundle?, finished: Boolean) {
        Intent(
            applicationContext,
            next
        ).also { intent ->
            if (bundle == null)
                intent.putExtras(Bundle())
            else
                intent.putExtras(bundle)
            // jump activity
            startActivity(intent)
            // close activity
            if (finished) this.finish()
        }
    }

    fun start(next: Class<*>) { this.start(next, null, false) }

    fun start(next: Class<*>, bundle: Bundle?) { this.start(next, bundle, false) }

    fun start(next: Class<*>, finished: Boolean) { this.start(next, null, finished) }
}