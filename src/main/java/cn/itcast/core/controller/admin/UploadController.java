package cn.itcast.core.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fckeditor.response.UploadResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.itcast.common.web.ResponseUtils;
import cn.itcast.core.web.Constants;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

@Controller
public class UploadController {
	@RequestMapping(value="/upload/uploadPic.do")
	public void uploadPic(HttpServletResponse response,@RequestParam(required=false) MultipartFile pic) throws UniformInterfaceException, ClientHandlerException, IOException{
		
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String format = df.format(new Date());
		
		Random random = new Random();
		for(int i=0;i<3;i++){
			format += random.nextInt(10);
		}
		String extString = FilenameUtils.getExtension(pic.getOriginalFilename());
		String path = "upload/" + format + "." + extString;
		
		//发送图片到远程服务器
	    String url = Constants.IMAGE_URL+ path;
		Client client = new Client();
		WebResource resource = client.resource(url);
		resource.put(String.class, pic.getBytes());
		
		//返回路径
		JSONObject jObject = new JSONObject();
		jObject.put("path", path);
		jObject.put("url", url);
		ResponseUtils.renderJson(response, jObject.toString());
	}
	
	
	@RequestMapping(value="/upload/uploadFck.do")
	public String uploadFck(HttpServletRequest request,HttpServletResponse response){
		//强转
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)request;
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		for(Entry<String, MultipartFile> entry : entrySet){
			MultipartFile pic = entry.getValue();
			
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			
			String format = df.format(new Date());
			
			Random random = new Random();
			for(int i=0;i<3;i++){
				format += random.nextInt(10);
			}
			String extString = FilenameUtils.getExtension(pic.getOriginalFilename());
			String path = "upload/" + format + "." + extString;
			
			//发送图片到远程服务器
		    String url = Constants.IMAGE_URL+ path;
			Client client = new Client();
			WebResource resource = client.resource(url);
			try {
				resource.put(String.class, pic.getBytes());
				UploadResponse uploadResponse = UploadResponse.getOK(url);
				response.getWriter().print(uploadResponse);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
