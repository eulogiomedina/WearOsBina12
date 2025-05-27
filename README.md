# WearOsBina12
# 🌐 DISEÑO DE INTERFACES RESPONSIVAS - Wear OS

Este repositorio contiene el diseño y desarrollo de una interfaz responsiva para dispositivos Wear OS utilizando Jetpack Compose y herramientas complementarias.

## ✅ Herramientas utilizadas

- **Jetpack Compose para Wear OS** (Material3, Preview)
- **Figma** con plugin **Wear OS UI Kit**
- **Android Studio** para diseño y emulación
- **Dispositivo físico** (opcional) para pruebas reales
- **Markdown** para documentación (`README.md`)

## 🎨 Diseño adaptable

- Se emplearon **unidades relativas** como `dp` y `sp` para garantizar que la interfaz se adapte correctamente a diferentes tamaños de pantalla y densidades.
- Se probaron **múltiples resoluciones típicas** de Wear OS: `320x320`, `454x454`, entre otras.
- Se integraron **mockups en Figma**, con base en las recomendaciones de diseño de Google para Wear OS.

## 👀 Previews y pruebas

- Las pantallas se anotaron con `@Preview` para simular distintos dispositivos desde Android Studio.
- Se revisaron los comportamientos con distintos tamaños de pantalla, navegación y scroll vertical.
- Componentes utilizados:
  - `Chip`, `Text`, `Button`, `Column`, `Row`
  - `TransformingLazyColumn`, `AppScaffold`, `ScreenScaffold`

## 🚀 Navegación y funcionalidad

- Al pulsar el botón “Aceptar” en la pantalla de bienvenida, se navega a una segunda pantalla que muestra el **estatus del pago** y una **fecha futura generada dinámicamente**.
- Se utilizó `navigation-compose` para gestionar las pantallas.
- Se puede regresar a la pantalla anterior mediante navegación o `popBackStack()`.

## 🧪 Emuladores y dispositivos

- Se utilizó el emulador de Android Studio configurado con dispositivos Wear OS redondos (Large Round, Small Round).
- También es compatible con dispositivos físicos Wear OS reales para pruebas más precisas.

## 📁 Estructura del proyecto

