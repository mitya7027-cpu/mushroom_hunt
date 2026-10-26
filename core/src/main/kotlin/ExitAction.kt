// ExitAction.kt — что происходит при нажатии EXIT.
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Stage

class ExitAction(private val game: Main, private val stage: Stage) : MenuAction {

    override fun run() {
        // ЗАДАЧА: выход из игры — Gdx.app.exit(). Пока честная заглушка.
        stage.addActor(InfoPopup("EXIT — IN DEVELOPMENT", game.skin))
    }
}
