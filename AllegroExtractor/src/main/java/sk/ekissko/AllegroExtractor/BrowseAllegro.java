package sk.ekissko.AllegroExtractor;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * Hello world!
 *
 */
public class BrowseAllegro {
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public void setUp() throws Exception {
		driver = new InternetExplorerDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}


	public void browse() {
		driver.get("http://moto.allegro.pl/");
		
		//driver.findElement(By.linkText("Cz─Ö┼¢ci samochodowe   (7508444)")).click(); 
	}
	
	
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	} 


	
   
}
