# PageReader

* Kotlin Multiplatform module that downloads a web page over HTTPS and extracts text with a
CSS selector (e.g. `title`, `h1`). Used by a native Android app and a native iOS app.

* MVVM, with all logic in one shared module:
`UI → PageReaderViewModel (StateFlow) → PageReaderRepository → UrlNormalizer / PageReaderApi / HtmlParser`

* **Shared:** ViewModel, state, networking, HTML parsing, validation, errors, unit tests.
* **Native:** the UI (Compose on Android, SwiftUI on iOS) and the HTTP engine (OkHttp / Darwin). On iOS, `PageReaderViewModelOwner` (Kotlin, `iosMain`) owns the shared ViewModel, and `PageReaderViewModelWrapper` (Swift `ObservableObject`) mirrors its `StateFlow` into `@Published state` for SwiftUI and clears it in `deinit`.

* Kotlin Multiplatform, Ktor, Ksoup, Coroutines/StateFlow, androidx ViewModel (multiplatform),
Compose + Material 3, SwiftUI, kotlin-test. Shipped as a Maven artifact and an XCFramework.

- HTTPS only, enforced in code and by the platform. Redirects cannot downgrade to HTTP.
- No WebView: the HTML is never rendered, only plain text extracted by a real parser.
- Timeouts (10 s / 15 s) and a 2 MB page limit, enforced while streaming.
- Strict URL and selector validation, with typed errors (`PageReaderError`) instead of crashes.
