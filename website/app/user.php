<?php

$user = new user($db_mysql);

// logout
if(isset($_GET['logout'])) {
	if(!empty($_COOKIE['LOGIN_TOKEN']) && !empty($_COOKIE['LOGIN_USER']))
		$user->logout($_COOKIE['LOGIN_TOKEN'], $_COOKIE['LOGIN_USER']);
	elseif(!empty($_SESSION['LOGIN_TOKEN']) && !empty($_SESSION['LOGIN_USER']))
		$user->logout($_SESSION['LOGIN_TOKEN'], $_SESSION['LOGIN_USER']);
	header('Location: '.ROOT);
	exit();
// register
} elseif(!empty($_POST['username']) && !empty($_POST['password']) && isset($_POST['email'])) {
	lg('trying to register');
	$success = $user->register($_POST['username'], $_POST['password'], $_POST['email'] ?? NULL, $_POST['g-recaptcha-response'], 'ok');
	if($success === TRUE) {
		lg('registered');
	} elseif($success == 'reCaptcha failed') {
		header('Location: '.ROOT.'?failed_register_recaptcha#login-form');
		exit();
	}
	else {
		header('Location: '.ROOT.'?failed_register#login-form');
		exit();
	}
// login
} elseif(!empty($_POST['username']) && !empty($_POST['password'])) {
	if($user->login($_POST['username'], $_POST['password'], $_POST['remember_me'] ?? false)) {
		lg('login successfull!');
	} else {
		header('Location: '.ROOT.'?failed_login&user='.$_POST['username'].'#login-form');
		exit();
	}
// login per cookie
} elseif(!empty($_COOKIE['LOGIN_TOKEN']) && !empty($_COOKIE['LOGIN_USER'])) {
	$success = $user->loginViaToken($_COOKIE['LOGIN_TOKEN'], $_COOKIE['LOGIN_USER']);
	if($success)
		lg('still logged in [cookie]');
	else
		lg('your login cookie is wrong');
// login per session
} elseif(!empty($_SESSION['LOGIN_TOKEN']) && !empty($_SESSION['LOGIN_USER'])) {
	$success = $user->loginViaToken($_SESSION['LOGIN_TOKEN'], $_SESSION['LOGIN_USER']);
	if($success)
		lg('still logged in [session]');
	else
		lg('your login session is wrong');
} else {
	lg('not logged in');
}
//$user->showAllUsers();



class user {
	private $logged = false; //eingeloggt?
	private $username = 'anonym';
	private $currentToken = '';
	private $db;
	private $tokenInterval = 'INTERVAL 1 MONTH';
	
	function __construct($db_mysql) {
		$this->db = $db_mysql;
	}
	
	public function getUsername() {
		return $this->username;
	}
	
	public function getToken() {
		return $this->currentToken;
	}
	
	
	public function islogged() {
		return $this->logged;
	}
	
	public function getEmail() {
		if($this->logged) {
			$sth = $this->db->prepare("SELECT email FROM USERS WHERE user = :user");
			$sth->bindParam(':user', $this->username);
			$sth->execute();
			while ($row = $sth->fetch(PDO::FETCH_ASSOC)) {
				return $this->symmetric_openssl_decrypt($row['email'], OPENSSL_KEY);
			}
		}
	}
	
	public function changeEmail($newEmail) {
		$newEmail = $this->symmetric_openssl_encrypt(trim($newEmail), OPENSSL_KEY);
		$sth = $this->db->prepare("UPDATE USERS SET email = :newEmail WHERE user = :user;");
		$sth->bindParam(':user', $this->username);
		$sth->bindParam(':newEmail', $newEmail);
		$sth->execute();
	}
	
	public function changePassword($newPassword, $oldPassword) {
		$newPassword = password_hash($newPassword, PASSWORD_DEFAULT);
		
		$user = $this->db->prepare("SELECT * FROM USERS WHERE LOWER(user) = LOWER(:user);");
		$user->bindParam(':user', $this->username);
		$user->execute();
		$user = $user->fetch(PDO::FETCH_ASSOC);
		
		if(password_verify($oldPassword, $user['password'])) {
			$sth = $this->db->prepare("UPDATE USERS SET password = :newPassword WHERE user = :user;");
			$sth->bindParam(':user', $this->username);
			$sth->bindParam(':newPassword', $newPassword);
			$sth->execute();
			
			// logout all other instances of this user
			$user = $this->db->prepare("DELETE FROM LOGIN_TOKENS WHERE user = :user AND token != :currentToken;");
			$user->bindParam(':user', $this->username);
			$user->bindParam(':currentToken', $this->currentToken);
			$user->execute();
			
			return SUCCESS;
		}
		return FAILURE;
	}
	
	public function check($username) {
		if($this->logged && $username == $this->username)
			return true;
		else
			return false;
	}

	public function login($username, $password, $remember_me = false) {
		$return = false;
		$username = trim($username);
		
		try {
			$sth = $this->db->prepare("SELECT * FROM USERS WHERE LOWER(user) = LOWER(:user)");
			$sth->bindParam(':user', $username);
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if(password_verify($password, $result['password'])) {
				$this->username = $result['user'];
				$this->logged = true;
				//$_SESSION['username'] = $result['user'];
				$token = $this->createToken();
				$this->currentToken = $token;
				
				if($remember_me == 'checked') {
					setcookie( 'LOGIN_TOKEN', $token, strtotime( '+30 days' ));
					setcookie( 'LOGIN_USER', $result['user'], strtotime( '+30 days' ));
					$_COOKIE['LOGIN_TOKEN'] = $token;
					$_COOKIE['LOGIN_USER'] = $result['user'];
				} else {
					$_SESSION['LOGIN_TOKEN'] = $token;
					$_SESSION['LOGIN_USER'] = $result['user'];
				}
				
				lg('logged in');
				$return = true;
			} else {
				lg('login failed');
			}
		} catch(PDOException $e) {
			lg('Exception : '.$e->getMessage());
		}
		return $return;
	}
	
	public function jsonLogin($username, $password) {
		$return = false;
		$username = trim($username);
		
		try {
			$sth = $this->db->prepare("SELECT * FROM USERS WHERE LOWER(user) = LOWER(:user)");
			$sth->bindParam(':user', $username);
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if(password_verify($password, $result['password'])) {
				$this->username = $result['user'];
				$this->logged = true;
				$token = $this->createToken();
				$this->currentToken = $token;
				
				$return = $token;
			}
		} catch(PDOException $e) {
			die('Exception : '.$e->getMessage());
		}
		return $return;
	}
	
	public function loginViaToken($token, $user) {
		$return = false;
		
		try {
			$sth = $this->db->prepare("SELECT * FROM LOGIN_TOKENS WHERE token = :token AND user = :user AND date >= DATE_SUB(CURRENT_TIMESTAMP(), ".$this->tokenInterval.")");
			$sth->bindParam(':token', $token);//DATE_SUB(NOW(), INTERVAL 30 DAY)
			$sth->bindParam(':user', $user);
			$sth->execute();
			if($result = $sth->fetch(PDO::FETCH_ASSOC)) {
				$username = $result['user'];
				$this->username = $result['user'];
				$this->logged = true;
				
				$this->updateToken($token);
				
				lg('cookie logged in');
				$this->currentToken = $token;
				$return = true;
			} else {
				lg('cookie login failed');
			}
		} catch(PDOException $e) {
			lg('Exception : '.$e->getMessage());
		}
		
		return $return;
	}
	
	// reset expire date
	private function updateToken($token) {
		if(!empty($token)) {
			$sth = $this->db->prepare('UPDATE LOGIN_TOKENS SET date = CURRENT_TIMESTAMP() WHERE token = :token;');
			$sth->bindParam(':token', $token);
			$sth->execute();
		}
	}
	
	// status can be 'ok', 'suspended', 'resetting password'
	public function register($username, $password, $email, $recaptchaResponse, $status = 'ok') {
		$return = false;
		$password = password_hash($password, PASSWORD_DEFAULT);
		$username = trim($username);
		if($email != NULL)
			$email = $this->symmetric_openssl_encrypt(trim($email), OPENSSL_KEY);
		
		if(!empty($username) && strtolower($username) != 'anonym') {
			try {
				$sth = $this->db->prepare("SELECT COUNT(*) as num_rows FROM USERS WHERE LOWER(user) = LOWER(:username)");
				$sth->bindParam(':username', $username);
				$sth->execute();
				$result = $sth->fetch(PDO::FETCH_ASSOC);
				if($result['num_rows'] == 0) {
					$response = $_POST['g-recaptcha-response'];
					$url = 'https://www.google.com/recaptcha/api/siteverify';
					$postdata = http_build_query(
					    array(
					        'secret' => RECAPTCHA_PRIVATE_KEY,
							'response' => $recaptchaResponse,
							'remoteip' => $_SERVER['REMOTE_ADDR']
					    )
					);
					
					$opts = array('http' =>
					    array(
					        'method'  => 'POST',
					        'header'  => 'Content-type: application/x-www-form-urlencoded',
					        'content' => $postdata
					    )
					);
					
					$context  = stream_context_create($opts);
					$result = file_get_contents($url, false, $context);
					$responseData = json_decode($result);
					
					if($responseData != NULL && $responseData->success === true) {
						$sth = $this->db->prepare("INSERT INTO USERS (user, password, email, status) VALUES (:user, :password, :email, :status)");
						$sth->bindParam(':user', $username);
						$sth->bindParam(':password', $password);
						$sth->bindParam(':email', $email);
						$sth->bindParam(':status', $status);
						$sth->execute();
						
						$this->username = $username;
						$this->logged = true;
						
						$token = $this->createToken();
						$this->currentToken = $token;
						setcookie( 'LOGIN_TOKEN', $token, strtotime( '+30 days' ));
						setcookie( 'LOGIN_USER', $username, strtotime( '+30 days' ));
						$_COOKIE['LOGIN_TOKEN'] = $token;
						$_COOKIE['LOGIN_USER'] = $username;
						
						lg('register successfull');
						$this->logged = true;
						$return = true;
					} else {
						lg('reCaptcha failed');
						$return = 'reCaptcha failed';
					}
				} else {
					lg('registering failed');
				}
			} catch(PDOException $e) {
				lg('Exception : '.$e->getMessage());
			}
		}
		return $return;
	}
	
	public function logout($token, $user) {
		$this->logged = false;
		$this->username = 'anonym';
		$this->currentToken = '';
		unset($_COOKIE['LOGIN_TOKEN']);
	    setcookie('LOGIN_TOKEN', '', time() - 3600);
		unset($_COOKIE['LOGIN_USER']);
	    setcookie('LOGIN_USER', '', time() - 3600);
	    unset($_SESSION['LOGIN_TOKEN']);
	    unset($_SESSION['LOGIN_USER']);
		
		if(!empty($token)) {
			try {
				// delete current token + all expired tokens
				$sth = $this->db->prepare("DELETE FROM LOGIN_TOKENS WHERE token = :token AND user = :user LIMIT 1; DELETE FROM LOGIN_TOKENS WHERE date < DATE_SUB(CURRENT_TIMESTAMP(), ".$this->tokenInterval.");");
				$sth->bindParam(':token', $token);
				$sth->bindParam(':user', $user);
				$sth->execute();
				lg('Token deleted');
			} catch(PDOException $e) {
				lg('Exception : '.$e->getMessage());
			}
		}
	}
	
	private function createToken($length = 100) {
		$token = generateRandomString($length);
		
		try {
			$sth = $this->db->prepare("SELECT COUNT(*) as num_rows FROM LOGIN_TOKENS WHERE token = :token");
			$sth->bindParam(':token', $token);
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result['num_rows'] == 0) {
				$sth = $this->db->prepare("INSERT INTO LOGIN_TOKENS (user, token)
					VALUES (:user, :token)");
				$sth->bindParam(':user', $this->username);
				$sth->bindParam(':token', $token);
				$sth->execute();
				lg('create token successfull');
			} else {
				lg('create token failed');
				return $this->createToken($length);
			}
		} catch(PDOException $e) {
			lg('Exception : '.$e->getMessage());
		}
		
		return $token;
	}

	// thanks to https://bhoover.com/using-php-openssl_encrypt-openssl_decrypt-encrypt-decrypt-data/
	public function symmetric_openssl_encrypt($data, $key) {
	    // Remove the base64 encoding from our key
	    $encryption_key = base64_decode($key);
	    // Generate an initialization vector
	    $iv = openssl_random_pseudo_bytes(openssl_cipher_iv_length('aes-256-cbc'));
	    // Encrypt the data using AES 256 encryption in CBC mode using our encryption key and initialization vector.
	    $encrypted = openssl_encrypt($data, 'aes-256-cbc', $encryption_key, 0, $iv);
	    // The $iv is just as important as the key for decrypting, so save it with our encrypted data using a unique separator (::)
	    return base64_encode($encrypted . '::' . $iv);
	}
	 
	public function symmetric_openssl_decrypt($data, $key) {
	    // Remove the base64 encoding from our key
	    $encryption_key = base64_decode($key);
	    // To decrypt, split the encrypted data from our IV - our unique separator used was "::"
	    list($encrypted_data, $iv) = explode('::', base64_decode($data), 2);
	    return openssl_decrypt($encrypted_data, 'aes-256-cbc', $encryption_key, 0, $iv);
	}
	
	// to test the database
	public function showAllUsers() {
		$sth = $this->db->prepare("SELECT * FROM USERS");
		$sth->execute();
		while ($row = $sth->fetch(PDO::FETCH_ASSOC)) {
			lg(implode("\t ", $row));
			lg($row['email'] . "\t=>\t" .$this->symmetric_openssl_decrypt($row['email'], OPENSSL_KEY));
		}
	}
}





















