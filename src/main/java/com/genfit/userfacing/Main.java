package com.genfit.userfacing;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.genfit.userfacing.handlers.DiscoverPageHandler;
import com.genfit.userfacing.handlers.FrontpageHandler;
import com.genfit.userfacing.handlers.ItemPageHandler;
import com.genfit.userfacing.handlers.LoginHandler;
import com.genfit.userfacing.handlers.OutfitPageHandler;
import com.genfit.userfacing.handlers.SignupHandler;
import com.genfit.userfacing.handlers.UserItemRetriever;
import com.genfit.userfacing.handlers.UserOutfitRetriever;
import com.genfit.userfacing.handlers.UserPageHandler;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author lhuang21
 */
public final class Main {
  public static final int DEFAULT_PORT = 4567;
  public static final String URL_ENCODING = "UTF-8";
  public static final Gson GSON = new Gson();
  private MainREPL repl;
  private String[] args;
  private GenFitApp mainApp;

  private Main(String[] args) {
    this.args = args;
  }

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(this.args);
    this.mainApp = new GenFitApp();
    this.repl = new MainREPL(this.mainApp);

    if (options.has("gui")) {
      System.out.println("gui");
      this.runSparkServer((int) options.valueOf("port"));
    }

    this.repl.runREPL();
    // if reached here, IREPL has exited
    Spark.stop();
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
//    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup spark routes for main pages

//    Spark.get("/", new UserPageHandler(this.mainApp), freeMarker);
    Spark.get("/", new FrontpageHandler(), freeMarker);
    Spark.get("/user", new UserPageHandler(this.mainApp), freeMarker);
    Spark.get("/items", new ItemPageHandler(this.mainApp), freeMarker);
    Spark.get("/outfits", new OutfitPageHandler(this.mainApp), freeMarker);
    Spark.get("/discover", new DiscoverPageHandler(this.mainApp), freeMarker);

    // Setup spark routes for getting data (ie. json endpoints)
    Spark.post("/login", new LoginHandler(this.mainApp.getDb()));
    Spark.post("/signup", new SignupHandler(this.mainApp.getDb()));
    Spark.post("/userItems", new UserItemRetriever());
    Spark.post("/userOutfits", new UserOutfitRetriever());

  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author lhuang21
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
