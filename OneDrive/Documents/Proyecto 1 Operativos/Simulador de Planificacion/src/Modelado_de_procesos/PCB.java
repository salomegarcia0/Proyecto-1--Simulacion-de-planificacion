/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelado_de_procesos;

/**
 *
 * @author salom
 */
public class PCB {
    private int procesoID;
    private String procesoNombre;
    private EstadoProceso estadoActual;
    private TipoProceso tipo;
    private int programCounter;
    private int instruccionesContador;
    private int instruccionesTotal;
}
