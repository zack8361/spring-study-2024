package spring.study._4.forWork.indexer.module;


import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import spring.study._4.forWork.indexer.preProcess.PreProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class IndexJobRunner implements Runnable {
    public enum STATUS { READY, RUNNING, SUCCESS, ERROR, STOP }
    private final Job job;
    private boolean preProcess;
    private boolean autoDynamic;
    private IndexService service;
    private Ingester ingester;
    private final Gson gson = new Gson();
    boolean enableRemoteCmd;
    String remoteCmdUrl;

    public IndexJobRunner(Job job) {
        this.job = job;
        service = null;
        ingester = null;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            Map<String, Object> payload = job.getRequest();
            job.setStatus(STATUS.RUNNING.name());
            log.info("{}",gson.toJson(payload));

            // 전처리 컬렉션인지 체크
            preProcess = (boolean) payload.getOrDefault("preProcess", false);

            if(preProcess) {
                //전처리시 동적색인 ON/OFF 처리
                autoDynamic = (Boolean) payload.getOrDefault("autoDynamic", false);

                // 전처리 컬렉션일때 (곽햄 질문 1)
                if (autoDynamic) {
                    disableAutoDynamic();
                }
                PreProcess process = new PreProcess.EmptyPreProcess();
                process.starter(job);
                return;
            }

            log.info("Started IndexJobRunner");

            // 공통(ES 호스트)
            String host = (String) payload.get("host");
            // 공통(ES 포트)
            Integer port = (Integer) payload.get("port");

            String esUsername = (String) payload.getOrDefault("esUsername", null);
            String esPassword = (String) payload.getOrDefault("esPassword", null);

            // http, https
            String scheme = (String) payload.get("scheme");
            // 인덱스명
            String index = (String) payload.get("index");
            // 소스타입명 : ndjson, json, csv, jdbc 등등
            String type = (String) payload.get("type");

            // index가 존재하면 색인전에 지우는지 여부. indexTemplate 이 없다면 맵핑과 셋팅은 모두 사라질수 있다.
            Boolean reset = (Boolean) payload.getOrDefault("reset", true);

            // 필터 클래스 이름. 패키지명까지 포함해야 한다. 예) com.danawa.fastcatx.filter.MockFilter
            String filterClassName = (String) payload.get("filterClass");

            // ES bulk API 사용시 벌크갯수.
            Integer bulkSize = (Integer) payload.get("bulkSize");
            Integer threadSize = (Integer) payload.getOrDefault("threadSize", 1);
            String pipeLine = (String) payload.getOrDefault("pipeLine","");
            Boolean checkData = (Boolean) payload.getOrDefault("checkData", false);


            Map<String, Object> indexSettings;
            try {
                indexSettings = (Map<String, Object>) payload.get("indexSettings");
                if(indexSettings == null) {
                    throw new ClassCastException();
                }
            } catch (Exception e) {
                indexSettings = new HashMap<>();
            }

            if(type.equals("procedure-link")) {

                String driverClassName = (String) payload.get("driverClassName");
                String url = (String) payload.get("url");
                String user = (String) payload.get("user");
                String password = (String) payload.get("password");
                String procedureName = (String) payload.getOrDefault("procedureName","PRLINKPRODUCT"); //PRSEARCHPRODUCT
                String groupSeqs = (String) payload.get("groupSeqs");
                String dumpFormat = (String) payload.get("dumpFormat"); //ndjson, konan
                String rsyncPath = (String) payload.get("rsyncPath"); //rsync - Full Path
                String rsyncIp = (String) payload.get("rsyncIp"); // rsync IP
                String bwlimit = (String) payload.getOrDefault("bwlimit","0"); // rsync 전송속도 - 1024 = 1m/s
                boolean procedureSkip  = (Boolean) payload.getOrDefault("procedureSkip",false); // 프로시저 스킵 여부
                boolean rsyncSkip = (Boolean) payload.getOrDefault("rsyncSkip",false); // rsync 스킵 여부
                String procedureThreads = (String) payload.getOrDefault("procedureThreads","4"); // rsync 스킵 여부
                //            원격 호출 사용 여부 (패스트캣 임시로직)
                enableRemoteCmd = (boolean) payload.getOrDefault("enableRemoteCmd",false);
                //            원격 호출 URL (패스트캣 임시로직)
                remoteCmdUrl = (String) payload.getOrDefault("remoteCmdUrl","");

                String[] groupSeqLists = groupSeqs.split(",");

                if(!procedureSkip) {
                    ExecutorService threadPool = Executors.newFixedThreadPool(Integer.parseInt(procedureThreads));
                    List threadResults = new ArrayList<Future<Object>>();
                    for (String groupSeq : groupSeqLists) {
                         Integer groupSeqNumber = Integer.parseInt(groupSeq);
                        Callable callable = new Callable() {
                            @Override
                            public Object call() throws Exception {
                                Map<String, Object> result = new HashMap<>();
                                result.put("groupSeq", groupSeqNumber);
                                result.put("result","procedureCall");
                            }
                        };
                    }
                }
            }
        }
        catch (Exception e) {
            log.error("Error", e);
        }
    }

    private void disableAutoDynamic() {
        System.out.println("동적 색인 끄기");
    }
}

