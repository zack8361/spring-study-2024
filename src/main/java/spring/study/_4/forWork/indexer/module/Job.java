package spring.study._4.forWork.indexer.module;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;


@Getter
@Setter
public class Job implements Serializable {
    private UUID id;
    private Map<String, Object> request;
    private String status;
    private String error;
    private long startTime;
    private long endTime;
    private String action;

    protected Job(UUID id, Map<String, Object> request,String action) {
        this.id = id;
        this.request = request;
        this.action = action;
    }

    public static Job of(UUID id, Map<String, Object> request, String action) {
        return new Job(id, request, action);
    }
}
