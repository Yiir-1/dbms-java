package wyr;

import java.io.IOException;

/**
 * 
 * @ClassName:  Start   
 * @Description:开始！   
 * @author: 王怡如  
 * @Copyright:
 */
public class Start {

    public static void main(String[] args){
    	Input.user();
        try {
			Input.get();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }
}
