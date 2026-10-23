// GameButton.kt — НАША кнопка, наследуемся от TextButton

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class GameButton(text: String, skin: Skin) : TextButton(text, skin) {

    // Свойство-колбэк: «что делать при нажатии». Задаёт тот, кто создал кнопку.
    var onClick: () -> Unit = {}

    init {
        addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                onClick() // зовём то, что кто-то снаружи положил в onClick
            }
        })
    }
}
