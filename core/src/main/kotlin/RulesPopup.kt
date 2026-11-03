package ru.triplethall.mushroomhunt
import com.badlogic.gdx.scenes.scene2d.ui.*
class RulesPopup(skin: Skin) : Table() {
    init {
        setFillParent(true)
        background(skin.newDrawable("white", 0f, 0f, 0f, 0.6f))
// Многострочный текст берём из словаря локализации — ключ rules.text.
// Раньше здесь лежала огромная строка прямо в коде: перевести её было
// невозможно, не трогая Kotlin. Теперь текст живёт в
// assets/i18n/bundle.properties (английский) и bundle_ru.properties (русский),
// а код просто просит его по ключу. Хотите поправить правила — правите
// properties-файл, код не пересобирается вообще.
        val text = Localization["rules.text"]
        val label = Label(text, skin)
        label.setWrap(true) // ВАЖНО: разрешить перенос строк. Без этого текст уползёт за экран одной
// ScrollPane — «окно» с прокруткой: внутри лента, снаружи рамка.
// В конструктор — контейнер с текстом и стиль из скина.
        val textTable = Table()
        textTable.pad(10f)
        textTable.add(label).width(520f).left() // фиксированная ширина текста, перенос работает по
        val scroll = ScrollPane(textTable, skin)
        val btnClose = GameButton(Localization["common.close"], skin)
        btnClose.onClick = { remove() }
// Карточка
        val box = Table(skin)
        box.background(skin.newDrawable("white", 0.15f, 0.12f, 0.1f, 0.95f))
        box.add(Label(Localization["rules.title"], skin)).pad(16f).row()
        box.add(scroll).pad(10f).size(560f, 300f).row()
        box.add(btnClose).pad(16f).row()
        add(box).expand().center()
    }
}
