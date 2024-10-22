package spring.study._4.forWork.indexer.module;


import lombok.RequiredArgsConstructor;

public class IndexJobRunner implements Runnable {

    private Job job;
    public IndexJobRunner(Job job) {
        this.job = job;
        job.setStatus();

    }

    @Override
    public void run() {
        System.out.println("Job is running : " + job);
        sleepRun();
        System.out.println("Job is done : " + job);
    }


    private void sleepRun(){
        try {
            Thread.sleep(5000);
            System.out.println("Job is Sleeping : " + job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
