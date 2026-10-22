# Paolicias Sweet - Aplicacion Movil Android

Aplicacion movil desarrollada en Kotlin y Jetpack Compose para la pasteleria y servicio de banquetes y catering Paolicias Sweet. Permite a los clientes explorar productos artesanales, cotizar eventos segun cantidad de asistentes, reservar fechas con persistencia local en Room, subir comprobantes de pago de abono mediante la galeria nativa, recibir notificaciones del estado de su solicitud y experimentar respuesta haptica mediante vibracion.

Este proyecto corresponde a la Evaluacion Parcial 2 de la asignatura Desarrollo de Aplicaciones Moviles (DSY1105) de Duoc UC.

---

## Integrantes del Equipo (Grupo 11)

* Integrantes:
  * Milton Espinoza
  * Felipe Zapata
  * Jeremi Macaya

* Institucion: Duoc UC  
* Asignatura: Desarrollo de Aplicaciones Moviles (DSY1105)  
* Docente: Eduardo Baeza  
* Repositorio GitHub: https://github.com/jeremi457/Paolicias.sweet_Grupo11.git  

---

## Funcionalidades y Composables Implementados (8+ Composables)

El proyecto cumple ampliamente con el requisito de construir un minimo de 6 composables funcionales:

1. ScreenHeroe: Pantalla de bienvenida y presentacion de la marca con identidad visual.
2. Login: Formulario de inicio de sesion con validacion de credenciales contra Room SQLite.
3. Register: Formulario de registro con validaciones de campos en tiempo real.
4. Principal: Menu principal con saludo dinamico al usuario, banner destacado y accesos directos.
5. CatalogScreen (Catalogo): Catalogo interactivo de tortas y cocteles con paso de parametros productoId.
6. ReservaScreen (Reserva): Cotizador dinamico con contador de asistentes (+ / -), calculo de total y abono del 50%.
7. ReservationsScreen (MisReservas): Historial reactivo de reservas desde Room con badges de estado (Pendiente de pago / Comprobante enviado).
8. UploadReceiptScreen (UploadReceipt): Pantalla de confirmacion y carga de foto del comprobante de abono.
9. Componentes Modulares Reutilizables:
   * BarraInferior: Navegacion compartida entre pantallas principales.
   * IconoError: Indicador visual de alerta circular rojo ante errores de validacion.
   * MensajeError: Mensajes de validacion animados con AnimatedVisibility.

---

## Persistencia de Datos Local (Room Database v2)

* UsuarioEntity / UsuarioDao: Gestion local de usuarios y autenticacion.
* ReservaEntity / ReservaDao: Registro de cotizaciones, montos calculados, fechas, estado y ruta de imagen del comprobante.
* Migracion Room v2: Singleton AppDatabase configurado con soporte para consultas asincronas con Coroutines (Dispatchers.IO).

---

## Recursos Nativos del Dispositivo (Android APIs)

La pauta exige un minimo de 2 recursos nativos. En este proyecto se integraron 3 recursos nativos:

1. Photo Picker / Galeria del Sistema (ActivityResultContracts.PickVisualMedia):  
   Permite al cliente seleccionar una foto de su comprobante bancario desde la galeria de forma moderna, segura y sin requerir permisos invasivos de almacenamiento.
2. Notificaciones del Sistema (NotificationManagerCompat):  
   Creacion de canal de notificacion Android (reservas_paolicias) y solicitud de permisos en tiempo de ejecucion para Android 13+ (POST_NOTIFICATIONS). Dispara alertas al confirmar reservas y al enviar comprobantes.
3. Vibracion Haptica del Dispositivo (Vibrator / VibrationEffect en Vibracion.kt):  
   Emision de respuesta tactil en el hardware del telefono al presionar botones clave y validar acciones.

---

## Arquitectura MVVM y Desacoplamiento

* Model: UsuarioEntity, ReservaEntity, ProductoCatalogo, ReservaModel, Tarifario.
* ViewModel: LoginViewModel, RegisterViewModel, ReservaViewModel, ReservasViewModel. Toda la logica de calculo financiero y las validaciones de negocio (fechas validas no anteriores a hoy, telefono de exactamente 9 digitos, notas) se encuentran 100% aisladas de los Composables.
* View: Composables reactivos que observan el uiState inmutable.

---

## Diseno Visual Material 3 y Animaciones

* Diseno consistente basado en Material Design 3 con paleta corporativa (#4C5BF5, fondo #F5F6FA).
* Animaciones de navegacion: Transiciones suaves en NavHost (slideInHorizontally + fadeIn / slideOutHorizontally + fadeOut).
* Micro-interacciones: AnimatedVisibility para mostrar/ocultar suavemente los mensajes de error y la previsualizacion de la foto seleccionada.

---

## Tecnologias y Librerias

* Lenguaje: Kotlin 2.0+
* Framework UI: Jetpack Compose (BOM)
* Diseno: Material Design 3
* Navegacion: Jetpack Navigation Compose (2.8.0)
* Base de Datos: Room Database (2.8.4) + KSP + SQLite
* Asincronia: Kotlin Coroutines + StateFlow / mutableStateOf
* Min SDK: 23 (Android 6.0 Marshmallow)
* Target / Compile SDK: 34 / 36

---

## Pasos para Ejecutar el Proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/jeremi457/Paolicias.sweet_Grupo11.git
   ```
2. Abrir en Android Studio:
   * Abrir Android Studio (Ladybug, Koala o version reciente).
   * Seleccionar File > Open... y escoger la carpeta raiz del proyecto.
3. Sincronizar Gradle:
   * Aguardar la sincronizacion automatica de dependencias (Gradle Sync).
4. Ejecutar en Emulador o Dispositivo Fisico:
   * Seleccionar un emulador Android (API 26 o superior, recomendado API 33+ para notificaciones).
   * Presionar Run 'app' (Shift + F10).
5. Flujo de Prueba Sugerido:
   * Registrarse o iniciar sesion con un usuario.
   * Entrar al Catalogo, elegir una torta o servicio de catering y pulsar en cotizar.
   * Modificar asistentes con + / -, verificar calculo dinamico de total y abono del 50%.
   * Ingresar telefono de 9 digitos y confirmar (se emitira notificacion local nativa).
   * Enviar foto del comprobante desde la galeria (Photo Picker).
   * Consultar en Mis reservas el estado actualizado del pedido.

---

## Gestion Colaborativa y Git Flow

* Estrategia de Ramas:
  * master: Rama principal con codigo estable y versiones de entrega.
  * Ramas de caracteristicas por integrante: Milton-duoc, felipe-duoc, jeremi-duoc.
  * Integracion mediante Pull Requests validados.
* Planificacion Agil: Tablero organizado en Trello con Epicas, Historias de Usuario, Criterios de Aceptacion (Checklists) y seguimiento del flujo de trabajo.
