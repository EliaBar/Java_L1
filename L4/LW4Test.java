import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LW4Test {

    // ✅ 1. Перевірка звичайного заповнення
    @Test
    public void testNormalBoarding() throws Exception {
        Bus bus = new Bus(3);
        bus.boardPassenger(new Human("Anna"));
        bus.boardPassenger(new Policeman("Bob"));
        assertEquals(2, bus.getOccupiedSeats());
    }

    // ✅ 2. Перевірка обмеження місць
    @Test
    public void testOverCapacityThrowsException() {
        Taxi taxi = new Taxi(1);
        assertThrows(Exception.class, () -> {
            taxi.boardPassenger(new Human("Tom"));
            taxi.boardPassenger(new Human("Alex"));
        });
    }

    // ✅ 3. Перевірка типів пасажирів
    @Test
    public void testTypeRestriction() throws Exception {
        FireTruck fireTruck = new FireTruck(2);
        fireTruck.boardPassenger(new Firefighter("John"));
        // ❌ Наступний рядок не скомпілюється, якщо розкоментувати:
        // fireTruck.boardPassenger(new Policeman("Bob"));
        assertEquals(1, fireTruck.getOccupiedSeats());
    }

    // ✅ 4. Перевірка винятку при видаленні неіснуючого пасажира
    @Test
    public void testRemoveNonexistentPassenger() throws Exception {
        Bus bus = new Bus(2);
        Human anna = new Human("Anna");
        bus.boardPassenger(anna);
        Human tom = new Human("Tom");
        assertThrows(Exception.class, () -> bus.removePassenger(tom));
    }

    // ✅ 5. Перевірка підрахунку пасажирів на дорозі
    @Test
    public void testCountHumansOnRoad() throws Exception {
        Road road = new Road();

        Bus bus = new Bus(3);
        bus.boardPassenger(new Human("Anna"));
        bus.boardPassenger(new Policeman("Bob"));

        Taxi taxi = new Taxi(2);
        taxi.boardPassenger(new Human("Tom"));

        FireTruck fireTruck = new FireTruck(2);
        fireTruck.boardPassenger(new Firefighter("John"));

        PoliceCar policeCar = new PoliceCar(1);
        policeCar.boardPassenger(new Policeman("Max"));

        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);

        assertEquals(5, road.getCountOfHumans());
    }
}
