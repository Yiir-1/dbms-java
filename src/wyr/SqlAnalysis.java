package wyr;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @ClassName:  SqlAnalysis   
 * @Description:TODO进行语句分析，主要判断是哪种类型的语句，就进入哪个类中.  
 * @author: 王怡如  
 * @Copyright:
 */
public class SqlAnalysis {

    //下面是目前支持的sql语句类型
    private static final String create = "create";
    private static final String help = "help";
    private static final String insert = "insert";
    private static final String select = "select";
    private static final String delete = "delete";
    private static final String grant = "grant";
    private static final String revoke = "revoke";
    private static final String update = "update";
    public static void analysis(String sql) throws IOException{
        //str = sql.split(" ");
        String start = "";
        //正则表达式的匹配规则
        String regex = "^[a-z]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);
        //获取匹配值
        while(matcher.find()){
            start = matcher.group();
        }

        //根据第一个单词判断该语句的作用
        switch (start){
            case create:
                Create.createSql(sql);
                break;
            case help:
                Help.help(sql);
                break;
            case insert:
                Insert.insertSql(sql);
                break;
            case select:
                Select.selectSql(sql);
                break;
            case delete:
                Delete.deleteSql(sql);
                break;
            case grant:
            	Grant.grant(sql);
            	break;
            case revoke:
            	Revoke.revoke(sql);
            	break;
            case update:
            	Update.updatesql(sql);
            	break;
            default:
                System.out.println("输入的命令无法识别,出现语法错误！！");
                Input.get();
                break;
        }

    }


}
