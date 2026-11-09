/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Santiago
 */
public abstract class Alumno implements Serializable {

    protected int codigo;
    protected String nombre;
    protected ArrayList<Asesoria> asesorias;

    public Alumno(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.asesorias = new ArrayList<>();
    }

    //Adiciona la asesoria al inicio de la coleccion y retorna su esfuerzo
    public int adicionarAsesoria(Asesoria nvaAsesoria){
        asesorias.add(0, nvaAsesoria);
        return nvaAsesoria.calcularEsfuerzo();
    }

    //Cada tipo de alumno calcula su aprovechamiento a su manera
    public abstract String calcularAprovechamiento();

    public int getNumeroAsesorias(){
        return asesorias.size();
    }

    //Suma de los minutos de todas las asesorias recibidas
    public int calcularMinutos(){
        int res = 0;

        for(int i = 0; i < asesorias.size(); i++){
            res += asesorias.get(i).getMinutos();
        }

        return res;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Asesoria> getAsesorias() {
        return asesorias;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAsesorias(ArrayList<Asesoria> asesorias) {
        this.asesorias = asesorias;
    }

    @Override
    public String toString() {
        return "Alumno{" + "codigo=" + codigo + ", nombre=" + nombre + '}';
    }


}