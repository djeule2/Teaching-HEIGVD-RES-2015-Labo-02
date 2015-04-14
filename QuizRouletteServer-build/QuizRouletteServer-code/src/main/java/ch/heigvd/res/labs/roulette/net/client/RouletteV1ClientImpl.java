package ch.heigvd.res.labs.roulette.net.client;

import ch.heigvd.res.labs.roulette.data.EmptyStoreException;
import ch.heigvd.res.labs.roulette.data.JsonObjectMapper;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV1Protocol;
import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.data.StudentsStoreImpl;
import ch.heigvd.res.labs.roulette.net.protocol.InfoCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.RandomCommandResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the client side of the protocol specification (version
 * 1).
 *
 * @author Olivier Liechti
 */
public class RouletteV1ClientImpl implements IRouletteV1Client {

    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;
    boolean connected = true;
    StudentsStoreImpl studentStore;
    InfoCommandResponse inforeponse;

    private static final Logger LOG = Logger.getLogger(RouletteV1ClientImpl.class.getName());

    @Override
    public void connect(String server, int port) throws IOException {
        connected = true;
        clientSocket = new Socket(server, port);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnect() throws IOException {
        //LOG.log(Level.INFO,"{0} has requested to be disconnected." );
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        connected = false;
        out.println(RouletteV1Protocol.CMD_BYE);
        out.close();
        in.close();
        clientSocket.close();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isConnected() {
        return connected;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadStudent(String fullname) throws IOException {
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.println(RouletteV1Protocol.CMD_LOAD);
        out.flush();
        out.println(fullname);
        out.flush();
        out.println(RouletteV1Protocol.CMD_LOAD_ENDOFDATA_MARKER);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadStudents(List<Student> students) throws IOException {
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.println(RouletteV1Protocol.CMD_LOAD);
        for (Student s : students) {
            out.println(s.getFullname());
        }
        out.println(RouletteV1Protocol.CMD_LOAD_ENDOFDATA_MARKER);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Student pickRandomStudent() throws EmptyStoreException, IOException {
        String commandLine;
       

        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        studentStore  = new StudentsStoreImpl();
        out.println(RouletteV1Protocol.CMD_INFO);
        
        if (connected && studentStore.getNumberOfStudents() != 0) {
         //   return Student.fromJson(commandLine);
        return studentStore.pickRandomStudent();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        throw new EmptyStoreException();
    }
    

    @Override
    public int getNumberOfStudents() throws IOException {
        String commandLine;
        
        connected = true;
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        studentStore  = new StudentsStoreImpl();

        out.println(RouletteV1Protocol.CMD_INFO);
        if (connected && ((commandLine = in.readLine()) != null)) {
            //return (JsonObjectMapper.parseJson(commandLine, InfoCommandResponse.class).getNumberOfStudents());
            return studentStore.getNumberOfStudents();

        }
        return 0;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProtocolVersion() throws IOException {
        String commandLine;
        inforeponse = new InfoCommandResponse();
        connected = true;
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.println(RouletteV1Protocol.CMD_INFO);
        //commandLine = in.readLine();
        // if(connected && ((commandLine = in.readLine()) != null)){
        //return (JsonObjectMapper.parseJson(commandLine, InfoCommandResponse.class).getProtocolVersion());
        return inforeponse.getProtocolVersion();
    //}
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
