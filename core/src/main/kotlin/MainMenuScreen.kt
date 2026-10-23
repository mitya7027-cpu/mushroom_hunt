// MainMenuScreen.kt — экран главного меню

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport
import ru.triplethall.mushroomhunt.Main

class MainMenuScreen(private val game: Main) : Screen {

    private val camera = OrthographicCamera()
    private val stage = Stage(FitViewport(800f, 1280f, camera)) // вертикальный мобильный формат
    private val root = Table()

    override fun show() {
        Gdx.input.inputProcessor = stage // БЕЗ ЭТОГО кнопки мёртвые (грабля №1)

        root.setFillParent(true) // растянуть на весь экран
        stage.addActor(root)

        val btnPlay = GameButton("PLAY", game.skin)
        val btnRules = GameButton("RULES", game.skin)
        val btnSettings = GameButton("SETTINGS", game.skin)
        val btnExit = GameButton("EXIT", game.skin)

        // Пока у всех кнопок одно действие: попап. Когда появятся экраны — сменим только эти строки.
        val showPopup = { stage.addActor(InfoPopup("IN DEVELOPMENT", game.skin)) }
        btnPlay.onClick = showPopup
        btnRules.onClick = showPopup
        btnSettings.onClick = showPopup
        btnExit.onClick = showPopup

        root.add(btnPlay).pad(12f).row()
        root.add(btnRules).pad(12f).row()
        root.add(btnSettings).pad(12f).row()
        root.add(btnExit).pad(12f).row()
    }

    override fun render(delta: Float) {
        stage.act(delta)   // двигает анимации
        stage.draw()       // рисует всех актёров
    }

    override fun resize(width: Int, height: Int) = stage.viewport.update(width, height, true)

    override fun hide() {}
    override fun pause() {}
    override fun resume() {}

    override fun dispose() {
        stage.dispose()
    }
}
