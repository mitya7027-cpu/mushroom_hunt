
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table

/**
 * Всплывающее окно подтверждения выхода из игры.
 *
 * TODO:
 * Реализовать подтверждение выхода из приложения.
 *
 * Окно содержит две кнопки:
 * Да — завершить игру.
 * Нет — закрыть окно и вернуться в меню.
 */
class ExitConfirmPopup(skin: Skin) : Table() {

    init {
        // TODO: Растянуть окно на весь экран.
        setFillParent(true)

        // TODO: Сделать окно доступным для обработки нажатий.
        touchable = Touchable.enabled

        // TODO: Добавить затемнение фона под окном.
        background(skin.newDrawable("white", 0f, 0f, 0f, 0.6f))

        // TODO: Создать текст с вопросом о выходе.
        val label = Label(
            "Вы уверены, что хотите выйти из игры?",
            skin
        )

        // TODO: Создать кнопку подтверждения выхода.
        val btnYes = GameButton("Да", skin)

        // TODO: Создать кнопку отмены выхода.
        val btnNo = GameButton("Нет", skin)

        // TODO: При подтверждении закрыть приложение.
        btnYes.onClick = {
            remove()
            Gdx.app.exit()
        }

        // TODO: При отмене удалить окно со сцены.
        btnNo.onClick = {
            remove()
        }

        // TODO: Создать внутреннюю карточку окна.
        val box = Table(skin)

        // TODO: Установить цвет фона карточки.
        box.background(
            skin.newDrawable("white", 0.15f, 0.12f, 0.1f, 0.95f)
        )

        // TODO: Добавить заголовок и кнопки.
        box.add(label)
            .colspan(2)
            .pad(20f)
            .row()

        // TODO: Разместить кнопки рядом друг с другом.
        box.add(btnYes)
            .width(120f)
            .pad(10f)

        box.add(btnNo)
            .width(120f)
            .pad(10f)

        // TODO: Разместить карточку по центру экрана.
        add(box).expand().center()

        // TODO: Реализовать плавное появление окна.
        color.a = 0f
        addAction(Actions.fadeIn(0.2f))
    }
}
