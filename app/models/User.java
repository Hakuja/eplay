package models;

import javax.persistence.Entity;
import javax.persistence.Transient;

import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class User extends Model {

	@Required
	@MinSize(6)
	public String username;
	@Required
	@Email
	public String email;
	@Required
	@Password
	@Transient
	@Equals("passwordConfirmation")
	public String password;
	@Required
	@Password
	@Transient
	public String passwordConfirmation;
	public String passwordHash;
	@Required
	public String firstname;
	@Required
	public String lastname;

	public void setPassword(String password) {
		this.password = password;
		this.passwordHash = Codec.hexMD5(password);
	}

	public static boolean isValidLogin(String username, String password) {
		// return TRUE if there is a single matching username/passwordHash
		return (count("username = ? AND passwordHash = ?", username,
				Codec.hexMD5(password)) == 1);
	}
	
	public static void main(String[] args) {
		System.out.println(Codec.hexMD5("test"));
	}
}
