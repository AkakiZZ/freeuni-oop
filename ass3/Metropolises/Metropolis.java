package Metropolises;

import java.util.Objects;

public class Metropolis {
    private String name;
    private String continent;
    private long population;

    public Metropolis(String name, String continent, long population) {
        this.name = name;
        this.continent = continent;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metropolis that = (Metropolis) o;
        return population == that.population && Objects.equals(name, that.name) && Objects.equals(continent, that.continent);
    }

    @Override
    public String toString() {
        return "Metropolis{" +
                "name='" + name + '\'' +
                ", continent='" + continent + '\'' +
                ", population=" + population +
                '}';
    }
}
