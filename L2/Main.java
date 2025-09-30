import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();

        Person original = new Person("Bread", "Peat", 25);

        String json = gson.toJson(original);
        System.out.println("JSON: " + json);

        Person deserialized = gson.fromJson(json, Person.class);
        System.out.println("Deserialized: " + deserialized);

        System.out.println("Objects equal? " + original.equals(deserialized));
    }
}


/* 
javac -cp "lib\gson-2.11.0.jar;." Main.java Person.java     
java -cp "lib\gson-2.11.0.jar;." Main
 */