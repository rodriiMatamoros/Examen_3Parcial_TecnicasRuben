package org.example;
import java.util.*;
import java.util.*;

public class Grafo {
    private Map<String, List<Arco>> mapa;

    public Grafo() {
        this.mapa = new HashMap<>();
    }

    public void agregarPuerto(String puerto) {
        mapa.put(puerto, new ArrayList<>());
    }

    public void agregarArco(String puerto1, String puerto2, int distancia) {
        Arco arco1 = new Arco(puerto1, puerto2, distancia);
        Arco arco2 = new Arco(puerto2, puerto1, distancia);
        mapa.get(puerto1).add(arco1);
        mapa.get(puerto2).add(arco2);
    }

    public void barridoEnProfundidad(String origen) {
        Set<String> visitados = new HashSet<>();
        Stack<String> pila = new Stack<>();
        pila.push(origen);
        while (!pila.isEmpty()) {
            String actual = pila.pop();
            if (!visitados.contains(actual)) {
                visitados.add(actual);
                System.out.print(actual + " ");
                for (Arco arco : mapa.get(actual)) {
                    pila.push(arco.destino);
                }
            }
        }
    }

    public int caminoMasCorto(String origen, String destino) {
        Map<String, Integer> distancias = new HashMap<>();
        Set<String> visitados = new HashSet<>();
        PriorityQueue<String> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(distancias::get));
        distancias.put(origen, 0);
        colaPrioridad.offer(origen);
        while (!colaPrioridad.isEmpty()) {
            String actual = colaPrioridad.poll();
            if (actual.equals(destino)) {
                return distancias.get(actual);
            }
            if (!visitados.contains(actual)) {
                visitados.add(actual);
                for (Arco arco : mapa.get(actual)) {
                    int nuevaDistancia = distancias.get(actual) + arco.distancia;
                    if (!distancias.containsKey(arco.destino) || nuevaDistancia < distancias.get(arco.destino)) {
                        distancias.put(arco.destino, nuevaDistancia);
                        colaPrioridad.offer(arco.destino);
                    }
                }
            }
        }
        return -1;
    }

    public String eliminarPuertoConMasAristas() {
        int maxAristas = -1;
        String puertoMaxAristas = null;
        for (Map.Entry<String, List<Arco>> entry : mapa.entrySet()) {
            int aristas = entry.getValue().size();
            if (aristas > maxAristas) {
                maxAristas = aristas;
                puertoMaxAristas = entry.getKey();
            }
        }
        mapa.remove(puertoMaxAristas);
        for (Map.Entry<String, List<Arco>> entry : mapa.entrySet()) {
            Iterator<Arco> iterator = entry.getValue().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().destino.equals(puertoMaxAristas)) {
                    iterator.remove();
                }
            }
        }
        return puertoMaxAristas;
    }

    private static class Arco {
        String origen;
        String destino;
        int distancia;

        public Arco(String origen, String destino, int distancia) {
            this.origen = origen;
            this.destino = destino;
            this.distancia = distancia;
        }
    }

    public static class Main {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            Grafo grafo = new Grafo();
            // Agregar puerto de origen
            grafo.agregarPuerto("Puerto Madero");
            // Pedir al usuario que ingrese 6 puertos
            System.out.println("Ingrese 5 puertos (no incluyendo Puerto Madero ni Puerto Rodas):");
            for (int i = 0; i < 5; i++) {
                String puerto = sc.nextLine();
                grafo.agregarPuerto(puerto);
            }
            // Generar distancias aleatorias para las aristas
            Random rand = new Random();
            for (String puerto1 : grafo.mapa.keySet()) {
                for (String puerto2 : grafo.mapa.keySet()) {
                    if (!puerto1.equals(puerto2)) {
                        grafo.agregarArco(puerto1, puerto2, rand.nextInt(50) + 1);
                    }
                }
            }


            // Mostrar el resultado de las cuatro tareas mencionadas
            System.out.print("Barrido en profundidad: ");
            grafo.barridoEnProfundidad("Puerto Madero");
            System.out.println();
            int distancia = grafo.caminoMasCorto("Puerto Madero", "Puerto Rodas");
            if (distancia < 0) {
                System.out.println("No hay camino de Puerto Madero a Puerto Rodas.");
            } else {
                System.out.println("Distancia de Puerto Madero a Puerto Rodas: " + distancia + " Mn.");
            }
            String puertoEliminado = grafo.eliminarPuertoConMasAristas();
            System.out.println("Puerto eliminado: " + puertoEliminado);
            System.out.println("\nNuevo grafo:");
            for (String puerto : grafo.mapa.keySet()) {
                System.out.print("Puerto " + puerto + " -> ");
                for (Grafo.Arco arco : grafo.mapa.get(puerto)) {
                    System.out.print("(" + arco.destino + " tiene " + arco.distancia + " Mn) ");
                }
                System.out.println();
            }
        }
    }

}

