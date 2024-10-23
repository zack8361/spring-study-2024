package spring.study._4.forWork.indexer.preProcess;

import lombok.extern.slf4j.Slf4j;
import spring.study._4.forWork.indexer.module.Job;

import java.util.Map;

public interface PreProcess {
    void start() throws Exception;

    static PreProcess of(String className, Job job) throws Exception {
        String classFullName = "spring.study._4.forWork.indexer.preProcess." + className;
        Class klass = Class.forName(classFullName);

        Object instance = null;
        if(klass != null) {
            instance = klass.getConstructor(Job.class).newInstance(job);
        }
        return (PreProcess) instance;
    }

    default void starter(Job job) throws Exception {
        Map<String, Object> payload = job.getRequest();
        String className = (String) payload.getOrDefault("type", "");
        of(className, job).start();
    }


    @Slf4j
    class EmptyPreProcess implements PreProcess {
        @Override
        public void start() {
            System.out.println("EmptyPreProcess !!!!!!!!!!!!!!!");
        }
    }
}
