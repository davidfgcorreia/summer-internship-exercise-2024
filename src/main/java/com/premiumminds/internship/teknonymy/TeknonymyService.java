package com.premiumminds.internship.teknonymy;

import com.premiumminds.internship.teknonymy.Person;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

class TeknonymyService implements ITeknonymyService {

    /**
     * Method to get a Person Teknonymy Name
     * 
     * @param Person person
     * @return String which is the Teknonymy Name 
     */
    @Override
    public String getTeknonymy(Person person) {
        // Verifica se a pessoa é nula ou não tem filhos
        if (person == null || person.children() == null || person.children().length == 0) {
            return "";
        }

        // Utilizamos uma fila para a BFS (Busca em Largura)
        Queue<Person> queue = new LinkedList<>();
        queue.add(person);

        // Variáveis para guardar o descendente mais velho e a geração mais distante
        Person oldestDescendant = null;
        int maxGeneration = -1;

        // Mapa para armazenar a geração de cada pessoa
        Map<Person, Integer> generationMap = new HashMap<>();
        generationMap.put(person, 0);

        // Correr uma BFS
        while (!queue.isEmpty()) {
            Person current = queue.poll();
            int currentGeneration = generationMap.get(current);

            // Verifica se o descendente atual é o mais velho na geração mais distante
            if (current != person && 
                (currentGeneration > maxGeneration ||
                (currentGeneration == maxGeneration && current.dateOfBirth().isBefore(oldestDescendant.dateOfBirth())))) {
                
                oldestDescendant = current;
                maxGeneration = currentGeneration;
            }

            // Adiciona os filhos à fila e atualiza suas gerações
            if (current.children() != null) {
                for (Person child : current.children()) {
                    queue.add(child);
                    generationMap.put(child, currentGeneration + 1);
                }
            }
        }

        // Constrói o tecnónimo baseado no sexo da pessoa
        StringBuilder teknonymy = new StringBuilder();
        if (maxGeneration > 0) {
            if (person.sex() == 'M') {
                appendTitle(teknonymy, "father", "grandfather", maxGeneration);
            } else if (person.sex() == 'F') {
                appendTitle(teknonymy, "mother", "grandmother", maxGeneration);
            }
            teknonymy.append(" of ").append(oldestDescendant.name());
        }

        return teknonymy.toString();
    }

    /**
     * Append the appropriate title based on the number of generations.
     *
     * @param teknonymy StringBuilder to append the title to
     * @param parentTitle Title to use for the immediate parent
     * @param grandParentTitle Title to use for grandparents
     * @param generations Number of generations
     */
    private void appendTitle(StringBuilder teknonymy, String parentTitle, String grandParentTitle, int generations) {
        if (generations == 1) {
            teknonymy.append(parentTitle);
        } else {
            for (int i = 2; i < generations; i++) {
                teknonymy.append("great-");
            }
            teknonymy.append(grandParentTitle);
        }
    }
}