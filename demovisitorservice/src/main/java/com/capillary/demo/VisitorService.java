package com.capillary.demo;

import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class VisitorService {

  private ConcurrentHashMap<String, AtomicInteger> visitorCount = new ConcurrentHashMap<>();

  @GetMapping("/visits/{name}")
  public Integer getVisits(@PathVariable String name) throws InterruptedException {
    for(int i = 0; i < 500; i++ ) {
      Thread.sleep(1);
    }
    int visits = visitorCount.computeIfAbsent(name, s -> new AtomicInteger(0)).incrementAndGet();
    return visits;
  }

}
