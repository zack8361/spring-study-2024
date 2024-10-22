package spring.study._4.forWork.indexer.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class IndexJobManager {

    private ConcurrentHashMap<UUID, Job> jobs = new ConcurrentHashMap<>();

    public Job start(String action, Map<String, Object> payload) {
        UUID id = genId();
        Job job = Job.of(id, payload, action);
        jobs.put(id, job);
        log.info("job Id : {}, {}", id, action);
        if("FULL_INDEX".equalsIgnoreCase(action)) {
            new Thread(new IndexJobRunner(job)).start();
        }
        return job;
    }


    private UUID genId() {
        UUID uuid = UUID.randomUUID();
        while (jobs.containsKey(uuid)) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }
}
