
package wyr;

/**
 * 
 * @ClassName:  SQLConstant   
 * @Description:sql里面的固定 
 * @author: 王怡如  
 * @Copyright:
 */
public class SQLConstant {

    //数据库的根路径
    private static final String path = "D:\\MyDatabase";

    //数据库的当前路径
    private static String nowPath = path;

    //自定义的分隔符
    private static final String separate = ",";

    public static String getPath(){
        return path;
    }
    private static final String Null="null";
    
    private static final String maxnum="100000000";

    
    public static String getmaxnum()
    {

    	return maxnum;
    }
    public static String getNowPath(){
        return nowPath;
    }
    public static String getNull()
    {
    	return Null;
    }
    /**
     * 
     * @Title: setNowPath   
     * @Description: TODO(描述这个方法的作用)   
     * @param name      
     * @return: void      
     * @throws
     */
    public static void setNowPath(String name){
        nowPath = nowPath + "\\" + name;
    }

    public static String getSeparate(){
        return separate;
    }

}
