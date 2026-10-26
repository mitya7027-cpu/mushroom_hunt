// SettingsAction.kt — что происходит при нажатии SETTINGS
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Stage

class SettingsAction(private val game: Main, private val stage: Stage) : MenuAction {

    override fun run() {
        // ЗАДАЧА: здесь будет экран настроек (звук, язык и т.д.). Пока — заглушка.
        stage.addActor(InfoPopup("SETTINGS — IN DEVELOPMENT", game.skin))
    }
}
