/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package program1;

//import java.util.Scanner;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;

//
//public class Program1 {
//
//    int   name= 4;
//    static {
//    System.loadLibrary("Program1c");
//    }
//    public static native String Concatenation(String username, String Password);
//
//    public static void main(String[] args) {
//
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter the User name : ");
//        String username = scanner.next();
//        System.out.println("Enter the PassWord : ");
//        String passWord = scanner.next();
//        String str=null;
//        str=Concatenation(username, passWord);
//          System.out.println( str);
//        
//    }
//
//}

/*
--id="sip:6001@192.168.0.122" 
--registrar="sip:192.168.0.122" 
--realm="*" --username="6001" 
--password="1234"
*/

public class Program1 {
  static {
      System.loadLibrary("pjsua2");
      System.out.println("Library loaded");
  }

  public static void main(String argv[]) {
      try {
          // Create endpoint
          Endpoint ep = new Endpoint();
          ep.libCreate();
          // Initialize endpoint
          EpConfig epConfig = new EpConfig();
          ep.libInit( epConfig );
          // Create SIP transport. Error handling sample is shown
          TransportConfig sipTpConfig = new TransportConfig();
          sipTpConfig.setPort(5060);
          ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
          // Start the library
          ep.libStart();

          AccountConfig acfg = new AccountConfig();
          acfg.setIdUri("sip:test@pjsip.org");
          acfg.getRegConfig().setRegistrarUri("sip:pjsip.org");
          AuthCredInfo  cred = new AuthCredInfo("digest", "*", "test", 0, "secret");
          acfg.getSipConfig().getAuthCreds().add( cred );
          // Create the account
          
          
          // Here we don't have anything else to do..
          Thread.sleep(10000);
          /* Explicitly delete the account.
           * This is to avoid GC to delete the endpoint first before deleting
           * the account.
           */
     
          // Explicitly destroy and delete endpoint
          ep.libDestroy();
          ep.delete();

      } catch (Exception e) {
          System.out.println(e);
          return;
      }
  }
}