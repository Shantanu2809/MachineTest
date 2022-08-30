package TestRun;


import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestRunner {
	WebDriver driver;
	@Parameters("browser")
	@BeforeTest
	public void beforetest(String browser)
	{
		if(browser.equalsIgnoreCase("chrome"))
		{
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		}
		else if(browser.equalsIgnoreCase("edge"))
		{
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		driver.manage().window().maximize();
		driver.get("http://timesheet.aqmtechnologies.com/");
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		
	}
	
	@Test(dataProvider = "logindata")
	public void test(String username, String pass) throws Exception
	{
		WebElement loginname = driver.findElement(By.id("txtUserName"));
		loginname.click();
		loginname.sendKeys(username);
		Thread.sleep(2000);
		WebElement password = driver.findElement(By.id("txtPassword"));
		password.click();
		password.sendKeys(pass);
		Thread.sleep(2000);
		driver.findElement(By.id("btnLogin")).click();
		Thread.sleep(2000);
		Assert.assertTrue(driver.getTitle().contains("Home Page"));
		Thread.sleep(2000);
		
		
		
	}
	
	@AfterTest
	public void aftertest()
	{
		driver.quit();
	}
	
	@DataProvider
	public String[][] logindata()
	{
		String[][] data = new String [3][2];
		data[0][0] = "shantanu.mondal";
		data[0][1] = "asd@5858";
		data[1][0]	= "shantanu.mondal";
		data[1][1]	= "xchg";
		data[2][0] = "deep.shah";
		data[2][1]	= "dpshah@08";
		
		return data;
		
	}
	
	@AfterMethod
	public void aftermethod(ITestResult result) throws Exception
	{
		if (result.getStatus()== ITestResult.FAILURE)
		{
			TakesScreenshot ts = (TakesScreenshot)driver;
			File from = ts.getScreenshotAs(OutputType.FILE);
			FileHandler.copy(from, new File("D:\\Users\\Temp\\Desktop\\TaskFramework\\Screenshot"+result.getName()+".png"));
		}
	}

}
