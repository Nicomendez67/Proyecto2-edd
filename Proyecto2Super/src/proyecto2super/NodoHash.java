/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class NodoHash {
    private String clave;
    private Investigacion valor;


    public NodoHash(String clave, Investigacion valor) {
        this.clave = clave;
        this.valor = valor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Investigacion getValor() {
        return valor;
    }

    public void setValor(Investigacion valor) {
        this.valor = valor;
    }

      
    
}
