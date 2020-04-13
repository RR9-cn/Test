package com.java456.controller.admin;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.java456.dao.ClientDao;
import com.java456.entity.Client;
import com.java456.util.DateUtil;
import com.java456.util.ExcelUtil;
import com.java456.util.FileUtil;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/admin/client")
public class Admin_Client_Controller {
	
	@Resource
	private ClientDao clientDao;
	
	/**
	 * /admin/client/add
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JSONObject add(@Valid Client client,BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		if(bindingResult.hasErrors()){
			result.put("success", false);
			result.put("msg", bindingResult.getFieldError().getDefaultMessage());
			return result;
		}else{
			client.setCreateDateTime(new Date());
			clientDao.save(client);
			result.put("success", true);
			result.put("msg", "添加成功");
			return result;
		}
	}
	
	/**
	 * /admin/client/update
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JSONObject update(@Valid  Client client,BindingResult bindingResult)throws Exception {
		JSONObject result = new JSONObject();
		if(bindingResult.hasErrors()){
			result.put("success", false);
			result.put("msg", bindingResult.getFieldError().getDefaultMessage());
			return result;
		}else{
			//时间不更新
			Client temp= clientDao.findId(client.getId());
			client.setCreateDateTime(temp.getCreateDateTime());
			//时间不更新
			
			clientDao.save(client);
			result.put("success", true);
			result.put("msg", "添加成功");
			return result;
		}
	}
	
	
	/**
	 * /admin/client/list
	 * @param page    默认1
	 * @param limit   数据多少
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "limit", required = false) Integer limit, 
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Client> clientList = clientDao.findAll(new PageRequest(page-1,limit)).getContent();
		long total = clientDao.count();
		map.put("data", clientList);
		map.put("count", total);
		map.put("code", 0);
		map.put("msg", "");
		return map;
	}
	
	/**
	 * /admin/client/delete
	 */

	/*
	 * public JSONObject delete(@RequestParam(value = "id", required = false) String
	 * ids, HttpServletResponse response) throws Exception { String[] idsStr =
	 * ids.split(","); JSONObject result = new JSONObject();
	 * 
	 * for (int i = 0; i < idsStr.length; i++) {
	 * clientDao.deleteById(Integer.parseInt(idsStr[i])); } result.put("success",
	 * true); return result; }
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JSONObject delete(@RequestParam(value = "id", required = false) String id, HttpServletResponse response)
			throws Exception {
		 	JSONObject result = new JSONObject();
			clientDao.deleteById(Integer.parseInt(id));
			result.put("success", true);
			result.put("msg", "删除成功");
			return result;
	}
	
	@ResponseBody
	@RequestMapping("/fuzzyQuery")
	public Map<String, Object> fuzzyQuery(@RequestParam(value = "selectValue", required = false) String selectValue, HttpServletResponse response)
			throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		 List<Client> fuzzyFind = clientDao.fuzzyFind(selectValue);
		 long total = clientDao.count();
		 for (Client t : fuzzyFind) {
			System.out.println(t.getName());
		}
		 map.put("data", fuzzyFind);
		 map.put("code", 0);
		 map.put("count", total);
		 map.put("msg", "搜索成功");
		 return map;
	}
	
	
	/**
	 *   接受文件   解析  上传资料。
	 *   /admin/client/upload_excel
	 */
	@ResponseBody
	@RequestMapping("/upload_excel")
	public JSONObject upload_excel(@RequestParam("file") MultipartFile file, HttpServletResponse response,HttpServletRequest request)throws Exception {
		JSONObject result = new JSONObject();
		System.out.println(file.getOriginalFilename());
		if(!file.isEmpty()){
            String webPath=request.getServletContext().getRealPath("");
            String filePath= "/static/upload_file/excel/";
            //把文件名子换成（时间搓.png）
            // String imageName="houtai_logo."+file.getOriginalFilename().split("\\.")[1];
            String fileName=DateUtil.formatDate(new Date(), "yyyyMMdd-HHmmssSSS")+"_"+file.getOriginalFilename();
            
            FileUtil.makeDirs(webPath+filePath);
            //保存服务器
            file.transferTo(new File(webPath+filePath+fileName));
            //保存服务器
            
            //解析 
            List<Client> list =  excel_to_clientInfo(new File(webPath+filePath+fileName));
            //解析 
            
           //开始 上传 数据库
            for(Client client:list) {
            	clientDao.save(client);
            }
            //开始 上传 数据库
            
            //删除用过的文件 
            FileUtil.deleteFile(webPath+filePath+fileName);
            //删除用过的文件 
        }

		result.put("success", true);
		result.put("msg", "导入成功");
		return result;
	}
	//解析成excel
	private List<Client> excel_to_clientInfo(File userUploadFile) throws ParseException, InvalidFormatException {
		List<Client> list = new ArrayList<Client>();
		Client client = null;
		try {
			//获取excel文件
		
		if(userUploadFile.toString().endsWith("xlsx")) {
			OPCPackage opc=OPCPackage.open(userUploadFile);
			XSSFWorkbook wb1 =new XSSFWorkbook(opc);
			XSSFSheet xfsheet=wb1.getSheetAt(0);
			System.out.println(userUploadFile);
			if(xfsheet!=null) {
				
				for(int rowNum=1;rowNum<=xfsheet.getLastRowNum();rowNum++) {
					XSSFRow xssfRow = xfsheet.getRow(rowNum);
					client=new Client();
					if(xssfRow==null) continue;
					String bianma=ExcelUtil.formatCell(xssfRow.getCell(0));
					client.setBianhao(bianma);
					client.setName(ExcelUtil.formatCell(xssfRow.getCell(1)).split("\\.")[0]);
					client.setPhone(ExcelUtil.formatCell(xssfRow.getCell(2)));
					client.setRemark(ExcelUtil.formatCell(xssfRow.getCell(3)));
					client.setCreateDateTime(ExcelUtil.formatDate(xssfRow.getCell(4)));
					list.add(client);
				}
			}
			}
		
		if(userUploadFile.toString().endsWith("xls")) {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(userUploadFile));
			 HSSFWorkbook wb = new HSSFWorkbook(fs); 
			//获取第一个sheet页 
			  HSSFSheet sheet = wb.getSheetAt(0);
			  
			  if(sheet!=null){ for(int rowNum =1;rowNum<=sheet.getLastRowNum();rowNum++){
			  HSSFRow row = sheet.getRow(rowNum); 
			  if(row==null){ continue; } 
			  client =new Client();
			  //去掉编码中的 .0 如果全是数字 后面有.0 
			  String bianma =ExcelUtil.formatCell(row.getCell(0)); client.setBianhao(bianma);
			  client.setName(ExcelUtil.formatCell(row.getCell(1)).split("\\.")[0]);
			  client.setPhone(ExcelUtil.formatCell(row.getCell(2)));
			  client.setRemark(ExcelUtil.formatCell(row.getCell(3)));
			  client.setCreateDateTime(ExcelUtil.formatDate(row.getCell(4)));
			  list.add(client); } }
			 
		}
		}  catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
}
