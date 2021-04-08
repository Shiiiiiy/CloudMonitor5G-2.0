package com.datang.common.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;


/**
 * Jxls2.4.2版本，主要用来导出图片到excel
 * @author lucheng
 *
 */
public class Jxls2Utils {
	 static{
	        //添加自定义指令（可覆盖jxls原指令）
	        XlsCommentAreaBuilder.addCommandMapping("image", ImageCommand.class);
//	        XlsCommentAreaBuilder.addCommandMapping("each", EachCommand.class);
//	        XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
//	        XlsCommentAreaBuilder.addCommandMapping("link", LinkCommand.class);
	    }
	
	public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException{
        try {
			Context context = PoiTransformer.createInitialContext();
			if (model != null) {
			    for (String key : model.keySet()) {
			        context.putVar(key, model.get(key));
			    }
			}
			JxlsHelper jxlsHelper = JxlsHelper.getInstance();
			Transformer transformer  = jxlsHelper.createTransformer(is, os);
			//获得配置
			JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
			//设置静默模式，不报警告
			evaluator.getJexlEngine().setSilent(true);
			//函数强制，自定义功能
			Map<String, Object> funcs = new HashMap<String, Object>();
			funcs.put("utils", new Jxls2Utils());    //添加自定义功能
			evaluator.getJexlEngine().setFunctions(funcs);
			//必须要这个，否者表格函数统计会错乱
			jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void exportExcel(File xls, File out, Map<String, Object> model) throws FileNotFoundException, IOException {
            exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }
    
    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws Exception {
    	InputStream is = ClassUtil.getResourceAsStream(templatePath);
    	exportExcel(is, os, model);    
    }
    
    //获取jxls模版文件
    public static File getTemplate(String path){
        File template = new File(path);
        if(template.exists()){
            return template;
        }
        return null;
    }    
    
    // 日期格式化
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    // if判断
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }
    
}
