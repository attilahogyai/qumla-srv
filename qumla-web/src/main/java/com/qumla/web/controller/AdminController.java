package com.qumla.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.im4java.core.IM4JavaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qumla.domain.answer.Answer;
import com.qumla.domain.image.ExifData;
import com.qumla.domain.image.Image;
import com.qumla.domain.question.Question;
import com.qumla.domain.question.QuestionFilter;
import com.qumla.domain.user.Session;
import com.qumla.image.ImageConfig;
import com.qumla.image.ImageTools;
import com.qumla.service.impl.AnswerDaoMapper;
import com.qumla.service.impl.AnswerServiceImpl;
import com.qumla.service.impl.ImageServiceImpl;
import com.qumla.service.impl.LocationDataServiceImpl;
import com.qumla.service.impl.LoginDaoMapper;
import com.qumla.service.impl.QuestionDaoMapper;
import com.qumla.util.Constants;
import com.qumla.util.GeolocationDataUtil;
import com.qumla.util.HttpHelper;

@Controller
public class AdminController {
	GeolocationDataUtil geoDataUtil;
	
	@Autowired
	LoginDaoMapper loginDaoMapper;
	
	@Autowired
	private LocationDataServiceImpl locationDataService;
	
	@Autowired
	private QuestionDaoMapper questionMapper;
	@Autowired
	private AnswerDaoMapper answerMapper;
	@Autowired
	private AnswerServiceImpl answerServiceImpl;
	@Autowired	
	private ImageServiceImpl imageServiceImpl;
	
	
	private int imageLoadCount=0;
	
	public AdminController() {

	}
	
	@RequestMapping(value="/adminmanager",method=RequestMethod.GET)
	@ResponseBody
	public Object updateManager(HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(geoDataUtil==null){
			geoDataUtil=new GeolocationDataUtil(locationDataService);
		}		
		String command = request.getParameter("command");
		String status = request.getParameter("status");
		if(command.equals("sessionlocation")){
			updateSessionsWithoutLocation();
		}else if(command.equals("updatequestion")){
			String quid=request.getParameter("qid");
			String [] ids=null;
			
			if(quid.indexOf("-")>-1){
				String [] intervall=quid.split("-");
				List<String> intids=new ArrayList<String>(); 
				for (int i = Integer.parseInt(intervall[0]); i <= Integer.parseInt(intervall[1]); i++) {
					intids.add(Integer.toString(i));
				}
				ids=intids.toArray(new String[intids.size()]);
			}else{
				ids=quid.split(",");
			}
			return updateQuestionAnswer(ids);
		}else if(command.equals("importimage")){
			if(imageLoadCount==0 && status==null){
				createLoadImagesJob().start();
				return "started";
			}else if(status.equals("query")){
				return imageLoadCount==0?"Ready":"In proress:"+imageLoadCount;				
			}
		}
		return "OK";
	}	
	public void updateSessionsWithoutLocation(){
		List<Session> sessionList=loginDaoMapper.getSessionWithoutLocation();
		System.out.println("sessionList.size:"+sessionList.size());
		for (Session session : sessionList) {
			try {
				Thread.sleep(1000);
				System.out.println("update session:"+session.getCode());				
				if(session.getCity()==null){
					System.out.println("get location");									
					geoDataUtil.updateCountryCity(session, session.getRemoteIp());
				}				
				
				geoDataUtil.updateSessionLocation(session);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			loginDaoMapper.updateSession(session);
		}
	}
	public boolean updateQuestionAnswer(String... ids){

		QuestionFilter filter=new QuestionFilter(RequestWrapper.getSession().getCountry());
		filter.setSession("aaaa");
		System.out.println("question ids:"+ids.length);
		for (int i = 0; i < ids.length; i++) {
			System.out.println("update question:"+ids[i]);
			
			filter.setId(Long.parseLong(ids[i]));
			filter.setOffset(0);
			filter.setLimit(10);
			Question question=questionMapper.findOneQuestion(filter);
			if(question==null) {
				System.out.println("question not found");
				continue;
			}
			answerServiceImpl.resetStatForQuestion(question.getId());
			List<Answer> answers= answerServiceImpl.findAnswerForQuestion(question.getId());
			for (Answer answer : answers) {
				if(answer.getLocation()!=null){
					answerServiceImpl.updateAnswerStat(answer);
				}
			}
			System.out.println("reset question done");
		}
		return true;
	}
	
	public Thread createLoadImagesJob(){
		final File path=new File(Constants.IMPORTDIR);
		final File backup=new File(Constants.IMPORTDIR+"/import_backup");
		if(!backup.exists()){
			if(!backup.mkdirs()) throw new RuntimeException("unable to create backup path:"+backup);
		}
		
		Thread t=new Thread(){
			public void run(){
				File[] content=path.listFiles();
				for (int i = 0; i < content.length; i++) {
					if(content[i].isDirectory()) continue;
					String filename = null;
					String absoulutePath = null;
					try {
						filename = new String(content[i].getName().getBytes("UTF-8"),"UTF-8");
						absoulutePath = new String(content[i].getAbsolutePath().getBytes("UTF-8"),"UTF-8");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println(">> start processing: "+filename);
					try {
						ExifData exifData=ImageTools.exifTool(absoulutePath);
						String artist=exifData.getArtist();
						Image im = new Image();
						im.setPath(HttpHelper.replacetoAsciiChars(filename));

						Image old = imageServiceImpl.findByPath(im.getPath());		
						if(old!=null){
							im=old;
						}
						if(artist==null){
							artist = "";
							System.out.println("artist is null for image: "+im.getPath());
						}
						if(im.getStatus()==null){
							im.setStatus(1);							
						}
						if(im.getType()==null){
							im.setType(1);							
						}
						
						Set<String> s=new HashSet<String>();	
						String [] tokens=null;
						if(filename.indexOf("_")>-1){
							tokens=filename.split("_");
							for (int j = 0; j < tokens.length; j++) {
								s.add(tokens[j]);
							}

						}
						if(filename.indexOf("-")>-1){						
							tokens=filename.split("-");
							for (int j = 0; j < tokens.length; j++) {
								s.add(tokens[j]);
							}
						}
						if(exifData.getArtist()!=null){
							tokens=exifData.getArtist().split(" ");
							for (int j = 0; j < tokens.length; j++) {
								s.add(tokens[j].trim());
							}
						}
						if(s!=null && s.size()>0){
							tokens=s.toArray(tokens);
							String tags="";
							for (int j = 0; j < tokens.length; j++) {
								try{
									Integer.parseInt(tokens[j]);
								}catch(NumberFormatException e){
									if(tokens[j]==null) continue;
									if(tokens[j].indexOf(".")!=-1){
										tokens[j]=tokens[j].substring(0,tokens[j].indexOf("."));
									}
									tags+=" "+tokens[j].trim();
								}
							}
							System.out.println(tags);
							im.setTag(tags.trim());
						}
						im.setTitle(filename);
						String ext = HttpHelper.getExt(im.getPath());
						InputStream is=new FileInputStream(content[i]);
						FileController.storePicture(im,ext,is);
						is.close();
						
						imageServiceImpl.save(im);
						imageLoadCount++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for (int i = 0; i < content.length; i++) {
					if(content[i].isDirectory()) continue;
					try {
						if(!content[i].renameTo(new File(backup+"/"+new String(content[i].getName().getBytes("UTF-8"),"UTF-8")))){
							System.out.println("unable to move to backup:"+ new String(content[i].getName().getBytes("UTF-8"),"UTF-8"));
						}
						imageLoadCount++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				imageLoadCount=0;
			}
		};	
		return t;
	}
}
