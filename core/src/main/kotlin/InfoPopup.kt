// InfoPopup.kt — НАШ попап, наследуемся от Table

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table

class InfoPopup(message: String, skin: Skin) : Table() {

    init {
        setFillParent(true) // накрывает весь экран полупрозрачным фоном
        background(skin.newDrawable("white", 0f, 0f, 0f, 0.6f)) // затемнение 60%

        val label = Label(message, skin)
        val btnClose = GameButton("CLOSE", skin)
        btnClose.onClick = { remove() } // актёр уходит со сцены сам

        val box = Table(skin)
        box.background(skin.newDrawable("white", 0.15f, 0.12f, 0.1f, 0.95f))
        box.add(label).pad(20f).row()
        box.add(btnClose).pad(10f)
        add(box).expand().center() // коробка по центру, фон на весь экран

        color.a = 0f
        addAction(Actions.fadeIn(0.2f)) // плавное появление
    }
}
