package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport




class PlayScreen(private val game: Main) : ScreenAdapter() {
    private val camera = OrthographicCamera()
    private val stage = Stage(FitViewport(900f, 450f, camera))
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val player = Player()

    init {
        game.gameState.ingame = true
    }


//    Это у нас задник, который в главном меню
    private val bg = Texture(Gdx.files.internal("main-menu1.png"))


    // функция показа
    override fun show() {
        Gdx.input.inputProcessor = stage // Оживляем кнопку

        val root = Table()
        root.setFillParent(true)
        // -------------
        root.top().left()
        root.pad(16f) // внешний отступ от слова padded
        // Тут мы делаем положение и отступ кнопки возврата в меню
        stage.addActor(root) // Добавили кноаку полностью


        val btnOptions = GameButton(Localization["game.options"], game.skin)
        btnOptions.onClick = { stage.addActor(InGameOptionsPopup(game.skin) { returnToMenu() }) }
        root.add(btnOptions)
    }

    private fun returnToMenu() {
        game.setScreen(MainMenuScreen(game))
        dispose()
    }

    // тут я пока сам не понимаю, что такое, но по названию скорее всего рендер картинки
    override fun render(delta: Float) {
        batch.begin()
        batch.draw(bg, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.end()

        drawHealthHearts()

        stage.act(delta)
        stage.draw()
    }

    /** Рисует шкалу здоровья игрока в правом верхнем углу виртуального экрана. */
    private fun drawHealthHearts() {
        val heartSize = 12f
        val heartStep = 28f
        val totalWidth = (player.maxHealth - 1) * heartStep + heartSize * 2f
        val startX = stage.viewport.worldWidth - totalWidth - 16f
        val y = stage.viewport.worldHeight - heartSize * 2f - 16f

        stage.viewport.apply()
        shapeRenderer.projectionMatrix = stage.camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (index in 0 until player.maxHealth) {
            shapeRenderer.color = if (index < player.health) {
                Color(0.88f, 0.12f, 0.16f, 1f)
            } else {
                Color(0.28f, 0.22f, 0.22f, 0.85f)
            }
            drawHeart(startX + index * heartStep, y, heartSize)
        }
        shapeRenderer.end()
    }

    /** Простая векторная форма сердца: два круглых лепестка и нижний кончик. */
    private fun drawHeart(x: Float, y: Float, size: Float) {
        shapeRenderer.triangle(
            x, y + size * 0.65f,
            x + size * 2f, y + size * 0.65f,
            x + size, y
        )
        shapeRenderer.circle(x + size * 0.55f, y + size * 0.65f, size * 0.5f)
        shapeRenderer.circle(x + size * 1.45f, y + size * 0.65f, size * 0.5f)
    }

    override fun resize(width: Int, height: Int) = stage.viewport.update(width, height, true)

    override fun dispose() {
        stage.dispose()
        bg.dispose()
        batch.dispose()
        shapeRenderer.dispose()
    }

}
