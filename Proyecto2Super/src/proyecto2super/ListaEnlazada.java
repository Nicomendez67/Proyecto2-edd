/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class ListaEnlazada {
    private NodoLista cabeza;
    private int size;

    public ListaEnlazada(NodoLista cabeza, int size) {
        this.cabeza = cabeza;
        this.size = size;
    }

    public NodoLista getCabeza() {
        return cabeza;
    }

    public void setCabeza(NodoLista cabeza) {
        this.cabeza = cabeza;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
}
