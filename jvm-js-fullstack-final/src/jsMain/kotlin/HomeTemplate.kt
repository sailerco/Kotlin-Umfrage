import kotlinx.browser.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction

private val scope = MainScope()
fun homeTemplate() {
    document.body!!.append.div {
        h1 {
            +"Welcome to Poll!"
        }
        button(type = ButtonType.button) {
            onClickFunction = {
                if (document.body!!.childElementCount < 4)
                    pollTemplate()
                else {
                    document.body!!.lastChild?.let { it1 -> document.body!!.removeChild(it1) }
                    pollTemplate()
                }
            }
            +"Create Poll"
        }
        button(type = ButtonType.button) {
            onClickFunction = {
                if (document.body!!.childElementCount < 4)
                    scope.launch {
                        val list = getPolls()
                        showPollsTemplate(list)
                    }
                else {
                    document.body!!.lastChild?.let { it1 -> document.body!!.removeChild(it1) }
                    scope.launch {
                        val list = getPolls()
                        showPollsTemplate(list)
                    }
                }
            }
            +"Show Polls"
        }
    }
}


