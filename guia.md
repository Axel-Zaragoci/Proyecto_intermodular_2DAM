# PROYECTO INTERMODULAR
## REQUISITOS
- Nombre del proyecto: Sistema de Gestión de Reservas para Hotel Pere María
- Requisitos técnicos:
    - Interfaz intuitiva
    - Gestión integral de habitaciones con seguimineto en tiempo real de disponibilidad y categorización de habitaciones
    - Sistema de reservas online con app móvil para reservas, app desktop para gestión y confirmación por email
    - Calendario de ocupación
    - Gestión de perfiles de cliente con historial de reservas
    - Sistema de comentarios y valoraciones
    - Notificaciones automáticas de reservas y alertas al personal sobre cambios de reservas

## OBJETIVOS MÍNIMOS
- Tipo de usuarios:
    - Administrador
    - Empleado
    - Cliente

### APP DESKTOP
- Usuario administrador
    - Crear empleados
    - Iniciar sesión
- Usuario empleado
    - Insertar información de habitaciones
    - Gestionar reservas

### APP MOVIL
- Usuario cliente
    - Registrar // iniciar sesión
    - Reservar habitación
    - Ver historial de reservas
    - Modificar información personal

### API REST
- Operaciones CRUD
- Middleware de validación de datos de entrada 
- Realizar en Node.js y Express

## DOCUMENTACIÓN

### BASES DE DATOS
- Cada alumno debe entregar un pdf donde se detalla la creación y evolución de la bd. Cada alumno se centra en su colección y explicar el trabajo conjunto del grupo
- Se debe incluir enlace al proyecto github

### ACCESO A DATOS
- Diagrama entidad-relación
- Modelo relacional
- Apartados:
    - Funcionalidad básica -> hablarlo
    - Diagrama entidad relación
    - Modelo relacional
    - Colecciones y campos
    - Archivos de validación
    - Bd en mongo atlas

### INTERFACES Y ANDROID
- Detallar cada función de la API
- Explicar Middlewares
- Mostrar pruebas de la API mediante ThunderClient
- Indicar división de trabajo
- Detallar ampliaciones

## FUNCIONALIDADES POR ASIGNATURA
### INTERFACES
- Usuario administrador:
    - Crear empleados
    - Iniciar sesión
- Usuario empleado:
    - Insertar información de habitaciones
    - Gestionar reservas
- Hasheo de contraseñas
- Funcionalidad por HTTPS

### ANDROID
- Usuario cliente
    - Registro // login
    - Reservar habitación
    - Ver historial de reserva
    - Modificar información personal

### PSP
- Usuario administrador
    - Inicio y fin de sesión mediante JWT
    - Gestión de empleados:
        - Crear empleado -> POST
        - Consultar empleados -> GET
        - Modificar empleados -> PATCH
        - Borrar empleados -> DELETE
- Usuario empleado
    - Inicio y fin de sesión mediante JWT
    - Gestión de habitaciones:
        - Crear habitacion -> POST
        - Consultar habitacion -> GET
        - Modificar habitacion -> PATCH
        - Borrar habitacion -> DELETE -> solo admin
    - Gestión de reservas:
        - Crear reserva -> POST
        - Consultar reserva -> GET
        - Modificar reserva -> PATCH
        - Borrar reserva -> DELETE -> solo admin
- Usuario cliente
    - Registrarse como cliente -> POST
    - Modificar información personal -> PATCH
    - Iniciar y cerrar sesión mediante JWT
    - Gestión de reservas
        - Reservar habitación -> POST
        - Modificar reserva -> PATCH
        - Cancelar reserva -> DELETE / PATCH
        - Consultar historial de reservas -> GET

## MÓDULOS PRINCIPALES DEL PROYECTO
### MÓVIL
#### USUARIOS
##### REGISTRO
Datos a ingresar:
- Nombre y apellidos -> obligatorio
- Email -> obligatorio
- Contraseña -> obligatorio y debe pedirla dos veces y confirmar que son iguales
- DNI -> obligatorio
- Fecha de nacimiento -> obligatorio
- Ciudad
- Sexo -> obligatorio y con 3 opciones:
    - Hombre
    - Mujer
    - Prefiero no decirlo
- Añadir imagen de perfil -> opcional

Validaciones:
- Email con formato válido
- Contraseña de al menos 8 caracteres. Debe incluir:
    - mayúsculas
    - minúsculas
    - números
    - caracteres especiales (&%$ etc.)
- DNI con 8 números seguidos de una letra A-Z siendo Ñ inválida

##### LOGIN
Formulario de inicio de sesión con:
- Email
- Contraseña

Validaciones: 
- Si es correcto dar acceso a la app
- Si es incorrecto indicar credenciales incorrectas sin indicar cual falla

##### CONFIGURACIÓN
Ventana de configuración del cliente
Funcionalidad:
- Modificar los datos personales
- Elegir entre tema claro y tema oscuro

##### VER RESERVAS
Ver historial de reservas

#### HABITACIONES
Deben de poder ver todas las habitaciones y acceder a ellas para ver imágenes y los servicios que ofrecen. Además tienen que poder filtrar la lista de habitaciones
Filtros:
- Por rango de precio
- Por tipo
- Por servicios
- Ofertas
En cada habitación:
- Carrusel de imágenes de la habitación
- Ver servicios que tiene

#### RESERVAS
El usuario debe seleccionar las fechas y el número de personas que desean hospedarse y la app debe devolver las habitaciones disponibles para esas fechas, notificar al usuario del caso de que no haya disponibilidad o el caso de que debe adquirir más de una habitación para el número seleccionado de personas
Funcionalidades:
- Mostrar las habitaciones disponibles para las fechas seleccionadas
- Notificar si no hay habitaciones disponibles
- El número de personas que van a hospedarse no puede ser superior al límite de la capacidad de una habitación. El usuario está obligado a adquirir más de una habitación
- Debe haber conformidad con la reserva. La app llevará al usuario a una pasarela de pago ficticia y tras introducir los datos se anuncia la reserva como realizada


### GESTIONES DE LOS MÓDULOS
#### USUARIOS
##### CREAR USUARIOS
Permite gestionar usuarios, incluyendo añadir, editar y buscar usuarios en el sistema
Formulario de añadir usuario:
- Nombre y apellidos
- Rol del usuario (ComboBox)
- Email
- Contraseña
- Fecha de nacimiento (DatePicker)
- Ciudad (ComboBox)
- Sexo (RadioButton) -> H / M / Indeterminado
- Imagen de perfil (subida de archivo)
- Cliente VIP -> ToggleButton

Validaciones:
- El email debe tener formato válido
- DNI debe cumplir con el formato nacional
- Validación que todos los campos no pueden estar vacíos
- Validación de imágenes para perfil de empleado y administrador
##### BUSCADOR Y EDICIÓN DE USUARIOS
Permite buscar, añadir y editar usuarios
Formulario de añadir usuario:
- Nombre y apellidos
- Rol del usuario (ComboBox)
- Email
- Contraseña
- Fecha de nacimiento (DatePicker)
- Ciudad (ComboBox)
- Sexo (RadioButton) -> H / M / Indeterminado
- Imagen de perfil (subida de archivo)
- Cliente VIP -> ToggleButton
- Mostrar los datos del usuario
##### ACCESO Y LOGIN
Campos:
- Email
- Contraseña
- Botón de acceso con validación de credenciales
- Opción de recordar contraseña
Validaciones:
- Email debe ser válido
- El campo contraseña no puede estar vacío

#### HABITACIONES
##### AÑADIR HABITACIÓN
Permite añadir y editar información de las habitaciones
Formulario de añadir habitación:
- Nombre de la habitación
- Número de huéspedes (ComboBox)
- Descripción (Campo de texto multilineal)
- Imagen princial (Subida de archivo)
- Precio por noche (campo numérico)
- Oferta
- Opciones adicionales:
    - Cuna (CheckBox)
    - Cama extra (CheckBox)
- Botón para enviar los datos
Validaciones:
- Nombre y descripción no deben estar vacíos
- El número de huéspedes y precio deben ser positivos
- Validación de subida de archivos para imágenes

##### BUSCADOR DE HABITACIONES
Permite filtrar por diversos criterios y visualizar información de habitaciones disponibles en el hotel y la opción de editar y añadir habitación
Formulario de búsqueda de habitación:
- Nombre de la habitación
- Número de huéspedes (ComboBox)
- Descripción (Campo de texto multilineal)
- Imagen princial (Subida de archivo)
- Precio por noche (campo numérico)
- Oferta
- Opciones adicionales:
    - Cuna (CheckBox)
    - Cama extra (CheckBox)
- Reservada/ocupada (ComboBox para seleccionar Si o No)
- Fecha de reserva
- Botón de búsqueda
- Mostrar los datos de la habitación

##### EDITAR HABITACIONES
Permite editar y modificar la información de las habitaciones del hotel
Campos de edición:
- Nombre de la habitación
- Número de huéspedes (ComboBox)
- Descripción (Campo de texto multilineal)
- Imagen princial (Subida de archivo)
- Precio por noche (campo numérico)
- Oferta
- Opciones adicionales:
    - Cuna (CheckBox)
    - Cama extra (CheckBox)
- Reservada/ocupada (No editable)
- Fecha de reserva (No editable)
- Botón de modificación de datos

#### RESERVAS
##### CREAR UNA RESERVA
Permite crear una reserva a partir de una habitación seleccionada disponible
Formulario de reserva:
- Información de la habitación (nombre e imagen)
- Fecha de entrada y salida
- Precio total calculado automáticamente
- Selección del cliente (ComboBox con lista de usuarios)
Validaciones:
- Las fechas deben ser coherentes
- La habitación debe estar disponible
- Los huespedes no deben exceder la capacidad de la habitación

##### BUSCADOR DE DISPONIBILIDAD DE RESERVAS
Permite filtrar habitaciones por diversos criterios
Campos del buscador:
- Número de huéspedes (ComboBox)
- Fecha de entrada y Fecha de salida (DatePicker)
- Miembro VIP (RadioButton)
- Extras:
    - Cuna (CheckBox)
    - Cama extra (CheckBox)
- Precio por noche (Slider de rango 0 y 1000)
- Botones de Ofertas y Buscar
Resultados:
- Las habitaciones se muestran en una vista con
    - Imagen de la habitación
    - Nombre de la habitación
    - Precio 
    - Botón para reservar

##### LISTADOS DE RESERVAS
Permite visualizar y gestionar las reservas existentes
Lista de reservas:
- ID
- Nombre del cliente
- Empleado
- Rol
- Estado VIP
- Acciones (editar o eliminar)
Resultados:
- Información de la habitación (nombre e imagen)
- Fecha de entrada y salida
- Número de huéspedes
- Precio total calculado automáticamente
- Selección del empleado/cliente

## RESUMEN GENERAL
### CASOS DE PRUEBA IMPORTANTES
- Prueba de login: Ingreso con credenciales válidas e inválidas
- Añadir habitación: Validar campos vacíos y límite de huéspedes/precios
- Crear reserva: Probar fechas no válidas y exceso de capacidad
- Buscar habitaciones: Filtrar con distintos criterios y validar resultados
- Gestionar usuarios: Añadir, editar y eliminar un usuario comprobando restricciones

## PRESENTACIÓN ITERMODULAR GRUPAL 
- Introducción (5 minutos)
    - Cada integrante se presenta brevemente y menciona su rol en el desarrollo de la aplicación
    - Explicar brevemente el problema que resuelve la aplicación, sus funcionalidades principales y peculiaridades
- Contenido extra (5 minutos)
    - Cada grupo presenta el contenido extra que ha desarrollado como, por ejemplo informes, cancelaciones o reservas con extras, realización de facturas, funcionalidad log para control y registro de acciones...
- Desarrollo de la app (20 minutos)
    - Cada integrante explica en detalle la parte de la app que ha desarrollado
    - Realizar una breve demostración de la app funcionando correctamente:
        - Demostrar que se puede registrar un cliente, realizar una reserva y que salgan las habitaciones o el tipo de habitación disponible en el rango de fecha seleccionado
        - Demostrar que el cliente puede ver las reservas pendientes y las ya finalizadas, asi como modificar sus datos personales
        - Demostrar que estando logueado el cliente no puede acceder a la ficha de otro cliente (bien para modificar o ver reservas)
        - Demostrar que en la app desktop hay dos usuarios, admin y empleado con funciones distintas. El admin lo puede hacer todo, el empleado no puede realizar las funciones más críticas
        - Demostrar que el empleado puede ver las reservas de un determinado rango de fechas y puede gestionar las reservas
- Recomendado utilizar diapositivas para apoyar la presentación
- Evaluación
    - Claridad y organización de la presentación
    - Dominio del tema y conocimiento técnico
    - Capacidad para trabajar en equipo
    - Creatividad e innovación en el desarrollo de la aplicación
    - Eficacia de la demostración
    - Calidad del contenido extra