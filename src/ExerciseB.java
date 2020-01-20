import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ExerciseB {
    public static final int NUM_ITER = 1000000;
    public static final int NUM_ELEMENTS = 500;

    static class MyThread extends Thread {
        private Map<Integer, Integer> database;
        private Random numGenerator;

        MyThread (Map<Integer, Integer> database) {
            this.database = database;
            this.numGenerator = new Random();
        }

        public void run () {
            for (int i = 0; i < NUM_ITER; i++) {
                //select an element to change
                int id = numGenerator.nextInt(NUM_ELEMENTS);

                synchronized (database) {
                    // add/update the element to the database
                    if (database.containsKey(id)) {
                        //update the element
                        Integer element = database.get(id);
                        element += 1;
                        database.put(id, element);
                    } else {
                        //create the element
                        database.put(id, 1);
                    }
                }
            }//for
        }//run
    }//Threadclass

    public static void main (String[] args) throws Exception {
        Map<Integer, Integer> DB = Collections.synchronizedMap(new HashMap<Integer, Integer>());

        Thread a = new MyThread(DB);
        Thread b = new MyThread(DB);
        long start = System.currentTimeMillis();
        a.start();
        b.start();

        a.join();
        b.join();
        long end = System.currentTimeMillis();
        // sum the elements in the map
        int total = 0;
        for(int i=0; i < NUM_ELEMENTS; i++) {
            Integer el = DB.get(i);
            if (el != null){
                System.out.println("Elements in bucket #"+i+":"+el);
                total += DB.get(i);
            }
        }//for
        System.out.println("Total items:"+total);
        System.out.println("Duration in ms:"+(end-start));

    }
}
