package brickGame;

/**
 * GameEngine is used to is used to handle certain key events
 */
public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    public boolean isStopped = true;
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * setFps method is responsible for setting the fps
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps;
    }

    private synchronized void Update() {
        updateThread = new Thread(() -> {
            while (!updateThread.isInterrupted()) {
                try {
                    onAction.onUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    break;  // loop was not breaking causing the program to enter nextLevel and initBoard indefinitely
                }
            }
        });
        updateThread.start();
    }

    private synchronized void Initialize() {
        onAction.onInit();
    }

    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!physicsThread.isInterrupted()) {
                    try {
                        onAction.onPhysicsUpdate();
                        Thread.sleep(fps);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        break;  // loop was not breaking causing the program to enter nextLevel and initBoard indefinitely
                    }
                }
            }
        });

        physicsThread.start();

    }

    /**
     * start method is responsible for starting the game and initialising the time as 0
     */
    public synchronized void start() {
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    /**
     * stop method used to stop the game
     */
    public synchronized void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();
        }
    }

    private long time = 0;

    private Thread timeThread;

    private synchronized void TimeStart() {
        timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        time++;
                        onAction.onTime(time);
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        });
        timeThread.start();
    }

    /**
     * OnAction is used to update the game when certain key events happen
     */
    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}