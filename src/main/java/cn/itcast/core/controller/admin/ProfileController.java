package cn.itcast.core.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.itcast.common.encode.Md5Pwd;
import cn.itcast.common.web.ResponseUtils;
import cn.itcast.core.bean.country.City;
import cn.itcast.core.bean.country.Province;
import cn.itcast.core.bean.country.Town;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.query.country.CityQuery;
import cn.itcast.core.query.country.TownQuery;
import cn.itcast.core.service.country.CityService;
import cn.itcast.core.service.country.ProvinceService;
import cn.itcast.core.service.country.TownService;
import cn.itcast.core.service.user.BuyerService;
import cn.itcast.core.web.Constants;
import cn.itcast.core.web.session.SessionProvider;

import com.octo.captcha.service.image.ImageCaptchaService;
@Controller
public class ProfileController {
	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private Md5Pwd md5Pwd;
	@Autowired
	private BuyerService buyerService;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private ProvinceService provinceService;
	@Autowired
	private CityService cityService;
	@Autowired
	private TownService townService;
	
	@RequestMapping(value="/shopping/login.shtml",method=RequestMethod.GET)
	public String login(){ 
		return "buyer/login";
	}
	
	
	@RequestMapping(value="/shopping/login.shtml",method=RequestMethod.POST)
	public String login(HttpServletRequest request,Buyer buyer,String captcha,String returnUrl,ModelMap model){ 
		
		if(StringUtils.isNotBlank(captcha)){
			if(imageCaptchaService.validateResponseForID(sessionProvider.getSessionId(request), captcha)){
				if(null != buyer && StringUtils.isNotBlank(buyer.getUsername())){
					if(StringUtils.isNotBlank(buyer.getPassword())){
						Buyer buyer2 = buyerService.getBuyerByKey(buyer.getUsername());
						if(null != buyer2){
							if(buyer2.getPassword().equals(md5Pwd.encode(buyer.getPassword()))){
								sessionProvider.setAttribute(request, Constants.BUYER_SESSION_STRING, buyer2);
								if(StringUtils.isNotBlank(returnUrl)){									
									return "redirect:" + returnUrl;
								}else {									
									return "redirect:/buyer/index.shtml";
								}
							}else {
								model.addAttribute("error","密码错误");
							}
						}else {
							model.addAttribute("error","用户名错误");
						}
					}else {
						model.addAttribute("error","请输入密码");
					}
				}else {
					model.addAttribute("error","请输入用户名");
				}
			}else {
				model.addAttribute("error","验证码错误");
			}
		}else{
			model.addAttribute("error","请输入验证码");
		}
		return "buyer/login";
	}
	
	@RequestMapping(value="/buyer/index.shtml")
	public String index(){
		return "/buyer/index";
	}
	
	@RequestMapping(value="/buyer/city.shtml")
	public void city(String code,HttpServletResponse response){
		CityQuery cityQuery = new CityQuery();
		cityQuery.setProvince(code);
		List<City> cities = cityService.getCityList(cityQuery);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("cities", cities);
		ResponseUtils.renderJson(response, jsonObject.toString());
	}
	
	@RequestMapping(value="/buyer/strict.shtml")
	public void strict(String code,HttpServletResponse response){
		TownQuery townQuery = new TownQuery();
		townQuery.setCity(code);
		
		List<Town> towns = townService.getTownList(townQuery);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("towns", towns);
		ResponseUtils.renderJson(response, jsonObject.toString());
	}
	
	@RequestMapping(value="/buyer/profile.shtml")
	public String profile(ModelMap model,HttpServletRequest request){
		Buyer buyer = (Buyer) sessionProvider.getAttribute(request, Constants.BUYER_SESSION_STRING);
		Buyer buyer2 = buyerService.getBuyerByKey(buyer.getUsername());
		List<Province> provinces = provinceService.getProvinceList(null);
		List<City> cities = cityService.getCityList(null);
		List<Town> towns = townService.getTownList(null);
		model.addAttribute("buyer",buyer2);
		model.addAttribute("provinces",provinces);
		model.addAttribute("cities",cities);
		model.addAttribute("towns",towns);

		return "/buyer/profile";
	}
	
	
	@RequestMapping(value="/buyer/deliver_address.shtml")
	public String deliver_address(){
		return "/buyer/deliver_address";
	}
}
