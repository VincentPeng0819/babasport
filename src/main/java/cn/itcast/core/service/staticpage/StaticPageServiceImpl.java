package cn.itcast.core.service.staticpage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * @author Shadow
 *
 */
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {

	private Configuration configuration;
	private ServletContext servletContext;
	
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.configuration = freeMarkerConfigurer.getConfiguration();
	}
	@Override
	public void productIndex(Map<String,Object> rootMap,Integer name) {
		// 设置模板目录
//		configuration.setDirectoryForTemplateLoading(dir);
//		"/WEB_INF/ftl/"
		Template template;
		//写入磁盘
		Writer out = null;
		try {
			//读入内存
			template = configuration.getTemplate("productDetail.html");
			String pathString = getPath("/html/product/" + name + ".html");
			File file = new File(pathString);
			
			File parentFile = file.getParentFile();
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}
			out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			template.process(rootMap, out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String getPath(String name){
		return servletContext.getRealPath(name);
	}
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
}
