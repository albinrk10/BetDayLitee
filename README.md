# BetDay Lite - App de Apuestas Deportivas

**BetDay Lite** es una aplicación Android que permite visualizar eventos deportivos del día, realizar apuestas simuladas en mercado 1X2 y gestionar un historial de apuestas con persistencia local.

## Características
- Timeline de eventos deportivos por hora
- Apuestas simuladas con mercado 1X2
- Historial de apuestas en perfil
- Detalle completo de cada apuesta
- Persistencia local con Room

---

## Clonar el Proyecto

### Desde Git
```powershell
git clone https://github.com/albinrk10/BetDayLitee.git
cd BetDayLite
```

### O descargar como ZIP
1. Ir a: https://github.com/albinrk10/BetDayLitee
2. Click `Code → Download ZIP`
3. Extraer archivo ZIP
4. Abrir carpeta en terminal

---

## Requisitos
- JDK 11+
- Android Studio 2024.1+
- Android SDK API 34
- Emulador Android o dispositivo físico

---

## Ejecución

### Opción 1: Desde terminal
```powershell
cd C:\Users\Albin\Desktop\saldo\BetDayLite
.\gradlew.bat :app:assembleDebug
adb install "app\build\outputs\apk\debug\app-debug.apk"
adb shell am start -n com.albin.betdaylite/.MainActivity
```

### Opción 2: Desde Android Studio
1. Abrir proyecto en Android Studio
2. Click `Run → Run 'app'`
3. Seleccionar emulador o dispositivo

### Opción 3: APK precompilado
```powershell
adb install "C:\Users\Albin\Desktop\BetDayLite-Debug.apk"
adb shell am start -n com.albin.betdaylite/.MainActivity
```

---

## Stack Técnico
- Kotlin
- Jetpack Compose
- MVVM + Clean Architecture
- Hilt (inyección de dependencias)
- Room (persistencia local)
- Retrofit + OkHttp (datos mock)
- Coroutines + Flow
- JUnit + Mockito

---

## Testing

### Ejecutar Tests Unitarios
```powershell
.\gradlew.bat test
```

O desde Android Studio:
- Click `Run → Run All Tests`
- O click derecho en carpeta `test/` → `Run Tests`

### Ejecutar Tests Instrumentados
```powershell
.\gradlew.bat connectedAndroidTest
```

Requiere emulador o dispositivo conectado.

### Tests Disponibles
- `PlaceBetUseCaseTest.kt` - Validación de reglas de negocio
- `HomeViewModelTest.kt` - Manejo de estado en UI
- `ExampleInstrumentedTest.kt` - Tests en dispositivo

---

## Estructura del Proyecto
```
app/src/main/java/com/albin/betdaylite/
├── presentation/    (UI y ViewModels)
├── domain/          (Lógica de negocio)
├── data/            (Persistencia y API)
├── core/di/         (Inyección de dependencias)
└── assets/          (Datos mock JSON)
```

