package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private WebDriverWait webDriverWait;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new FirefoxDriver();
		this.webDriverWait = new WebDriverWait(driver, 2);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void unauthorizedHomePageTest() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	public void doSignupLogin() {
		// signup
		doMockSignUp("Quan", "Nguyen", "quannb2", "quannb2");

		// login
		doLogIn("quannb2", "quannb2");
	}

	public void redirectionToFilesTab() {
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab")));
		driver.findElement(By.id("nav-files-tab")).click();
	}

	public void redirectionToNotesTab() {
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
	}

	public void redirectionToCredentialsTab() {
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
	}

	@Test
	public void testUserSignupLoginLogout() {
		doSignupLogin();

		// logout
		WebElement logoutBtn = driver.findElement(By.id("logout-btn"));
		logoutBtn.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testAddingNote() {
		doSignupLogin();

		redirectionToNotesTab();
		Assertions.assertTrue(driver.findElement(By.id("nav-notes")).isDisplayed());

		// click on Add a New Note button
		WebElement addNoteButton = driver.findElement(By.id("add-note-btn"));
		addNoteButton.click();

		// Input to New Note modal
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.click();
		inputNoteTitle.sendKeys("Note 1");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputNoteDescription = driver.findElement(By.id("note-description"));
		inputNoteDescription.click();
		inputNoteDescription.sendKeys("Note description 1");

		// Click submit New Note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-btn")));
		WebElement submitNoteBtn = driver.findElement(By.id("submit-note-btn"));
		submitNoteBtn.click();

		redirectionToNotesTab();
		// Check The Note has been successfully created
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(driver.findElement(By.id("note-item-title")).getText().contains("Note 1"));
	}

	@Test
	public void testEditingNote() {
		testAddingNote();

		// open edit Note modal
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note-btn")));
		WebElement editNoteBtn = driver.findElement(By.id("edit-note-btn"));
		editNoteBtn.click();

		// Edit the note description
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement editNoteDescription = driver.findElement(By.id("note-description"));
		editNoteDescription.click();
		editNoteDescription.clear();
		editNoteDescription.sendKeys("edited note description");

		// Click submit to save the edited note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-btn")));
		WebElement submitNote = driver.findElement(By.id("submit-note-btn"));
		submitNote.click();

		redirectionToNotesTab();
		// Check The Note has been successfully edited
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(
				driver.findElement(By.id("note-item-description")).getText().contains("edited note description"));
	}

	@Test
	public void testDeletingNote() {
		testAddingNote();
		// Click on delete button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note-btn")));
		WebElement deleteNoteBtn = driver.findElement(By.id("delete-note-btn"));
		deleteNoteBtn.click();

		redirectionToNotesTab();
		// check note is deleted
		List<WebElement> notesList = driver.findElements(By.id("notes-body"));
		Assertions.assertEquals(0, notesList.size());
	}

	@Test
	public void testAddingCredential() {
		doSignupLogin();

		redirectionToCredentialsTab();
		Assertions.assertTrue(driver.findElement(By.id("nav-credentials")).isDisplayed());

		// click on Add a New Credential button
		WebElement addNoteButton = driver.findElement(By.id("add-credentials-button"));
		addNoteButton.click();

		// Input to New Credential modal
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputCredentialUrl = driver.findElement(By.id("credential-url"));
		inputCredentialUrl.click();
		inputCredentialUrl.sendKeys("http://localhost:8080/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputCredentialUsername = driver.findElement(By.id("credential-username"));
		inputCredentialUsername.click();
		inputCredentialUsername.sendKeys("QuanNB2");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputCredentialPassword = driver.findElement(By.id("credential-password"));
		inputCredentialPassword.click();
		inputCredentialPassword.sendKeys("123");

		// Click submit New Credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-credential-btn")));
		WebElement submitNoteBtn = driver.findElement(By.id("submit-credential-btn"));
		submitNoteBtn.click();

		redirectionToCredentialsTab();
		// Check The Credential has been successfully created
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertTrue(
				driver.findElement(By.id("credential-item-url")).getText().contains("http://localhost:8080/home"));
	}

	@Test
	public void TestEditingCredential() throws InterruptedException {
		testAddingCredential();

		// open Credential modal
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-credential-btn")));
		WebElement editCredentialBtn = driver.findElement(By.id("edit-credential-btn"));
		editCredentialBtn.click();

		// Change URL
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement editCredentialURL = driver.findElement(By.id("credential-url"));
		editCredentialURL.click();
		editCredentialURL.clear();
		editCredentialURL.sendKeys("http://localhost:8080/login");

		// get the unencrypted pwd
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		String inputPassword = driver.findElement(By.id("credential-password")).getAttribute("testing");

		// Click submit to save the edited credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-credential-btn")));
		WebElement submitCredentialBtn = driver.findElement(By.id("submit-credential-btn"));
		submitCredentialBtn.click();

		redirectionToCredentialsTab();
		// Check The Credential has been successfully edited
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertTrue(
				driver.findElement(By.id("credential-item-url")).getText().contains("http://localhost:8080/login"));

		Assertions.assertNotEquals(driver.findElement(By.id("credential-item-password")).getText(), inputPassword);
	}

	@Test
	public void testDeletingCredential() {
		testAddingCredential();
		// Click on delete button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-credential-btn")));
		WebElement deleteNoteBtn = driver.findElement(By.id("delete-credential-btn"));
		deleteNoteBtn.click();

		redirectionToCredentialsTab();
		// check note is deleted
		List<WebElement> notesList = driver.findElements(By.id("credentials-body"));
		Assertions.assertEquals(0, notesList.size());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		 // You may have to modify the element "success-msg" and the sign-up
		 // success message below depening on the rest of your code.
		 */
		//		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}



	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 * 
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void testDeletingFile() {
		doSignupLogin();

		// Try to upload an arbitrary large file
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();

		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		
		redirectionToFilesTab();
		// Click on delete button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-file-btn")));
		WebElement deleteNoteBtn = driver.findElement(By.id("delete-file-btn"));
		deleteNoteBtn.click();

		redirectionToFilesTab();
		// check note is deleted
		List<WebElement> notesList = driver.findElements(By.id("files-body"));
		Assertions.assertEquals(0, notesList.size());
	}

}
