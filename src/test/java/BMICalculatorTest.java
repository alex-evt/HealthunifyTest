import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BMICalculatorTest {

    private WebDriver driver;
    private final String height = "183";

    @BeforeClass
    public void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test(priority = 1, description = "negative: click 'Calculate' with empty fields")
    public void verifyEmptyAllFieldsTest() {
        driver.get("https://healthunify.com/bmicalculator/");
        driver.findElement(By.xpath("//input[@value='Calculate']")).click();
        String actualText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        Assert.assertEquals(actualText, "Enter the value for weight");
    }

    @Test(priority = 2, description = "negative: input less than 10kg in Weight field")
    public void verifyWeightLessThan10kgTest() {
        driver.get("https://healthunify.com/bmicalculator/");
        driver.findElement(By.xpath("//input[@name='ht']")).sendKeys(height);
        driver.findElement(By.xpath("//input[@name='wg']")).sendKeys("7");
        driver.findElement(By.xpath("//input[@value='Calculate']")).click();
        String actualText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        Assert.assertEquals(actualText, "Weight should be greater than 10kgs");
    }

    @Test(priority = 3, description = "negative: check inputting invalid characters in the Weight and Height fields")
    public void verifyInputtingInvalidCharactersInFieldsTest() {
        driver.get("https://healthunify.com/bmicalculator/");
        driver.findElement(By.xpath("//input[@name='ht']")).sendKeys("qwert");
        driver.findElement(By.xpath("//input[@name='wg']")).sendKeys("qwert");
        driver.findElement(By.xpath("//input[@value='Calculate']")).click();
        String actualText = driver.findElement(By.xpath("//input[@name='si']")).getAttribute("value");
        Assert.assertEquals(actualText, "NaN");
    }

    @Test(priority = 4, description = "positive: check from 11-50 is starvation category with height 183")
    public void verifyStarvationCategoryTest() {
        driver.get("https://healthunify.com/bmicalculator/");
        driver.findElement(By.xpath("//input[@name='ht']")).sendKeys(height);
        driver.findElement(By.xpath("//input[@name='wg']")).sendKeys("48");
        driver.findElement(By.xpath("//input[@value='Calculate']")).click();

        String actualText = driver.findElement(By.xpath("//input[@class='content']")).getAttribute("value");
        Assert.assertEquals(actualText, "Your category is Starvation");
    }

    @Test(priority = 5, description = "positive: check from 84-100 is overweight category with height 183 via dropdowns")
    public void verifyOverweightCategoryViaDropdownsTest() {
        driver.get("https://healthunify.com/bmicalculator/");
        Select weightDropdown = new Select(driver.findElement(By.xpath("//select[@name='opt1']")));
        Select firstHeightDropdown = new Select(driver.findElement(By.xpath("//select[@name='opt2']")));
        weightDropdown.selectByVisibleText("pounds");
        driver.findElement(By.xpath("//input[@name='wg']")).clear();
        driver.findElement(By.xpath("//input[@name='wg']")).sendKeys("93");
        firstHeightDropdown.selectByIndex(5);
        driver.findElement(By.xpath("//input[@value='Calculate']")).click();
        String actualText = driver.findElement(By.xpath("//input[@class='content']")).getAttribute("value");
        Assert.assertEquals(actualText, "Your category is Overweight");
    }

    @AfterClass
    public void closeBrowser() {
        driver.quit();
    }
}
