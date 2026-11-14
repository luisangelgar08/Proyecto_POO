/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

/**
 *
 * @author Luis Garcia
 */
import modelo.Alumno;
import modelo.AlumnoEnPrueba;
import modelo.AlumnoSinPrueba;
import modelo.Asignatura;
import modelo.Asesoria;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.File;

public class UsaAlumno extends javax.swing.JFrame implements Listado, Persistencia {

    private ArrayList<Alumno> losAlumnos = new ArrayList<>();
    private ArrayList<Asignatura> lasAsignaturas = new ArrayList<>();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(UsaAlumno.class.getName());
    private static final String ARCHIVO_ALUMNOS = "alumnos.obj";
    private static final String ARCHIVO_ASIGNATURAS = "asignaturas.obj";

    /**
     * Creates new form UsaAlumno
     */
    public UsaAlumno() {
        initComponents();
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");

        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField10.setText("");

        fieldFecha.setText("dd/MM/yyyy");

        cuadroReportes.setText("");
        cuadroReportes.setEditable(false);

        jTextField3.setEnabled(true);
        jTextField4.setEnabled(false);
        
        if (!new File(ARCHIVO_ALUMNOS).exists()
                && !new File(ARCHIVO_ASIGNATURAS).exists()) {

            cargarDatosIniciales();
        }
    }
    
    public Alumno buscarAlumno(ArrayList<Alumno> dato,
                          int codigoDado) {

        for (int i = 0; i < dato.size(); i++) {

            Alumno obj = dato.get(i);

            if (obj.getCodigo() == codigoDado) {
                return obj;
            }
        }

        return null;
    }
    
    public Asignatura buscarAsignatura(ArrayList<Asignatura> dato,
                                  String codigoDado) {

        Asignatura obj;

        for (int i = 0; i < dato.size(); i++) {

            obj = dato.get(i);

            if (obj.getCodigo().equalsIgnoreCase(codigoDado)) {
                return obj;
            }
        }

        return null;
    }
    
    public void ordenarAlumnos(ArrayList<Alumno> dato) {

        Alumno aux;

        for (int i = 0; i < dato.size() - 1; i++) {

            for (int j = 0; j < dato.size() - 1 - i; j++) {

                if (dato.get(j).getCodigo() >
                        dato.get(j + 1).getCodigo()) {

                    aux = dato.get(j);

                    dato.set(j, dato.get(j + 1));

                    dato.set(j + 1, aux);
                }
            }
        }
    }
    
    @Override
    public String listarAlumnos(ArrayList<Alumno> dato) {

        String res = "LISTADO DE ALUMNOS\n\n";

        if (dato.isEmpty()) {
            return "No hay alumnos registrados.";
        }

        // Ordenar los alumnos por código
        ordenarAlumnos(dato);

        Alumno obj;

        for (int i = 0; i < dato.size(); i++) {

            obj = dato.get(i);

            res += "Código: " + obj.getCodigo() + "\n"
                    + "Nombre: " + obj.getNombre() + "\n"
                    + "Aprovechamiento: "
                    + obj.calcularAprovechamiento() + "\n"
                    + "Número de asesorías: "
                    + obj.getNumeroAsesorias() + "\n"
                    + "--------------------------------\n";
        }

        return res;
    }

    @Override
    public String consultarAlumno(ArrayList<Alumno> dato,
                                 int codigoDado) {

        Alumno objAlumno = buscarAlumno(dato, codigoDado);

        if (objAlumno == null) {
            return "No existe un alumno con ese código.";
        }

        String res = "CONSULTA DE ALUMNO\n\n";

        res += "Código: " + objAlumno.getCodigo() + "\n";
        res += "Nombre: " + objAlumno.getNombre() + "\n";
        // Mostrar datos según el tipo de alumno
        if (objAlumno instanceof AlumnoEnPrueba) {

            AlumnoEnPrueba alumnoPrueba =
                    (AlumnoEnPrueba) objAlumno;

            res += "Promedio: "
                    + alumnoPrueba.getPromedio() + "\n";

        } else if (objAlumno instanceof AlumnoSinPrueba) {

            AlumnoSinPrueba alumnoSinPrueba =
                    (AlumnoSinPrueba) objAlumno;

            res += "Semestre: "
                    + alumnoSinPrueba.getSemestre() + "\n";
        }
        res += "Aprovechamiento: "
                + objAlumno.calcularAprovechamiento() + "\n";
        res += "Número de asesorías: "
                + objAlumno.getNumeroAsesorias() + "\n\n";

        res += "ASESORÍAS RECIBIDAS\n";
        res += "------------------------------\n";

        if (objAlumno.getAsesorias().isEmpty()) {
            res += "El alumno no tiene asesorías registradas.\n";
        }

        Asesoria objAsesoria;

        for (int i = 0; i < objAlumno.getAsesorias().size(); i++) {

            objAsesoria = objAlumno.getAsesorias().get(i);

            res += "\nAsesoría " + (i + 1) + "\n";

            res += "Asignatura: "
                    + objAsesoria.getAsignatura().getNombre() + "\n";

            res += "Modalidad: "
                    + objAsesoria.getModalidad() + "\n";

            res += "Fecha: "
                    + objAsesoria.getFecha() + "\n";

            res += "Duración: "
                    + objAsesoria.getMinutos() + " minutos\n";

            res += "Esfuerzo: "
                    + objAsesoria.calcularEsfuerzo() + " unidades\n";

            res += "------------------------------\n";
        }

        return res;
    }

    @Override
    public void guardarAsignaturas(ArrayList<Asignatura> dato) {

        try (ObjectOutputStream salida =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO_ASIGNATURAS))) {

            salida.writeObject(dato);

            JOptionPane.showMessageDialog(this,
                    "Asignaturas guardadas correctamente.");

        } catch (NotSerializableException e) {

            JOptionPane.showMessageDialog(this,
                    "Error: un objeto no es serializable.\n"
                    + e.getMessage());

        } catch (IOException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al guardar las asignaturas.\n"
                    + e.getMessage());
        }
    }

    @Override
    public void guardarAlumnos(ArrayList<Alumno> dato) {

        try (ObjectOutputStream salida =
                new ObjectOutputStream(
                        new FileOutputStream(ARCHIVO_ALUMNOS))) {

            salida.writeObject(dato);

            JOptionPane.showMessageDialog(this,
                    "Alumnos guardados correctamente.");

        } catch (NotSerializableException e) {

            JOptionPane.showMessageDialog(this,
                    "Error: un objeto no es serializable.\n"
                    + e.getMessage());

        } catch (IOException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al guardar los alumnos.\n"
                    + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<Alumno> recuperarAlumnos() {

        try (ObjectInputStream entrada =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO_ALUMNOS))) {

            ArrayList<Alumno> datos =
                    (ArrayList<Alumno>) entrada.readObject();

            return datos;

        } catch (FileNotFoundException e) {

            JOptionPane.showMessageDialog(this,
                    "No se encontró el archivo de alumnos.");

        } catch (IOException | ClassNotFoundException
                | ClassCastException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al recuperar los alumnos.\n"
                    + e.getMessage());
        }

        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<Asignatura> recuperarAsignaturas() {

        try (ObjectInputStream entrada =
                new ObjectInputStream(
                        new FileInputStream(ARCHIVO_ASIGNATURAS))) {

            ArrayList<Asignatura> datos =
                    (ArrayList<Asignatura>) entrada.readObject();

            return datos;

        } catch (FileNotFoundException e) {

            JOptionPane.showMessageDialog(this,
                    "No se encontró el archivo de asignaturas.");

        } catch (IOException | ClassNotFoundException
                | ClassCastException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al recuperar las asignaturas.\n"
                    + e.getMessage());
        }

        return null;
    }
    
    private void cargarDatosIniciales() {

        // Crear asignaturas
        Asignatura objA1 = new Asignatura(
                "POO", "Programación Orientada a Objetos");

        Asignatura objA2 = new Asignatura(
                "AvP", "Algoritmia y Programación");

        Asignatura objA3 = new Asignatura(
                "EDyA1", "Estructura de Datos y Algoritmos 1");

        Asignatura objA4 = new Asignatura(
                "BD1", "Bases de Datos 1");

        lasAsignaturas.add(objA1);
        lasAsignaturas.add(objA2);
        lasAsignaturas.add(objA3);
        lasAsignaturas.add(objA4);


        // Crear alumnos
        Alumno objAlumno1 = new AlumnoEnPrueba(
                2.8, 2233409, "Mario Ríos");

        Alumno objAlumno2 = new AlumnoSinPrueba(
                2, 2241834, "Camila Ramírez");

        Alumno objAlumno3 = new AlumnoSinPrueba(
                5, 2219045, "Walter Malagón");

        Alumno objAlumno4 = new AlumnoEnPrueba(
                2.5, 2228342, "Bernardo Guzmán");

        Alumno objAlumno5 = new AlumnoEnPrueba(
                3.1, 2211834, "Jenifer Valencia");

        losAlumnos.add(objAlumno1);
        losAlumnos.add(objAlumno2);
        losAlumnos.add(objAlumno3);
        losAlumnos.add(objAlumno4);
        losAlumnos.add(objAlumno5);


        // Primera asesoría de Mario
        Asesoria objAsesoria1 = new Asesoria(
                objA1,
                "Virtual",
                LocalDate.of(2026, 8, 12),
                15);

        objAlumno1.adicionarAsesoria(objAsesoria1);


        // Segunda asesoría de Mario
        Asesoria objAsesoria2 = new Asesoria(
                objA1,
                "Presencial",
                LocalDate.of(2026, 8, 28),
                20);

        objAlumno1.adicionarAsesoria(objAsesoria2);


        // Primera asesoría de Camila
        Asesoria objAsesoria3 = new Asesoria(
                objA2,
                "Presencial",
                LocalDate.of(2026, 8, 11),
                22);

        objAlumno2.adicionarAsesoria(objAsesoria3);


        // Primera asesoría de Walter
        Asesoria objAsesoria4 = new Asesoria(
                objA4,
                "Presencial",
                LocalDate.of(2026, 10, 9),
                25);

        objAlumno3.adicionarAsesoria(objAsesoria4);


        // Segunda asesoría de Walter
        Asesoria objAsesoria5 = new Asesoria(
                objA3,
                "Virtual",
                LocalDate.of(2026, 10, 14),
                15);

        objAlumno3.adicionarAsesoria(objAsesoria5);

    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tipoAlumno = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        registrarAlumno = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        selectorModalidad = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        fieldFecha = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        buttonResgisAsesoria = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        ListarAlumnos = new javax.swing.JButton();
        consultarAlumnos = new javax.swing.JButton();
        guardarDatos = new javax.swing.JButton();
        recuperarDatos = new javax.swing.JButton();
        buttonSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        cuadroReportes = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        ingresarAsignatura = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Datos del alumno");

        jLabel2.setText("Codigo: ");

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jLabel3.setText("Nombre:");

        jTextField2.setText("jTextField2");

        jLabel4.setText("Tipo de alumno:");

        tipoAlumno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "sin prueba", "en prueba" }));
        tipoAlumno.addActionListener(this::tipoAlumnoActionPerformed);

        jLabel5.setText("Promedio:");

        jTextField3.setText("jTextField3");

        jLabel6.setText("Semestre:");

        jTextField4.setText("jTextField4");

        registrarAlumno.setText("Ingresar alumno");
        registrarAlumno.addActionListener(this::registrarAlumnoActionPerformed);

        jLabel7.setText("Datos de la asignatura");

        jLabel8.setText("Codigo:");

        jTextField5.setText("jTextField5");

        jLabel9.setText("Nombre:");

        jTextField6.setText("jTextField6");

        jLabel10.setText("Registrar asesoria");

        jLabel11.setText("Alumno(codigo):");

        jTextField7.setText("jTextField7");

        jLabel12.setText("Asignatura(Codigo):");

        jTextField8.setText("jTextField8");

        jLabel13.setText("Modalidad:");

        selectorModalidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Presencial", "Virtual" }));
        selectorModalidad.addActionListener(this::selectorModalidadActionPerformed);

        jLabel14.setText("Fecha:");

        fieldFecha.setText("dd/mm/yy");
        fieldFecha.addActionListener(this::fieldFechaActionPerformed);

        jLabel15.setText("Duracion(Minutos):");

        jTextField10.setText("jTextField10");

        buttonResgisAsesoria.setText("Registrar asesoria");
        buttonResgisAsesoria.addActionListener(this::buttonResgisAsesoriaActionPerformed);

        jLabel16.setText("Operaciones");

        ListarAlumnos.setText("Listar alumnos");
        ListarAlumnos.addActionListener(this::ListarAlumnosActionPerformed);

        consultarAlumnos.setText("Consultar alumnos");
        consultarAlumnos.addActionListener(this::consultarAlumnosActionPerformed);

        guardarDatos.setText("Guardar datos");
        guardarDatos.addActionListener(this::guardarDatosActionPerformed);

        recuperarDatos.setText("Recuperar datos");
        recuperarDatos.addActionListener(this::recuperarDatosActionPerformed);

        buttonSalir.setText("Salir");
        buttonSalir.addActionListener(this::buttonSalirActionPerformed);

        cuadroReportes.setColumns(20);
        cuadroReportes.setRows(5);
        jScrollPane1.setViewportView(cuadroReportes);

        jLabel17.setText("Reportes");

        ingresarAsignatura.setText("Ingresar asignatura");
        ingresarAsignatura.addActionListener(this::ingresarAsignaturaActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(27, 27, 27)
                                .addComponent(tipoAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                    .addComponent(jTextField2)))))
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(registrarAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(selectorModalidad, 0, 108, Short.MAX_VALUE)
                                .addComponent(jTextField7)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonResgisAsesoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField8)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ingresarAsignatura)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(79, 79, 79)
                                .addComponent(buttonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ListarAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                    .addComponent(guardarDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(65, 65, 65)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(recuperarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(consultarAlumnos))))
                        .addContainerGap(39, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 27, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(consultarAlumnos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ListarAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(tipoAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(registrarAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ingresarAsignatura, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(selectorModalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(fieldFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonResgisAsesoria, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guardarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recuperarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel1.getAccessibleContext().setAccessibleName("Datos de alumnos");
        jLabel1.getAccessibleContext().setAccessibleDescription("");
        jTextField1.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void fieldFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldFechaActionPerformed

    private void buttonResgisAsesoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResgisAsesoriaActionPerformed
        try {

            // Capturar los datos de la interfaz
            int codigoAlumno = Integer.parseInt(
                    jTextField7.getText().trim());

            String codigoAsignatura = jTextField8.getText().trim();

            String modalidad = selectorModalidad.getSelectedItem().toString();

            String fechaTexto = fieldFecha.getText().trim();

            int minutos = Integer.parseInt(
                    jTextField10.getText().trim());

            // Buscar el alumno
            Alumno objAlumno = buscarAlumno(
                    losAlumnos, codigoAlumno);

            if (objAlumno == null) {

                JOptionPane.showMessageDialog(this,
                        "El alumno no está registrado.");

                return;
            }

            // Buscar la asignatura
            Asignatura objAsignatura = buscarAsignatura(
                    lasAsignaturas, codigoAsignatura);

            if (objAsignatura == null) {

                JOptionPane.showMessageDialog(this,
                        "La asignatura no está registrada.");

                return;
            }

            // Validar la duración
            if (minutos <= 0) {

                JOptionPane.showMessageDialog(this,
                        "La duración debe ser mayor que cero.");

                return;
            }

            // Convertir la fecha
            DateTimeFormatter formato =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate fecha = LocalDate.parse(
                    fechaTexto, formato);

            // Crear el objeto Asesoria
            Asesoria nueva = new Asesoria(
                    objAsignatura, modalidad, fecha, minutos);

            // Adicionar la asesoría al alumno
            int esfuerzo = objAlumno.adicionarAsesoria(nueva);

            // Mostrar el resultado
            cuadroReportes.setText(
                    "Asesoría registrada correctamente\n\n"
                    + "Alumno: " + objAlumno.getNombre() + "\n"
                    + "Asignatura: " + objAsignatura.getNombre() + "\n"
                    + "Modalidad: " + modalidad + "\n"
                    + "Fecha: " + fecha + "\n"
                    + "Duración: " + minutos + " minutos\n"
                    + "Esfuerzo: " + esfuerzo + " unidades");

            // Limpiar los campos
            jTextField7.setText("");
            jTextField8.setText("");
            jTextField10.setText("");
            fieldFecha.setText("dd/MM/yyyy");

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Verifique los valores numéricos ingresados.");

        } catch (java.time.format.DateTimeParseException e) {

            JOptionPane.showMessageDialog(this,
                    "La fecha debe tener el formato dd/MM/yyyy.");
        }
    }//GEN-LAST:event_buttonResgisAsesoriaActionPerformed

    private void registrarAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarAlumnoActionPerformed

        try {
            // Capturar datos de la GUI
            int codigo = Integer.parseInt(
                    jTextField1.getText().trim());

            String nombre = jTextField2.getText().trim();

            String tipo = tipoAlumno.getSelectedItem().toString();

            // Validar los datos
            if (codigo <= 0 || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese un código y nombre válidos.");
                return;
            }

            // Verificar que no exista previamente
            if (buscarAlumno(losAlumnos, codigo) != null) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un alumno con ese código.");
                return;
            }

            Alumno nuevo;

            if (tipo.equals("en prueba")) {

                double promedio = Double.parseDouble(
                        jTextField3.getText().trim());

                if (!Double.isFinite(promedio)
                        || promedio < 0 || promedio > 5) {
                    JOptionPane.showMessageDialog(this,
                            "El promedio debe estar entre 0 y 5.");
                    return;
                }

                nuevo = new AlumnoEnPrueba(
                        promedio, codigo, nombre);

            } else {

                int semestre = Integer.parseInt(
                        jTextField4.getText().trim());

                if (semestre <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Ingrese un semestre válido.");
                    return;
                }

                nuevo = new AlumnoSinPrueba(
                        semestre, codigo, nombre);
            }

            // Almacenar el alumno en la colección
            losAlumnos.add(nuevo);

            // Mostrar resultado
            cuadroReportes.setText(
                    "Alumno registrado correctamente\n\n"
                    + nuevo.toString());

            // Limpiar campos
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Verifique los valores numéricos ingresados.");
        }

    }//GEN-LAST:event_registrarAlumnoActionPerformed

    private void buttonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSalirActionPerformed
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea salir?\n"
                + "Recuerde guardar los datos antes de salir.",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_buttonSalirActionPerformed

    private void recuperarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recuperarDatosActionPerformed
        ArrayList<Alumno> alumnosRecuperados =
                recuperarAlumnos();

        ArrayList<Asignatura> asignaturasRecuperadas =
                recuperarAsignaturas();

        if (alumnosRecuperados != null
                && asignaturasRecuperadas != null) {

            losAlumnos = alumnosRecuperados;

            lasAsignaturas = asignaturasRecuperadas;

            JOptionPane.showMessageDialog(this,
                    "Datos recuperados correctamente.");

            cuadroReportes.setText(
                    listarAlumnos(losAlumnos));
        }
    }//GEN-LAST:event_recuperarDatosActionPerformed

    private void consultarAlumnosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consultarAlumnosActionPerformed
        String codigoTexto = JOptionPane.showInputDialog(
                this, "Ingrese el código del alumno:");

        if (codigoTexto == null) {
            return;
        }

        try {

            int codigoDado = Integer.parseInt(codigoTexto.trim());

            cuadroReportes.setText(
                    consultarAlumno(losAlumnos, codigoDado));

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Debe ingresar un código numérico válido.");
        }
    }//GEN-LAST:event_consultarAlumnosActionPerformed

    private void guardarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarDatosActionPerformed
        guardarAlumnos(losAlumnos);

        guardarAsignaturas(lasAsignaturas);
    }//GEN-LAST:event_guardarDatosActionPerformed

    private void selectorModalidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectorModalidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectorModalidadActionPerformed

    private void tipoAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoAlumnoActionPerformed
        String tipo = tipoAlumno.getSelectedItem().toString();

        if (tipo.equals("en prueba")) {
            jTextField3.setEnabled(true);
            jTextField4.setEnabled(false);
            jTextField4.setText("");
        } else {
            jTextField3.setEnabled(false);
            jTextField4.setEnabled(true);
            jTextField3.setText("");
        }
    }//GEN-LAST:event_tipoAlumnoActionPerformed

    private void ingresarAsignaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingresarAsignaturaActionPerformed
        String codigo = jTextField5.getText().trim();
        String nombre = jTextField6.getText().trim();

        // Verificar que los campos no estén vacíos
        if (codigo.isEmpty() || nombre.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Debe ingresar el código y nombre de la asignatura.");

            return;
        }

        // Verificar que la asignatura no exista
        if (buscarAsignatura(lasAsignaturas, codigo) != null) {

            JOptionPane.showMessageDialog(this,
                    "Ya existe una asignatura con ese código.");

            return;
        }

        // Crear el objeto Asignatura
        Asignatura nueva = new Asignatura(codigo, nombre);

        // Agregar la asignatura a la colección
        lasAsignaturas.add(nueva);

        // Mostrar resultado
        cuadroReportes.setText(
                "Asignatura registrada correctamente\n\n"
                + nueva.toString());

        // Limpiar los campos
        jTextField5.setText("");
        jTextField6.setText("");        
    }//GEN-LAST:event_ingresarAsignaturaActionPerformed

    private void ListarAlumnosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ListarAlumnosActionPerformed
        cuadroReportes.setText(
        listarAlumnos(losAlumnos)
        );
    }//GEN-LAST:event_ListarAlumnosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new UsaAlumno().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ListarAlumnos;
    private javax.swing.JButton buttonResgisAsesoria;
    private javax.swing.JButton buttonSalir;
    private javax.swing.JButton consultarAlumnos;
    private javax.swing.JTextArea cuadroReportes;
    private javax.swing.JTextField fieldFecha;
    private javax.swing.JButton guardarDatos;
    private javax.swing.JButton ingresarAsignatura;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JButton recuperarDatos;
    private javax.swing.JButton registrarAlumno;
    private javax.swing.JComboBox<String> selectorModalidad;
    private javax.swing.JComboBox<String> tipoAlumno;
    // End of variables declaration//GEN-END:variables
}
