package com.genfit.userfacing;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.genfit.userfacing.handlers.*;
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
      this.runSparkServer((int) options.valueOf("port"));
    }

    this.repl.runREPL();
    // if reached here, IREPL has exited
    this.mainApp.getDb().closeConnection();
    Spark.stop();
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup spark routes for main pages

//    Spark.get("/", new UserPageHandler(this.mainApp), freeMarker);
    Spark.get("/", new FrontpageHandler(), freeMarker);
    Spark.get("/user", new UserPageHandler(this.mainApp), freeMarker);
    Spark.get("/items", new ItemPageHandler(this.mainApp), freeMarker);
    Spark.get("/outfits", new OutfitPageHandler(this.mainApp), freeMarker);
    Spark.get("/discover", new DiscoverPageHandler(this.mainApp), freeMarker);

    // Setup spark routes for getting data (ie. json endpoints)
    Spark.post("/userform", new UserformHandler(this.mainApp.getDb()));
    Spark.post("/userItems", new UserItemRetriever(this.mainApp));
    Spark.post("/userOutfits", new UserOutfitRetriever(this.mainApp));
    Spark.post("/outfitComponents",
        new OutfitComponentsRetriever(this.mainApp));
    Spark.post("/outfitByAttribute",
        new OutfitByAttributeRetriever(this.mainApp));
    Spark.post("/itemSuggestions", new ItemSuggestionRetriever(this.mainApp));

    // Setup post requests for forms and buttons
    Spark.post("/addItem", new AddItemHandler(this.mainApp));
    Spark.post("/deleteItem", new DeleteItemHandler(this.mainApp));
    Spark.post("/getOutfitWithItem", new OutfitWithItemRetriever(this.mainApp));
    Spark.post("/singleItem", new SingleItemRetriever(this.mainApp));

    Spark.post("/like", new OutfitLikeHandler(this.mainApp));
    Spark.post("/discover", new DiscoverOutfitRetriever(this.mainApp));
    Spark.post("/liked", new LikedOutfitRetriever(this.mainApp));
    Spark.post("/addOutfit", new AddOutfitHandler(this.mainApp));
    Spark.post("/deleteOutfit", new DeleteOutfitHandler(this.mainApp));
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
