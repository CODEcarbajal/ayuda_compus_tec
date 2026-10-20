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
| 1. Perfil | 2. Mis Tickets | 3. Nuevo Ticket |
| :---: | :---: | :---: |
|<img width="250" height="500" alt="WhatsApp Image 2026-10-20 at 01 25 00" src="https://github.com/user-attachments/assets/65e23edb-f134-4fdc-811f-1ddfa239a35a" />|<img width="250" height="500" alt="WhatsApp Image 2026-10-20 at 01 25 42" src="https://github.com/user-attachments/assets/85f32255-bbc4-4d32-a95b-260bd1fe6f6e" />|<img width="250" height="500" alt="WhatsApp Image 2026-10-20 at 01 27 04" src="https://github.com/user-attachments/assets/37dcb1ad-4ccb-4b1d-a39b-e6f96b6fbf9a" />|


### 🛠️ Perfil Soporte TI (Admin)
| 4. Panel de Ajustes | 5. Panel de Soporte | 6. Atender Ticket |
| :---: | :---: | :---: |
|<img width="250" height="500" alt="WhatsApp Image 2026-10-20 at 01 25 42(1)" src="https://github.com/user-attachments/assets/f23666dd-456d-4db1-9737-80a0b75fc8b6" />|<img width="250" height="500" alt="WhatsApp Image 2026-10-20 at 01 23 52" src="https://github.com/user-attachments/assets/815e3e15-80e4-400c-b0f8-582751b9b47d" />|<img width="250" height="500" alt="WhatsApp Image 2026-10-20 at 01 23 52(1)" src="https://github.com/user-attachments/assets/7197c9a9-6f83-4040-a1f7-a7d34adde1b8" />|

---

### 🧠 Respuestas de las preguntas para pensar'

* **1. Reglas en ViewModel:** El ViewModel concentra la lógica de negocio y validaciones de estado para mantener los Composables limpios y no sobrecargar al DAO con reglas que no son de almacenamiento.
* **2. suspend vs Flow:** `crearTicket` es una operación puntual de una sola ejecución (`suspend`), mientras que `observarTicket` transmite actualizaciones constantes en tiempo real (`Flow`).
* **3. Cierre sin calificación:** Se perderían métricas de calidad del servicio y se cerrarían incidencias sin la confirmación del estudiante sobre si el problema se resolvió realmente.
* **4. Migración a servidor:** Solo se crearía una nueva implementación del repositorio (`AyudaRepositoryRemote`) usando Retrofit/Ktor, sin necesidad de modificar los ViewModels ni la interfaz visual.

---
