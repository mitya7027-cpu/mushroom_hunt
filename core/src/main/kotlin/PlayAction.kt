// PlayAction.kt — что происходит при нажатии PLAY
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Stage

class PlayAction(private val game: Main, private val stage: Stage) : MenuAction {

    override fun run() {
        // ЗАДАЧА: когда-нибудь здесь будет запуск игры (переход на игровой экран).
        // Пока — заглушка.
        stage.addActor(InfoPopup("PLAY — IN DEVELOPMENT", game.skin))
    }
}
