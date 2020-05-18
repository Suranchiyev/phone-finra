package com.phone.phonefinra;

import com.phone.phonefinra.beans.Phone;
import com.phone.phonefinra.service.PhoneService;
import com.phone.phonefinra.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.Set;

@SpringBootTest
class PhoneFinraApplicationTests {

	@Test
	void testOne() {
		String str = "7745346272";
		PhoneService phoneService = new PhoneService();
		Set<Phone> result = phoneService.generatePhones(str);
		Assertions.assertEquals(151200, result.size());
	}

	@Test
	void testTwo() {
		String str = "4234422421";
		PhoneService phoneService = new PhoneService();
		Set<Phone> result = phoneService.generatePhones(str);
		File tmpFile = new File("src/test/resources/tmp/4234422421.txt");
		FileUtils.writeAllPhones(result, tmpFile);

		List<String> expectedData = FileUtils.getFileContent("src/test/resources/test-data/1589816838642.txt");
		List<String> actualData = FileUtils.getFileContent(tmpFile.getAbsolutePath());
		Assertions.assertLinesMatch(expectedData, actualData);

		tmpFile.delete();
	}

	@Test
	void testThree() {
		String str = "1111111111";
		PhoneService phoneService = new PhoneService();
		Set<Phone> result = phoneService.generatePhones(str);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testFour() {
		try {
			// negative test with letter in the number
			String str = "774534627q";
			PhoneService phoneService = new PhoneService();
			phoneService.generatePhones(str);
			Assertions.fail("There was no exception when letter in the number");
		}catch(Exception e){
			// Expecting exception here
		}
	}

	@Test
	void testFive() {
		try {
			// negative test with extra number
			String str = "77453462734";
			PhoneService phoneService = new PhoneService();
			phoneService.generatePhones(str);
			Assertions.fail("There was no exception when extra number");
		}catch(Exception e){
			// Expecting exception here
		}
	}
}
