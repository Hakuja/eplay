import java.util.List;
import java.util.Map;

import models.User;

import org.junit.Before;
import org.junit.Test;

import play.data.validation.Error;
import play.data.validation.Validation;
import play.test.Fixtures;
import play.test.UnitTest;


public class UserTest extends UnitTest {

	@Before
    public void setup() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }
    
	@Test
	public void test_User() {
		User user = User.find("username = ?", "bobmarlay").first();
		assertNotNull(user);
		assertEquals("bob@gmail.com", user.email);
	}
	
	@Test
	public void test_isValidLogin_ユーザ名とパスワードが正しければ成功すること() {
		assertTrue(User.isValidLogin("bobmarlay", "test"));
	}

	@Test
	public void test_isValidLogin_ユーザ名とパスワードが正しくなければ失敗すること() {
		assertFalse(User.isValidLogin("bobmarlay", "hoge"));
	}
	
	@Test
	public void test_getValidUser() {
		User user = getValidUser();
		assertTrue(Validation.current().valid(user).ok);
	}
	
	@Test
	public void test_バリデーション_パスワードが一致しなければNG() {
		User user = getValidUser();
		user.passwordConfirmation = "hoge";
		
		assertFalse(Validation.current().valid(user).ok);
		assertTrue(Validation.current().hasError(".password"));
	}
	
	User getValidUser() {
		User user = new User();
		user.username = "bobmarlay";
		user.password = "test";
		user.passwordConfirmation = "test";
		user.email = "bob@gmail.com";
		user.firstname = "Bob";
		user.lastname = "Marlay";

		return user;
	}
}
