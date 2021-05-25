import java.util.Random;

public class MontyHall implements Runnable {

    public static int simulations;
    public static int swapWorked;
    private Thread thread;
    private String thread_id;
    private Random rand;
    public static int finished = 0;
    final static int iterations_per_thread = 100000000;
    final static int quarts_of_iterations = iterations_per_thread / 4;

    public static void main(String[] args) throws InterruptedException {
        MontyHall[] thread_arr = new MontyHall[10];
        for (int i = 0; i < thread_arr.length; i++) {
            thread_arr[i] = new MontyHall("" + (i + 1));
            thread_arr[i].start();
        }
        while (finished < thread_arr.length) {
            Thread.sleep(500);
        }
        int total_iterations = thread_arr.length * iterations_per_thread;
        double ratio = (double) swapWorked / total_iterations;
        int percentage = (int) (ratio * 100.0);
        System.out.println("\n\nPercentage of success with swap: " + percentage + "%\nRatio of success: " + ratio + "\nTotal Simulations: " + iterations_per_thread * thread_arr.length);
    }

    public MontyHall(String thread_id) {
        this.thread_id = thread_id;
        System.out.println("Created Monty Hall Thread #" + this.thread_id);
    }

    public void start() {
        System.out.println("Starting thread #" + thread_id);
        if (thread == null) {
            thread = new Thread(this, thread_id);
            rand = new Random();
            thread.start();
        }
    }

    @Override
    public void run() {
        int swap_success = 0;
        for (int i = 0; i <= iterations_per_thread; i++) {
            int prize_door = rand.nextInt(3);
            int chosen_door = rand.nextInt(3);
            int index_open_door = other_door(prize_door, chosen_door);
            int index_swap_door = other_door(index_open_door, chosen_door);
            if (index_swap_door == prize_door) {
                swap_success++;
            }
            if (i % quarts_of_iterations == 0) {
                String progress = String.format("Thread #%s: %d%% Done | #Successful Swaps: %d | Total Swaps: %d", thread_id, ((i / quarts_of_iterations) * 25),
                        swap_success, i);
                System.out.println(progress);
            }
        }
        swapWorked += swap_success;
        System.out.println("Thread #" + thread_id + " finished");
        finished++;
    }

    private int other_door(int prize_door, int chosen_door) {
        if (prize_door == chosen_door) {
            boolean LorR = rand.nextBoolean();
            switch (prize_door) {
                case 0:
                    if (LorR) {
                        return 1;
                    } else {
                        return 2;
                    }
                case 1:
                    if (LorR) {
                        return 0;
                    } else {
                        return 2;
                    }
                case 2:
                    if (LorR) {
                        return 0;
                    } else {
                        return 1;
                    }
                default:
                    break;
            }
        }
        int available_door = 3 - prize_door - chosen_door;
        return available_door;
    }

}
