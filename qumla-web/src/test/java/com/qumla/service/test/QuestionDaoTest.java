package com.qumla.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.qumla.domain.PagingFilter;
import com.qumla.domain.location.Country;
import com.qumla.domain.location.Location;
import com.qumla.domain.location.LocationData;
import com.qumla.domain.question.Option;
import com.qumla.domain.question.Question;
import com.qumla.domain.question.QuestionFilter;
import com.qumla.domain.search.SearchResult;
import com.qumla.domain.user.Session;
import com.qumla.service.impl.AnswerDaoMapper;
import com.qumla.service.impl.AnswerStatServiceImpl;
import com.qumla.service.impl.LocationDataDaoMapper;
import com.qumla.service.impl.QuestionDaoMapper;
import com.qumla.service.impl.QuestionServiceImpl;
import com.qumla.service.impl.TagDaoMapper;
import com.qumla.service.impl.TagServiceImpl;
import com.qumla.util.BBCodeParser;
import com.qumla.web.controller.RequestWrapper;

public class QuestionDaoTest extends AbstractTest {
	private static final String session="testtoken";
	@Before
	public void before() {
		
		super.before(QuestionDaoMapper.class);
	}
	
	protected Question createQuestion(QuestionServiceImpl service){
		Session s=RequestWrapper.getSession();
		s.setCountry("HU");
		
		Question q=new Question();
		q.setSession(s);
		q.setTitle("test title #nos1 #nos2 ");
		q.setDescription("testdesc #nos2 #nos3");
		q.setStatus(Question.STATUS_ACTIVE);
		q.setImg("img1.jpg");
		//q.setTargetCountry("HU");
		LocationData l = new LocationData();
		l.setId(new Long(11)); // Budapest
		q.setTargetLocation(l);
		q.setType(1);
		q.setLanguage("hungarian");
		q.setCountry("HU");
		q.setMandatory(true);
		q.setUrl("http://test");
		q.setColor("255,255,255");
		
		Option o1=new Option();
		o1.setQuestion(q);
		o1.setOrd(1);
		o1.setStyle("positive");
		o1.setText("yes");
		Option o2=new Option();
		o2.setQuestion(q);
		
		o2.setStyle("negative");
		o2.setOrd(2);
		o2.setText("no");
		q.getOptions().add(o1);
		q.getOptions().add(o2);			
		service.save(q);
		return q;
		
	}
	protected Question shortTitleTest(QuestionServiceImpl service){
		Session s=RequestWrapper.getSession();
		s.setCountry("HU");
		
		Question q=new Question();
		q.setSession(s);
		q.setTitle("szerinted?");
		q.setDescription("testdesc #nos2 #nos3");
		q.setStatus(Question.STATUS_ACTIVE);
		q.setImg("img1.jpg");
		//q.setTargetCountry("HU");
		LocationData l = new LocationData();
		l.setId(new Long(11)); // Budapest
		q.setTargetLocation(l);
		q.setType(1);
		q.setLanguage("hungarian");
		q.setCountry("HU");
		q.setMandatory(true);
		q.setUrl("http://test");
		q.setColor("255,255,255");
		
		Option o1=new Option();
		o1.setQuestion(q);
		o1.setOrd(1);
		o1.setStyle("positive");
		o1.setText("yes");
		Option o2=new Option();
		o2.setQuestion(q);
		
		o2.setStyle("negative");
		o2.setOrd(2);
		o2.setText("no");
		q.getOptions().add(o1);
		q.getOptions().add(o2);			
		service.save(q);
		return q;
		
	}	
	@Test
	public void eolTextStave(){
		
		String f=BBCodeParser.safeProcessor("Nos<br/>Nosika");
		Assert.assertEquals("Nos<br/>Nosika", f);
		f=BBCodeParser.safeProcessor("Nos<br>Nosika");
		Assert.assertEquals("Nos<br/>Nosika", f);
		f=BBCodeParser.safeProcessor("Nos<br>Nosika");
		Assert.assertEquals("Nos<br/>Nosika", f);		
	}
	
	@Test
	public void brTextStave(){
		String t="<br> <br> <br> <br> <br> <br> <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>";
		BBCodeParser.safeProcessor(t);
	}
	@Test
	public void textStave(){
		String t=("A Daily Mailen akadtunk egy cikkre, illetve vitaindítóra. Egy anya egy baba-mama fórumon azt a kérdést dobta be a közösbe, hogy egyedül hagynánk-e egy 10 hónapos gyereket egy lakásban 10 percre. A kérdés felvetője leírta, hogy ő bizony egyedül hagyja a babát pár percre, amit úgy magyaráz, hogy a baba mindig, minden nap ugyanabban az időben alszik egy órát, ébredés nélkül. Addig a kedves szülő kiugrik az 50 méterre lévő boltba, hogy beszerezze a legszükségesebbeket. Valószínűleg neki sem lehet teljesen egyértelmű ez a dolog, ha felvetette a témát a fórumon.<br/><br/>Mint azt sejteni lehet, rövid idő alatt 800 vérmes felhasználó szállt be a vitába, a legtöbben egy határozott „nem” -mel feleltek. Sokan közülük a hírhedt, és máig megoldatlan Madeleine- ügyet említették, mint a tízperces nem-odafigyelés lehetséges kockázata illetve eredménye. Madeleine McCann 2007-ben tűnt el egy portugál üdülőhely apartmanjából, ahol szülei hagyták, míg ők egy 120 méterre lévő étteremben ebédeltek. A kislány azóta sem került elő, bár már a világ minden szegletén látni vélte valaki. Olyannyira foglalkoztatta az ügy a közvéleményt, hogy a wikipédián már külön szócikk van a kislány eltűnéséről, és amikor a google keresőjébe beírtam a Madeleine szót, kiegészítésként a kislány vezetékneve jött fel először.<br/><br/><br/>Kizárt, hogy egyedül hagyjam<br/><br/>Nekem speciel pont ez az ügy nem jutott eszembe, de sok más igen; először is hogyan állíthatja valaki teljesen biztosan, hogy kisbabája nem ébred fel azalatt a pár perc alatt, míg ő távol van. Lehet, hogy 365 napon keresztül minden nap így történt, de mi a biztosíték arra, hogy így történjen a 366-dik napon is? És mi van akkor, ha azalatt a pár perc alatt rátekeredik a takaró, fulladni kezd, bukik, ami visszafolyik a torkába, beüti a fejét, stb.? Egy kellőképpen pesszimista, ugyanakkor nagy fantáziával rendelkező anyuk kapásból fel tudna sorolni még legalább tízféle tragikus végkimenetelt. Sajnos elég öt perc a végzetes tragédiára, de a visszafordíthatatlan egészségügyi károsodásra is. Nem éri meg.<br/><br/>Ha nem ennyire végletes eseményekben gondolkodunk, akkor is történhet más, kisebb súlyú dolog, ami – annak ellenére, hogy nem károsodik a baba egészsége – igenis rossz élmény neki és ezáltal nekünk is. Mi van, ha megébred a baba, és elkezd sírni? Sírni, sírni, sírni... És nem lesz ott senki, aki megvigasztalja. Ha nem válaszol rá senki, előbb-utóbb feladja szegény, és megtanulja, hogy nem is érdemes jelezni a gondot, mert úgyis egyedül lesz a bajával, senki nem fog segíteni neki. Persze ez a „tanult tehetetlenség” nem egyetlen alkalom hatására alakul ki; ez elsősorban intézetben nevelt babákra igaz, akikre nem jut annyi segítő kéz. Nem történik visszafordíthatatlan tragédia, ha egyszer-kétszer nem tudunk rögtön odaszaladni, amint meghalljuk babánk sírását, de igenis törekednünk kell az azonnali reakcióra.<br/><br/><br/>A másik, amire sokan hivatkoznak a vitában, egy lehetséges tűzeset, ami bármikor, bárhol előfordulhat. Ekkor már mindegy, hogy csak egy percre ugrottunk le a boltba, vagy a kutyával egy háztömb-körüli körre, a tűz olyan gyorsan képes terjedni, hogy másodpercek is számítanak.<br/><br/>A harmadik dolog, amire egy kommentelő felhívta a figyelmet, logikus, mégsem biztos, hogy mindenkinek eszébe jutna; nevezetesen, hogy nem is a babával történik valami a kiságyában, hanem velünk, azalatt az idő alatt, amíg levisszük a szemetet, megsétáltatjuk a kutyát, stb. Elüthetnek az úttesten, elájulhatunk, ránk eshet egy tégla, ráadásul, ha eszméletünket vesztjük, még el sem tudjuk mondani, hogy otthon hagytunk egy kisbabát pár percre, amíg mi levonultunk a szeméttel.<br/><br/>És ott van még pl. a zár kérdése; én például hajlamos vagyok egy tökéletesen működő zárral is szerencsétlenkedni, kulcsot beletörni, kódot elfelejteni, félreütni, stb. Ebben az esetben sem a babával történik baj, legalábbis első körben. De a dolgok szerencsétlen összjátéka tragédiába is fordulhat, jobb nem kísérteni a sorsot.<br/><br/>És ha mégis?<br/><br/>Azt hiszem, eleget írtam azokról a dolgokról, amik az embert megakadályozzák abban, hogy pár percre egyedül hagyja a lakásban a kisbabáját. De léteznek – számomra érthetetlen módon – emberek, akiknek elfogadható az, ha a gyermek pár percre kívül esik a szülő „hatótávolságán”. Ők olyan indokokkal szálltak be a vitába, mint hogy mindenkinek joga van élni, és igenis vannak dolgok, amiket meg kell tenni (valószínűleg itt a szemétkidobásra gondolhattak), és az alatt semmi rossz nem fog történni a babával.<br/><br/>Vagy például azzal, hogy ha megbizonyosodunk, hogy a baba édesdeden alszik, akkor még ha azalatt a pár perc alatt, amíg nem vagyunk a lakásban, fel is ébred, nem tud olyan kevés idő alatt semmi baj történni.<br/><br/>A facebook hírfolyamban gyakran szembejövő életigazságot írnék ide; onnantól, hogy megszületik a gyermeked, már nincs olyan, hogy te, hanem csak TI!<br/><br/>Amivel persze nem értek egyet, de azzal igen, hogy a felelősség igenis az enyém, bármi történik a gyerekemmel. El sem tudom képzelni azt az életfogytig tartó lelkiismeret-furdalást, amit az okozna, ha két percre elengedném a felelősséget, és véletlenül pont azalatt a két perc alatt történne valami baj.<br/><br/>Ismerőseim körében is felvetettem a kérdést. Egy történetben egy valamivel több mint kétéves kislány anyukája szaladt le a szeméttel, ez idő alatt a kislány – mert ezt látta a szülőktől – elfordította a biztonsági kallantyút, aminek következtében anyukája már nem tudott visszamenni a lakásba. Hiába magyarázta a kislánynak, hogy mit csináljon, annyira eluralkodott mindkettejükön a pánik, hogy a kislány nem tudta visszafordítani a kallantyút, a megoldás ebben az esetben tűzoltók képében érkezett.<br/><br/>A legmegdöbbentőbb esetet egy kollégám mesélte, akinek az ismerőse viszonylag fiatalon és meggondolatlanul vállalt gyereket, és a baba nevelését is ebben a szellemben folytatta. Pár hónapos volt a kisbaba, amikor anyukája úgy érezte, moziba kell mennie; el is ment, a babát két összetolt fotelra tette. Még sem kúszni, sem mászni nem tudott, de azért valahogy leküzdötte magát a két fotel közé, mire az anyuka hazaért. Iszonyúan megijedt, ráeszmélt, hogy mit tett, és soha többé nem fordult elő ilyen. Igaz, ez még a 70-es években történt.<br/><br/>De nem kell ehhez ilyen messzire a múltba menni, most is hallunk olyan szülőkről, akik simán elszaladnak egy késő esti moziba/bekapni egy gyrost, amikor a gyerek otthon alszik. Ők azzal érvelnek, hogy jó alvó gyerekekről van szó, akik nem ébrednek fel soha, és többen is laknak rajtuk kívül a társasházban, tehát nem egy tanyán hagyják a gyereket az éhes farkasoknak, hanem a civilizációban.<br/><br/>Kollégáink között is van olyan, aki elmondta, hogy igenis szaladt el alvó gyerek mellől a kisboltba - a teljes menet nem volt tíz perc, de addig egyedül volt a gyerek a lakásban. Moziba soha nem mentem volna el, de hat kifliért elugrottam, nem rendszeresen, de mondjuk ötször az évek során - mondja ő. <br/><br/>Sőt, nem is kell feltétlenül leragadni a lakásnál. Sokan vágják rá azonnal, hogy soha nem hagynák egyedül a gyereket, de sokkal kevesebben vannak azok, akik a hiszti után végre alvó gyereket kiszedik a benzinkútnál az ülésből, míg beszaladnak kifizetni a tankolást, vagy nem hagyják kint a beöltöztetett (és a hiszti után végre alvó!!!) gyereket a gangon, ahelyett, hogy becibálnák, levetkőztetnék és az ágyába fektetnék. Egy tankolást kifizetni gyerek nélkül egyáltalán nem tűnik olyan sokkolónak - nekünk. Amerikában élő barátnőm, amikor először látott ilyet - falfehér arccal mondta, hogy egy ilyenért náluk már a rendőrök várnák a visszatérő szülőt. És ezzel most nem azt akarjuk mondani, hogy ott jobb, tudjuk, fegyverviselés - csak azt, hogy más, ott például nagyon máshol húzódik az a határ, ami még belefér.<br/><br/>Nézzük, mit ír erről a Btk;<br/><br/>Btk. 208. § Kiskorú veszélyeztetése<br/><br/>208. § (1) A kiskorú nevelésére, felügyeletére vagy gondozására köteles személy – ideértve a szülői felügyeletet gyakorló szülő, illetve gyám élettársát, továbbá a szülői felügyeleti jogától megfosztott szülőt is, ha a kiskorúval közös háztartásban vagy egy lakásban él –, aki e feladatából eredő kötelességét súlyosan megszegi, és ezzel a kiskorú testi, értelmi, erkölcsi vagy érzelmi fejlődését veszélyezteti, bűntett miatt egy évtől öt évig terjedő szabadságvesztéssel büntetendő.<br/><br/>Egy jogászt is megkérdeztem erről; vajon az a konkrét eset, hogy egy anyuka öt percre egyedül hagyja a kisbabáját a lakásban, kimeríti-e a kiskorú veszélyeztetésének tényállását, mely büntetőjogi kategória. Molnár Adél ügyvédnő szerint ilyen esetekben gumiszabályként kezelik a törvényeket; az általános bírói gyakorlat szerint nem számít súlyos kötelességszegésnek a gyerek rövid ideig felügyelet nélkül hagyása, de a körülményeket minden esetben vizsgálni kell - vannak fokozatok, ennek megfelelően differenciálni kell; nem mindegy, hogy 40 fokos kánikulában, egy kocsiban hagyom öt percre a gyereket egyedül, vagy a kiságyában szunyókálva. Az első esetben – ahogy a közelmúltban történt esetben is – elítélik a szülőt, a második esetben azonban nem; ha egy alvó babát a kiságyban hagyunk, azzal még nem vétünk a törvény ellen, más kérdés, hogy saját lelkiismeretünkkel miként tudunk megbirkózni.");
		
		BBCodeParser.safeProcessor(t);
		
	}	
	
	
	
	@Test
	public void bTextStave(){	
		BBCodeParser.safeProcessor("<b></b> <b></b> <b></b> <b></b> <b></b> <b></b> <b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b><b></b>");
	}
	
	@Test
	public void createQuestionNoOption(){
		QuestionDaoMapper mapper=(QuestionDaoMapper)getMapper(QuestionDaoMapper.class);
		Question q=new Question();
		q.setDescription("testdesc");
		q.setStatus(Question.STATUS_ACTIVE);
		q.setImg("img1.jpg");
		q.setTargetCountry("HU");
		LocationData l = new LocationData();
		l.setId(new Long(11)); // Budapest
		q.setTargetLocation(l);
		q.setType(1);
		q.setMandatory(true);
		q.setUrl("http://test");
		q.setTitle("testing");
		q.setColor("255,255,255");
		q.setTags(new String[]{"tag1","tag2"});
		q.setLanguage("hungarian");
		
		Session s=new Session();
		s.setCode("testtoken");
		q.setSession(s);
		
		mapper.insertQuestion(q);

		QuestionFilter filter=new QuestionFilter(RequestWrapper.getSession().getCountry());
		filter.setSession(session);
		filter.setId(q.getId());

		Question returned = mapper.findOneQuestion(filter);
		assertEquals(0, returned.getOptions().size());
		assertTrue(returned.getTags().length==2);
	}
	protected QuestionServiceImpl createQuestionService(){
		QuestionServiceImpl questionServiceImpl=new QuestionServiceImpl();
		TagServiceImpl tagServiceImpl=new TagServiceImpl();
		QuestionDaoMapper questionDaoMapper=(QuestionDaoMapper)getMapper(QuestionDaoMapper.class);
		AnswerDaoMapper answerDaoMapper=(AnswerDaoMapper)getMapper(AnswerDaoMapper.class);
		TagDaoMapper tagDaoMapper=(TagDaoMapper)getMapper(TagDaoMapper.class);
		tagServiceImpl.setMapper(tagDaoMapper);

		LocationDataDaoMapper locationDataMapper=(LocationDataDaoMapper)getMapper(LocationDataDaoMapper.class);

		AnswerStatServiceImpl anserstat=new AnswerStatServiceImpl();
		
		anserstat.setAnswerServiceMapper(answerDaoMapper);
		questionServiceImpl.setTagServiceImpl(tagServiceImpl);
		questionServiceImpl.setAnswerStatServiceImpl(anserstat);
		questionServiceImpl.setQuestionDaoMapper(questionDaoMapper);
		questionServiceImpl.setLocationDaoMapper(locationDataMapper);
		return questionServiceImpl;
	}
	
	@Test
	public void testGetSession() throws Exception {
		
		QuestionServiceImpl questionServiceImpl=createQuestionService();
		QuestionDaoMapper questionDaoMapper=(QuestionDaoMapper)getMapper(QuestionDaoMapper.class);

		Session s=new Session();
		s.setCountry("HU");
		s.setCode("testtoken");
		RequestWrapper.setSession(s);
		Question q=createQuestion(questionServiceImpl);
		assertEquals(3, q.getTags().length);
		
		
		Question returned = questionServiceImpl.findOne(q.getId(), null);
		assertNotNull(returned);
		assertNotNull(returned.getColor());
		assertNotNull(returned.getColor());
		assertEquals("hungarian", returned.getLanguage());
		
		assertTrue(returned.getId().equals(q.getId()));
		assertTrue(returned.getUrl().equals(q.getUrl()));
		assertTrue(returned.getTargetLocation().getCityName().equals("Budapest"));
		assertEquals(3, returned.getTags().length);
		
		//assertNotNull(returned.getTargetCountry());
		assertNotNull(returned.getOptions());
		assertTrue(returned.getOptions().size()==2);
		assertTrue(returned.getOptions().get(0).getOrd()==1);
		
		assertTrue(returned.getCountry().equals("HU"));
		
		returned.setTitle("test title");
				
		Option o=returned.getOptions().get(0);
		o.setText("test option");
		returned.getOptions().remove(1);
		returned.setStatus(100);
		questionServiceImpl.save(returned);
		assertEquals(2, returned.getTags().length); // one tag removed bacause of title 
		
		Question returned2 = questionServiceImpl.findOne(q.getId(), null);
		assertEquals("test title", returned2.getTitle());
		assertEquals(2, returned2.getTags().length);
		
		assertEquals(2, returned2.getOptions().size());
		
		assertEquals("yes", returned2.getOptions().get(0).getText());
		
		QuestionFilter qf=new QuestionFilter(RequestWrapper.getSession().getCountry());
		qf.setLimit(5);
		qf.setSession("test session");
		
		List<Question> questions = questionDaoMapper.findPopularQuestion(qf);
		assertNotNull(questions);
		assertTrue(questions.size()>1);
		
	}
	@Test
	public void countTest() throws Exception {
		QuestionServiceImpl questionService=createQuestionService();
		Question q=createQuestion(questionService);
		QuestionFilter filter=new QuestionFilter(RequestWrapper.getSession().getCountry());
		filter.setSession(session);
		long count=questionService.findPopularQuestionCount(filter);
		assertTrue(count>0);
		
		QuestionDaoMapper questionDaoMapper=(QuestionDaoMapper)getMapper(QuestionDaoMapper.class);

		List<Question> l=questionDaoMapper.findAsweredQuestion(filter);
		assertTrue(l.size()==0);
	}
	@Test
	public void countryCountTest() throws Exception {
		QuestionDaoMapper questionDaoMapper=(QuestionDaoMapper)getMapper(QuestionDaoMapper.class);
		QuestionFilter filter=new QuestionFilter(RequestWrapper.getSession().getCountry());
		filter.setSession(session);
		long count=questionDaoMapper.getQuestionCountryCount(filter);
		assertTrue(count>0);
	}
	@Test
	public void queryTest() throws Exception {
		QuestionServiceImpl questionService=createQuestionService();
		Question q=createQuestion(questionService);
		PagingFilter filter=new QuestionFilter(RequestWrapper.getSession().getCountry());
		filter.setLimit(5);
		filter.setQuery("kutya");
		filter.setLanguage("hungarian");
		List<SearchResult> question=questionService.getQuestionDaoMapper().freeTextQueryQuestion(filter);
		assertTrue(question.size()>0);
		
		QuestionDaoMapper questionDaoMapper=(QuestionDaoMapper)getMapper(QuestionDaoMapper.class);

		List<Question> l=questionDaoMapper.findAsweredQuestion(filter);
		assertTrue(l.size()==0);
	}	
}
