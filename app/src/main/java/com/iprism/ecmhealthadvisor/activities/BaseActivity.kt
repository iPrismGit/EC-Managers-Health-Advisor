import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let {
            val config = it.resources.configuration
            config.fontScale = 1.0f
            val newContext = it.createConfigurationContext(config)
            super.attachBaseContext(newContext)
        } ?: super.attachBaseContext(newBase)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            overrideConfiguration.fontScale = 1.0f
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        newConfig.fontScale = 1.0f
        val metrics = resources.displayMetrics
        val configuration = resources.configuration
        metrics.scaledDensity = configuration.fontScale * metrics.density
        resources.updateConfiguration(configuration, metrics)
    }

}
