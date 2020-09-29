import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.io.IOException;
import java.net.*;

public class Server {
    private DatagramSocket serverSocket;

    public Server() {
        try {
            this.serverSocket = new DatagramSocket(8080);
            System.out.println("Server is running ...");
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                new MyTask(receivePacket);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public class MyTask extends Thread {
        private DatagramPacket receivePacket;
        
        public MyTask(DatagramPacket receivePacket) {
            this.receivePacket = receivePacket;
            this.start();
        }

        @Override
        public void run() {
            this.doMission();
        }

        private void doMission() {
            try {
                System.out.println("Connected");
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String instr = new String(receivePacket.getData(), "UTF-8");
                String res = "";
                res += "Chuoi dao: " + this.reverse(instr) + "\n";
                res += "Chu hoa: " + this.manipulateString(instr, 0).trim() + "\n";
                res += "Chu thuong: " + this.manipulateString(instr, 1).trim() + "\n";
                res += "Vua hoa vua thuong: " + this.manipulateString(instr, 2).trim() + "\n";
                res += this.lengthAndVowel(instr).trim() + "\n";
                byte[] _sendData = res.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(_sendData, _sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }
        
        private String reverse(String s) {
        	String resString = "";
        	for (int i = s.length() - 1; i >= 0; i--) {
        		resString += s.charAt(i) + "";
        	}
        	return resString.trim();
        }
        /**
         * @param inStr string to manipulate
         * @param type  0: to upper; 1: to lower; 2: do both
         * @return string after manipulated
         */
        private String manipulateString(String inStr, int type) {
            String returnStr = "";
            char tempCh;

            for (int i = 0; i < inStr.length(); i++) {
                tempCh = inStr.charAt(i);
                switch (type) {
                    case 0:
                        tempCh = (tempCh >= 97 && tempCh <= 122) ? (char) (tempCh - 32) : tempCh;
                        break;
                    case 1:
                        tempCh = (tempCh >= 65 && tempCh <= 90) ? (char) (tempCh + 32) : tempCh;
                        break;
                    case 2:
                        if (tempCh >= 97 && tempCh <= 122)
                            tempCh = (char) (tempCh - 32);
                        else if (tempCh >= 65 && tempCh <= 90)
                            tempCh = (char) (tempCh + 32);
                        break;
                }
                returnStr += tempCh;
            }
            return returnStr;
        }

        private String lengthAndVowel(String inStr) {
            String returnStr = "";
            	HashMap<String, Integer> dic = new HashMap<String, Integer>();
            int countWord = 0;
            String[] words = inStr.trim().split(" +");
            for (int i = 0; i < words.length; i++) {
                if (words[i].length() > 0 && isWord(words[i])) {
                    countWord++;
                    	if (dic.containsKey(words[i])) {
                    		dic.put(words[i], dic.get(words[i]) + 1);
                    	} else {
                    		dic.put(words[i], 1);
                    	}
                }
            }
            returnStr += "So tu: " + countWord;

            returnStr += "\nTan suat xuat hien tu: \n";
            	for (String keyString : dic.keySet()) {
            		returnStr += keyString + ": " + dic.get(keyString) + "\n";
            	}
            return returnStr;
        }

        private Boolean isWord(String s) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if ((c < 'A' || c > 'z') || (c > 'Z' && c < 'a'))
                    return false;
            }
            return true;
        }
    }
}
