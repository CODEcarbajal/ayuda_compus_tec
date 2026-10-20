# Prompts utilizados

## Prompt 1
**Texto enviado a Gemini:** Implementa la mejora "Comentarios internos": notas de Soporte TI en un ticket que el estudiante NO puede ver. Cambios por capa (respeta MVVM, sin XML): Comentario.kt: Agrega campo esInterno: Boolean = false. AyudaRepository.kt y AyudaRepositoryRoom.kt: Actualiza agregarComentario para recibir esInterno: Boolean. AtenderViewModel.kt: Añade estado para marcar el comentario como nota interna. AtenderTicketScreen.kt: Añade un Switch/Checkbox ("Nota interna") al escribir y un indicador de candado en las notas internas. DetalleTicketScreen.kt: Oculta notas internas al rol CLIENTE. Muéstrame solo los archivos que cambian. Capas que cambian: Entidad + DAO + ViewModel + View.
**Que genero la IA:** Comentario.kt, AyudaRepository.kt, AyudaRepositoryRoom.kt, AtenderViewModel.kt, AtenderTicketScreen.kt, DetalleTicketScreen.kt, BurbujaComentario.kt y AyudaDatabase.kt. Agregó la propiedad esInterno a la entidad Comentario, actualizó el repositorio con Room, añadió el estado en AtenderViewModel, colocó el Switch con el ícono de candado en la vista de soporte y filtró las notas en la pantalla del estudiante.
**Que tuve que corregir yo:** Importación manual del enum Rol en AtenderTicketScreen.kt.
**Respeta MVVM?:** Sí, la lógica de visibilidad y el envío de comentarios están gestionados en el ViewModel, delegando la persistencia a Room y manteniendo la UI completamente reactiva.

## Prompt 2
**Texto enviado a Gemini:** Implementa la mejora "Contador de SLA por prioridad": muestra en cada ticket el tiempo restante para su atención según su prioridad (Alta: 4h, Media: 24h, Baja: 72h). Cambios por capa (respeta MVVM, sin XML): MisTicketsViewModel.kt / BandejaViewModel.kt / TicketDetalleViewModel.kt: Agrega una función/propiedad calculada que determine las horas/minutos restantes ((fecha + limiteSLA) - System.currentTimeMillis()) según la prioridad (ALTA -> 4h, MEDIA -> 24h, BAJA -> 72h) y si el ticket ya expiró. TicketCard.kt / DetalleTicketScreen.kt: Muestra un badge o chip visual con el tiempo restante (ej. "SLA: 2h restantes" o "SLA vencido hace 30m" en rojo si expiró). Si el ticket está RESUELTO o CERRADO, oculta el contador o muestra "Finalizado". Muéstrame solo los nombres de los archivos que cambian.
**Que genero la IA:** SlaInfo.kt, SlaChip.kt, TicketCard.kt, DetalleTicketScreen.kt, MisTicketsViewModel.kt, BandejaViewModel.kt y TicketDetalleViewModel.kt. Creó el modelo SlaInfo con la función de cálculo del tiempo límite según la prioridad, construyó el componente SlaChip con estilos condicionales en rojo cuando expira e integró el contador en las listas y vistas de detalle.
**Que tuve que corregir yo:** Ninguno, la generación fue correcta y compiló sin errores.
**Respeta MVVM?:** Sí, el cálculo del SLA se realiza a partir de los datos expuestos por el ViewModel y el Repositorio, desacoplando la lógica del tiempo del componente visual Compose.

## Prompt 3
**Texto enviado a Gemini:** Implementa la mejora "Tiempo promedio de resolución": calcula y muestra en el panel de soporte cuánto tarda en promedio resolverse un ticket (fechaCierre - fecha). Cambios por capa (respeta MVVM, sin XML): TicketDao.kt: Agrega SELECT AVG(fechaCierre - fecha) FROM tickets WHERE fechaCierre IS NOT NULL retornando Flow<Double?>. AyudaRepository.kt y AyudaRepositoryRoom.kt: Agrega observarTiempoPromedioResolucion(): Flow<Double?>. PanelViewModel.kt: Expone tiempoPromedioText: StateFlow<String> formateado en horas/minutos (ej. "2.5 hrs"). PanelScreen.kt: Muestra el valor en una nueva tarjeta del panel. Muéstrame solo los archivos que cambian.
**Que genero la IA:** TicketDao.kt, AyudaRepository.kt, AyudaRepositoryRoom.kt, PanelViewModel.kt y PanelScreen.kt. Agregó la consulta SQL agregada AVG en el DAO de Room, propagó la observación reactiva con Flow mediante el repositorio, formateó el tiempo en el ViewModel a un String legible e integró la tarjeta en el panel de soporte.
**Que tuve que corregir yo:** Ninguno, la implementación cumplió todas las especificaciones y se integró perfectamente.
**Respeta MVVM?:** Sí, la consulta SQL pertenece estrictamente al DAO de Room, el formateo y procesamiento lo realiza el ViewModel y la vista solo renderiza el texto formateado.

## Reflexion (5 lineas)
- La IA mejoró la velocidad al estructurar layouts en Jetpack Compose y consultas SQL en Room, pero no siempre resolvió importaciones implícitas.
- El error cometido fue omitir el import de un enum (`Rol`) en una de las pantallas generadas.
- Aprendí que MVVM con Room y Kotlin Flow permite que la UI se actualice automáticamente en tiempo real sin acoplar la lógica de negocio.
