/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelado_de_procesos;

/**
 *
 * @author salom
 */
public class EstadoProceso {
    public static EstadoProceso Nuevo = new EstadoProceso("Nuevo");
    public static EstadoProceso Listo = new EstadoProceso("Listo");
    public static EstadoProceso Ejecutando = new EstadoProceso("Ejecutando");
    public static EstadoProceso Bloqueado = new EstadoProceso("Bloqueado");
    public static EstadoProceso Terminado = new EstadoProceso("Terminado");
    public static EstadoProceso Listo_Susoendido = new EstadoProceso("Listo Suspendido");
    public static EstadoProceso Bloqueado_Supendido = new EstadoProceso("Listo Suspendido");
    
    private String nombre;
    
    private EstadoProceso(String Nombre){
        this.nombre = nombre;
    }
}
