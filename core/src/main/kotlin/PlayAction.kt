// PlayAction.kt — что происходит при нажатии PLAY.
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Stage

// : MenuAction — реализуем контракт. Компилятор заставит написать run().
// В конструкторе принимаем всё, что может понадобиться: игру (Main) и сцену (Stage).
class PlayAction(private val game: Main, private val stage: Stage) : MenuAction {

    override fun run() {
        // ЗАДАЧА: когда-нибудь здесь будет переход на игровой экран
        // (setScreen у game). Пока показываем заглушку.
        // stage.addActor(...) — добавить актёра на сцену; попап накроет меню.
        stage.addActor(InfoPopup("PLAY — IN DEVELOPMENT", game.skin))
    }
}
