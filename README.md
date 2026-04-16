<div align="center">

<img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
<img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
<img src="https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
<img src="https://img.shields.io/badge/AI-Gemini_API-4285F4?style=for-the-badge&logo=google&logoColor=white"/>
<img src="https://img.shields.io/badge/DB-SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white"/>
<img src="https://img.shields.io/badge/Status-Beta-orange?style=for-the-badge"/>

<br/><br/>

# 🏋️ FitTimer — Tu Entrenador Personal Digital

**Aplicación Android desarrollada como Trabajo de Fin de Grado (DAM · CENEC Málaga · 2024)**

*Planifica entrenamientos, consulta a una IA personal y lleva el control de tu progreso físico, todo en una sola app.*

<br/>

[📱 Descargar APK](#instalación) · [✨ Ver Funcionalidades](#funcionalidades) · [🏗️ Arquitectura](#arquitectura)

</div>

---

## 📖 ¿Qué es FitTimer?

FitTimer nació de una idea sencilla: **muchas personas que empiezan en el gimnasio no tienen guía, no saben qué comer y no llevan seguimiento de su progreso**. Las apps existentes o son de pago, o son demasiado complejas, o simplemente no ofrecen un asistente inteligente real.

FitTimer resuelve eso combinando cuatro herramientas en una sola aplicación gratuita:

| Herramienta | Qué hace |
|---|---|
| 🤖 **Asistente IA (Gemini)** | Actúa como entrenador personal: responde preguntas, genera rutinas personalizadas y da consejos de nutrición |
| 📅 **Tabla semanal** | Organiza tu rutina arrastrando iconos de ejercicios a los días de la semana |
| 🍽️ **Recetas fitness** | Catálogo de recetas para objetivos de definición o volumen, con calorías |
| ⚖️ **Calculadora IMC** | Calcula y guarda tu IMC en el tiempo para visualizar tu evolución |

---

## ✨ Funcionalidades

### 🤖 Asistente Virtual con IA Gemini
- Integración con la **API de Gemini** (Google DeepMind) mediante solicitudes de chat en Kotlin
- Prompt inicial configurado para que actúe exclusivamente como entrenador personal
- Filtros de contenido activos: bloquea acoso, mensajes de odio y contenido peligroso
- Respuestas en tiempo real con formato de conversación

### 📅 Planificador Semanal
- Tabla de 7 días con indicador del día actual destacado
- Iconos de grupos musculares arrastrables (Pecho, Espalda, Pierna, Brazo, Push, Pull)
- Persistencia en base de datos: tu horario se guarda entre sesiones
- Botón para limpiar toda la semana de un vistazo

### 🍽️ Sección de Alimentación
- Recetas categorizadas por objetivo: **Definición** y **Volumen**
- Información calórica en cada receta
- Vista detallada con ingredientes e instrucciones

### ⚖️ Control de Progreso
- Cálculo de IMC con selector de altura, peso y edad
- Mensajes personalizados según resultado: Bajo peso / Normal / Sobrepeso / Obesidad
- Historial de pesos guardado: compara tu peso anterior con el actual
- Accesible desde cualquier pantalla de la app

### 🔐 Sistema de Autenticación
- Registro con validación de contraseña (mínimo 8 caracteres, hash SHA-256)
- Control de nombres de usuario únicos
- Cierre de sesión accesible desde el icono de usuario en cualquier pantalla

---

## 🏗️ Arquitectura

### Stack Tecnológico

```
📱 FitTimer
├── 🖥️  IDE              Android Studio
├── 🔤  Lenguajes        Kotlin · Java
├── 🗄️  Base de datos    SQLite (DatabaseHelper)
├── 🤖  IA               Gemini API (Google AI Studio)
├── 🔥  Cloud (opcional) Firebase
└── 🎨  UI               XML Layouts · Material Design
```

### Estructura de la Base de Datos (E-R)

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│    USUARIOS     │       │      PESOS      │       │     ICONOS      │
├─────────────────┤       ├─────────────────┤       ├─────────────────┤
│ id (PK)         │──┐    │ id (PK)         │   ┌───│ id (PK)         │
│ nombre          │  └───▶│ usuario_id (FK) │   │   │ usuario_id (FK) │
│ contraseña      │       │ peso            │   │   │ dia             │
└─────────────────┘       │ imc             │   │   │ icono           │
                          └─────────────────┘   │   └─────────────────┘
                                                └───▶ (relacionado con usuario)
```

### Flujo de Pantallas

```
Inicio
  ├── Registro ──▶ IMC ──▶ NavigationMenu
  └── Login    ──────────▶ NavigationMenu
                                │
                    ┌───────────┼───────────┬───────────┐
                    ▼           ▼           ▼           ▼
                 Home      Entrenamiento Alimentación Progreso
               (horario)   (chat IA)   (recetas)    (IMC + peso)
```

### Clases Principales

| Clase | Responsabilidad |
|---|---|
| `Inicio.kt` | Pantalla de bienvenida con navegación a Registro/Login |
| `Registro.kt` | Registro de usuario con validación y hash SHA-256 |
| `InicioSesion.kt` | Autenticación contra la base de datos |
| `IMC.kt` | Cálculo y guardado del IMC con selector interactivo |
| `NavigationMenu.kt` | Gestión de navegación entre fragmentos con animaciones |
| `HomeFragment.kt` | Tabla semanal con funcionalidad drag & drop |
| `EntrenamientoFragment.kt` | Chat con asistente IA (Gemini API) |
| `AlimentacionFragment.kt` | Catálogo de recetas por objetivo |
| `ProgresoFragment.kt` | Visualización del historial de peso e IMC |
| `DatabaseHelper.kt` | Capa de acceso a datos SQLite (CRUD completo) |

---

## 🚀 Instalación

### Requisitos previos
- Android Studio **Hedgehog** o superior
- SDK Android mínimo: API 24 (Android 7.0)
- Cuenta en [Google AI Studio](https://aistudio.google.com) para obtener la API Key de Gemini

### Pasos

```bash
# 1. Clona el repositorio
git clone https://github.com/sergioglezz/FitTimer-TFG.git

# 2. Abre el proyecto en Android Studio
# File > Open > selecciona la carpeta del proyecto

# 3. Añade tu API Key de Gemini
# Crea el archivo local.properties (si no existe) y añade:
# GEMINI_API_KEY=tu_api_key_aqui

# 4. Sincroniza Gradle y ejecuta en emulador o dispositivo físico
```

> ⚠️ **Nota:** La API Key de Gemini no está incluida en el repositorio por seguridad. Debes obtener la tuya en [Google AI Studio](https://aistudio.google.com/app/apikey) de forma gratuita.

---

## 📊 Análisis de Mercado

FitTimer se posiciona en el mercado con una propuesta diferenciadora frente a la competencia:

| App | Rutinas personalizables | Asistente IA interactivo | Recetas | Seguimiento IMC | Gratuita |
|---|:---:|:---:|:---:|:---:|:---:|
| MyFitnessPal | ⚠️ Limitado | ❌ | ✅ | ✅ | ⚠️ Premium |
| Nike Training Club | ❌ | ❌ | ❌ | ❌ | ✅ |
| StrongLifts 5x5 | ✅ | ❌ | ❌ | ❌ | ✅ |
| Fitbit | ⚠️ | ❌ | ❌ | ✅ | ⚠️ Dispositivo |
| **FitTimer** | ✅ | ✅ | ✅ | ✅ | ✅ |

---

## 🗺️ Roadmap

- [x] Sistema de autenticación con hash SHA-256
- [x] Calculadora IMC con historial
- [x] Tabla semanal con drag & drop persistente
- [x] Asistente virtual con Gemini API
- [x] Catálogo de recetas por objetivo
- [ ] Gráfico de evolución de peso en el tiempo
- [ ] Sistema de notificaciones push para recordatorios
- [ ] Upload de recetas propias por usuarios
- [ ] Integración con smartwatches / wearables
- [ ] Soporte multiidioma (inglés)

---

## 🧪 Pruebas Realizadas

- ✅ Validación de contraseña (mínimo 8 caracteres, coincidencia)
- ✅ Control de nombres de usuario duplicados
- ✅ Toast de error en credenciales incorrectas
- ✅ Guardado y recuperación de iconos en base de datos
- ✅ Mensajes adaptativos según rango de IMC (Bajo peso / Normal / Sobrepeso / Obesidad)
- ✅ Comparación de peso anterior vs actual en la pantalla de progreso

---

## 👤 Autor

**Sergio González Castilla**
Graduado en Desarrollo de Aplicaciones Multiplataforma · CENEC Málaga · 2024

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/tu-perfil)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/sergioglezz)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:sergioglezz04@gmail.com)

---

## 📚 Tecnologías y Referencias

- [Android Developers Documentation](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs)
- [Gemini API — Google AI Studio](https://ai.google.dev)
- [SQLite Documentation](https://sqlite.org/docs.html)
- [Firebase Documentation](https://firebase.google.com/docs)

---

<div align="center">

*Proyecto académico desarrollado como TFG del Grado Superior en DAM · Curso 2023/24*

⭐ Si te ha resultado útil o interesante, dale una estrella al repositorio

</div>
