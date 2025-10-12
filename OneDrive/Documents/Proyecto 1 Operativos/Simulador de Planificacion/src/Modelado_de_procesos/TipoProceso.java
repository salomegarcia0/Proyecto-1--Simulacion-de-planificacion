/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelado_de_procesos;

/**
 *
 * @author salom
 */
public class TipoProceso {
    public static TipoProceso CPU_BOUND = new TipoProceso("CPU Bound");
    public static TipoProceso IO_BOUND = new TipoProceso("I/O Bound");
    
    private String nombre;
    
    private TipoProceso(String nombre) {
        this.nombre = nombre;
    }
}
