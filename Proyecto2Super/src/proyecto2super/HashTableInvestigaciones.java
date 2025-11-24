/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class HashTableInvestigaciones {
    private ListaEnlazada<NodoHash>[] tabla = (ListaEnlazada<NodoHash>[]) new ListaEnlazada[0];
    private int capacidad;
    private int size;
    private final double FACTOR_CARGA = 0.75;
    
    public HashTableInvestigaciones() {
        this.capacidad = 101;
        this.tabla = (ListaEnlazada<NodoHash>[]) new ListaEnlazada[this.capacidad];
        this.size = 0;
    }
    
    public ListaEnlazada<NodoHash>[] getTabla() {
        return tabla;
    }
    
    public void setTabla(ListaEnlazada<NodoHash>[] tabla) {
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
    
    /**
    * Constructor con capacidad inicial.
    *
    * @param capacidadInicial capacidad inicial deseada
    */
    public HashTableInvestigaciones(int capacidadInicial) {
        if (capacidadInicial <= 0) {
            this.capacidad = 101;
        } else {
            this.capacidad = capacidadInicial;
        }
        this.tabla = (ListaEnlazada<NodoHash>[]) new ListaEnlazada[this.capacidad];
        this.size = 0;
    }
    
    /**
     * Esta función produce un índice dentro del rango [0, capacidad-1] usando:
     *  - suma ponderada de caracteres (con primes)
     *  - módulo por la capacidad
     *
     * @param clave cadena a hashear (título)
     * @return índice en el arreglo de buckets
     */
    public int hash(String clave) {
        if (clave == null) {
            return 0;
        }
        
        // estrategia: suma de (charValue * primo[i mod k]) para evitar patrones sencillos
        int[] primos = new int[] {31, 37, 41, 43, 47};
        long total = 0;
        
        for (int i = 0; i < clave.length(); i++) {
            char c = clave.charAt(i);
            int primo = primos[i % primos.length];
            total = total + ((long) c * primo);
        }
        
        // evitar negativos
        long valorPositivo = total;
        if (valorPositivo < 0) {
            valorPositivo = -valorPositivo;
        }
        
        int indice = (int) (valorPositivo % this.capacidad);
        return indice;
    }
    
    
    /**
     * Inserta una investigación en la tabla con clave (título).
     *
     * @param clave título de la investigación (clave primaria)
     * @param inv investigación a insertar
     * @return true si se insertó correctamente; false si ya existía
     */
    public boolean insertar(String clave, Investigacion inv) {
    
        if (clave == null || inv == null) {
            return false;
        }

        // normalizar clave para consistencia
        String claveNorm = normalizarClave(clave);

        // si ya existe la clave, no insertar
        boolean existe = contieneClave(claveNorm);
        if (existe) {
            return false;
        }
        
        // comprobar factor de carga y rehash si es necesario
        double carga = 0.0;
        if (this.capacidad != 0) {
            carga = (double) this.size / (double) this.capacidad;
        }
        
        if (carga >= FACTOR_CARGA) {
            rehash();
        }
        int idx = hash(claveNorm);
        // Si el bucket todavía es null, inicializar la lista
        if (this.tabla[idx] == null) {
            this.tabla[idx] = new ListaEnlazada<NodoHash>();
        }
        NodoHash nuevo = new NodoHash(claveNorm, inv);
        this.tabla[idx].agregar(nuevo);
        this.size = this.size + 1;
        
        return true;
    }
    
    /**
     * Verifica si una clave está presente en la tabla.
     *
     * @param clave título a buscar
     * @return true si existe, false en caso contrario
     */
    public boolean contieneClave(String clave) {
    
        if (clave == null) {
            return false;
        }

        String claveNorm = normalizarClave(clave);

        int idx = hash(claveNorm);
        if (this.tabla[idx] == null) {
            return false;
        } else {
        
            ListaEnlazada<NodoHash> lista = this.tabla[idx];
            int tam = lista.tamano();
            
            for (int i = 0; i < tam; i++) {
            
                NodoHash nodo = lista.obtener(i);
                
                if (nodo != null) {
                    String k = nodo.getClave();
                
                    if (k != null) {
                        if (k.equalsIgnoreCase(claveNorm)) {
                            return true;
                        }
                    }
                }
            }
            
            return false;
        }
    }
    
    /**
     * Busca y devuelve la investigación asociada a la clave.
     *
     * @param clave título a buscar
     * @return Investigación encontrada o null si no existe
     */
    public Investigacion buscar(String clave) {
    
        if (clave == null) {
            return null;
        }

        String claveNorm = normalizarClave(clave);

        int idx = hash(claveNorm);
        if (this.tabla[idx] == null) {
            return null;
        } else {
        
            ListaEnlazada<NodoHash> lista = this.tabla[idx];
            int tam = lista.tamano();
            
            for (int i = 0; i < tam; i++) {
            
                NodoHash nodo = lista.obtener(i);
                
                if (nodo != null) {
                
                    String k = nodo.getClave();
                    
                    if (k != null) {
                        if (k.equalsIgnoreCase(claveNorm)) {
                            return nodo.getValor();
                        }
                    }
                }
            }
            
            return null;
        }
    }
    /**
     * Recupera todas las investigaciones almacenadas en la tabla.
     *
     * @return ListaEnlazada con todas las investigaciones
     */
    public ListaEnlazada<Investigacion> listarInvestigaciones() {
    
        ListaEnlazada<Investigacion> resultado = new ListaEnlazada<Investigacion>();
        
        for (int idx = 0; idx < this.capacidad; idx++) {
        
            if (this.tabla[idx] != null) {
            
                ListaEnlazada<NodoHash> lista = this.tabla[idx];
                int tam = lista.tamano();
                
                for (int j = 0; j < tam; j++) {
                
                    NodoHash nodo = lista.obtener(j);
                    
                    if (nodo != null) {
                        resultado.agregar(nodo.getValor());
                    }
                }
            }
        }
       
        return resultado;
    }
    
    /**
     * Guarda el contenido de la tabla en un archivo de texto.
     * 
     * @param ruta ruta del archivo donde guardar
     * @throws IOException si ocurre un error de E/S
     */
    public void guardarEnArchivo(String ruta) throws IOException {
        
        if (ruta == null) {
            return;
        }
        
        BufferedWriter writer = null;
        
        try {
            File archivo = new File(ruta);
            writer = new BufferedWriter(new FileWriter(archivo));
        
            for (int idx = 0; idx < this.capacidad; idx++) {
            
                if (this.tabla[idx] != null) {
                
                    ListaEnlazada<NodoHash> lista = this.tabla[idx];
                    int tam = lista.tamano();
                    
                    for (int j = 0; j < tam; j++) {
                    
                        NodoHash nodo = lista.obtener(j);
                        
                        if (nodo != null) {
                        
                            Investigacion inv = nodo.getValor();
                            
                            if (inv != null) {
                            
                                writer.write("---INICIO_INVESTIGACION---");
                                writer.newLine();
                                
                                // Título
                                writer.write("titulo:");
                                if (inv.getTitulo() != null) {
                                    writer.write(inv.getTitulo());
                                }
                                writer.newLine();
                                
                                // Autores (separados por ;)
                                writer.write("autores:");
                                String[] autores = inv.getAutores();
                                if (autores != null) {
                                    for (int k = 0; k < autores.length; k++) {
                                        writer.write(autores[k]);
                                
                                        if (k < autores.length - 1) {
                                            writer.write(";");
                                        }
                                    }
                                }
                                writer.newLine();
                                
                                // Palabras clave (separadas por ;)
                                writer.write("palabras:");
                                String[] claves = inv.getPalabrasClaves();
                                
                                if (claves != null) {
                                    for (int k = 0; k < claves.length; k++) {
                                        writer.write(claves[k]);
                                
                                        if (k < claves.length - 1) {
                                            writer.write(";");
                                        }
                                    }
                                }
                                writer.newLine();
                                
                                // Cuerpo (convertir saltos a un token <NL> para una línea)
                                writer.write("cuerpo:");
                                if (inv.getCuerpo() != null) {
                                
                                    String cuerpo = inv.getCuerpo();
                                    String cuerpoEnUnaLinea = cuerpo.replace("\n", "<NL>");
                                    writer.write("<CORPO>" + cuerpoEnUnaLinea + "<FINCORPO>");
                                }
                                writer.newLine();
                                writer.write("---FIN_INVESTIGACION---");
                                writer.newLine();
                            }
                        }
                    }
                }
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    /**
     * Carga el contenido de un archivo y lo inserta en la tabla.
     *
     * @param ruta ruta del archivo a cargar
     * @throws IOException si ocurre un error de E/S
     */
    public void cargarDesdeArchivo(String ruta) throws IOException {
        if (ruta == null) {
            return;
        }
        BufferedReader reader = null;
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                return;
            }
            reader = new BufferedReader(new FileReader(archivo));
            String linea = null;
            String titulo = null;
            String[] autores = null;
            String[] palabras = null;
            String cuerpo = null;
            while (true) {
                linea = reader.readLine();
                if (linea == null) {
                    break;
                }
                if (linea.equals("---INICIO_INVESTIGACION---")) {
                    // reset campos
                    titulo = null;
                    autores = null;
                    palabras = null;
                    cuerpo = null;
                    // leer hasta FIN_INVESTIGACION
                    while (true) {
                        linea = reader.readLine();
                        if (linea == null) {
                            break;
                        }
                        if (linea.startsWith("titulo:")) {
                            String valor = linea.substring("titulo:".length());
                            titulo = valor;
                        } else {
                            if (linea.startsWith("autores:")) {
                                String valor = linea.substring("autores:".length());
                                if (valor != null && valor.length() > 0) {
                                    autores = valor.split(";");
                                } else {
                                    autores = new String[0];
                                }
                            } else {
                                if (linea.startsWith("palabras:")) {
                                    String valor = linea.substring("palabras:".length());
                                    if (valor != null && valor.length() > 0) {
                                        palabras = valor.split(";");
                                    } else {
                                        palabras = new String[0];
                                    }
                                } else {
                                    if (linea.startsWith("cuerpo:")) {
                                        String valor = linea.substring("cuerpo:".length());
                                        if (valor != null && valor.length() > 0) {
                                            // extraer dentro de <CORPO> ... <FINCORPO>
                                            int posInicio = valor.indexOf("<CORPO>");
                                            int posFin = valor.indexOf("<FINCORPO>");
                                            if (posInicio >= 0 && posFin > posInicio) {
                                                String cuerpoToken = valor.substring(posInicio + "<CORPO>".length(), posFin);
                                                cuerpo = cuerpoToken.replace("<NL>", "\n");
                                            } else {
                                                cuerpo = "";
                                            }
                                        } else {
                                            cuerpo = "";
                                        }
                                    } else {
                                        if (linea.equals("---FIN_INVESTIGACION---")) {
                                            // crear objeto Investigacion y añadirlo
                                            Investigacion inv = new Investigacion(titulo, autores, cuerpo, palabras);
                                            // insertar en la tabla (evita duplicados internamente)
                                            this.insertar(titulo, inv);
                                            // salir del while interno para continuar con siguientes entradas
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } // fin while lectura de investigacion
                } // fin if INICIO_INVESTIGACION
            } // fin while general
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    /**
     * Aumenta la capacidad de la tabla y reubica todas las entradas.
     *
     * Estrategia sencilla: nueva capacidad = capacidad * 2 + 1 (mantener impar).
     */
    private void rehash() {
        int nuevaCapacidad = this.capacidad * 2 + 1;
        ListaEnlazada<NodoHash>[] viejaTabla = this.tabla;
        // crear nueva tabla vacía y actualizar capacidad y size antes de reinsertar
        this.tabla = (ListaEnlazada<NodoHash>[]) new ListaEnlazada[nuevaCapacidad];
        this.capacidad = nuevaCapacidad;
        this.size = 0;
        // recorrer tabla antigua y re-insertar cada nodo usando insertar()
        for (int idx = 0; idx < viejaTabla.length; idx++) {
            if (viejaTabla[idx] != null) {
                ListaEnlazada<NodoHash> lista = viejaTabla[idx];
                int tam = lista.tamano();
                for (int j = 0; j < tam; j++) {
                    NodoHash nodo = lista.obtener(j);
                    if (nodo != null) {
                        String clave = nodo.getClave();
                        Investigacion inv = nodo.getValor();
                        if (clave != null && inv != null) {
                            // llamar a insertar para mantener la lógica centralizada
                            this.insertar(clave, inv);
                        }
                    }
                }
            }
        }
    }
    
    
    /**
     * normaliza claves para evitar duplicados por mayúsculas/espacios
     *
     * @param clave clave original
     * @return cadena normalizada
     */
    private String normalizarClave(String clave) {
        if (clave == null) return "";
        String t = clave.trim().toLowerCase();
        t = t.replaceAll("\\s+", " ");
        return t;
    }
    
}