package org.swellrt.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PadWriterMain {



  public static String[] defaultAppArgs = {
      "/home/pablo/Development/projects/swellrt-selenium/webdriver/chromedriver,/home/pablo/Development/projects/swellrt-selenium/webdriver/geckodriver",
      "https://jetpad.net/edit/jp-test-1",
      "/home/pablo/Development/projects/swellrt-selenium/input/input-1.txt,4,firefox",
      "/home/pablo/Development/projects/swellrt-selenium/input/input-2.txt,10"

  };
  /*
   * "/home/pablo/Development/projects/swellrt-selenium/input/input-2.txt,8,firefox",
   * "/home/pablo/Development/projects/swellrt-selenium/input/input-1.txt,10"
   */

  public static void main(String[] args) throws IOException {

    if (args.length < 4) {
      System.out.println("No arguments");
      args = defaultAppArgs;
    }

    String[] browsers = args[0].split(",");
    Arrays.stream(browsers).forEach(browser -> {

      if (browser.contains("chrome")) {
        System.setProperty("webdriver.chrome.driver", browser);
      } else if (browser.contains("firefox") || browser.contains("gecko")) {
        System.setProperty("webdriver.gecko.driver", browser);
      }

    });

    String padUrl = args[1];

    List<PadWriter> tasks =
        Arrays.stream(args)
            .skip(2)
        .flatMap(s -> {
          String[] params = s.split(",");
              String browser = params.length >= 3 ? params[2] : "chrome";
              return Stream
                  .of(new PadWriter(params[0], padUrl, Integer.valueOf(params[1]), browser));
         })
        .collect(Collectors.<PadWriter> toList());

    ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
    try {
      List<Future<Optional<String>>> taskResultList = executor.invokeAll(tasks);

      boolean completed = false;
      while (!completed) {
        Thread.sleep(2000);
        completed = taskResultList.stream().filter(future -> future.isDone())
            .count() == taskResultList.size();

      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Press key to close...");
    System.in.read();
    tasks.forEach(t -> {
      t.close();
    });
    executor.shutdown();


   }
}
