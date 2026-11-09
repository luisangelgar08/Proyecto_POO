/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Santiago
 */
public class AlumnoEnPrueba extends Alumno {

    private double promedio;
    private static final int UMBRAL_MINUTOS = 50;

    public AlumnoEnPrueba(double promedio, int codigo, String nombre) {
        super(codigo, nombre);
        this.promedio = promedio;
    }

    //BUENO si la suma de minutos es mayor que el umbral, de lo contrario MEJORABLE
    @Override
    public String calcularAprovechamiento(){
        String res;

        if(calcularMinutos() > UMBRAL_MINUTOS){
            res = "BUENO";
        }
        else{
            res = "MEJORABLE";
        }

        return res;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    @Override
    public String toString() {
        return "AlumnoEnPrueba{" + "codigo=" + codigo + ", nombre=" + nombre + ", promedio=" + promedio + '}';
    }


}