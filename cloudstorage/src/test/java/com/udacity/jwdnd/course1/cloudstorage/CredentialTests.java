package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for Credential Creation, Viewing, Editing, and Deletion.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTests extends CloudStorageApplicationTests {

	public static final String FIRST_URL = "https://www.google.com/";
	public static final String FIRST_USERNAME = "first";
	public static final String FIRST_PASSWORD = "first";
	public static final String SECOND_URL = "http://www.facebook.com/";
	public static final String SECOND_USERNAME = "second";
	public static final String SECOND_PASSWORD = "second";
	public static final String THIRD_URL = "http://www.twitter.com/";
	public static final String THIRD_USERNAME = "third";
	public static final String THIRD_PASSWORD = "third";

	/**
	 * Test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed
	 * password is encrypted.
	 */
	@Test
	public void testCredentialCreation() {
		HomePage homePage = signUpAndLogin();
		System.out.println("1");
		createAndVerifyCredential(FIRST_URL, FIRST_USERNAME, FIRST_PASSWORD, homePage);
		System.out.println("2");
		homePage.deleteCredential();
		System.out.println("3");
		ResultPage resultPage = new ResultPage(driver);
		System.out.println("4");
		resultPage.clickOk();
		System.out.println("5");
		homePage.logout();
	}

	private void createAndVerifyCredential(String url, String username, String password, HomePage homePage) {
		createCredential(url, username, password, homePage);
		homePage.navToCredentialsTab();
		Credentials credential = homePage.getFirstCredential();
		Assertions.assertEquals(url, credential.getUrl());
		Assertions.assertEquals(username, credential.getUsername());
		Assertions.assertNotEquals(password, credential.getPassword());
	}

	private void createCredential(String url, String username, String password, HomePage homePage) {
		homePage.navToCredentialsTab();
		homePage.addNewCredential();
		setCredentialFields(url, username, password, homePage);
		homePage.saveCredentialChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToCredentialsTab();
	}

	private void setCredentialFields(String url, String username, String password, HomePage homePage) {
		homePage.setCredentialUrl(url);
		homePage.setCredentialUsername(username);
		homePage.setCredentialPassword(password);
	}

	/**
	 * Test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the
	 * credentials, and verifies that the changes are displayed.
	 */
	@Test
	public void testCredentialModification() {
		HomePage homePage = signUpAndLogin();
		createAndVerifyCredential(FIRST_URL, FIRST_USERNAME, FIRST_PASSWORD, homePage);
		Credentials originalCredential = homePage.getFirstCredential();
		String firstEncryptedPassword = originalCredential.getPassword();
		homePage.editCredential();
		String newUrl = SECOND_URL;
		String newCredentialUsername = SECOND_USERNAME;
		String newPassword = SECOND_PASSWORD;
		setCredentialFields(newUrl, newCredentialUsername, newPassword, homePage);
		homePage.saveCredentialChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToCredentialsTab();
		Credentials modifiedCredential = homePage.getFirstCredential();
		Assertions.assertEquals(newUrl, modifiedCredential.getUrl());
		Assertions.assertEquals(newCredentialUsername, modifiedCredential.getUsername());
		String modifiedCredentialPassword = modifiedCredential.getPassword();
		Assertions.assertNotEquals(newPassword, modifiedCredentialPassword);
		Assertions.assertNotEquals(firstEncryptedPassword, modifiedCredentialPassword);
		homePage.deleteCredential();
		resultPage.clickOk();
		homePage.logout();
	}

	/**
	 * Test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
	 */
	@Test
	public void testDeletion() {
		HomePage homePage = signUpAndLogin();
		createCredential(FIRST_URL, FIRST_USERNAME, FIRST_PASSWORD, homePage);
		createCredential(SECOND_URL, SECOND_USERNAME, SECOND_PASSWORD, homePage);
		createCredential(THIRD_URL, THIRD_USERNAME, THIRD_PASSWORD, homePage);
//		Assertions.assertFalse(homePage.noCredentials(driver));
		homePage.deleteCredential();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToCredentialsTab();
		homePage.deleteCredential();
		resultPage.clickOk();
		homePage.navToCredentialsTab();
		homePage.deleteCredential();
		resultPage.clickOk();
		homePage.navToCredentialsTab();
		Assertions.assertTrue(homePage.noCredentials(driver));
		homePage.logout();
	}
}
