// SettingsPopup.kt — окошко настроек (модальное, поверх меню).
// Наследуемся от Table — таблица-контейнер, раскладывает детей по ячейкам.
package ru.triplethall.mushroomhunt

// Слушатель смены языка живёт в пакете scene2d.utils, а не scene2d.ui —
// звёздочного импорта ui.* для него мало, тащим явно:
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.Gdx
class SettingsPopup(skin: Skin) : Table() {
    init {
        setFillParent(true) // занять весь экран (затемнение под окошком)
        // Затемняющий слой должен перехватывать касания вне карточки настроек.
        touchable = Touchable.enabled
        background(skin.newDrawable("white", 0f, 0f, 0f, 0.6f)) // полупрозрачная тень
// === ВИДЖЕТЫ ===
// Все надписи — из словаря локализации (assets/i18n/bundle*.properties).
// Хардкодить текст в коде — плохая привычка: его не перевести без пересборки.

// Переключатель (чекбокс): включён/выключен. Сам ничего не делает — просто тумблер.
        val chkMute = CheckBox("  " + Localization["settings.mute"], skin)
// Ползунок громкости: Slider(мин, макс, шаг, вертикальный?, скин)
        val sldVolume = Slider(0f, 100f, 1f, false, skin)
        sldVolume.value = 70f // начальное положение ползунка
// Переключатель вибрации (для телефонов)
        val chkVibration = CheckBox("  " + Localization["settings.vibration"], skin)

// --- ПЕРЕКЛЮЧАТЕЛЬ ЯЗЫКА (живой, не декорация) ----------------------
// SelectBox — выпадающий список. Пункты берём из Localization.SUPPORTED_LANGUAGES
// ("ru", "en") — если позже добавите bundle_de.properties, просто допишите
// "de" в тот список в Localization.kt, и здесь он появится сам.
        val selLanguage = SelectBox<String>(skin)
        selLanguage.setItems(*Localization.SUPPORTED_LANGUAGES.toTypedArray())
// Показываем текущий язык выбранным: uppercase для красоты ("ru" -> "RU")
        selLanguage.selected = Localization.currentLanguage

// Слушатель выбора. Событие ChangeEvent стреляет, когда пользователь ткнул пункт.
        selLanguage.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                // 1. Говорим модулю локализации сменить язык.
                //    Внутри: перезагрузка словаря + запоминание выбора в Preferences
                //    (выбранный язык переживёт перезапуск игры).
                Localization.setLanguage(selLanguage.selected)
                // 2. Язык сменился, но надписи уже НАРИСОВАНЫ старым текстом:
                //    кнопки меню собрались при входе в экран и сами себя
                //    не переведут. Пересоздаём весь экран меню — он соберётся
                //    из свежего словаря, а попап уйдёт вместе со старым экраном.
                //    postRunnable = «сделай в начале следующего кадра»: менять
                //    экран прямо посреди обработки клика опасно, кадр ещё
                //    держит старую сцену.
                Gdx.app.postRunnable {
                    // Ссылку на Main мы нигде не хранили — и не надо:
                    // libGDX сам знает главное приложение.
                    val game = Gdx.app.applicationListener as Main
                    game.screen.dispose() // setScreen сам не диспозит — не забыть!
                    game.screen = MainMenuScreen(game)
                }
            }
        })
// --------------------------------------------------------------------

// Кнопка закрытия — наш общий GameButton
        val btnClose = GameButton(Localization["common.close"], skin)
        btnClose.onClick = { remove() } // remove() — «уйти со сцены», окошко исчезнет
// === РАСКЛАДКА ===
// Карточка настроек — тёмная коробочка по центру
        val box = Table(skin)
        box.background(skin.newDrawable("white", 0.15f, 0.12f, 0.1f, 0.95f))
// Заголовок. Label — просто текстовая надпись.
        box.add(Label(Localization["settings.title"], skin)).pad(20f).colspan(2).row()
// Строка «VOLUME + ползунок»: слева надпись, справа виджет
        box.add(Label(Localization["settings.volume"], skin)).pad(10f).left()
        box.add(sldVolume).pad(10f).width(220f).row()
// Строка «MUTE + тумблер»
        box.add(Label(Localization["settings.mute"], skin)).pad(10f).left()
        box.add(chkMute).pad(10f).row()
// Строка «VIBRATION + тумблер»
        box.add(Label(Localization["settings.vibration"], skin)).pad(10f).left()
        box.add(chkVibration).pad(10f).row()
// Строка «LANGUAGE + выпадашка»
        box.add(Label(Localization["settings.language"], skin)).pad(10f).left()
        box.add(selLanguage).pad(10f).row()
// Кнопка CLOSE во всю ширину, побольше отступ сверху
        box.add(btnClose).pad(20f).colspan(2).growX().row()
// Коробочку — в центр экрана
        add(box).expand().center()
    }
}
