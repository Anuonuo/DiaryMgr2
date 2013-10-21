/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basicDataStructure;

/**
 *
 * @author Administrator
 */
public class DiaryMultiMedia {
        private byte [] RichContent;
        private byte [] Voice;
	public void setRichContent(byte [] rich)
        {
            RichContent=rich;
        }
        public byte [] getrichContent()
        {
            return RichContent;
        }
        public void setVoice(byte [] voice)
        {
            Voice=voice;
        }
        public byte [] getVoice()
        {
            return Voice;
        }
}
