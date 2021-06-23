package io.gatling.computerdatabase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ComputerDatabaseTest extends AbstractTestCase {
    @DataProvider
    public static Object[][] computersDataProvider() {
        return new Object[][] { { "Zenbook UX31", "2010-06-23", "2020-06-23", "ASUS" } };
    }

    @Test(priority = 0)
    public void computerAddNew() {
        driver.get("http://computer-database.gatling.io/computers");

        driver.findElement(By.className("success"));
        driver.findElement(By.id("add")).click();
    }

    @Test(priority = 1, dataProvider = "computersDataProvider")
    public void computerAddForm(String computerName, String introducedDate, String discontinuedDate, String vendor) {
        driver.get("http://computer-database.gatling.io/computers/new");

        WebElement computerNameField = driver.findElement(By.id("name"));
        computerNameField.sendKeys(computerName);
        WebElement introducedAt = driver.findElement(By.id("introduced"));
        introducedAt.sendKeys(introducedDate);
        WebElement discontinuedAt = driver.findElement(By.id("discontinued"));
        discontinuedAt.sendKeys(discontinuedDate);
        Select companyName = new Select(driver.findElement(By.id("company")));
        companyName.selectByVisibleText(vendor);

        WebElement form = driver.findElement(By.xpath("//*[@id=\"main\"]/form"));
        form.submit();
    }

    @Test(priority = 2, dataProvider = "computersDataProvider")
    public void computerCheckTable(String computerName, String introducedDate, String discontinuedDate, String vendor) {

        Assert.assertEquals(driver.getCurrentUrl(), "http://computer-database.gatling.io/computers");
        String targetTitle = driver.getTitle();
        Assert.assertEquals(targetTitle, "Computers database");
        String alertMessage = driver
                .findElement(By.xpath("//div[@class=\"alert-message warning\"][strong/text()[ .='Done !  ']]"))
                .getText();
        Assert.assertNotNull(alertMessage);

        driver.findElement(By.id("searchbox")).sendKeys(computerName);
        driver.findElement(By.id("searchsubmit")).click();

        WebElement searchResults = driver.findElement(By
                .xpath("//table[@class=\"computers zebra-striped\"]/tbody/tr/td/a/text()[ .='" + computerName + "']"));
        Assert.assertNotNull(searchResults);
    }
}
