package io.mosip.kernel.masterdata.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.hibernate5.HibernateObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.masterdata.dto.LanguageResponseDto;
import io.mosip.kernel.masterdata.entity.Language;
import io.mosip.kernel.masterdata.exception.LanguageFetchException;
import io.mosip.kernel.masterdata.exception.LanguageNotFoundException;
import io.mosip.kernel.masterdata.repository.LanguageRepository;
import io.mosip.kernel.masterdata.service.LanguageService;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class LanguageServiceImplTest {

	@Autowired
	private LanguageService languageService;

	@MockBean
	private LanguageRepository languageRepository;

	private List<Language> languages;
	private Language hin;
	private Language eng;

	@Test
	public void testSucessGetAllLaguages() {
		loadSuccessData();
		LanguageResponseDto dto = languageService.getAllLaguages();
		assertNotNull(dto);
		assertEquals(2, dto.getLanguages().size());
	}

	@Test(expected = LanguageNotFoundException.class)
	public void testLanguageNotFoundException() {
		Mockito.when(languageRepository.findAll(Language.class)).thenReturn(null);
		languageService.getAllLaguages();
	}

	@Test(expected = LanguageNotFoundException.class)
	public void testLanguageNotFoundExceptionWhenNoLanguagePresent() {
		Mockito.when(languageRepository.findAll(Language.class)).thenReturn(new ArrayList<Language>());
		languageService.getAllLaguages();
	}

	@Test(expected = LanguageFetchException.class)
	public void testLanguageFetchException() {
		Mockito.when(languageRepository.findAll(Language.class)).thenThrow(HibernateObjectRetrievalFailureException.class);
		languageService.getAllLaguages();
	}

	private void loadSuccessData() {
		languages = new ArrayList<>();

		// creating language
		hin = new Language();
		hin.setLanguageCode("hin");
		hin.setLanguageName("hindi");
		hin.setLanguageFamily("hindi");
		hin.setNativeName("hindi");
		hin.setActive(Boolean.TRUE);

		eng = new Language();
		eng.setLanguageCode("en");
		eng.setLanguageName("english");
		eng.setLanguageFamily("english");
		eng.setNativeName("english");
		eng.setActive(Boolean.TRUE);

		// adding language to list
		languages.add(hin);
		languages.add(eng);

		// when asked then return
		Mockito.when(languageRepository.findAll(Language.class)).thenReturn(languages);
	}

}
