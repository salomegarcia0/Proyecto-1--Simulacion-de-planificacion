/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaz;
import Modelado_de_procesos.*;
import Politicas_de_Planificacion.*;
import Estructuras_de_Datos.*;
import main.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author pjroj
 */
public class Ventana2 extends javax.swing.JFrame {

    /**
     * Creates new form Ventana2
     */
    public Ventana2() {
        initComponents();
        
        //Reloj de sistema en segundos
        Timer timer = new Timer(1000, e -> {
            long ahora = System.currentTimeMillis();
            long segundos = (ahora - CPU.getReloj_global())/1000 ; //entre 1000 para que sea en segundos
            jLabelRelojGlobal.setText(segundos + " seg");
        });
        timer.start();
        
        Timer timer2 = new Timer(1000, e -> {
            jLabelMemoriaUsada.setText(Integer.toString(CPU.getGestorMemoria().getMemoriaDisponible()));
        });
        timer2.start();
        
        //Menu para la creacion de procesos (Barra del menu)
        jMenuItemCrearProceso.addActionListener(e -> {
            CrearProceso ventana = new CrearProceso();
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setVisible(true);
        });
        
        //Menu para aplicar SPN
        jMenuItemSPN.addActionListener(e -> {
            /**CPU.getColaGuardados();
            System.out.println("guardados");
            CPU.getColaGuardados().print2();*/
            reiniciar();
                       
            SPN spn = new SPN();
            spn.organizarCola(CPU.getColaListos());
            
            System.out.println("SPN en cola de listos: ");
            CPU.getColaListos().print2();
            
            
            new Thread(() -> {
                CPU.ejecutarProcesoCompleto();
                prints();
            }).start();
       
        });
        
        //Menu para aplicar FCFS
        jMenuItemFCFS.addActionListener(e -> {
            /**CPU.getColaGuardados();
            System.out.println("guardados");
            CPU.getColaGuardados().print2();
            CPU.setColaNuevos(CPU.getColaGuardados());*/
            reiniciar();
            
            System.out.println("FCFS - ColaListos");
            CPU.getColaListos().print2();
            
            //CPU.admitirProceso();
            System.out.println("Cola Listos:" + CPU.getColaListos().getSize());
            CPU.getColaListos().print2();
            
            new Thread(() -> {
                CPU.ejecutarProcesoCompleto();
                prints();
            }).start();
        });
        
        //Valor de un ciclo en ms, tiene limites entre 500ms y 1000ms
        jSliderCiclo.addChangeListener(e -> {
            int valor = jSliderCiclo.getValue();
            jLabelCiclo.setText(valor + " ms");
        });
        jButtonCambiarCiclo.addActionListener(e -> {
            int valor = jSliderCiclo.getValue();
            CPU.setCiclo_reloj(valor);//en ms
            //System.out.println(CPU.getCiclo_reloj());
        });
        
        //Cada segundo se estarán actualizando los datos en las colas
        Timer timerColas = new Timer(1000, e -> {
            ColaListosMostrar();
            ColaNuevosMostrar();
            ColaBloqueadosMostrar();
            ColaTerminadoMostrar();
            ColaListosSuspendidosMostrar();
            ColaBloqueadosSuspendidosMostrar();
            EjecutadoMostrar();
        });
        timerColas.start();
        
        //NO TOCAR
        new Thread(() -> {
            CPU.ejecutarProcesoCompleto();
            prints();
        }).start();
        
        timerColas.start();
        
    }
    
    private void reiniciar(){
        
        Cola colaNuevosBackup = CPU.getColaNuevos();
        
        CPU.setColaListos(new Cola());
        CPU.setColaListosSuspendidos(new Cola());
        CPU.setColaBloqueadosSuspendidos(new Cola());
        CPU.setColaBloqueados(new Cola());
        CPU.setColaTerminado(new Cola());
        CPU.setGestorMemoria(new GestorMemoria());
        CPU.setCountInstrucciones(0);
        CPU.setCountRound_Robin(0);
        
        CPU.setColaNuevos(colaNuevosBackup);
        CPU.admitirProceso();
    }
    
    private void prints(){
        System.out.println("nuevos");
        CPU.getColaNuevos().print2();
        System.out.println("listos");
        CPU.getColaListos().print2();
        System.out.println("Terminado");
        CPU.getColaTerminado().print2();
        System.out.println("Bloqueado");
        CPU.getColaBloqueados().print2();
        System.out.println("Listo susp");
        CPU.getColaListosSuspendidos().print2();
        System.out.println("Bloqueado susp");
        CPU.getColaBloqueadosSuspendidos().print2();
    }
    
    private void ColaListosMostrar(){
        Nodo pListo = CPU.getColaListos().getHead();
        //configuraciones del panel de fondo del scrollball
        JPanel panelScrollbar =  new JPanel ();
        String tipoProceso = "CPU_BOUND";
        panelScrollbar.setBackground(Color.decode("#FFFFFF"));
        if(!CPU.getColaListos().isEmpty()){
            while (pListo != null){
                JPanel p =  new JPanel (new GridLayout(0, 1));
                //configuraciones de diseño del proceso
                p.setBackground(Color.decode("#C6DFB9"));
                p.setSize(300,600);
                p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                //ID del proceso
                JLabel id = new JLabel(Integer.toString(pListo.getProceso().getProcesoID()));
                p.add(id,BorderLayout.CENTER);
                //Nombre del proceso
                JLabel proceso = new JLabel(pListo.getProceso().getProcesoNombre());
                p.add(proceso, BorderLayout.CENTER);
                //Tipo del proceso
                if (pListo.getProceso().getTipo() == TipoProceso.IO_BOUND){
                    tipoProceso = "IO_BOUND";
                } else{
                    tipoProceso = "CPU_BOUND";
                }
                JLabel tipo = new JLabel(tipoProceso);
                p.add(tipo, BorderLayout.CENTER);
                //PC y MAR
                JLabel MAR = new JLabel("MAR: " +Integer.toString(pListo.getProceso().getMAR()));
                p.add(MAR, BorderLayout.CENTER);
                JLabel PC = new JLabel("PC: " +Integer.toString(pListo.getProceso().getPC()));
                p.add(PC, BorderLayout.CENTER);

                //se agrega al scrollbar
                panelScrollbar.add(p);

                pListo = pListo.getNext();
            } 
            jScrollPaneListo.setViewportView(panelScrollbar);
        }
        
    }
    
    private void ColaNuevosMostrar(){
        Nodo pNuevo = CPU.getColaNuevos().getHead();
        //configuraciones del panel de fondo del scrollball
        JPanel panelScrollbar =  new JPanel ();
        String tipoProceso = "CPU_BOUND";
        panelScrollbar.setBackground(Color.decode("#FFFFFF"));
        if(!CPU.getColaNuevos().isEmpty()){
            while (pNuevo != null){
                JPanel p =  new JPanel (new GridLayout(0, 1));
                //configuraciones de diseño del proceso
                p.setBackground(Color.decode("#D8FFA1"));
                p.setSize(300,600);
                p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                //ID del proceso
                JLabel id = new JLabel(Integer.toString(pNuevo.getProceso().getProcesoID()));
                p.add(id,BorderLayout.CENTER);
                //Nombre del proceso
                JLabel proceso = new JLabel(pNuevo.getProceso().getProcesoNombre());
                p.add(proceso, BorderLayout.CENTER);
                //Tipo del proceso
                if (pNuevo.getProceso().getTipo() == TipoProceso.IO_BOUND){
                    tipoProceso = "IO_BOUND";
                } else{
                    tipoProceso = "CPU_BOUND";
                }
                JLabel tipo = new JLabel(tipoProceso);
                p.add(tipo, BorderLayout.CENTER);
                //PC y MAR
                JLabel MAR = new JLabel("MAR: " +Integer.toString(pNuevo.getProceso().getMAR()));
                p.add(MAR, BorderLayout.CENTER);
                JLabel PC = new JLabel("PC: " +Integer.toString(pNuevo.getProceso().getPC()));
                p.add(PC, BorderLayout.CENTER);

                //se agrega al scrollbar
                panelScrollbar.add(p);

                pNuevo = pNuevo.getNext();
            } 
            jScrollPaneNuevo.setViewportView(panelScrollbar);
        }
        
    }
    
    private void ColaBloqueadosMostrar(){
        Nodo pBloqueado = CPU.getColaBloqueados().getHead();
        //configuraciones del panel de fondo del scrollball
        JPanel panelScrollbar =  new JPanel ();
        String tipoProceso = "CPU_BOUND";
        panelScrollbar.setBackground(Color.decode("#FFFFFF"));
        if(!CPU.getColaListos().isEmpty()){
            while (pBloqueado != null){
                JPanel p =  new JPanel (new GridLayout(0, 1));
                //configuraciones de diseño del proceso
                p.setBackground(Color.decode("#FFBDBD"));
                p.setSize(300,600);
                p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                //ID del proceso
                JLabel id = new JLabel(Integer.toString(pBloqueado.getProceso().getProcesoID()));
                p.add(id,BorderLayout.CENTER);
                //Nombre del proceso
                JLabel proceso = new JLabel(pBloqueado.getProceso().getProcesoNombre());
                p.add(proceso, BorderLayout.CENTER);
                //Tipo del proceso
                if (pBloqueado.getProceso().getTipo() == TipoProceso.IO_BOUND){
                    tipoProceso = "IO_BOUND";
                } else{
                    tipoProceso = "CPU_BOUND";
                }
                JLabel tipo = new JLabel(tipoProceso);
                p.add(tipo, BorderLayout.CENTER);
                //PC y MAR
                JLabel MAR = new JLabel("MAR: " +Integer.toString(pBloqueado.getProceso().getMAR()));
                p.add(MAR, BorderLayout.CENTER);
                JLabel PC = new JLabel("PC: " +Integer.toString(pBloqueado.getProceso().getPC()));
                p.add(PC, BorderLayout.CENTER);


                //se agrega al scrollbar
                panelScrollbar.add(p);

                pBloqueado = pBloqueado.getNext();
            } 
            jScrollPaneBloqueado.setViewportView(panelScrollbar);
        }
        
    }
    
    private void ColaTerminadoMostrar(){
        Nodo pTerminado = CPU.getColaTerminado().getHead();
        //configuraciones del panel de fondo del scrollball
        JPanel panelScrollbar =  new JPanel ();
        String tipoProceso = "CPU_BOUND";
        panelScrollbar.setBackground(Color.decode("#FFFFFF"));
        if(!CPU.getColaListos().isEmpty()){
            while (pTerminado != null){
                JPanel p =  new JPanel (new GridLayout(0, 1));
                //configuraciones de diseño del proceso
                p.setBackground(Color.decode("#CCCCCC"));
                p.setSize(300,600);
                p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                //ID del proceso
                JLabel id = new JLabel(Integer.toString(pTerminado.getProceso().getProcesoID()));
                p.add(id,BorderLayout.CENTER);
                //Nombre del proceso
                JLabel proceso = new JLabel(pTerminado.getProceso().getProcesoNombre());
                p.add(proceso, BorderLayout.CENTER);
                //Tipo del proceso
                if (pTerminado.getProceso().getTipo() == TipoProceso.IO_BOUND){
                    tipoProceso = "IO_BOUND";
                } else{
                    tipoProceso = "CPU_BOUND";
                }
                JLabel tipo = new JLabel(tipoProceso);
                p.add(tipo, BorderLayout.CENTER);
                //PC y MAR
                JLabel MAR = new JLabel("MAR: " +Integer.toString(pTerminado.getProceso().getMAR()));
                p.add(MAR, BorderLayout.CENTER);
                JLabel PC = new JLabel("PC: " +Integer.toString(pTerminado.getProceso().getPC()));
                p.add(PC, BorderLayout.CENTER);



                //se agrega al scrollbar
                panelScrollbar.add(p);

                pTerminado = pTerminado.getNext();
            } 
            jScrollPaneCompletado.setViewportView(panelScrollbar);
        }
        
    }
    
    private void ColaListosSuspendidosMostrar(){
        Nodo pListosS = CPU.getColaListosSuspendidos().getHead();
        //configuraciones del panel de fondo del scrollball
        JPanel panelScrollbar =  new JPanel ();
        String tipoProceso = "CPU_BOUND";
        panelScrollbar.setBackground(Color.decode("#FFFFFF"));
        if(!CPU.getColaListos().isEmpty()){
            while (pListosS != null){
                JPanel p =  new JPanel (new GridLayout(0, 1));
                //configuraciones de diseño del proceso
                p.setBackground(Color.decode("#FFBB8E"));
                p.setSize(300,600);
                p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                //ID del proceso
                JLabel id = new JLabel(Integer.toString(pListosS.getProceso().getProcesoID()));
                p.add(id,BorderLayout.CENTER);
                //Nombre del proceso
                JLabel proceso = new JLabel(pListosS.getProceso().getProcesoNombre());
                p.add(proceso, BorderLayout.CENTER);
                //Tipo del proceso
                if (pListosS.getProceso().getTipo() == TipoProceso.IO_BOUND){
                    tipoProceso = "IO_BOUND";
                } else{
                    tipoProceso = "CPU_BOUND";
                }
                JLabel tipo = new JLabel(tipoProceso);
                p.add(tipo, BorderLayout.CENTER);
                //PC y MAR
                JLabel MAR = new JLabel("MAR: " +Integer.toString(pListosS.getProceso().getMAR()));
                p.add(MAR, BorderLayout.CENTER);
                JLabel PC = new JLabel("PC: " +Integer.toString(pListosS.getProceso().getPC()));
                p.add(PC, BorderLayout.CENTER);

                //se agrega al scrollbar
                panelScrollbar.add(p);

                pListosS = pListosS.getNext();
            } 
            jScrollPaneListoSuspendido.setViewportView(panelScrollbar);
        }
        
    }
    
    private void ColaBloqueadosSuspendidosMostrar(){
        Nodo pBloqueadoS = CPU.getColaBloqueadosSuspendidos().getHead();
        //configuraciones del panel de fondo del scrollball
        JPanel panelScrollbar =  new JPanel ();
        String tipoProceso = "CPU_BOUND";
        panelScrollbar.setBackground(Color.decode("#FFFFFF"));
        if(!CPU.getColaListos().isEmpty()){
            while (pBloqueadoS != null){
                JPanel p =  new JPanel (new GridLayout(0, 1));
                //configuraciones de diseño del proceso
                p.setBackground(Color.decode("#FFBB8E"));
                p.setSize(300,600);
                p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                //ID del proceso
                JLabel id = new JLabel(Integer.toString(pBloqueadoS.getProceso().getProcesoID()));
                p.add(id,BorderLayout.CENTER);
                //Nombre del proceso
                JLabel proceso = new JLabel(pBloqueadoS.getProceso().getProcesoNombre());
                p.add(proceso, BorderLayout.CENTER);
                //Tipo del proceso
                if (pBloqueadoS.getProceso().getTipo() == TipoProceso.IO_BOUND){
                    tipoProceso = "IO_BOUND";
                } else{
                    tipoProceso = "CPU_BOUND";
                }
                JLabel tipo = new JLabel(tipoProceso);
                p.add(tipo, BorderLayout.CENTER);
                //PC y MAR
                JLabel MAR = new JLabel("MAR: " +Integer.toString(pBloqueadoS.getProceso().getMAR()));
                p.add(MAR, BorderLayout.CENTER);
                JLabel PC = new JLabel("PC: " +Integer.toString(pBloqueadoS.getProceso().getPC()));
                p.add(PC, BorderLayout.CENTER);

                //se agrega al scrollbar
                panelScrollbar.add(p);

                pBloqueadoS = pBloqueadoS.getNext();
            } 
            jScrollPaneBloqueadoSuspendido.setViewportView(panelScrollbar);
        }
        
    }
        
    private void EjecutadoMostrar(){
        
        PCB pEjecutado = CPU.getProcesoEnEjecucion();
        if(pEjecutado != null){
            //configuraciones para el proceso que se esta ejecutando
            idProceso.setText(Integer.toString(pEjecutado.getProcesoID()));
            nombreProceso.setText(pEjecutado.getProcesoNombre());

            TipoProceso tipo = pEjecutado.getTipo();
            String mensaje = "CPU_BOUND";
            if (tipo == TipoProceso.IO_BOUND){
                mensaje = "IO_BOUND";
            }
            tipoProceso.setText(mensaje);
            //PC y MAR
            int suma = pEjecutado.getPC() + 1;
            MAR.setText(Integer.toString(pEjecutado.getMAR()));
            PC.setText(Integer.toString(suma));
            
        }else if(pEjecutado == null){
            idProceso.setText("000000");
            nombreProceso.setText("vacio");
            tipoProceso.setText("----");
            MAR.setText("00");
            PC.setText("00");
        }
    }
    
    private void actualizarTodaLaUI() {
    ColaListosMostrar();
    ColaNuevosMostrar();
    ColaBloqueadosMostrar();
    ColaTerminadoMostrar();
    ColaListosSuspendidosMostrar();
    ColaBloqueadosSuspendidosMostrar();
    EjecutadoMostrar();
    
    // Fuerza la actualización de los scrollpanes
    jScrollPaneListo.revalidate();
    jScrollPaneListo.repaint();
    jScrollPaneNuevo.revalidate();
    jScrollPaneNuevo.repaint();
    jScrollPaneBloqueado.revalidate();
    jScrollPaneBloqueado.repaint();
    jScrollPaneCompletado.revalidate();
    jScrollPaneCompletado.repaint();
    jScrollPaneListoSuspendido.revalidate();
    jScrollPaneListoSuspendido.repaint();
    jScrollPaneBloqueadoSuspendido.revalidate();
    jScrollPaneBloqueadoSuspendido.repaint();
}

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabelRelojGlobal = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        PC = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        idProceso = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        nombreProceso = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tipoProceso = new javax.swing.JLabel();
        MAR = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPaneListo = new javax.swing.JScrollPane();
        jScrollPaneBloqueado = new javax.swing.JScrollPane();
        jLabel7 = new javax.swing.JLabel();
        jScrollPaneCompletado = new javax.swing.JScrollPane();
        jLabel9 = new javax.swing.JLabel();
        jScrollPaneListoSuspendido = new javax.swing.JScrollPane();
        jScrollPaneBloqueadoSuspendido = new javax.swing.JScrollPane();
        jLabel10 = new javax.swing.JLabel();
        jSliderCiclo = new javax.swing.JSlider();
        jLabel = new javax.swing.JLabel();
        jLabelCiclo = new javax.swing.JLabel();
        jScrollPaneNuevo = new javax.swing.JScrollPane();
        jLabel12 = new javax.swing.JLabel();
        jButtonCambiarCiclo = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabelMemoriaUsada = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemFCFS = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItemSPN = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuCrearProceso = new javax.swing.JMenu();
        jMenuItemCrearProceso = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 153, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setText("Bloqueado");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 450, 130, 30));

        jLabelRelojGlobal.setFont(new java.awt.Font("Segoe UI Symbol", 1, 18)); // NOI18N
        jLabelRelojGlobal.setForeground(new java.awt.Color(0, 0, 0));
        jLabelRelojGlobal.setText("0 seg");
        jPanel1.add(jLabelRelojGlobal, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 170, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 255));
        jLabel4.setText("Ejecutando");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 130, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 255, 0));
        jLabel5.setText("Nuevo");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 120, 30));

        jPanel2.setBackground(new java.awt.Color(221, 211, 231));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 0, 102), 4, true));
        jPanel2.setForeground(new java.awt.Color(204, 153, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PC.setForeground(new java.awt.Color(0, 0, 0));
        PC.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        PC.setText("00");
        jPanel2.add(PC, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 120, 20));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("ID:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 30, 20));

        idProceso.setForeground(new java.awt.Color(0, 0, 0));
        idProceso.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        idProceso.setText("000000");
        jPanel2.add(idProceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 80, 20));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Nombre:");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 60, 20));

        nombreProceso.setForeground(new java.awt.Color(0, 0, 0));
        nombreProceso.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nombreProceso.setText("vacio");
        jPanel2.add(nombreProceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 120, 20));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Tipo:");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 40, 20));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("MAR:");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 40, 20));

        tipoProceso.setForeground(new java.awt.Color(0, 0, 0));
        tipoProceso.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tipoProceso.setText("-----");
        jPanel2.add(tipoProceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 120, 20));

        MAR.setForeground(new java.awt.Color(0, 0, 0));
        MAR.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MAR.setText("00");
        jPanel2.add(MAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 120, 20));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("PC:");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 50, 20));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 220, 150));
        jPanel1.add(jScrollPaneListo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 200, 840, 100));
        jPanel1.add(jScrollPaneBloqueado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 480, 840, 100));

        jLabel7.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Completado");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 310, 150, 30));
        jPanel1.add(jScrollPaneCompletado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 340, 840, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 102, 0));
        jLabel9.setText("Bloqueado Suspendido");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 590, 290, 30));
        jPanel1.add(jScrollPaneListoSuspendido, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 620, 410, 100));
        jPanel1.add(jScrollPaneBloqueadoSuspendido, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 620, 410, 100));

        jLabel10.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 102, 0));
        jLabel10.setText("Listo Suspendido");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 590, 200, 30));

        jSliderCiclo.setBackground(new java.awt.Color(204, 153, 255));
        jSliderCiclo.setForeground(new java.awt.Color(102, 102, 102));
        jSliderCiclo.setMaximum(4000);
        jSliderCiclo.setMinimum(500);
        jPanel1.add(jSliderCiclo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 550, -1, 30));

        jLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel.setForeground(new java.awt.Color(0, 0, 0));
        jLabel.setText("Valor ciclo:");
        jPanel1.add(jLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 490, -1, -1));

        jLabelCiclo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelCiclo.setForeground(new java.awt.Color(0, 0, 0));
        jLabelCiclo.setText("1000 ms");
        jPanel1.add(jLabelCiclo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 520, -1, -1));
        jPanel1.add(jScrollPaneNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 840, 100));

        jLabel12.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 153, 0));
        jLabel12.setText("Listo");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, 70, 30));

        jButtonCambiarCiclo.setText("cambiar");
        jPanel1.add(jButtonCambiarCiclo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 590, 100, 30));

        jLabel17.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Reloj:");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 70, 30));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelMemoriaUsada.setForeground(new java.awt.Color(0, 0, 0));
        jLabelMemoriaUsada.setText("----");
        jPanel3.add(jLabelMemoriaUsada, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 80, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Memoria U:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 220, 120));

        jMenu2.setText("Politicas de planificacion");

        jMenuItemFCFS.setText("FCFS");
        jMenu2.add(jMenuItemFCFS);

        jMenuItem2.setText("Round Robin");
        jMenu2.add(jMenuItem2);

        jMenuItemSPN.setText("SPN");
        jMenu2.add(jMenuItemSPN);

        jMenuItem4.setText("SRT");
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("HRRN");
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("FeedBack");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        jMenuCrearProceso.setText("Crear proceso");

        jMenuItemCrearProceso.setText("Crear proceso");
        jMenuCrearProceso.add(jMenuItemCrearProceso);

        jMenuBar1.add(jMenuCrearProceso);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1287, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel *///<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana2().setVisible(true);
                CPU.ejecutarProcesoCompleto();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MAR;
    private javax.swing.JLabel PC;
    private javax.swing.JLabel idProceso;
    private javax.swing.JButton jButtonCambiarCiclo;
    private javax.swing.JLabel jLabel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCiclo;
    private javax.swing.JLabel jLabelMemoriaUsada;
    private javax.swing.JLabel jLabelRelojGlobal;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuCrearProceso;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItemCrearProceso;
    private javax.swing.JMenuItem jMenuItemFCFS;
    private javax.swing.JMenuItem jMenuItemSPN;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPaneBloqueado;
    private javax.swing.JScrollPane jScrollPaneBloqueadoSuspendido;
    private javax.swing.JScrollPane jScrollPaneCompletado;
    private javax.swing.JScrollPane jScrollPaneListo;
    private javax.swing.JScrollPane jScrollPaneListoSuspendido;
    private javax.swing.JScrollPane jScrollPaneNuevo;
    private javax.swing.JSlider jSliderCiclo;
    private javax.swing.JLabel nombreProceso;
    private javax.swing.JLabel tipoProceso;
    // End of variables declaration//GEN-END:variables
}
