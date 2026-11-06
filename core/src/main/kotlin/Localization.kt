package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.I18NBundle
import java.util.Locale

/**
 * МОДУЛЬ ЛОКАЛИЗАЦИИ (i18n = internationalization).
 *
 * Зачем это нужно:
 *   Все тексты игры (кнопки, заголовки, правила) лежат НЕ в коде,
 *   а в properties-файлах: assets/i18n/bundle.properties (английский,
 *   базовый) и bundle_ru.properties (русский оверлей).
 *   Код обращается к тексту по КЛЮЧУ: Localization["menu.play"] —
 *   и неважно, какой язык сейчас включён.
 *
 * Как добавить новый язык:
 *   1. Скопировать bundle_ru.properties -> bundle_de.properties
 *   2. Перевести значения
 *   3. Добавить код языка в SUPPORTED_LANGUAGES ниже
 *   Больше ничего: код игры не меняется.
 *
 * Почему object (синглтон):
 *   Словарь нужен всем экранам сразу, а создаём мы его один раз
 *   за запуск игры. Kotlin `object` = «один экземпляр на всё приложение»,
 *   идеально для глобальных сервисов вроде этого.
 */
object Localization {

    /** Путь к базе словаря (без языкового суффикса — libGDX допишет сам). */
    private const val BUNDLE_PATH = "i18n/bundle"

    /** Имя файла настроек, где храним выбранный язык между запусками. */
    private const val PREFS_NAME = "mushroom-hunt-settings"
    private const val PREF_KEY_LANG = "language"

    /** Языки, которые игра умеет. Ключ для переключателя в настройках. */
    val SUPPORTED_LANGUAGES = listOf("en", "ru")

    /** Текущий словарь. null означает «ещё не загрузили» — зовите [load] в create() игры. */
    private var bundle: I18NBundle? = null

    /** Текущий язык в виде кода ("en" / "ru"). */
    var currentLanguage: String = "en"
        private set

    /**
     * Загрузка словаря. Вызывается ОДИН раз при старте игры (Main.create()).
     *
     * Логика:
     *   1. Читаем сохранённый язык из Preferences (постоянное хранилище
     *      libGDX — на десктопе это файл в ~/.prefs, на Android — SharedPreferences).
     *   2. Если сохранённого нет (первый запуск) — берём язык системы,
     *      если он поддерживается, иначе английский.
     *   3. Грузим bundle для выбранного языка.
     */
    fun load() {
        val prefs = Gdx.app.getPreferences(PREFS_NAME)
        val saved = prefs.getString(PREF_KEY_LANG, null)
        val lang = when {
            saved != null -> saved
            SUPPORTED_LANGUAGES.contains(Locale.getDefault().language) -> Locale.getDefault().language
            else -> "en"
        }
        loadBundle(lang)
    }

    /**
     * Смена языка «на лету»: перегружает словарь и запоминает выбор.
     * Вызывается переключателем в настройках. После смены языка экрану
     * нужно перерисоваться — это делает экран сам (см. SettingsPopup).
     */
    fun setLanguage(langCode: String) {
        if (langCode == currentLanguage) return // тот же язык — ничего не делаем
        if (!SUPPORTED_LANGUAGES.contains(langCode)) return // незнакомый код — игнорируем
        loadBundle(langCode)
        Gdx.app.getPreferences(PREFS_NAME).putString(PREF_KEY_LANG, langCode).flush()
    }

    /** Сама загрузка словаря. createBundle ищет bundle_<язык>.properties,
     *  а если его нет — тихо берёт базовый bundle.properties. */
    private fun loadBundle(langCode: String) {
        bundle = I18NBundle.createBundle(Gdx.files.internal(BUNDLE_PATH), Locale(langCode))
        currentLanguage = langCode
    }

    /**
     * Достать текст по ключу: Localization["menu.play"] -> "ИГРАТЬ".
     *
     * Operator-функция: позволяет обращаться к объекту как к Map через [].
     * Если ключа нет ни в одном файле — вернём сам ключ с пометкой,
     * чтобы баг сразу бросался в глаза на экране, а не молчал.
     */
    operator fun get(key: String): String =
        bundle?.get(key) ?: "[no-locale: $key]"
}
