package live.lingting.spring

import live.lingting.framework.application.ApplicationComponent
import org.springframework.stereotype.Component

/**
 * @author lingting 2024-02-02 15:27
 */
@Component
class SpringTestBean : ApplicationComponent {
    val value: String = "bean"

    var isStart: Boolean = false
        private set

    override fun onApplicationStart() {
        this.isStart = true
    }

    override fun onApplicationStop() {
        //
    }
}
