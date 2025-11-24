/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;   // ← agregado para validación del archivo

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
     * Normaliza cadenas para comparación/almacenamiento:
     * - trim
     * - convertir a minúsculas
     * - colapsar múltiples espacios a uno
     */
    private String normalizar(String s) {
        if (s == null) return null;
        String t = s.trim().toLowerCase();
        t = t.replaceAll("\\s+", " ");  // colapsar múltiples espacios
        return t;
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
        if (!insertado) {
            return false;
        }

        // Insertar autores en el AVL
        String[] autores = inv.getAutores();
        if (autores != null) {
            for (int i = 0; i < autores.length; i++) {
                String autor = autores[i];
                if (autor != null) {

                    // *** NORMALIZACIÓN OBLIGATORIA ***
                    autor = normalizar(autor);

                    // evitar vacíos
                    if (!autor.isEmpty()) {
                        this.avlAutores.insertar(autor, inv);
                    }
                }
            }
        }

        // Insertar palabras clave en el AVL
        String[] claves = inv.getPalabrasClaves();
        if (claves != null) {
            for (int i = 0; i < claves.length; i++) {
                String clave = claves[i];
                if (clave != null) {

                    // *** NORMALIZACIÓN OBLIGATORIA ***
                    clave = normalizar(clave);

                    // evitar vacíos
                    if (!clave.isEmpty()) {
                        this.avlPalabrasClave.insertar(clave, inv);
                    }
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

        // *** VALIDACIÓN OBLIGATORIA DEL ARCHIVO ***
        File f = new File(ruta);
        if (!f.exists() || f.length() == 0) {
            System.out.println("Archivo inválido o vacío: " + ruta);
            return false;
        }

        int cargados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            String linea;
            String titulo = null;
            String autores = null;
            String claves = null;
            StringBuilder resumenSb = new StringBuilder();
            boolean leyendoResumen = false;

            while ((linea = br.readLine()) != null) {

                linea = linea.trim();
                if (linea.isEmpty()) continue;

                if (linea.startsWith("TITULO:")) {
                    titulo = linea.substring("TITULO:".length()).trim();
                    leyendoResumen = false;
                    resumenSb.setLength(0);

                } else if (linea.startsWith("AUTORES:")) {
                    autores = linea.substring("AUTORES:".length()).trim();
                    leyendoResumen = false;

                } else if (linea.startsWith("PALABRAS:")) {
                    claves = linea.substring("PALABRAS:".length()).trim();
                    leyendoResumen = false;

                } else if (linea.startsWith("RESUMEN:")) {
                    String after = linea.substring("RESUMEN:".length()).trim();
                    resumenSb.setLength(0);
                    resumenSb.append(after);
                    leyendoResumen = true;

                } else if (linea.equals("FIN")) {

                    if (titulo == null || autores == null || claves == null) {
                        System.out.println("Artículo inválido omitido (faltan campos): " + titulo);
                    } else {

                        String[] listaAutores = autores.split(",");
                        String[] listaClaves = claves.split(",");
                        String resumen = resumenSb.toString();

                        Investigacion inv = new Investigacion(titulo, listaAutores, resumen, listaClaves);
                        boolean ok = insertarInvestigacion(inv);

                        if (ok) cargados++;
                    }

                    titulo = null;
                    autores = null;
                    claves = null;
                    resumenSb.setLength(0);
                    leyendoResumen = false;

                } else {

                    if (leyendoResumen) {
                        if (resumenSb.length() > 0) resumenSb.append("\n");
                        resumenSb.append(linea);
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println("Error leyendo archivo: " + ex.getMessage());
            return false;
        }

        return cargados > 0;
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
        ListaEnlazada<PalabraFrecuencia> resultado = this.analizador.obtenerTopFrecuencias(texto, 10);
        return resultado;
    }
    
    
    
    /**
     * Genera un reporte (String) con la lista de autores y las investigaciones
     * asociadas.
     */
    public String generarReporteAutores() {
        StringBuilder sb = new StringBuilder();
        ListaEnlazada<String> autores = listarAutoresOrdenados();
        for (int i = 0; i < autores.tamano(); i++) {
            String autor = autores.obtener(i);
            ListaEnlazada<Investigacion> invs = buscarPorAutor(autor);
            sb.append("Autor: ").append(autor).append("\n");
            if (invs == null || invs.tamano() == 0) {
                sb.append("  (sin investigaciones)\n\n");
                continue;
            }
            for (int j = 0; j < invs.tamano(); j++) {
                sb.append("  - ").append(invs.obtener(j).getTitulo()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    
    /**
     * Genera un reporte con las palabras clave y las investigaciones relacionadas.
     */
    public String generarReportePalabrasClave() {
        StringBuilder sb = new StringBuilder();
        ListaEnlazada<String> claves = listarPalabrasClaveOrdenadas();
        for (int i = 0; i < claves.tamano(); i++) {
            String clave = claves.obtener(i);
            ListaEnlazada<Investigacion> invs = buscarPorPalabraClave(clave);
            sb.append("Palabra clave: ").append(clave).append("\n");
            if (invs == null || invs.tamano() == 0) {
                sb.append("  (sin investigaciones)\n\n");
                continue;
            }
            for (int j = 0; j < invs.tamano(); j++) {
                sb.append("  - ").append(invs.obtener(j).getTitulo()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    
    /**
     * Genera un reporte completo con todas las investigaciones (orden: no garantizado).
     */
    public String generarReporteCompleto() {
        StringBuilder sb = new StringBuilder();
        ListaEnlazada<Investigacion> lista = obtenerTodasLasInvestigaciones();
        for (int i = 0; i < lista.tamano(); i++) {
            Investigacion inv = lista.obtener(i);
            sb.append(inv.toString()).append("\n\n");
        }
        return sb.toString();
    }
    
    
    /**
     * Busca investigaciones cuyo cuerpo contenga la palabra/frase dada.
     * Búsqueda básica: normaliza y busca subcadena.
     */
    public ListaEnlazada<Investigacion> buscarPorContenido(String termino) {
        ListaEnlazada<Investigacion> resultado = new ListaEnlazada<>();
        if (termino == null || termino.trim().isEmpty()) return resultado;
        
        String tnorm = normalizar(termino);
        ListaEnlazada<Investigacion> todas = obtenerTodasLasInvestigaciones();
        
        for (int i = 0; i < todas.tamano(); i++) {
            Investigacion inv = todas.obtener(i);
            if (inv == null || inv.getCuerpo() == null) continue;
            
            String cuerpoNorm = normalizar(inv.getCuerpo());
            if (cuerpoNorm.contains(tnorm)) {
                resultado.agregar(inv);
            }
        }
        
        return resultado;
    }
    
}