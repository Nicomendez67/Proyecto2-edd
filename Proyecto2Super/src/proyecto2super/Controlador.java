/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class Controlador {
    
    private HashTableInvestigaciones tablaInvestigaciones;
    private AVL<String> avlAutores;
    private AVL<String> avlPalabrasClave;
    private AnalizadorDeResumen analizador;


    public Controlador() {
        this.tablaInvestigaciones = new HashTableInvestigaciones();
        this.avlAutores = new AVL<>();
        this.avlPalabrasClave = new AVL<>();
        this.analizador = new AnalizadorDeResumen();
    }

    
    /**
     * Inserta una investigación en todas las estructuras.
     *
     * @param inv investigación a insertar
     * @return true si se insertó, false si ya existía
     */
    public boolean insertarInvestigacion(Investigacion inv) {

        if (inv == null) {
            return false;
        }

        String titulo = inv.getTitulo();

        if (titulo == null) {
            return false;
        }

        boolean insertado = this.tablaInvestigaciones.insertar(titulo, inv);

        if (insertado == false) {
            return false;
        }

        // Insertar autores en el AVL
        String[] autores = inv.getAutores();
        if (autores != null) {

            for (int i = 0; i < autores.length; i++) {

                String autor = autores[i];

                if (autor != null) {
                    this.avlAutores.insertar(autor, inv);
                }
            }
        }

        // Insertar palabras clave en el AVL
        String[] claves = inv.getPalabrasClaves();
        if (claves != null) {

            for (int i = 0; i < claves.length; i++) {

                String clave = claves[i];

                if (clave != null) {
                    this.avlPalabrasClave.insertar(clave, inv);
                }
            }
        }

        return true;
    }

    /**
     * Busca una investigación por su título.
     *
     * @param titulo título
     * @return investigación o null si no existe
     */
    public Investigacion buscarPorTitulo(String titulo) {

        if (titulo == null) {
            return null;
        }

        return this.tablaInvestigaciones.buscar(titulo);
    }

    /**
     * Obtiene todas las investigaciones asociadas a un autor.
     *
     * @param autor nombre del autor
     * @return lista de investigaciones o null si el autor no existe
     */
    public ListaEnlazada<Investigacion> buscarPorAutor(String autor) {

        if (autor == null) {
            return null;
        }

        return this.avlAutores.buscar(autor);
    }

    /**
     * Obtiene todas las investigaciones asociadas a una palabra clave.
     *
     * @param palabra palabra clave
     * @return lista de investigaciones o null si no existe
     */
    public ListaEnlazada<Investigacion> buscarPorPalabraClave(String palabra) {

        if (palabra == null) {
            return null;
        }

        return this.avlPalabrasClave.buscar(palabra);
    }

    /**
     * Obtiene todas las investigaciones (sin orden).
     *
     * @return lista de investigaciones
     */
    public ListaEnlazada<Investigacion> obtenerTodasLasInvestigaciones() {
        return this.tablaInvestigaciones.listarInvestigaciones();
    }

    /**
     * Obtiene las palabras clave ordenadas (recorrido inorden del AVL).
     */
    public ListaEnlazada<String> listarPalabrasClaveOrdenadas() {
        return this.avlPalabrasClave.recorridoInOrden();
    }

    /**
     * Obtiene los autores ordenados (recorrido inorden del AVL).
     */
    public ListaEnlazada<String> listarAutoresOrdenados() {
        return this.avlAutores.recorridoInOrden();
    }

    /**
     * Guarda todas las investigaciones en un archivo.
     */
    public void guardarEnArchivo(String ruta) throws Exception {
        this.tablaInvestigaciones.guardarEnArchivo(ruta);
    }

    /**
     * Carga investigaciones desde un archivo y las reinserta en todas
     * las estructuras.
     */
    public void cargarDesdeArchivo(String ruta) throws Exception {

        // Crear una nueva tabla vacía para evitar duplicados previos
        this.tablaInvestigaciones = new HashTableInvestigaciones();
        this.avlAutores = new AVL<>();
        this.avlPalabrasClave = new AVL<>();

        this.tablaInvestigaciones.cargarDesdeArchivo(ruta);

        ListaEnlazada<Investigacion> lista = this.tablaInvestigaciones.listarInvestigaciones();

        int tam = lista.tamano();

        for (int i = 0; i < tam; i++) {

            Investigacion inv = lista.obtener(i);
            insertarInvestigacion(inv);
        }
    }
    
    
    /**
     * Carga un archivo de texto con artículos.
     *
     * @param ruta ruta del archivo
     * @return true si se cargó bien
     */
    public boolean cargarArchivo(String ruta) {

        try {

            BufferedReader br = new BufferedReader(new FileReader(ruta));
            String linea;
            String titulo = "";
            String autores = "";
            String claves = "";
            String resumen = "";

            while ((linea = br.readLine()) != null) {

                if (linea.startsWith("TITULO:")) {
                    titulo = linea.substring(7).trim();
                }

                if (linea.startsWith("AUTORES:")) {
                    autores = linea.substring(8).trim();
                }

                if (linea.startsWith("PALABRAS:")) {
                    claves = linea.substring(9).trim();
                }

                if (linea.startsWith("RESUMEN:")) {
                    resumen = linea.substring(8).trim();
                }

                // FIN DE UN ARTÍCULO
                if (linea.equals("FIN")) {

                    // Convertir arrays
                    String[] listaAutores = autores.split(",");
                    String[] listaClaves = claves.split(",");

                    // Crear investigación
                    Investigacion inv = new Investigacion(titulo,listaAutores,resumen, listaClaves);

                    // Insertar investigación en la tabla hash
                    tablaInvestigaciones.insertar(titulo, inv);

                    // Insertar AUTOR por AUTOR en AVLAutores
                    for (int i = 0; i < listaAutores.length; i++) {
                        String a = listaAutores[i].trim();
                        avlAutores.insertar(a, inv);
                    }

                    // Insertar CLAVE por CLAVE en AVLPalabrasClave
                    for (int j = 0; j < listaClaves.length; j++) {
                        String c = listaClaves[j].trim();
                        avlPalabrasClave.insertar(c, inv);
                    }
                }
            }

            br.close();
            return true;

        } catch (IOException ex) {
            System.out.println("Error leyendo archivo: " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene todos los autores en orden ascendente (inorden del AVL).
     *
     * @return ListaEnlazada<String> (puede ser vacía)
     */
    public ListaEnlazada<String> listarAutores() {
        return this.avlAutores.recorridoInOrden();
    }
    
    
    /**
     * Recupera todos los títulos disponibles (recorriendo la tabla de dispersión).
     *
     * @return ListaEnlazada<String> con títulos (puede ser vacía)
     */
    public ListaEnlazada<String> listarTitulos() {

        ListaEnlazada<String> lista = new ListaEnlazada<String>();

        ListaEnlazada<Investigacion> invs = this.tablaInvestigaciones.listarInvestigaciones();

        if (invs == null) {
            return lista;
        }

        int tam = invs.tamano();

        for (int i = 0; i < tam; i++) {
            Investigacion inv = invs.obtener(i);
            if (inv != null && inv.getTitulo() != null) {
                lista.agregar(inv.getTitulo());
            }
        }

        return lista;
    }

    /**
     * Analiza un texto de resumen y devuelve las N palabras más frecuentes
     *
     * @param texto texto del resumen
     * @return ListaEnlazada PalabraFrecuencia (puede ser vacía)
     */
    public ListaEnlazada<PalabraFrecuencia> analizarResumen(String texto) {

        if (texto == null) {
            return new ListaEnlazada<PalabraFrecuencia>();
        }

        // por defecto obtener top 10
        ListaEnlazada<PalabraFrecuencia> resultado = this.analizador.obtenerTopFrecuencias(texto, 10);
        return resultado;
    }
}
