/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vista;

import java.util.ArrayList;
import modelo.Alumno;
import modelo.Asignatura;
/**
 *
 * @author Luis Garcia
 */
public interface Persistencia {
    void guardarAlumnos(ArrayList<Alumno> dato);

    ArrayList<Alumno> recuperarAlumnos();

    void guardarAsignaturas(ArrayList<Asignatura> dato);

    ArrayList<Asignatura> recuperarAsignaturas();    
}
