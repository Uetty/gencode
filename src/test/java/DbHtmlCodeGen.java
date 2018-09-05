import java.sql.SQLException;
import java.util.List;

import com.uetty.generator.CommonCodeGen;
import com.uetty.generator.CommonCodeGen.PlaceholderAdapter;
import com.uetty.generator.constant.Config;
import com.uetty.generator.util.DbStringUtil;

public class DbHtmlCodeGen {

	public static class CamelAdapter implements PlaceholderAdapter {
		public String transfer(String name) {
			String camelStyle = DbStringUtil.underLineToCamelStyle(name);
			return camelStyle;
		}
	}

	public static class NoTrans implements PlaceholderAdapter {
		public String transfer(String name) {
			return name;
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		CommonCodeGen ccg = new CommonCodeGen();
		System.out.println(Config.get("database.url"));
		ccg.setUrl("jdbc:mysql://localhost:3306/zhixindb");
		ccg.setUser("root");
		ccg.setPassword("123456");
		ccg.setTableName("t_resolution_region");
		ccg.setCodeTemplate(
				"						<div class=\"form-group\" style=\"padding-top: 5px;pading-bottom: 2px;\">\n" + 
				"						    <label class=\"control-label col-sm-2\">-comment-</label>\n" + 
				"						    <div class=\"col-sm-8\">\n" + 
				"						        <input name=\"-colname-\" type=\"text\" readonly=\"readonly\">\n" + 
				"						    </div>\n" + 
				"						</div>");
		ccg.setCommentTrans("-comment-", new NoTrans()).setColumnTrans("-colname-", new CamelAdapter());
		
		List<String> gencodes = ccg.gencode();
		for (String gen : gencodes) {
			System.out.println(gen + "\n");
		}
		
	}
}
