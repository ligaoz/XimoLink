import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class link {

    public static void main(String[] args) throws IOException {
        PrintWriter output = null;
        BufferedReader input = null;
        Socket LinkSocket;

        //Check if XimoStudio is running if its not start the program
      /*  if(CheckXimoStudio.isRunning()){
            System.out.println("Two instances of this program cannot " +
                        "be running at the same time.  Exiting now");
        }
        else{
            CheckXimoStudio.onStart();
            CheckXimoStudio.epicHeavyWorkGoesHere();
            CheckXimoStudio.onFinish();
        }
*/
        //Start communicate with XimoStudio
        try {
            LinkSocket = new Socket("127.0.0.1", 2525); //Opening a socket
            System.out.println("Connected to " + LinkSocket.getInetAddress().getHostName());
            input = new BufferedReader(new InputStreamReader(LinkSocket.getInputStream()));//Opening data input stream to receive response from XimoStudio
            output = new PrintWriter(LinkSocket.getOutputStream(), true);//Opening data output stream to send command
            System.out.println("Started talking");
            output.println("pasteClipboard");
            System.out.println("Command paste from clipboard sent");
            String inputLine;
            while (( inputLine = input.readLine()) != null){
                //wait for acknowledgment
                System.out.println("Server: " + inputLine);
                if (inputLine.equals("OK")) { //acknowledgement for paste from clipboard
                    System.out.println("Ack from XimoStudio received");
                }else if(inputLine.equals("ready")) { //Wait until receive command finished from XimoStudio
                    System.out.println("Results copied to clipboard\n Exiting XimoLink");
                    // clean up
                    try {
                        output.close();// close the output stream
                        input.close();// close the input stream
                        LinkSocket.close();// close the socket
                        System.exit(0);
                    }
                    catch (IOException e) {
                        System.out.println(e);
                    }
                }else
                    System.out.println("Unknown command ");

        }
        //Send a notification to XimoStudio to paste the clipboard
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e);
       }
    }
}
