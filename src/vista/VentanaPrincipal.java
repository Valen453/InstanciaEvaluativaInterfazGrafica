package vista;

import controlador.ControladorAcademico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final ControladorAcademico controlador;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelCentral = new JPanel();

    private JTable tablaMaterias;
    private DefaultTableModel modeloTabla;
    private JList<String> listaRiesgo;
    private DefaultListModel<String> modeloListaRiesgo;
    private JLabel lblEstadoVacio;

    private JTextField txtCodigo;
    private JTextField txtNombreMateria;
    private JComboBox<Integer> cmbCuatrimestre;
    private JTextField txtAnio;

    private JTextArea areaReporte;

    private JLabel lblPerfil;
    private JLabel lblAlerta;

    public VentanaPrincipal(ControladorAcademico controlador) {
        this.controlador = controlador;
        initComponents();
        refrescarTodo();
    }

    private void initComponents() {
        setTitle("Sistema de Autogestión Estudiantil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 620);
        setLocationRelativeTo(null);

        setJMenuBar(crearMenuBar());

        setLayout(new BorderLayout(8, 8));

        add(crearPanelPerfil(), BorderLayout.NORTH);
        add(crearBarraLateral(), BorderLayout.WEST);

        panelCentral.setLayout(cardLayout);
        panelCentral.add(crearPanelMaterias(), "MATERIAS");
        panelCentral.add(crearPanelReportes(), "REPORTES");
        add(panelCentral, BorderLayout.CENTER);

        cardLayout.show(panelCentral, "MATERIAS");
    }

    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemCerrar = new JMenuItem("Cerrar sesión");
        itemCerrar.addActionListener(e -> cerrarSesion());
        menuArchivo.add(itemCerrar);

        JMenu menuReportes = new JMenu("Reportes");
        JMenuItem itemSituacionGeneral = new JMenuItem("Situación general");
        JMenuItem itemEnRiesgo = new JMenuItem("Materias en riesgo");
        JMenuItem itemAprobadas = new JMenuItem("Materias aprobadas");

        itemSituacionGeneral.addActionListener(e -> mostrarReporte(controlador.generarReporteSituacionGeneral()));
        itemEnRiesgo.addActionListener(e -> mostrarReporte(controlador.generarReporteMateriasEnRiesgo()));
        itemAprobadas.addActionListener(e -> mostrarReporte(controlador.generarReporteMateriasAprobadas()));

        menuReportes.add(itemSituacionGeneral);
        menuReportes.add(itemEnRiesgo);
        menuReportes.add(itemAprobadas);

        menuBar.add(menuArchivo);
        menuBar.add(menuReportes);
        return menuBar;
    }

    private JPanel crearPanelPerfil() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        lblPerfil = new JLabel();
        lblPerfil.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(lblPerfil, BorderLayout.WEST);

        lblAlerta = new JLabel(" ");
        lblAlerta.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblAlerta.setForeground(new Color(180, 0, 0));
        panel.add(lblAlerta, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearBarraLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        panel.setPreferredSize(new Dimension(160, 0));

        JButton btnMaterias = new JButton("Mis Materias");
        JButton btnReportes = new JButton("Reportes");

        btnMaterias.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReportes.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMaterias.setMaximumSize(new Dimension(140, 35));
        btnReportes.setMaximumSize(new Dimension(140, 35));

        btnMaterias.addActionListener(e -> cardLayout.show(panelCentral, "MATERIAS"));
        btnReportes.addActionListener(e -> {
            areaReporte.setText(controlador.generarReporteSituacionGeneral());
            cardLayout.show(panelCentral, "REPORTES");
        });

        panel.add(btnMaterias);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnReportes);

        return panel;
    }

    private JPanel crearPanelMaterias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"Código", "Materia", "Condición", "Asistencia %", "Promedio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tablaMaterias = new JTable(modeloTabla);
        tablaMaterias.setRowHeight(24);
        JScrollPane scrollTabla = new JScrollPane(tablaMaterias);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Materias inscriptas"));

        lblEstadoVacio = new JLabel("Todavía no estás inscripto en ninguna materia.", SwingConstants.CENTER);
        lblEstadoVacio.setForeground(Color.GRAY);
        lblEstadoVacio.setVisible(false);

        JPanel panelTablaConEstado = new JPanel(new BorderLayout());
        panelTablaConEstado.add(scrollTabla, BorderLayout.CENTER);
        panelTablaConEstado.add(lblEstadoVacio, BorderLayout.SOUTH);

        modeloListaRiesgo = new DefaultListModel<>();
        listaRiesgo = new JList<>(modeloListaRiesgo);
        JScrollPane scrollLista = new JScrollPane(listaRiesgo);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Materias en riesgo (75%-85% asistencia)"));
        scrollLista.setPreferredSize(new Dimension(260, 0));

        JPanel panelCentroTabla = new JPanel(new BorderLayout(10, 0));
        panelCentroTabla.add(panelTablaConEstado, BorderLayout.CENTER);
        panelCentroTabla.add(scrollLista, BorderLayout.EAST);

        panel.add(panelCentroTabla, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(0, 8));

        JPanel panelForm = new JPanel(new GridLayout(2, 4, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Inscribir nueva materia"));

        panelForm.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panelForm.add(txtCodigo);

        panelForm.add(new JLabel("Nombre:"));
        txtNombreMateria = new JTextField();
        panelForm.add(txtNombreMateria);

        panelForm.add(new JLabel("Cuatrimestre:"));
        cmbCuatrimestre = new JComboBox<>(new Integer[]{1, 2});
        panelForm.add(cmbCuatrimestre);

        panelForm.add(new JLabel("Año:"));
        txtAnio = new JTextField();
        panelForm.add(txtAnio);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnInscribir = new JButton("Inscribir materia");
        JButton btnDarBaja = new JButton("Dar de baja");
        JButton btnAsistenciaPresente = new JButton("Marcar presente");
        JButton btnAsistenciaAusente = new JButton("Marcar ausente");
        JButton btnRegistrarNota = new JButton("Registrar nota");
        JButton btnBuscar = new JButton("Buscar materia");

        btnInscribir.addActionListener(e -> inscribirMateria());
        btnDarBaja.addActionListener(e -> darDeBaja());
        btnAsistenciaPresente.addActionListener(e -> registrarAsistencia(true));
        btnAsistenciaAusente.addActionListener(e -> registrarAsistencia(false));
        btnRegistrarNota.addActionListener(e -> registrarNota());
        btnBuscar.addActionListener(e -> buscarMateria());

        panelBotones.add(btnInscribir);
        panelBotones.add(btnDarBaja);
        panelBotones.add(btnAsistenciaPresente);
        panelBotones.add(btnAsistenciaAusente);
        panelBotones.add(btnRegistrarNota);
        panelBotones.add(btnBuscar);

        panelInferior.add(panelForm, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Reportes académicos", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.NORTH);

        areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaReporte);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnGeneral = new JButton("Situación general");
        JButton btnRiesgo = new JButton("Materias en riesgo");
        JButton btnAprobadas = new JButton("Materias aprobadas");

        btnGeneral.addActionListener(e -> areaReporte.setText(controlador.generarReporteSituacionGeneral()));
        btnRiesgo.addActionListener(e -> areaReporte.setText(controlador.generarReporteMateriasEnRiesgo()));
        btnAprobadas.addActionListener(e -> areaReporte.setText(controlador.generarReporteMateriasAprobadas()));

        panelBotones.add(btnGeneral);
        panelBotones.add(btnRiesgo);
        panelBotones.add(btnAprobadas);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void inscribirMateria() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombreMateria.getText().trim();
            int cuatrimestre = (Integer) cmbCuatrimestre.getSelectedItem();
            int anio = Integer.parseInt(txtAnio.getText().trim());

            controlador.procesarInscripcion(codigo, nombre, cuatrimestre, anio);

            JOptionPane.showMessageDialog(this,
                    "Inscripción realizada con éxito.",
                    "Inscripción exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

            limpiarFormularioMateria();
            refrescarTodo();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El año debe ser un número entero.",
                    "Dato inválido",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void darDeBaja() {
        String codigo = obtenerCodigoSeleccionado();
        if (codigo == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná una materia de la tabla primero.",
                    "Ninguna materia seleccionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que querés dar de baja la materia seleccionada?",
                "Confirmar baja",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controlador.darDeBajaActivo(codigo);
            JOptionPane.showMessageDialog(this,
                    "Materia dada de baja correctamente.",
                    "Baja exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            refrescarTodo();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarAsistencia(boolean presente) {
        String codigo = obtenerCodigoSeleccionado();
        if (codigo == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná una materia de la tabla primero.",
                    "Ninguna materia seleccionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controlador.registrarAsistenciaActivo(codigo, presente);

            if (controlador.asistenciaEnRiesgo(codigo)) {
                JOptionPane.showMessageDialog(this,
                        "Atención: la asistencia en esta materia bajó del 75%.",
                        "Alerta de asistencia",
                        JOptionPane.WARNING_MESSAGE);
            }

            refrescarTodo();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNota() {
        String codigo = obtenerCodigoSeleccionado();
        if (codigo == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná una materia de la tabla primero.",
                    "Ninguna materia seleccionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Ingresá la nota (0 a 10):");
        if (input == null) {
            return; 
        }

        try {
            double nota = Double.parseDouble(input.trim());
            controlador.registrarNotaActivo(codigo, nota);

            JOptionPane.showMessageDialog(this,
                    "Nota registrada correctamente.",
                    "Nota registrada",
                    JOptionPane.INFORMATION_MESSAGE);

            refrescarTodo();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingresá un número válido para la nota.",
                    "Dato inválido",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarMateria() {
        String texto = JOptionPane.showInputDialog(this, "Buscar por código o nombre:");
        if (texto == null || texto.trim().isEmpty()) {
            return;
        }

        java.util.ArrayList<String> codigosEncontrados = controlador.buscarInscripcionesPorTexto(texto);

        if (codigosEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron materias que coincidan con: " + texto,
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            String codigoFila = (String) modeloTabla.getValueAt(fila, 0);
            if (codigosEncontrados.contains(codigoFila)) {
                tablaMaterias.setRowSelectionInterval(fila, fila);
                tablaMaterias.scrollRectToVisible(tablaMaterias.getCellRect(fila, 0, true));
                break;
            }
        }
    }

    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que querés cerrar la sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame(controlador).setVisible(true);
        }
    }

    private void mostrarReporte(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Reporte", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refrescarTodo() {
        actualizarPerfil();
        actualizarTabla();
        actualizarListaRiesgo();
    }

    private void actualizarPerfil() {
        String[] datos = controlador.obtenerDatosPerfilActivo();
        
        if (datos == null) {
            lblPerfil.setText("Sin sesión activa");
            lblAlerta.setText(" ");
            return;
        }

        String nombre = datos[0];
        String legajo = datos[1];
        String carrera = datos[2];
        String anio = datos[3];
        String alerta = datos[5]; // Capturamos la alerta que ya procesó el Controlador

        lblPerfil.setText(String.format("%s | Legajo: %s | Carrera: %s | Año de ingreso: %s",
                nombre, legajo, carrera, anio));

        // La Vista ya no usa el if matemático, solo imprime el texto recibido
        lblAlerta.setText(alerta); 
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        Object[][] datos = controlador.obtenerDatosInscripcionesActivo();

        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }

        lblEstadoVacio.setVisible(datos.length == 0);
    }

    private void actualizarListaRiesgo() {
        modeloListaRiesgo.clear();
        for (String item : controlador.obtenerMateriasEnRiesgoActivo()) {
            modeloListaRiesgo.addElement(item);
        }
    }

    private void limpiarFormularioMateria() {
        txtCodigo.setText("");
        txtNombreMateria.setText("");
        txtAnio.setText("");
        cmbCuatrimestre.setSelectedIndex(0);
    }

    private String obtenerCodigoSeleccionado() {
        int fila = tablaMaterias.getSelectedRow();
        if (fila == -1) {
            return null;
        }
        return (String) modeloTabla.getValueAt(fila, 0);
    }
}