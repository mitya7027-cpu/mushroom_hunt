// Main.kt — директор игры.
// Game — базовый класс libGDX: он решает, КАКОЙ экран сейчас живёт и кто получает render().
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.ui.Skin

//Новые импорты для красивой кнопки:
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

//Импорты для шрифта с кириллицей (freetype-генератор):
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter

class Main : Game() {

    // lateinit = «поле будет, но заполню позже».
    // Кожа (skin) — пакет стилей: шрифты, текстуры кнопок, отступы.
    // Живёт всю игру, поэтому храним её тут, а не в каждом экране.

    //Жуть какая...
    lateinit var skin: Skin

    //Дежмим кнопку в переменнной, что бы потом её убить :)
    lateinit var buttonTexture: Texture

    //Генератор шрифта и сам шрифт с кириллицей — тоже ресурсы, убьём в dispose()
    private var fontGenerator: FreeTypeFontGenerator? = null
    private var cyrillicFont: BitmapFont? = null

    // create() вызывается ОДИН раз при старте приложения.
    // Здесь готовим ресурсы и показываем первый экран.
    override fun create() {
        // ЛОКАЛИЗАЦИЯ: грузим словарь ДО всех экранов, чтобы любой экран
        // мог сразу просить тексты: Localization["menu.play"] и т.п.
        // Порядок важен: сначала словарь, потом экраны, которые из него читают.
        Localization.load()

        // Gdx.files.internal(...) — файл из папки ассетов (android/assets/).
        skin = Skin(Gdx.files.internal("ui/uiskin.json"))

        // --- ШРИФТ С КИРИЛЛИЦЕЙ -------------------------------------------
        // Дефолтный шрифт скина — bitmap-картинка только с латиницей.
        // Русские буквы он просто не знает: вместо «ИГРАТЬ» будут квадраты.
        // Решение: берём обычный системный TTF-шрифт (DejaVuSans.ttf лежит
        // в assets/fonts/) и ГЕНЕРИРУЕМ из него BitmapFont на лету.
        fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("fonts/DejaVuSans.ttf"))

        // Параметры генерации. Размер — в пикселях высоты шрифта.
        val fontParams = FreeTypeFontParameter().apply {
            size = 22
            // Цвет по умолчанию — белый (можно красить и через стиль, как у кнопок)
            color = Color.WHITE

            // ГЛАВНОЕ МЕСТО ЭТОЙ ЗАДАЧИ. По умолчанию freetype запекает в битмапу
            // только DEFAULT_CHARS — это ASCII: латиница, цифры, базовые знаки.
            // Русских букв там НЕТ: шрифт их «не знает», и вместо «ИГРАТЬ»
            // рисуются пустоты или квадраты. Поэтому явно перечисляем всё,
            // что хотим видеть: кириллицу и типографские знаки из наших текстов.
            // Ё отдельно: в Юникоде она живёт ВНЕ основного блока А-я!
            characters = FreeTypeFontGenerator.DEFAULT_CHARS + buildString {
                for (c in 'а'..'я') append(c)   // строчные
                for (c in 'А'..'Я') append(c)   // прописные
                append("ёЁ")                    // ё — отдельно от блока
                append("«»—–−…→№°")             // ёлочки, тире, стрелка из правил
            }
        }
        cyrillicFont = fontGenerator!!.generateFont(fontParams)

        // Подменяем шрифт скина: кладём свой ПОД ТЕМ ЖЕ ИМЕНЕМ «default-font».
        skin.add("default-font", cyrillicFont)

        // НО! Одного add() мало — важный урок, как устроен Skin:
        // uiskin.json при загрузке РАЗРЕШИЛ имена шрифтов в стилях — каждый
        // стиль (LabelStyle, CheckBoxStyle...) уже держит прямую ССЫЛКУ на
        // старый bitmap-шрифт. add() обновляет ресурс в карте скина, но НЕ
        // трогает уже собранные стили. Поэтому меню (шрифт выдаём руками)
        // рисовало кириллицу, а Label-ы в попапах — пустоту.
        // Лечим: перепривязываем шрифт во всех дефолтных стилях скина.
        cyrillicFont?.let { font ->
            skin.get(Label.LabelStyle::class.java).font = font            // надписи
            skin.get(TextButton.TextButtonStyle::class.java).font = font  // кнопки
            skin.get(CheckBox.CheckBoxStyle::class.java).font = font      // тумблеры
            skin.get(SelectBox.SelectBoxStyle::class.java).font = font    // выпадашка
            skin.get(List.ListStyle::class.java).font = font              // список внутри выпадашки
        }
        // ------------------------------------------------------------------

        // Берём текстурку кнопки из папки
        buttonTexture = Texture(Gdx.files.internal("buttons/menubutton.png"))
        // Собираем стиль
        val menuStyle = TextButton.TextButtonStyle()
        // Обычный вид
        menuStyle.up = TextureRegionDrawable(buttonTexture)
        //Когда нажали на кнопку, меняем её цвет на секунду
        menuStyle.down = TextureRegionDrawable(buttonTexture).tint(Color(0.65f, 0.65f, 0.65f, 1f))
        // тут просто меняем цвет когда наводимся мышкой, что бы показать, что кнопка кликабельна
        menuStyle.over = TextureRegionDrawable(buttonTexture).tint(Color(0.85f, 0.85f, 0.85f, 1f))
        // Загружаем шрифт
        menuStyle.font = skin.getFont("default-font")
        // Цвет текста
        menuStyle.fontColor = Color.WHITE
        // Ииии... добавляем как скин для кнопок через вызов
        skin.add("menu", menuStyle)

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
        // Порядок смерти — обратный порядку рождения.
        // 1) Генератор шрифта: шрифт уже сгенерирован, TTF в памяти не нужен.
        //    Диспозить его ПОСЛЕ generateFont безопасно — шрифт хранит свою копию.
        fontGenerator?.dispose()

        // 2) Skin. ВАЖНО для студентоты: skin.dispose() сам убивает ВСЁ,
        //    что в него положили через skin.add(...) — включая наш
        //    «default-font». Руками его диспозить НЕ надо: двойной dispose
        //    и даёт «already disposed».
        skin.dispose()

        // 3) Текстуру кнопки в skin не клали — убиваем руками.
        buttonTexture.dispose()
    }
}
