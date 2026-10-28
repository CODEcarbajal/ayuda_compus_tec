## Nombre: HelpCampus - Mesa de Ayuda TI

**Descripcion:** AyudaCampus es una Aplicación móvil desarrollada en **Android con Jetpack Compose, MVVM, Room y DataStore** para la gestión de tickets de soporte técnico técnico en el campus universitario. Permite a los estudiantes reportar incidencias y al personal de TI atenderlas y analizarlas en tiempo real.

---

### 📁 Árbol de Paquetes

```text
com.carbajal.ayudacampus
│
├── data
│   ├── local
│   │   ├── dao (CategoriaDao, TicketDao, UsuarioDao)
│   │   ├── entity (Categoria, Comentario, Ticket, Usuario)
│   │   ├── relation (TicketConDetalle)
│   │   ├── AyudaDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── prefs
│   │   └── PreferenciasStore.kt (DataStore)
│   └── repository
│       ├── AyudaRepository.kt (Interface)
│       └── AyudaRepositoryRoom.kt
│
├── model (ConteoCategoria, EstadoTicket, Prioridad, Rol)
│
├── ui
│   ├── cliente (LoginScreen, MisTicketsScreen, NuevoTicketScreen, DetalleTicketScreen, PerfilScreen)
│   ├── soporte (PanelScreen, BandejaScreen, AtenderTicketScreen, CategoriasScreen, FormularioCategoriaScreen, AjustesScreen)
│   ├── components (AyudaButton, AyudaTextField, BurbujaComentario, EstadoChip, PrioridadChip, StepperTicket, TicketCard, Estrellas, graficos)
│   ├── navigation (NavAyuda, Rutas)
│   └── theme (Theme, Color, Type)
│
└── viewmodel (SesionViewModel, MisTicketsViewModel, NuevoTicketViewModel, TicketDetalleViewModel, PanelViewModel, BandejaViewModel, AtenderViewModel, CategoriasViewModel, AyudaViewModelFactory)
```

---

## 🏗️ Diagrama de Arquitectura MVVM

```mermaid
graph TD
    UI["Jetpack Compose UI (Screens & Components)"] -->|Observa StateFlow| VM["ViewModels (StateFlow / MutableStateFlow)"]
    VM -->|Invoca Métodos| Repo["AyudaRepository (Interfaz)"]
    Repo -->|Implementa| RepoImpl["AyudaRepositoryRoom"]
    RepoImpl -->|Consultas SQL| Room["Room Database (DAOs)"]
    RepoImpl -->|Preferencia / Ajustes| DataStore["DataStore (Preferences)"]
    Room -->|Flows de Datos| VM
    DataStore -->|Flows de Configuración| VM
```

---

## 📸 Capturas de Pantalla

### 👤 Perfil Estudiante (Cliente)
| 1. Inicio de Sesión | 2. Mis Tickets | 3. Nuevo Ticket |
| :---: | :---: | :---: |
| ![Login Estudiante](app/src/main/res/mipmap-hdpi/ic_launcher.png) | ![Mis Tickets](app/src/main/res/mipmap-hdpi/ic_launcher.png) | ![Nuevo Ticket](app/src/main/res/mipmap-hdpi/ic_launcher.png) |

### 🛠️ Perfil Soporte TI (Admin)
| 4. Panel de Soporte | 5. Bandeja de Tickets | 6. Atender Ticket |
| :---: | :---: | :---: |
| ![Panel Soporte](app/src/main/res/mipmap-hdpi/ic_launcher.png) | ![Bandeja Tickets](app/src/main/res/mipmap-hdpi/ic_launcher.png) | ![Atender Ticket](app/src/main/res/mipmap-hdpi/ic_launcher.png) |

---

### 🧠 Respuestas de las preguntas para pensar'

* **1. Reglas en ViewModel:** El ViewModel concentra la lógica de negocio y validaciones de estado para mantener los Composables limpios y no sobrecargar al DAO con reglas que no son de almacenamiento.
* **2. suspend vs Flow:** `crearTicket` es una operación puntual de una sola ejecución (`suspend`), mientras que `observarTicket` transmite actualizaciones constantes en tiempo real (`Flow`).
* **3. Cierre sin calificación:** Se perderían métricas de calidad del servicio y se cerrarían incidencias sin la confirmación del estudiante sobre si el problema se resolvió realmente.
* **4. Migración a servidor:** Solo se crearía una nueva implementación del repositorio (`AyudaRepositoryRemote`) usando Retrofit/Ktor, sin necesidad de modificar los ViewModels ni la interfaz visual.

---

## Agenda e historial de Soporte TI

Desde **Soporte TI → Ajustes → Herramientas de soporte**:

- **Agenda de atención:** selecciona un ticket abierto o en proceso, fecha, hora, duración (30, 60 o 90 minutos), lugar o enlace e indicaciones. Se rechazan fechas pasadas y cruces con otras citas programadas del mismo técnico. Las citas pueden cancelarse o completarse una vez iniciadas y conservan su registro en «Todas».
- **Historial de actividad:** consulta creaciones, comentarios (incluidas las notas internas de soporte), reaperturas y calificaciones, con autor, fecha y acceso al ticket. Incluye búsqueda y filtros por acción.

La agenda se guarda localmente: el horario debe confirmarse con el estudiante. No envía invitaciones externas. La migración 3 → 4 conserva los datos y recupera las creaciones, comentarios y calificaciones existentes con sus fechas originales. Las reaperturas anteriores a esta versión no tenían fecha registrada y no se reconstruyen.

Validación de estas funciones:

```bash
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest --offline
# Con un emulador o dispositivo conectado:
./gradlew :app:connectedDebugAndroidTest --offline
```

Las pruebas de soporte cubren validación de horarios, cruces, cancelaciones, historial, persistencia, migración y los flujos de las pantallas en tema claro/oscuro.
