/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Luis soto
 */ 
import java.time.LocalDate;

public class Asesoria {

    // Constantes (static final)
    private static final int MIN_UNIDAD_PRESENCIAL = 10;
    private static final int MIN_UNIDAD_VIRTUAL = 5;

    // Atributos
    private Asignatura asignatura;
    private String modalidad;
    private LocalDate fecha;
    private int minutos;

    // Constructor
    public Asesoria(Asignatura asignatura, String modalidad, LocalDate fecha, int minutos) {
        this.asignatura = asignatura;
        this.modalidad = modalidad;
        this.fecha = fecha;
        this.minutos = minutos;
    }

    // Calcula las "unidades de esfuerzo" según la modalidad
    public int calcularEsfuerzo() {
        if (modalidad.equalsIgnoreCase("Presencial")) {
            return minutos / MIN_UNIDAD_PRESENCIAL;
        } else {
            return minutos / MIN_UNIDAD_VIRTUAL;
        }
    }

    // Getters
    public Asignatura getAsignatura() {
        return asignatura;
    }

    public String getModalidad() {
        return modalidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getMinutos() {
        return minutos;
    }

    // Setters
    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    @Override
    public String toString() {
        return "Asesoria [asignatura=" + asignatura.getNombre()
                + ", modalidad=" + modalidad
                + ", fecha=" + fecha
                + ", minutos=" + minutos + "]";
    }
}


