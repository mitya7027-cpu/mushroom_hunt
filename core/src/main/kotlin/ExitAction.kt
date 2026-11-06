
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Stage

/**
 * Обработчик нажатия кнопки EXIT.
 *
 * TODO:
 * Вместо немедленного выхода из игры
 * показывать окно подтверждения.
 */
class ExitAction(
    private val game: Main,
    private val stage: Stage
) : MenuAction {

    override fun run() {

        // TODO: Создать окно подтверждения выхода.
        val popup = ExitConfirmPopup(game.skin)

        // TODO: Добавить окно на текущую сцену.
        stage.addActor(popup)
    }
}
