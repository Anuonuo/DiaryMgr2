/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package windows;

import java.awt.Image;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Administrator
 */
public /*static*/ class UiCommonFuctions 
{
    public static File ChoosePictureFile ()
    {
        JFileChooser jc=new JFileChooser();
        jc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                String tmp=f.getName().toLowerCase();
                if(f.isDirectory() || tmp.endsWith(".jpg") || tmp.endsWith(".gif")|| tmp.endsWith(".jpeg")|| tmp.endsWith(".bmp") || tmp.endsWith(".png"))
                     return true;
                else
                    return false;
            }

            @Override
            public String getDescription() {
                return "图像文件(*.jpg,* .gif,*.jpeg,*.bmp,*.png)";
            }
        });
        jc.showOpenDialog(null);
        return jc.getSelectedFile();
    }
    public static byte [] ConvertObjectToByteArray(Object doc)
    {
        try{
              ByteArrayOutputStream bos=new ByteArrayOutputStream();
              ObjectOutputStream oos=new ObjectOutputStream(bos);
              oos.writeObject(doc);
              oos.flush();
              oos.close();
              return bos.toByteArray();
        }
        catch (IOException e) {
			e.printStackTrace();
                        return null;
		}
    }
    public static Object ConvertByteArrayToObject(byte [] byt)
    {
        try{
        ByteArrayInputStream bis=new ByteArrayInputStream(byt);
        ObjectInputStream ois=new ObjectInputStream(bis);
        return ois.readObject();
        }
        catch (Exception e) {
			e.printStackTrace();
                        return null;
		}        
        
    }
    public static String getCurrentDateTime()
    {
        Date d=new Date();
	SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	return f.format(d);
    }
    public static ImageIcon getScalcedImageIcon(ImageIcon ficon, int newShortEdgeLength)
    {
        int width=ficon.getIconWidth();
        int height=ficon.getIconHeight();
        if(width<newShortEdgeLength||height<newShortEdgeLength)
            ;
        else{
            if(width<height)
            {
                width=newShortEdgeLength;
                float rate=(float)ficon.getIconHeight()/(float)ficon.getIconWidth();
                height=(int)(newShortEdgeLength*rate);
            }
            else
            {
                height=newShortEdgeLength;
                float rate=(float)ficon.getIconWidth()/(float)ficon.getIconHeight();
                width=(int)(newShortEdgeLength*rate);
            }
        }
        ficon.setImage(ficon.getImage().getScaledInstance (width, height, Image.SCALE_DEFAULT));
        return ficon;
    }
    
}
