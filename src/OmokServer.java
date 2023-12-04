import java.io.*;
import java.net.*;
import java.util.*;

public class OmokServer {
    private int port = 8100;
    private List<PrintWriter> clients;
    public OmokServer() {
        clients = new LinkedList<PrintWriter>();
    }

    public OmokServer(int port) {
        this.port = port;
        clients = new LinkedList<PrintWriter>();
    }

    public void start() {
        System.out.println("Omok server started on port "
                + port + "!");
        try {
            ServerSocket s = new ServerSocket(port);
            for (;;) {
                Socket incoming = s.accept();
                new ClientHandler(incoming).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Omok server stopped.");
    }

    public int getPortNumber(){
        return port;
    }

    private void addClient(PrintWriter out) {
        synchronized(clients) {
            clients.add(out);
        }
    }

    private void removeClient(PrintWriter out) {
        synchronized(clients) {
            clients.remove(out);
        }
    }


    private void broadcast(String msg) {
        for (PrintWriter out: clients) {
            out.println(msg);
            out.flush();
        }
    }

    public static void main(String[] args) {
        new OmokServer().start();
    }

    class ClientHandler extends Thread {
        private Socket incoming;
        public ClientHandler(Socket incoming) {
            this.incoming = incoming;
        }

        public void run() {
            PrintWriter out = null;
            try {
                out = new PrintWriter(
                        new OutputStreamWriter(incoming.getOutputStream()));

                OmokServer.this.addClient(out);

                out.println("You are connected to server started on port " + getPortNumber() + ".");
                out.flush();

                BufferedReader in
                        = new BufferedReader(
                        new InputStreamReader(incoming.getInputStream()));
                for (;;) {
                    String msg = in.readLine();
                    if (msg == null) {
                        break;
                    } else {
                        if (msg.trim().equals("BYE"))
                            break;
                        System.out.println("Received: " + msg);
                        OmokServer.this.broadcast(msg);
                    }
                }
                incoming.close();
                OmokServer.this.removeClient(out);
            } catch (Exception e) {
                if (out != null) {
                    OmokServer.this.removeClient(out);
                }
                e.printStackTrace();
            }
        }
    }
}