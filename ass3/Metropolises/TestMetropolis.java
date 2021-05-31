package Metropolises;

import Metropolises.Metropolis;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMetropolis {
    private static final String NEW_NAME = "Rome";
    private static final String NEW_CONTINENT = "Europe";
    private static final long NEW_POPULATION = 24;

    Metropolis metropolis;
    @BeforeAll
    public void setup() {
        metropolis = new Metropolis("Mumbai", "Asia", 123);
    }

    @Test
    public void testToString() {
        assertEquals("Metropolis{name='Mumbai', continent='Asia', population=123}", metropolis.toString());
    }

    @Test
    public void testGetters() {
        assertEquals("Mumbai", metropolis.getName());
        assertEquals("Asia", metropolis.getContinent());
        assertEquals(123, metropolis.getPopulation());
    }

    @Test
    public void testSetters() {
        metropolis.setName(NEW_NAME);
        metropolis.setContinent(NEW_CONTINENT);
        metropolis.setPopulation(NEW_POPULATION);
        assertEquals(NEW_NAME, metropolis.getName());
        assertEquals(NEW_CONTINENT, metropolis.getContinent());
        assertEquals(NEW_POPULATION, metropolis.getPopulation());
    }
}
