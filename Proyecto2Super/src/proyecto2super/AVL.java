/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class AVL<T extends Comparable<T>> {
    private NodoAVL<T> raiz;

    public AVL() {
        this.raiz = null;
    }

    public NodoAVL getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoAVL raiz) {
        this.raiz = raiz;
    }
    
    /**
     * Inserta una clave en el árbol AVL. Si la clave ya existe,
     * se agrega la investigación correspondiente a su lista.
     *
     * @param clave clave a insertar
     * @param inv investigación asociada
     */
    public void insertar(T clave, Investigacion inv) {
        raiz = insertarRec(raiz, clave, inv);
    }

    /**
     * Busca una clave en el árbol.
     *
     * @param clave clave a buscar
     * @return lista de investigaciones asociadas o null si no existe
     */
    public ListaEnlazada<Investigacion> buscar(T clave) {

        NodoAVL<T> nodo = buscarNodo(raiz, clave);

        if (nodo != null) {
            return nodo.getInvestigaciones();
        } else {
            return null;
        }
    }

    /**
     * Obtiene todas las claves del árbol en orden ascendente.
     *
     * @return lista enlazada con claves ordenadas
     */
    public ListaEnlazada<T> recorridoInOrden() {

        ListaEnlazada<T> lista = new ListaEnlazada<>();
        recorridoInOrdenRec(raiz, lista);
        return lista;
    }

    /**
     * Inserta una nueva clave en el árbol AVL de forma recursiva.
     *
     * @param nodo El nodo actual desde el cual se continúa la inserción.
     * @param clave La clave que se desea insertar.
     * @param inv La investigación asociada a la clave.
     * @return El nodo resultante después de la inserción y posibles rotaciones.
     */
    private NodoAVL<T> insertarRec(NodoAVL<T> nodo, T clave, Investigacion inv) {

        if (nodo == null) {
            NodoAVL<T> nuevo = new NodoAVL<>(clave, inv);
            return nuevo;
        }

        int cmp = clave.compareTo(nodo.getClave());

        if (cmp < 0) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), clave, inv));
        } else {

            if (cmp > 0) {
                nodo.setDerecho(insertarRec(nodo.getDerecho(), clave, inv));
            } else {

                if (cmp == 0) {
                    nodo.getInvestigaciones().agregar(inv);
                }

                return nodo;
            }
        }

        int alturaIzq = altura(nodo.getIzquierdo());
        int alturaDer = altura(nodo.getDerecho());
        nodo.setAltura(1 + Math.max(alturaIzq, alturaDer));

        int balance = factorBalance(nodo);

        
        // CASO LL
        if (balance > 1) {

            T claveIzq = nodo.getIzquierdo().getClave();

            if (clave.compareTo(claveIzq) < 0) {
                return rotacionDerecha(nodo);
            }
        }

        
        // CASO RR
        if (balance < -1) {

            T claveDer = nodo.getDerecho().getClave();

            if (clave.compareTo(claveDer) > 0) {
                return rotacionIzquierda(nodo);
            }
        }

        // CASO LR
        if (balance > 1) {

            T claveIzq = nodo.getIzquierdo().getClave();

            if (clave.compareTo(claveIzq) > 0) {
                nodo.setIzquierdo(rotacionIzquierda(nodo.getIzquierdo()));
                return rotacionDerecha(nodo);
            }
        }


        // CASO RL
        if (balance < -1) {

            T claveDer = nodo.getDerecho().getClave();

            if (clave.compareTo(claveDer) < 0) {
                nodo.setDerecho(rotacionDerecha(nodo.getDerecho()));
                return rotacionIzquierda(nodo);
            }
        }

        return nodo;
    }

    
    /**
     * Busca un nodo dentro del árbol AVL que contenga la clave especificada
     *
     * @param nodo El nodo raíz desde donde inicia la búsqueda.
     * @param clave La clave que se desea localizar.
     * @return El nodo que contiene la clave buscada, o {@code null} si no se encuentra.
     */
    private NodoAVL<T> buscarNodo(NodoAVL<T> nodo, T clave) {

        NodoAVL<T> actual = nodo;

        while (actual != null) {

            int cmp = clave.compareTo(actual.getClave());

            if (cmp < 0) {
                actual = actual.getIzquierdo();
            } else {

                if (cmp > 0) {
                    actual = actual.getDerecho();
                } else {

                    if (cmp == 0) {
                        return actual;
                    }
                }
            }
        }

        return null;
    }

    
    /**
     * Realiza un recorrido en orden (in-order) del árbol AVL.
     *
     * @param nodo El nodo actual desde el cual se continúa el recorrido.
     * @param lista La lista donde se almacenarán las claves en orden ascendente.
     */
    private void recorridoInOrdenRec(NodoAVL<T> nodo, ListaEnlazada<T> lista) {

        if (nodo != null) {

            recorridoInOrdenRec(nodo.getIzquierdo(), lista);
            lista.agregar(nodo.getClave());
            recorridoInOrdenRec(nodo.getDerecho(), lista);
        }
    }

    /**
     * Obtiene la altura de un nodo dentro del Arbol AVL
     *
     * @param nodo El nodo cuya altura se desea consultar.
     * @return La altura del nodo, o 0 si es {@code null}.
     */
    private int altura(NodoAVL<T> nodo) {

        if (nodo == null) {
            return 0;
        } else {
            return nodo.getAltura();
        }
    }

    
    /**
    * Calcula el factor de balance de un nodo AVL.Diferencia entre la altura del
    * subárbol izquierdo y la del subárbol derecho
    *
    * @param nodo El nodo cuyo factor de balance se desea calcular.
    * @return El factor de balance del nodo, o 0 si es {@code null}.
    */
    private int factorBalance(NodoAVL<T> nodo) {

        if (nodo == null) {
            return 0;
        } else {

            int izq = altura(nodo.getIzquierdo());
            int der = altura(nodo.getDerecho());
            return izq - der;
        }
    }

    
    /**
     * Realiza una rotación simple a la derecha sobre el subArbol cuya raIz es el nodo dado
     *
     * @param y El nodo raíz del subárbol que será rotado a la derecha.
     * @return El nuevo nodo raíz del subárbol después de la rotación.
     */
    private NodoAVL<T> rotacionDerecha(NodoAVL<T> y) {

        NodoAVL<T> x = y.getIzquierdo();
        NodoAVL<T> T2 = x.getDerecho();

        x.setDerecho(y);
        y.setIzquierdo(T2);

        int alturaIzqY = altura(y.getIzquierdo());
        int alturaDerY = altura(y.getDerecho());
        y.setAltura(1 + Math.max(alturaIzqY, alturaDerY));

        int alturaIzqX = altura(x.getIzquierdo());
        int alturaDerX = altura(x.getDerecho());
        x.setAltura(1 + Math.max(alturaIzqX, alturaDerX));

        return x;
    }

    /**
     * Realiza una rotación simple a la izquierda sobre el subArbol cuya raiz es el nodo dado
     *
     * @param x El nodo raíz del subárbol que será rotado a la izquierda.
     * @return El nuevo nodo raíz del subárbol después de la rotación.
     */
    private NodoAVL<T> rotacionIzquierda(NodoAVL<T> x) {

        NodoAVL<T> y = x.getDerecho();
        NodoAVL<T> T2 = y.getIzquierdo();

        y.setIzquierdo(x);
        x.setDerecho(T2);

        int alturaIzqX = altura(x.getIzquierdo());
        int alturaDerX = altura(x.getDerecho());
        x.setAltura(1 + Math.max(alturaIzqX, alturaDerX));

        int alturaIzqY = altura(y.getIzquierdo());
        int alturaDerY = altura(y.getDerecho());
        y.setAltura(1 + Math.max(alturaIzqY, alturaDerY));

        return y;
    }
}
