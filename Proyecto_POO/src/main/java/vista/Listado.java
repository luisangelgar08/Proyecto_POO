/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vista;

import java.util.ArrayList;
import modelo.Alumno;
/**
 *
 * @author Luis Garcia
 */
public interface Listado {

    String listarAlumnos(ArrayList<Alumno> dato);

    String consultarAlumno(ArrayList<Alumno> dato, int codigoDado);
    
}
