package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport

/**
 * Вступление к новой игре. История разделена на короткие карточки:
 * нажатие на текст или кнопку показывает следующий фрагмент.
 */
class IntroScreen(private val game: Main) : ScreenAdapter() {
    private val camera = OrthographicCamera()
    private val stage = Stage(FitViewport(900f, 450f, camera))
    private val batch = SpriteBatch()
    private val background = Texture(Gdx.files.internal("main-menu1.png"))

    private val pageCount = 5
    private var currentPage = 0

    private lateinit var storyLabel: Label
    private lateinit var pageLabel: Label
    private lateinit var nextButton: GameButton

    override fun show() {
        Gdx.input.inputProcessor = stage

        val shade = Table()
        shade.setFillParent(true)
        shade.touchable = Touchable.enabled
        shade.background(game.skin.newDrawable("white", 0f, 0f, 0f, 0.74f))

        val card = Table(game.skin)
        card.touchable = Touchable.enabled
        card.background(game.skin.newDrawable("white", 0.08f, 0.07f, 0.06f, 0.97f))

        val title = Label(Localization["intro.title"], game.skin)
        storyLabel = Label("", game.skin)
        storyLabel.setWrap(true)
        storyLabel.setAlignment(Align.left, Align.center)
        pageLabel = Label("", game.skin)
        nextButton = GameButton(Localization["intro.next"], game.skin)
        nextButton.onClick = { showNextPage() }

        // Нажатие на карточку или текст тоже перелистывает историю.
        // Нажатие на кнопку обрабатывается её собственным onClick, чтобы
        // один тап не перелистнул сразу две страницы.
        card.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (event?.target !== nextButton) showNextPage()
            }
        })

        card.add(title).pad(18f).row()
        card.add(storyLabel).width(650f).height(150f).pad(12f).left().row()
        card.add(pageLabel).pad(6f).row()
        card.add(nextButton).width(230f).height(52f).pad(12f).row()
        shade.add(card).width(710f).pad(18f).center()

        stage.addActor(shade)
        refreshPage()
    }

    private fun showNextPage() {
        if (currentPage < pageCount - 1) {
            currentPage++
            refreshPage()
        } else {
            startGame()
        }
    }

    private fun refreshPage() {
        storyLabel.setText(Localization["intro.page.${currentPage + 1}"])
        pageLabel.setText("${currentPage + 1} / $pageCount")
        nextButton.setText(
            Localization[if (currentPage == pageCount - 1) "intro.start" else "intro.next"]
        )
    }

    private fun startGame() {
        game.setScreen(PlayScreen(game))
        dispose()
    }

    override fun render(delta: Float) {
        batch.begin()
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.end()

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.dispose()
        background.dispose()
        batch.dispose()
    }
}
