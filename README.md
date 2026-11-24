# Paolicias.Sweet · Aplicación móvil Android

Aplicación móvil en **Kotlin y Jetpack Compose** para **Paolicias.Sweet**, un servicio de pastelería, banquetería y catering. Los clientes exploran el catálogo, cotizan un evento según la cantidad de asistentes, reservan una fecha y suben el comprobante de pago del abono. La administradora gestiona los productos y valida los pagos.

La app se conecta mediante **Retrofit** a cuatro **microservicios Spring Boot** desplegados en **Render**, con base de datos **PostgreSQL en Supabase**. En el dispositivo usa **Room (SQLite)** para los clientes registrados y la sesión activa.

Proyecto de la asignatura **Desarrollo de Aplicaciones Móviles (DSY1105)** · Duoc UC · Experiencia 3 · **Grupo 11**.

| | |
|---|---|
| Docente | Eduardo Baeza |
| Integrantes | Milton Espinoza · Felipe Zapata · Jeremi Macaya |
| Repositorio de la app | https://github.com/jeremi457/Paolicias.sweet_Grupo11 |
| Repositorio del backend | `<URL-del-repositorio-backend>` |

---

## 1. Roles y casos de uso

La app implementa **dos roles** con navegación y pantallas propias.

![Diagrama de casos de uso](docs/diagrama-casos-uso.png)

| Rol | Qué puede hacer |
|---|---|
| **Cliente** | Registrarse, iniciar sesión, ver productos, reservar (con observaciones opcionales), pagar la reserva subiendo el comprobante y ver sus reservas con su estado. |
| **Administrador** | Iniciar sesión con credencial de administrador, crear y editar productos, activarlos o desactivarlos, subir la foto de cada producto, ver todas las reservas, revisar comprobantes y aprobar o rechazar pagos. |

---

## 2. Arquitectura de la aplicación (MVVM)

![Arquitectura de la app](docs/arquitectura-android.png)

La app sigue el patrón **MVVM** con capas separadas:

| Capa | Paquete | Responsabilidad |
|---|---|---|
| **View** | `ui/screen`, `ui/components`, `ui/navigation` | Composables que muestran el estado y reenvían eventos. No contienen reglas de negocio. |
| **ViewModel** | `ui/viewmodel` | Estado de cada pantalla (`mutableStateOf`), validaciones de formularios y llamadas a los repositorios con corrutinas. |
| **Data** | `data/repository`, `data/session` | Repositorios que envuelven las llamadas a la API y manejan errores (`Result`); `SesionManager` guarda la sesión. |
| **Remoto** | `data/remote` | Interfaces Retrofit, `RetrofitFactory`, `ApiConfig` y mapeo de errores de red. |
| **Local** | `data/database`, `data/local` | Base Room: entidades y DAOs de usuarios y sesión. |
| **Model** | `model` | Modelos de formularios, `Tarifario` (cálculo de total y abono) y estados de reserva. |

```
app/src/main/java/com/example/myappnavigation/
├── MainActivity.kt
├── data/
│   ├── database/AppDatabase.kt          Room (versión 7)
│   ├── local/dao · local/entity         UsuarioDao, SesionDao, UsuarioEntity, SesionEntity
│   ├── remote/                          ApiConfig, RetrofitFactory, AuthApi, CatalogoApi,
│   │                                    ReservasApi, PagosApi, ErroresRed
│   ├── repository/                      Auth, Catalogo, Reservas y Pagos
│   └── session/SesionManager.kt         SesionActual (memoria) + persistencia en Room
├── model/                               Tarifario, EstadoReserva, modelos de formularios
├── ui/
│   ├── navigation/AppNavigation.kt      NavHost y rutas por rol
│   ├── screen/                          Pantallas Compose
│   ├── viewmodel/                       ViewModels
│   ├── components/                      Header, Footer, BarraInferior, ImagenProducto, IconoError
│   └── theme/                           Material 3
└── util/                                FechaUtil, ImagenUtil (compresión de imágenes)
```

---

## 3. Pantallas y navegación por rol

`AppNavigation` restaura la sesión guardada al abrir la app y decide la pantalla inicial: el administrador entra directo a su panel, el cliente al menú principal y quien no tiene sesión, a la bienvenida.

| Pantalla | Rol | Función |
|---|---|---|
| `ScreenHeroe` | Sin sesión | Bienvenida y presentación de la marca. |
| `Login` / `Register` | Cliente | Inicio de sesión y registro de clientes. |
| `LoginAdmin` | Administrador | Inicio de sesión contra `auth-service`. |
| `Principal` | Cliente | Menú principal con accesos directos y cierre de sesión. |
| `CatalogScreen` | Cliente | Catálogo de productos activos. |
| `ReservaScreen` | Cliente | Cotizador: fecha, horario, asistentes (+ / −), teléfono, observaciones, total y abono del 50 %. |
| `UploadReceiptScreen` | Cliente | Selección y envío de la foto del comprobante. |
| `ReservationsScreen` | Cliente | Historial de reservas con su estado. |
| `AdminProductosScreen` | Administrador | Lista de productos, activar o desactivar, ir al formulario. |
| `AdminProductoFormScreen` | Administrador | Crear o editar un producto y subir su foto. |
| `AdminReservasScreen` | Administrador | Todas las reservas; ver el comprobante y aprobar o rechazar el pago. |

Las transiciones usan animaciones de `NavHost` (`slideIn` + `fadeIn`) y los mensajes de error aparecen con `AnimatedVisibility`.

---

## 4. Autenticación y persistencia local

La gestión de autenticación se apoya en **Room (SQLite)** para ambos roles:

- **Clientes:** se registran y validan sus credenciales contra la tabla `users` de Room.
- **Administrador:** valida sus credenciales en `auth-service`, que devuelve un **JWT** y el rol.
- **Sesión:** en ambos casos se guarda en la tabla `sesion` (rol, nombre, correo y token). `SesionActual` mantiene una copia en memoria que lee el interceptor de Retrofit.
- **Cerrar sesión** borra la copia en memoria y la fila de Room.

| Tabla Room | Contenido |
|---|---|
| `users` | `id`, `name`, `email`, `password` |
| `sesion` | `id`, `rol`, `nombre`, `email`, `token` |

---

## 5. Conexión con los microservicios

Las URLs se definen en `data/remote/ApiConfig.kt`. Con `USE_RENDER = true` la app usa los servicios desplegados; con `false` usa el backend local desde el emulador (`10.0.2.2`).

| Servicio | Local (emulador) | Interfaz Retrofit |
|---|---|---|
| auth-service | `http://10.0.2.2:8081/` | `AuthApi` |
| catalogo-service | `http://10.0.2.2:8082/` | `CatalogoApi` |
| reservas-service | `http://10.0.2.2:8083/` | `ReservasApi` |
| pagos-service | `http://10.0.2.2:8084/` | `PagosApi` |

- `RetrofitFactory` crea un único cliente OkHttp con conversor Gson, registro de peticiones (`HttpLoggingInterceptor`) y un interceptor que agrega `Authorization: Bearer <token>` a todas las llamadas, excepto login y registro.
- Los *timeouts* son de **120 segundos**, porque en el plan gratuito de Render el primer acceso tras un rato de inactividad puede demorar.
- `llamarApi` convierte excepciones HTTP y de red en mensajes claros para el usuario, usando el campo `message` de la respuesta del servidor cuando existe.
- Las fotos de productos se descargan desde `GET /productos/{id}/imagen`; los productos sin foto propia usan imágenes incluidas en `res/drawable`.

Para el detalle de endpoints, reglas de negocio y despliegue, revisa el README del repositorio del backend.

---

## 6. Recursos nativos del dispositivo

| Recurso | Cliente | Administrador |
|---|---|---|
| **Photo Picker** (`ActivityResultContracts.PickVisualMedia`) | Elige la foto del comprobante de pago. | Elige la foto de cada producto. |
| **Vibración** (`Vibrator` + `VibrationEffect`, permiso `VIBRATE`) | Vibra al guardar la reserva. | Vibra al aprobar o rechazar un pago. |

El Photo Picker no requiere permisos de almacenamiento. Antes de enviar una imagen, `ImagenUtil` la reduce a un máximo de 1280 px de ancho y la comprime como JPEG, para respetar el límite de 5 MB del backend.

---

## 7. Validación de formularios

| Formulario | Validaciones |
|---|---|
| Registro | Campos obligatorios, correo válido, contraseña de al menos 8 caracteres, confirmación igual a la contraseña, correo no repetido. |
| Login cliente y administrador | Correo y contraseña obligatorios; error si las credenciales no coinciden. |
| Reserva | Fecha en formato `dd/mm/aaaa`, existente en el calendario y no anterior a hoy, horario elegido, asistentes de 10 en 10 hasta 100, teléfono de 9 dígitos, observaciones de máximo 200 caracteres. |
| Producto (admin) | Nombre y precio obligatorios (el precio solo admite dígitos) y foto del producto elegida desde la galería. |

El servidor repite estas reglas, así que los datos quedan validados en ambos lados.

---

## 8. Tecnologías y librerías

| Área | Tecnología |
|---|---|
| Lenguaje | Kotlin 2.2.10 |
| UI | Jetpack Compose (BOM 2026.02.01) y Material Design 3 |
| Navegación | Navigation Compose 2.8.0 |
| Persistencia local | Room 2.8.4 con KSP |
| Red | Retrofit 2.11.0, conversor Gson y OkHttp Logging Interceptor 4.12.0 |
| Asincronía | Corrutinas de Kotlin |
| Build | Android Gradle Plugin 9.3.3 |
| SDK | `minSdk` 23 · `targetSdk` y `compileSdk` 36 |

---

## 9. Cómo ejecutar el proyecto

1. Clona el repositorio:

   ```bash
   git clone https://github.com/jeremi457/Paolicias.sweet_Grupo11.git
   ```

2. Abre la carpeta en **Android Studio** (versión reciente) y espera la sincronización de Gradle.
3. Elige a qué backend conectarte en `data/remote/ApiConfig.kt`:
   - `USE_RENDER = true`: usa los microservicios desplegados en Render. No necesitas levantar nada.
   - `USE_RENDER = false`: usa el backend local. Levántalo antes con `docker compose up --build` (ver README del backend).
4. Ejecuta **Run 'app'** en un emulador o dispositivo con Android 6.0 (API 23) o superior.
5. Con Render, la primera petición puede tardar hasta que el servicio despierte. La app espera hasta 120 segundos.

### Flujo de prueba sugerido

1. **Cliente:** regístrate, inicia sesión y entra al catálogo.
2. Elige un producto, cambia la cantidad de asistentes y revisa el total y el abono del 50 %.
3. Completa fecha, horario y teléfono, y confirma la reserva (el teléfono vibra).
4. Sube la foto del comprobante desde la galería.
5. En **Mis reservas**, comprueba el estado `Comprobante enviado`.
6. **Administrador:** cierra sesión, entra con la credencial de administrador, abre las reservas, revisa el comprobante y aprueba el pago.
7. Vuelve al cliente y verifica que la reserva quedó `Confirmada`.

---

## 10. Pruebas

El módulo incluye las pruebas de ejemplo de Android Studio (`ExampleUnitTest` y `ExampleInstrumentedTest`). Las pruebas unitarias del backend se describen en el README del backend.

```bash
./gradlew test
```

---

## 11. Gestión colaborativa

- **Ramas:** `master` con el código estable, ramas por integrante (`Milton-duoc`, `felipe-duoc`, `jeremi-duoc`) y *Pull Requests* para integrar.
- **Rama `documents`:** contiene los diagramas de arquitectura de microservicios y de arquitectura del proyecto (carpeta `docs/`).
- **Planificación:** tablero en Trello con épicas, historias de usuario y criterios de aceptación.
