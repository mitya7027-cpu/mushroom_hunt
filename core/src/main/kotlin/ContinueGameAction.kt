package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Stage

class ContinueGameAction(private val game: Main, private val stage: Stage) : MenuAction {
    override fun run() {
        val menu = game.screen
        game.setScreen(PlayScreen(game))
        menu.dispose()
    }
}


