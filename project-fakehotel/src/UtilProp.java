
import java.util.Properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UtilProp {
   static final boolean _W = System.getProperty("os.name").toLowerCase().contains("windows");
   static String _PROP_FILENAME_WIN_LOCAL = "G:\\My Drive\\SWeng\\workspace-proj\\fakehotel\\project-fakehotel\\WebContent\\WEB-INF\\config.properties";
   static String _PROP_FILENAME_OSX_LOCAL = "/project-fakehotel/WebContent/WEB-INF/config.properties";
   // * Remote server path
   static String _PROP_FILENAME_REMOTE = "/var/lib/tomcat7/webapps/project-fakehotel/WEB-INF/config.properties";
   static Properties prop = new Properties();

   public static void loadProperty() throws Exception {
      FileInputStream inputStream = null;
      if (_W) {
         if (new File(_PROP_FILENAME_WIN_LOCAL).exists()) {
            System.out.println("[DBG] Loaded: " + new File(_PROP_FILENAME_WIN_LOCAL).getAbsolutePath());
            inputStream = new FileInputStream(_PROP_FILENAME_WIN_LOCAL);
         }
      } else {
         if (new File(_PROP_FILENAME_OSX_LOCAL).exists()) {
            System.out.println("[DBG] Loaded: " + new File(_PROP_FILENAME_OSX_LOCAL).getAbsolutePath());
            inputStream = new FileInputStream(_PROP_FILENAME_OSX_LOCAL);
         }
      }
      if (new File(_PROP_FILENAME_REMOTE).exists()) {
         System.out.println("[DBG] Loaded: " + new File(_PROP_FILENAME_REMOTE).getAbsolutePath());
         inputStream = new FileInputStream(_PROP_FILENAME_REMOTE);
      }
      if (inputStream == null) {
         throw new FileNotFoundException();
      }
      prop.load(inputStream);
   }

   public static String getProp(String key) {
      return prop.getProperty(key).trim();
   }
}
