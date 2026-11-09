/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Santiago
 */
public class AlumnoSinPrueba extends Alumno {

    private int semestre;
    private static final int UMBRAL_ASESORIAS = 1;

    public AlumnoSinPrueba(int semestre, int codigo, String nombre) {
        super(codigo, nombre);
        this.semestre = semestre;
    }

    //BUENO si las asesorias recibidas superan el umbral, de lo contrario NO
    @Override
    public String calcularAprovechamiento(){
        String res;

        if(getNumeroAsesorias() > UMBRAL_ASESORIAS){
            res = "BUENO";
        }
        else{
            res = "NO";
        }

        return res;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return "AlumnoSinPrueba{" + "codigo=" + codigo + ", nombre=" + nombre + ", semestre=" + semestre + '}';
    }


}