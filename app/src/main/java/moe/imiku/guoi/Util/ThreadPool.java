package moe.imiku.guoi.Util;

import java.util.ArrayList;

public class ThreadPool {
    private ArrayList<Runnable> runnable_list;
    private ArrayList<Thread> thread_list;
    private boolean stop = false;
    private boolean enforce_stop = false;
    private boolean runnable_list_finished = false;
    private boolean thread_list_finished = false;
    private int complete_count = 0;

    private int max_thread_in_same_time;
    private Thread run_thread = new Thread(() -> {
        runnable_list_finished = false;
        for (; !enforce_stop && (!stop || runnable_list.size() > 0); ) {
            if (runnable_list.size() > 0) {
                if (thread_list.size() < max_thread_in_same_time) {
                    activeThread();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        runnable_list_finished = true;
    });

    private Thread check_thread = new Thread(() -> {
        for (; !runnable_list_finished; ) {
            for (int i = 0; i < thread_list.size(); ) {
                if (!thread_list.get(i).isAlive()) {
                    thread_list.remove(i);
                    complete_count++;
                } else i++;
            }
            thread_list_finished = thread_list.size() == 0;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public ThreadPool (int max_thread_in_same_time) {
        this.max_thread_in_same_time = max_thread_in_same_time;

        runnable_list = new ArrayList<>();
        thread_list = new ArrayList<>();

        run_thread.start();
        check_thread.start();
    }

    private void activeThread() {
        Thread t = new Thread(runnable_list.get(0));
        runnable_list.remove(0);
        thread_list.add(t);
        t.start();
    }

    public void addNewThread (Runnable runnable) {
        runnable_list.add(runnable);
    }

    @Deprecated
    public void addNewThreadEnforce (Runnable runnable) {
        runnable_list.add(runnable);
    }

    public int getComplete_count() {
        return complete_count;
    }

    public void stop() {
        stop = true;
    }

    public void enforce_stop() {
        enforce_stop = true;
    }

    public boolean isFinished () {
        return runnable_list_finished && thread_list_finished;
    }

    public void waitForFinish() {
        try {
            run_thread.join();
            check_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
