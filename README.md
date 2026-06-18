# Sistema de Autogestión Estudiantil — Interfaz Gráfica (Swing)

Proyecto de la 2° Instancia Evaluativa: migración del Sistema de Autogestión
Estudiantil (consola, IE1) a una aplicación de escritorio con interfaz
gráfica en Java Swing, bajo arquitectura **MVC + DAO**.

## Integrantes y rol

| Integrante | Rol |
|---|---|
| Fontana Valentino | Controlador |
| Soria Mateo | Modelo |
| Otonello Paloma | Vista |
| Baldovino Simona | DAO |

> Recordar: todos los integrantes deben tener commits propios en el
> repositorio (la falta de commits individuales penaliza el 50% de la nota
> grupal de ese integrante).

## Instrucciones de ejecución

### Requisitos
- **JDK 17** o superior.
- **NetBeans** (se probó con NetBeans 29, pero cualquier versión moderna
  que soporte proyectos Ant/J2SE funciona).
- **MySQL Server** corriendo en `localhost:3306` (para el módulo JDBC de
  Estudiante — ver sección "Base de datos" más abajo).
- Conector JDBC de MySQL: `mysql-connector-j-9.7.0.jar`.
  **Importante:** por restricciones del entorno de generación, este `.jar`
  no viene incluido en el proyecto. Hay que descargarlo y colocarlo en
  `lib/mysql-connector-j-9.7.0.jar` (instrucciones detalladas en
  `lib/LEEME_DESCARGAR_CONECTOR.txt`).

### Pasos
1. Crear la base de datos ejecutando el script `sql/autogestion_db.sql` en
   MySQL (Workbench, consola `mysql`, o el cliente que prefieran).
2. Descargar el conector JDBC y colocarlo en `lib/` (ver punto anterior).
3. Abrir el proyecto en NetBeans: `Archivo > Abrir Proyecto` y seleccionar
   esta carpeta.
4. Verificar que `nbproject/project.properties` apunte correctamente al
   `.jar` del conector (ya viene configurado con una ruta relativa).
5. Si la base de datos no está en `localhost:3306` con usuario `root` sin
   contraseña, ajustar las constantes en `dao/ConexionDB.java`.
6. Ejecutar el proyecto (`Run > Run Project` o F6). La clase principal es
   `instanciaevaluativa1interfaz.Main`.
7. En la pantalla de login, ingresar uno de los legajos de ejemplo
   (`LEG001` o `LEG002`) o crear una cuenta nueva con "Registrarme".

## Arquitectura del proyecto

El proyecto sigue una separación estricta en 4 paquetes, tal como exige la
consigna:

```
src/
├── modelo/        → Estudiante, Materia, InscripcionMateria,
│                     Evaluable, Consultable, PersonaAcademica.
│                     Reutilizados de IE1 sin cambios estructurales.
│
├── dao/           → Acceso a datos. Una clase por entidad.
│                     - MateriaDAO            → texto plano (materias.txt)
│                     - InscripcionMateriaDAO  → texto plano
│                       (inscripciones_<legajo>.txt, uno por estudiante)
│                     - EstudianteDAOJDBC      → MySQL vía JDBC (BONUS)
│                     - ConexionDB             → maneja la conexión JDBC
│                     No conocen la Vista ni el Controlador.
│
├── controlador/   → ControladorAcademico. Recibe acciones de la Vista,
│                     aplica las validaciones del Modelo, llama a los DAO
│                     para persistir, y devuelve datos ya armados
│                     (Object[][], String[], etc.) listos para mostrar.
│                     No tiene NINGÚN import de javax.swing.
│
└── vista/         → LoginFrame y VentanaPrincipal (JFrame, NetBeans/Swing).
                      Los listeners de los botones solo llaman al
                      Controlador; no validan ni acceden a archivos.
```

### Por qué un estudiante "activo"

La consigna pide una app de **autogestión** (el alumno gestiona su propio
perfil), a diferencia de IE1 que manejaba listas globales de muchos
estudiantes a la vez. Por eso se agregó un mini "login" por legajo:
`ControladorAcademico.iniciarSesion(legajo)` busca al estudiante (vía JDBC)
y lo deja como `estudianteActivo` de la sesión. A partir de ahí, todas las
operaciones de materias/asistencias/notas trabajan sobre ese estudiante.

### Persistencia elegida: texto plano

Se eligió la opción de **texto plano** (no serialización) para Materia e
InscripcionMateria, reutilizando los métodos `toTexto()` / `fromTexto()` que
ya traían las clases del Modelo desde IE1.

Cada estudiante tiene su propio archivo de inscripciones
(`inscripciones_<legajo>.txt`), separado del catálogo global de materias
(`materias.txt`), para que la autogestión de cada alumno no choque con la
de otro.

## Componentes Swing utilizados

| Componente | Dónde se usa |
|---|---|
| `JLabel` | Perfil del estudiante, alertas de estado, títulos de sección |
| `JTable` | Tabla de materias inscriptas (código, condición, asistencia %, promedio) |
| `JList` | Lista de materias en riesgo (asistencia entre 75% y 85%) |
| `JButton` | Inscribir, dar de baja, marcar presente/ausente, registrar nota, buscar |
| `JMenuBar` | Menú "Archivo" (cerrar sesión) y "Reportes" (situación general, riesgo, aprobadas) |
| `JOptionPane` | Confirmación de baja, alerta de asistencia <75%, mensajes de éxito/error |
| `CardLayout` | Navegación entre el panel de Materias y el panel de Reportes |
| `JScrollPane` | Envolviendo la JTable y la JList |
| `BorderLayout` | Estructura principal de la ventana (perfil arriba, navegación a la izquierda, contenido al centro) |
| `GridLayout` / `FlowLayout` / `BoxLayout` | Formulario de inscripción, botones de acción y barra lateral, respectivamente |

## Base de datos (módulo BONUS — JDBC)

Solo la entidad **Estudiante** persiste en MySQL, como ítem bonus de la
consigna. El resto del sistema (Materia, InscripcionMateria) sigue en
archivos de texto, sin que esto afecte al Modelo, Controlador ni Vista.

Script de creación: `sql/autogestion_db.sql`. Crea la base
`autogestion_db`, la tabla `estudiantes`, y dos registros de ejemplo
(`LEG001`, `LEG002`) para poder probar el login sin tener que registrarse
primero.

## Desafíos encontrados y cómo se resolvieron

- _Completar con la experiencia real del grupo._ Algunos puntos que
  probablemente surjan al grabar el video explicativo:
  - Adaptar un sistema pensado para "muchos estudiantes" (consola, IE1) a
    una sesión de "un solo estudiante autogestionando su perfil".
  - Separar la persistencia de inscripciones por legajo para que cada
    alumno tenga su propio historial sin pisar el de otro.
  - Mantener el Controlador 100% libre de imports de Swing, devolviendo
    estructuras de datos simples (`Object[][]`, `String[]`) en vez de
    componentes gráficos.

## Diseño en Figma

_Completar con el link al archivo de Figma del grupo._

## Documentación de uso de IA

Fontana Valentino
https://gemini.google.com/share/d214a4d6e144
Soria Mateo
https://claude.ai/share/4988dda8-f64a-44d9-b456-fe438c1385ac
