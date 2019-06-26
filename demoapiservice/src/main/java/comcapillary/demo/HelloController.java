package comcapillary.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class HelloController {

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping("/hello/{name}")
  public String sayHello(@PathVariable String name) {
    String visitorService = System.getenv("VISITOR_SERVICE");
    Integer visitCount = restTemplate.getForEntity(String.format("http://%s/visits/%s", visitorService, name), Integer.class).getBody();
    return String.format("Hello %s. You have visited %d times.", name, visitCount);
  }

  @GetMapping("/loadtest/{threads}")
  public Double loadTest(@PathVariable Integer threads) {
    ExecutorService executorService = Executors.newFixedThreadPool(threads);
    try {
      long start = System.currentTimeMillis();
      Stream.generate(() -> UUID.randomUUID().toString())
        .limit(10000)
        .map(x -> executorService.submit(() -> sayHello(x)))
        .collect(Collectors.toList()).stream()
        .forEach(x -> {
          try {
            x.get();
          } catch (Throwable t) {
            throw new RuntimeException(t);
          }
        });
      long timeTaken = System.currentTimeMillis() - start;
      executorService.shutdownNow();
      return 1000.0 / timeTaken * 1000;
    } finally {
      executorService.shutdownNow();
    }
  }
}
