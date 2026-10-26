// InfoPopup.kt — НАШ попап (модальное окошко поверх экрана).
// Наследуемся от Table — это таблица-контейнер: раскладывает детей по ячейкам.
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table

class InfoPopup(message: String, skin: Skin) : Table() {

    init {
        // setFillParent(true) — растянуть себя на весь родитель (весь экран).
        // Без этого таблица сожмётся до размеров содержимого.
        setFillParent(true)

        // background(...) — фон таблицы.
        // skin.newDrawable("white", r,g,b,a) — берём белую текстуру из скина и красим.
        // Последний аргумент 0.6f = прозрачность 60%: экран под попапом затемнён, но виден.
        background(skin.newDrawable("white", 0f, 0f, 0f, 0.6f))

        // Label — текстовая надпись. Текст берём из конструктора (message).
        val label = Label(message, skin)

        // Кнопка закрытия — наш же GameButton. Композиция: попап СОДЕРЖИТ кнопку.
        val btnClose = GameButton("CLOSE", skin)
        btnClose.onClick = { remove() } // remove() = «уйти со сцены». Актёр удаляет сам себя.

        // Внутренняя коробочка-карточка, тоже Table.
        val box = Table(skin)
        box.background(skin.newDrawable("white", 0.15f, 0.12f, 0.1f, 0.95f)) // тёмно-коричневая, почти непрозрачная

        // Кладём в коробку текст и кнопку ВНИЗ под ним.
        // .pad(20f) — отступы вокруг ячейки. .row() — «перенос строки»: следующий add пойдёт ниже.
        box.add(label).pad(20f).row()
        box.add(btnClose).pad(10f)

        // Коробку — в корневую таблицу: expand() = занять всё доступное место,
        // center() = прижать содержимое к центру. Получаем попап посреди экрана.
        add(box).expand().center()

        // Анимация появления: стартуем полностью прозрачными...
        color.a = 0f
        // ...и плавно проявляемся за 0.2 секунды. Actions.fadeIn — готовое действие libGDX.
        addAction(Actions.fadeIn(0.2f))
    }
}
