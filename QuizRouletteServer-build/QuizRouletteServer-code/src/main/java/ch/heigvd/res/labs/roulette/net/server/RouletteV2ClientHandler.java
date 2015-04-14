package ch.heigvd.res.labs.roulette.net.server;

import ch.heigvd.res.labs.roulette.net.protocol.RouletteV2Protocol;
import ch.heigvd.res.labs.roulette.data.IStudentsStore;
import ch.heigvd.res.labs.roulette.data.JsonObjectMapper;
import static ch.heigvd.res.labs.roulette.net.server.RouletteV1ClientHandler.LOG;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the Roulette protocol (version 2).
 *
 * @author Michelle Sakam et Olivier Djeulezeck
 */
public class RouletteV2ClientHandler implements IClientHandler {
    final static Logger LOG = Logger.getLogger(RouletteV2ClientHandler.class.getName());

  private final IStudentsStore store;

  public RouletteV2ClientHandler(IStudentsStore store) {
    this.store = store;
  }

  @Override
  public void handleClientConnection(InputStream is, OutputStream os) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));

    writer.println("Hello. Online HELP is available. Will you find it?");
    writer.flush();

    String command;
    boolean done = false;
    while (!done && ((command = reader.readLine()) != null)) {
      LOG.log(Level.INFO, "COMMAND: {0}", command);
      switch (command.toUpperCase()) {
          case RouletteV2Protocol.CMD_CLEAR:
              store.clear();
              writer.println(RouletteV2Protocol.RESPONSE_CLEAR_DONE);
            break;
          case RouletteV2Protocol.CMD_LIST:
             writer.println(JsonObjectMapper.toJson(store.listStudents()));
      }  
}
  }
}