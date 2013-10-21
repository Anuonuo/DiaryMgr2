package dataProvider.dbo;


import java.io.IOException;

import java.io.InputStream;

import java.util.Properties;

 

/**

 * @author Gerry

 *

 */

public class PropertiesReader extends Properties {

    /**

     * 

     */

    private static final long serialVersionUID = 5741017798450635145L;

   private InputStream input =getClass().getResourceAsStream("jdbc.properties");

    private static PropertiesReader read;

    private PropertiesReader() {

       try {

           this.load(input);

           input.close();

       } catch (IOException e) {

           e.printStackTrace();

       }

    }

    

    public static PropertiesReader getInstance() {

       if(read == null) {

           return new PropertiesReader();

       }

       else {

           return read;

       }

    }

}

 
