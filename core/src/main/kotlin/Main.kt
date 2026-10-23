// Main.kt — директор игры

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class Main : Game() {

    lateinit var skin: Skin  // общий ресурс — живёт всю игру

    override fun create() {
        skin = Skin(Gdx.files.internal("ui/uiskin.json")) // стандартный скин libGDX
        setScreen(MainMenuScreen(this)) // стартуем с меню
    }

    override fun render() {
        Gdx.gl.glClearColor(0.06f, 0.07f, 0.08f, 1f) // тёмный фон, ночной лес
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        super.render() // рисует текущий экран
    }

    override fun dispose() {
        skin.dispose()
    }
}
