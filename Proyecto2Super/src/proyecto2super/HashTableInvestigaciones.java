/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class HashTableInvestigaciones {
    private NodoHash[] tabla;
    private int capacidad;
    private int size;

    public HashTableInvestigaciones(NodoHash[] tabla, int capacidad, int size) {
        this.tabla = tabla;
        this.capacidad = capacidad;
        this.size = size;
    }

    public NodoHash[] getTabla() {
        return tabla;
    }

    public void setTabla(NodoHash[] tabla) {
        this.tabla = tabla;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
}
