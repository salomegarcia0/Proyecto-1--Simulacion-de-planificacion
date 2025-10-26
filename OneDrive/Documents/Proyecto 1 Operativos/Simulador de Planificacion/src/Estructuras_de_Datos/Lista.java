/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras_de_Datos;
import Modelado_de_procesos.PCB;
/**
 *
 * @author pjroj
 */
public class Lista {
    private Nodo head,tail;
    private int size;

    public Lista(Nodo head, int size) {
        this.head = null;
        this.size = 0;
    }
    
    public Nodo getHead() {
        return head;
    }

    public void setHead(Nodo head) {
        this.head = head;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public boolean isEmpty(){
        return head == null;
    }
    
    public void insertarInicio(PCB proceso){
        Nodo nodo = new Nodo(proceso);
        if(isEmpty()){
            setHead(nodo);
        }else{
            nodo.setNext(getHead());
            setHead(nodo);
        }
        size ++;
    }
    
    public void insertarFinal(PCB proceso){
        Nodo nodo = new Nodo(proceso);
        if(isEmpty()){
            setHead(nodo);
        }else{
            Nodo pointer = getHead();
            while(pointer.getNext() != null){
                pointer = pointer.getNext();
            }
            pointer.setNext(nodo);
        }
        size ++;
    }
    
    public void insertarPorIndex(PCB proceso, int index){
        Nodo nodo = new Nodo(proceso);
        if(isEmpty()){
            insertarInicio(proceso);
        }else{
            if(index < getSize()){
                Nodo pointer = getHead();
                int cont = 0;
                while(cont < index -1){
                    pointer = pointer.getNext();
                    cont++;
                }
                Nodo temp = pointer.getNext();
                nodo.setNext(temp);
                pointer.setNext(nodo);
                size++;
                
            }else if(index == getSize()){
            }
        }
    }

}
