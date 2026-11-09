package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table

class InGameOptionsPopup(
    skin: Skin,
    onReturnToMenu: () -> Unit
) : Table() {
    init {
        setFillParent(true)
        // Перехватываем касания по затемнённому фону, чтобы экран игры
        // не получал нажатия, пока открыто окно опций.
        touchable = Touchable.enabled
        background(skin.newDrawable("white", 0f, 0f, 0f, 0.6f))

        val panel = Table(skin)
        panel.background(skin.newDrawable("white", 0.15f, 0.12f, 0.1f, 0.95f))
        panel.add(Label(Localization["game.options"], skin)).pad(20f).row()

        val btnReturn = GameButton(Localization["game.returnToMenu"], skin)
        btnReturn.onClick = onReturnToMenu
        panel.add(btnReturn).pad(10f).growX().row()

        val btnClose = GameButton(Localization["common.close"], skin)
        btnClose.onClick = { remove() }
        panel.add(btnClose).pad(10f).growX().row()

        add(panel).expand().center()
    }
}
