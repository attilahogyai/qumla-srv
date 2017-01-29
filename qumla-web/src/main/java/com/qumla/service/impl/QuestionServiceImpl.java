package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.queryParams.params.FilterParams;
import io.katharsis.repository.MetaRepository;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.response.MetaInformation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.qumla.domain.Metadata;
import com.qumla.domain.PagingFilter;
import com.qumla.domain.location.Country;
import com.qumla.domain.question.Option;
import com.qumla.domain.question.Question;
import com.qumla.domain.question.QuestionFilter;
import com.qumla.domain.user.Session;
import com.qumla.service.QuestionService;
import com.qumla.util.BBCodeParser;
import com.qumla.util.TranslationUtil;
import com.qumla.web.controller.AbstractController;
import com.qumla.web.controller.RequestWrapper;
import com.qumla.web.exception.AccessDenied;
import com.qumla.web.exception.BadRequest;
import com.qumla.web.exception.NotFoundException;

@Component(value="questionServiceImpl")
public class QuestionServiceImpl extends AbstractService implements QuestionService, 
		ResourceRepository<Question, Long>, MetaRepository<Question>{
	final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

	@Autowired
	private LocationDataDaoMapper locationDataMapper;
	
	private TranslationUtil translationUtil = new TranslationUtil();
	
	@Autowired
	private QuestionDaoMapper questionDaoMapper;
	@Autowired
	@Qualifier("tagServiceImpl")
	private TagServiceImpl tagServiceImpl;
	@Autowired
	@Qualifier("answerStatServiceImpl")
	private AnswerStatServiceImpl answerStatServiceImpl;
	
	@Autowired
	private LocationDataDaoMapper locationDaoMapper;
	
	
	public QuestionServiceImpl() {
	}

	@Override
	public Question findOne(Long id,QueryParams requestParams) {
		if(id!=null){
			QuestionFilter filter=new QuestionFilter(RequestWrapper.getSession().getCountry());
			filter.setSession(RequestWrapper.getSession().getCode());
			filter.setId(id);
			Question q=questionDaoMapper.findOneQuestion(filter);
			if(q==null){
				throw new NotFoundException("question ["+id+"] not found");
			}
			if(q.getTicket()!=null && !q.getSessionCode().equals(RequestWrapper.getSession().getCode())){
				if(RequestWrapper.httpRequest.get().getParameter("t") ==null || 
					!RequestWrapper.httpRequest.get().getParameter("t").equals(q.getTicket())){
					throw new AccessDenied();
				}
			}
			if(q.getValidation()==Question.VALIDATED_INVALID && q.getSessionCode()!=null && !q.getSessionCode().equals(RequestWrapper.getSession().getCode())){
				throw new AccessDenied();
			}
			if(q.getDescription()!=null){
				q.setEncodedDescription(BBCodeParser.parseString(q.getDescription()));				
			}
			return q;
		}
		return null;
	}

	@Override
	public Iterable<Question> findAll(QueryParams requestParams) {
		QuestionFilter filter = buildQuestionFilter(requestParams);
		if(filter.isSessionFilter()){
			return questionDaoMapper.findSessionFilterQuestion(filter);
		}else if(filter.isAnswered()){
			return questionDaoMapper.findAsweredQuestion(filter);			
		}else if(filter.isLatest()){
			AbstractController.setInternationalFlag(locationDataMapper, questionDaoMapper,filter);
			return questionDaoMapper.findLatestQuestion(filter);			
		}else if(filter.getTag()!=null){
			AbstractController.setInternationalFlag(locationDataMapper, questionDaoMapper,filter);
			return questionDaoMapper.findTagQuestion(filter);			
		}else if(filter.getQuery()!=null){
			Country c=AbstractController.setInternationalFlag(locationDataMapper, questionDaoMapper,filter);
			// search in local language and client display language
			String displayLanguage = filter.getLanguage();
			filter.setLanguage(AbstractController.searchLanguageByCode(c.getDefLang()));
			List<Question> result= questionDaoMapper.queryQuestion(filter);
			filter.setLanguage(displayLanguage);
			List<Question> result2 = questionDaoMapper.queryQuestion(filter);
			result.addAll(result2);
			return result;									
		}else{
			AbstractController.setInternationalFlag(locationDataMapper, questionDaoMapper,filter);
			return questionDaoMapper.findPopularQuestion(filter);
		}
	}



	@Override
	public MetaInformation getMetaInformation(Iterable<Question> resources, QueryParams queryParams) {
		QuestionFilter filter = buildQuestionFilter(queryParams);
		long count=0;
		if(filter.isSessionFilter()){
			count=questionDaoMapper.findSessionFilterQuestionCount(filter);
		}else if(filter.isAnswered()){
			count=questionDaoMapper.findAsweredQuestionCount(filter);			
		}else if(filter.isLatest()){
			count=questionDaoMapper.findLatestQuestionCount(filter);			
		}else if(filter.getTag()!=null){
			count=questionDaoMapper.findTagQuestionCount(filter);						
		}else if(filter.getQuery()!=null){
			count=questionDaoMapper.queryQuestionCount(filter);						
		}else{
			count=questionDaoMapper.findPopularQuestionCount(filter);
		}
		Metadata m=new Metadata();
		m.setCount(count);
		m.setTotalPages((long)Math.ceil((double)count/filter.getPerPage()));
		return m;
		
	}

	public static QuestionFilter buildQuestionFilter(QueryParams requestParams) {
		QuestionFilter filter=new QuestionFilter(RequestWrapper.getSession().getCountry());		
		filter.setSession(RequestWrapper.getSession().getCode());
		if(requestParams!=null){
			buildDefaultFilter(requestParams, filter);
			Map<String,FilterParams> params=requestParams.getFilters().getParams();

			for (String filterS : params.keySet()) {
				if(filterS.equals("sessionFilter")){
					filter.setSessionFilter(true);
				}
				if(filterS.equals("answered")){
					filter.setAnswered(true);
				}				
				if(filterS.equals("latest")){
					filter.setLatest(true);
				}	
				//params.get("latest").QuestionServiceImpl.getParams().get("")
				if(filterS.equals("tag")){
					Set tags=params.get("tag").getParams().get("");
					String [] tagaArray=(String [])tags.toArray(new String[tags.size()]);
					if(!tagaArray[0].startsWith("#")){
						filter.setTag("#"+tagaArray[0]);
					}else{
						filter.setTag(tagaArray[0]);
					}
				}
			}
		}
		return filter;
	}
	
	@Override
	public Iterable<Question> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Question> S save(S entity) {
		Session session=RequestWrapper.getSession();
		
		if(session.isRegistered() || checkCaptcha(RequestWrapper.httpRequest.get())){
			
			
			// update session
			initEntity(entity);

			// load old question
			Question oldQuestion=findOne(entity.getId(), null);

			if(!session.isAdmin() && oldQuestion!=null && oldQuestion.getAnswerCount()!=null && oldQuestion.getAnswerCount()>0){
				throw new com.qumla.web.exception.BadRequest("modify.not.allowed");
			}
			
			if(entity.getTargetCountry()!=null && !session.isCustomer()){
				throw new com.qumla.web.exception.AccessDenied();
			}
			
			entity.setCountry(session.getCountry());
			
			// update question data
			String language=entity.getLanguage();
			entity.setTitle(BBCodeParser.safeProcessor(entity.getTitle()));
			String enTitle=translationUtil.translateText(entity.getTitle(), AbstractController.searchLanguageByDesc(language));
			entity.setTitleEn(enTitle);
			
			if(entity.getDescription()!=null){
				entity.setDescription(BBCodeParser.cleanUpText(entity.getDescription()));
				entity.setEncodedDescription(BBCodeParser.parseString(entity.getDescription()));
				
			}
			
			// gather tags
			Set<String> newTags=tagServiceImpl.parseTags(entity.getTitle());
			if(entity.getDescription()!=null){
				newTags.addAll(tagServiceImpl.parseTags(entity.getDescription()));
			}
			entity.setTags(newTags.toArray(new String[newTags.size()]));
			String country=entity.getTargetCountry()!=null?entity.getTargetCountry():entity.getCountry();
			
			
			
			if(country==null) throw new BadRequest("country error");
			if(language==null) throw new BadRequest("language error");
			
			
			if(entity.getId()!=null){ // modify question
				// checking existing answers. Is there any and the target status of the question is not inactive (0) then throw an error
				long count=answerStatServiceImpl.getAnswerStatCountForQuestion(entity.getId());
				if(count>0 && !session.isAdmin()){
					if(entity.getStatus()==Question.STATUS_INACTIVE){ // question inactivation allowed
						questionDaoMapper.updateQuestionStatus(entity);
						return (S)findOne(entity.getId(), null); // refresh from database
					}else{
						logger.debug("There are votes for question ["+entity.getId()+"]. Saving not allowed.");
						throw new com.qumla.web.exception.BadRequest("modify.not.allowed");
					}
				}
				//START update question
				if(!session.isAdmin() && oldQuestion.getSession()!=null && !oldQuestion.getSession().getCode().equals(session.getCode())){
					throw new com.qumla.web.exception.AccessDenied();
				}
				if(oldQuestion!=null){ // restore original session 
					entity.setSession(oldQuestion.getSession());
				}

				// decrement unsued tags
				if(oldQuestion.getTags()!=null && oldQuestion.getTags().length>0){
					Set<String> oldTags = new HashSet<String>(Arrays.asList(oldQuestion.getTags()));
					oldTags.removeAll(newTags);
					if(oldTags.size()>0){
						tagServiceImpl.unsetTags(oldTags, country);
					}
					oldTags = new HashSet<String>(Arrays.asList(oldQuestion.getTags()));
					newTags.removeAll(oldTags);
				}
				
				// increment new tags
				
				if(newTags.size()>0){
					tagServiceImpl.setTags(newTags, country, language);
				}
				
				questionDaoMapper.updateQuestion(entity);
				
//				questionDaoMapper.deleteQuestionOptions(entity);
//				for (Option option : entity.getOptions()) {
//					questionDaoMapper.insertOption(option);
//				}	
				return (S)findOne(entity.getId(), null);
				// END update question
				
			}else{ 
				// START insert question
				if(newTags.size()>0){
					tagServiceImpl.setTags(newTags, country, language);
				}
				questionDaoMapper.insertQuestion(entity);
				Country c=new Country();
				c.setCode(entity.getCountry());
				locationDaoMapper.incrementCountryQuestionCount(c);
				for (Option option : entity.getOptions()) {
					questionDaoMapper.insertOption(option);
				}		
				// END insert question
			}
			return entity;
		}else{
			throw new com.qumla.web.exception.BadRequest("bad request");
		}
	}
	public void incrementAnswerCount(long id){
		questionDaoMapper.incrementAnswerCount(id);
	}

	@Override
	public void delete(Long id) {
		
		throw new UnsupportedOperationException();
	}

	public QuestionDaoMapper getQuestionServiceMapper() {
		return questionDaoMapper;
	}

	public void setQuestionServiceMapper(QuestionDaoMapper questionServiceMapper) {
		this.questionDaoMapper = questionServiceMapper;
	}

	public long findPopularQuestionCount(PagingFilter filter){
		return questionDaoMapper.findPopularQuestionCount(filter);
	}
	public QuestionDaoMapper getQuestionDaoMapper() {
		return questionDaoMapper;
	}

	public void setQuestionDaoMapper(QuestionDaoMapper questionDaoMapper) {
		this.questionDaoMapper = questionDaoMapper;
	}

	public TagServiceImpl getTagServiceImpl() {
		return tagServiceImpl;
	}

	public void setTagServiceImpl(TagServiceImpl tagServiceImpl) {
		this.tagServiceImpl = tagServiceImpl;
	}

	public AnswerStatServiceImpl getAnswerStatServiceImpl() {
		return answerStatServiceImpl;
	}

	public void setAnswerStatServiceImpl(AnswerStatServiceImpl answerStatServiceImpl) {
		this.answerStatServiceImpl = answerStatServiceImpl;
	}

	public LocationDataDaoMapper getLocationDaoMapper() {
		return locationDaoMapper;
	}

	public void setLocationDaoMapper(LocationDataDaoMapper locationDaoMapper) {
		this.locationDaoMapper = locationDaoMapper;
	}


}
