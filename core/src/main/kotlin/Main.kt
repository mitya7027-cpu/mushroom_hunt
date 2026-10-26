// Main.kt — директор игры.
// Game — базовый класс libGDX: он решает, КАКОЙ экран сейчас живёт и кто получает render().
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class Main : Game() {

    // lateinit = «поле будет, но заполню позже».
    // Кожа (skin) — пакет стилей: шрифты, текстуры кнопок, отступы.
    // Живёт всю игру, поэтому храним её тут, а не в каждом экране.
    lateinit var skin: Skin

    // create() вызывается ОДИН раз при старте приложения.
    // Здесь готовим ресурсы и показываем первый экран.
    override fun create() {
        // Gdx.files.internal(...) — файл из папки ассетов (android/assets/).
        skin = Skin(Gdx.files.internal("ui/uiskin.json"))

        // setScreen = переключить активный экран. Игра начинается с меню.
        setScreen(MainMenuScreen(this))
    }

    // render() вызывается КАЖДЫЙ КАДР (60 раз в секунду и чаще).
    // Наш override рисует фон, затем super.render() отдаёт кадр текущему экрану.
    override fun render() {
        // glClearColor(r,g,b,a) — ЗАЛИТЬ буфер цветом перед рисованием.
        // Числа 0..1: 0.06/0.07/0.08 — почти чёрный, ночной лес. 1f = непрозрачно.
        Gdx.gl.glClearColor(0.06f, 0.07f, 0.08f, 1f)

        // glClear — реально очистить экран этим цветом. Без этого будет «грязь» из прошлого кадра.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // super.render() — теперь рисует текущий экран (у нас — MainMenuScreen).
        super.render()
    }

    // dispose() вызывается при выходе из игры. Освобождаем видеопамять.
    // Правило: кто создал ресурс (Texture, Skin, Batch) — тот и dispose().
    override fun dispose() {
        skin.dispose()
    }
}
