<?php

$sets = new sets($db_mysql, $user);

// new set
if(!empty($_POST['setname']) && !empty($_POST['chapter']) && $_POST['chapter'] >= 1 && $user->islogged()) {
	
	$sets->newSet($_POST['setname'], $_POST['chapter'], $user->getUsername(), NULL);
	
// delete card
} elseif($page == 'edit_set' && ($_GET['subpage'] == 'delete' && !empty($_GET['id'])
	&& (!empty($_GET['fronttext']) || !empty($_GET['frontimageurl'])) ) && ( !empty($_GET['backtext']) || !empty($_GET['backimageurl'])) ){
	 	
	$success = $sets->deleteCard($_GET['id'], $_GET['fronttext'],
		$_GET['frontimageurl'], $_GET['backtext'], $_GET['backimageurl']);
	header('Location: '.ROOT.'?page=edit_set&id='.$_GET['id'].'&delete_success='.$success);
	exit();
		
// add card
} elseif($page == 'edit_set' && $_GET['subpage'] == 'add' && !empty($_GET['id']) && (!empty($_POST['fronttext']) || !empty($_POST['frontimageurl'])) 
	&& (!empty($_POST['backtext']) || !empty($_POST['backimageurl'])) ) {
		
	$sets->addCard($_GET['id'], $_POST['fronttext'], $_POST['frontimageurl'], $_POST['backtext'], $_POST['backimageurl']);
	header('Location: '.ROOT.'?page=edit_set&id='.$_GET['id'].'&add_success='.SUCCESS);
	exit();
} elseif($page == 'edit_set' && $_GET['subpage'] == 'add' && !empty($_GET['id'])) {
	
	$_GET['add_success'] = FAILURE;

// edit card
} elseif($page == 'edit_set' && $_GET['subpage'] == 'edit' && !empty($_GET['id'])
	&& (!empty($_GET['fronttext']) || !empty($_GET['frontimageurl'])) && ( !empty($_GET['backtext']) || !empty($_GET['backimageurl']))
	&& (!empty($_POST['newfronttext']) || !empty($_POST['newfrontimageurl'])) && ( !empty($_POST['newbacktext']) || !empty($_POST['newbackimageurl'])) ) {
	
	$success = $sets->editCard($_GET['id'], $_GET['fronttext'], $_GET['frontimageurl'], $_GET['backtext'], $_GET['backimageurl'],
		$_POST['newfronttext'], $_POST['newfrontimageurl'], $_POST['newbacktext'], $_POST['newbackimageurl']);
	header('Location: '.ROOT.'?page=edit_set&id='.$_GET['id'].'&edit_success='.$success);
	exit();
} elseif($page == 'edit_set' && $_GET['subpage'] == 'edit' && !empty($_GET['id'])
	&& (isset($_POST['newfronttext']) || isset($_POST['newfrontimageurl']) || isset($_POST['newbacktext']) || isset($_POST['newbackimageurl'])) ) {
	header('Location: '.ROOT.'?page=edit_set&id='.$_GET['id'].'&edit_success='.FAILURE);
	exit();

// copy/share set	
} elseif($page == 'my_sets' && $_GET['subpage'] == 'share' &&  !empty($_GET['id'])) {
	$success = $sets->copySet($_GET['id']);
		header('Location: '.ROOT.'?page=my_sets&share_success='.$success);
		exit();

// learn set
} elseif($page == 'learn_set') {
	if($sets->getSetFromId($_GET['id']) != array()) {
		
	} else {
		header('Location: '.ROOT.'?404');
		exit();
	}
	
// delete set
} elseif($page == 'my_sets' && !empty($_POST['extras_action']) && $_POST['extras_action'] == 'delete_allcards' && $_POST['iamaware_check'] == 'on'
	&& !empty($_POST['id'])) {
	echo $sets->deleteAllCards($_POST['id']);
} elseif($page == 'my_sets' && !empty($_POST['extras_action']) && $_POST['extras_action'] == 'delete_set' && $_POST['iamaware_check'] == 'on'
	&& !empty($_POST['id'])) {
	echo $sets->deleteSet($_POST['id']);
}

class sets {
	private $db, $user;
	
	function __construct($db_mysql, $user) {
		$this->db = $db_mysql;
		$this->user = $user;
	}

	public function getNewId() {
		do{
			$id = generateRandomString(8);
			$doAgain = true;
			$sth = $this->db->prepare("SELECT COUNT(*) as num_rows FROM SETS WHERE id = :id");
			$sth->bindParam(':id', $id);
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result['num_rows'] == 0)
				$doAgain = false;
		} while($doAgain);
		
		return $id;
	}

	public function newSet($name, $chapter, $user, $descreption = NULL) {
		$name = trim($name);
		$id = $this->getNewId();
		
		if(!empty($name)) {
			try {
				$sth = $this->db->prepare("SELECT COUNT(*) as anzahl FROM SETS WHERE
					LOWER(name) = LOWER(:name) AND chapter = :chapter AND user = :user");
				$sth->bindParam(':name', $name);
				$sth->bindParam(':chapter', $chapter);
				$sth->bindParam(':user', $user);
				$sth->execute();
				$result = $sth->fetch(PDO::FETCH_ASSOC);
				if($result['anzahl'] == 0) {
					$sth = $this->db->prepare("INSERT INTO SETS (id, name, chapter, user, descreption)
						VALUES (:id, :name, :chapter, :user, :descreption);");
					$sth->bindParam(':id', $id);
					$sth->bindParam(':name', $name);
					$sth->bindParam(':chapter', $chapter);
					$sth->bindParam(':user', $user);
					$sth->bindParam(':descreption', $descreption);
					$sth->execute();
					
					// $db = new PDO('sqlite:./sets/'.$filename.'.db');
					
					lg('set successfull created');
					$_GET['created_set'] = $name.' #'.$chapter;
					
					return true;
				} else {
					lg('set name is already taken');
				}
			} catch(PDOException $e) {
				lg('Exception : '.$e->getMessage());
			}
		}
		return false;
	}
	
	public function popularSets() {
		$return = array( 
			array(
				'id' => 123487,
				'name' => 'lalaz',
				'chapter' => '#1',
				'user' => 'anonym',
				'cards' => 100,
			),
			array(
				'id' => 2,
				'name' => 'Android macht Spaß',
				'chapter' => 'ist halt echt so #1',
				'user' => 'Yannick',
				'cards' => 200,
			)
		);
		return $return;
	}
	
	public function getSetsFromUser($user) {
		$result = array();
		
		$sth = $this->db->prepare("SELECT *, (SELECT COUNT(*) FROM CARDS WHERE CARDS.user = SETS.user AND CARDS.setname = SETS.name AND  CARDS.chapter = SETS.chapter) as cards 
									FROM SETS WHERE user = :user ORDER BY name, chapter");
		$sth->bindParam(':user', $user);
		
		if($sth->execute()) {
		    while ($row = $sth->fetch(PDO::FETCH_ASSOC)) {
        		$result[] = $row;
    		}
		}
		return $result;
	}
	
	public function getSetFromId($id) {
		$result = array();
		
		$sth = $this->db->prepare("SELECT *, (SELECT COUNT(*) FROM CARDS WHERE CARDS.user = SETS.user AND CARDS.setname = SETS.name AND  CARDS.chapter = SETS.chapter) as cards 
									FROM SETS WHERE id = :id LIMIT 1");
		$sth->bindParam(':id', $id);
		
		if($sth->execute()) {
		    while ($row = $sth->fetch(PDO::FETCH_ASSOC)) {
        		$result = $row;
    		}
		}
		return $result;
	}
	
	public function getChaptersFromSet($id) {
		$result = array();
		$sth = $this->db->prepare("SELECT s2.id, s2.name,  s2.chapter, s2.user,
			(SELECT COUNT(*) FROM CARDS WHERE CARDS.user = s2.user AND CARDS.setname = s2.name AND  CARDS.chapter = s2.chapter) as cards
			FROM SETS s1
			JOIN SETS s2 ON s1.user = s2.user AND s1.name = s2.name
			WHERE s1.id = :id
			ORDER BY chapter");
		$sth->bindParam(':id', $id);
		if($sth->execute()) {
		    while ($row = $sth->fetch(PDO::FETCH_ASSOC)) {
        		$result[] = $row;
    		}
		}
		return $result;
	}
	
	public function getCards($id) {
		$result = array();
		
		$sth = $this->db->prepare("SELECT * FROM CARDS
			JOIN SETS ON CARDS.user = SETS.user AND CARDS.setname = SETS.name AND CARDS.chapter = SETS.chapter
			WHERE SETS.id = :id");
		$sth->bindParam(':id', $id);
		
		if($sth->execute()) {
		    while ($row = $sth->fetch(PDO::FETCH_ASSOC)) {
        		$result[] = $row;
    		}
		}
		
		
		return $result;
	}
	
	public function addCard($id, $fronttext, $frontimageurl, $backtext, $backimageurl) {
		$set = $this->db->prepare("SELECT * FROM SETS WHERE id = :id");
		$set->bindParam(':id', $id);
		$set->execute();
		$set = $set->fetch(PDO::FETCH_ASSOC);
		if($this->user->check($set['user'])) {
			
			$card = $this->db->prepare("INSERT INTO `CARDS`
				(`user`, `setname`, `chapter`, `fronttext`, `backtext`, `frontimageurl`,
				`backimageurl`, `timetolearn`) VALUES
				(:user, :setname, :chapter, :fronttext, :backtext, :frontimageurl, 
				:backimageurl, CURRENT_TIMESTAMP);");
			$card->bindParam(':user', $set['user']);
			$card->bindParam(':setname', $set['name']);
			$card->bindParam(':chapter', $set['chapter']);
			$card->bindParam(':fronttext', $fronttext);
			$card->bindParam(':backtext', $backtext);
			$card->bindParam(':frontimageurl', $frontimageurl);
			$card->bindParam(':backimageurl', $backimageurl);
			
			if($card->execute()) {
				$_GET['card_added'] = true;
			}
		}
	}
	
	public function deleteCard($id, $fronttext, $frontimageurl, $backtext, $backimageurl) {
		
		$set = $this->db->prepare("SELECT * FROM SETS WHERE id = :id");
		$set->bindParam(':id', $id);
		$set->execute();
		$set = $set->fetch(PDO::FETCH_ASSOC);
		
		if($this->user->check($set['user'])) {
			$card = $this->db->prepare("DELETE FROM CARDS
				WHERE	
					user = :user AND setname = :setname AND chapter = :chapter AND
					fronttext = :fronttext AND backtext = :backtext AND
					frontimageurl = :frontimageurl AND backimageurl = :backimageurl
				LIMIT 1;");
			$card->bindParam(':user', $set['user']);
			$card->bindParam(':setname', $set['name']);
			$card->bindParam(':chapter', $set['chapter']);
			$card->bindParam(':fronttext', $fronttext);
			$card->bindParam(':backtext', $backtext);
			$card->bindParam(':frontimageurl', $frontimageurl);
			$card->bindParam(':backimageurl', $backimageurl);
			$card->execute();
			if($card->rowCount() > 0)
				return SUCCESS;
		}
		return FAILURE;
	}
	
	public function editCard($id, $fronttext, $frontimageurl, $backtext, $backimageurl, $newfronttext, $newfrontimageurl, $newbacktext, $newbackimageurl) {
		
		$set = $this->db->prepare("SELECT * FROM SETS WHERE id = :id");
		$set->bindParam(':id', $id);
		$set->execute();
		$set = $set->fetch(PDO::FETCH_ASSOC);
		if($this->user->check($set['user'])) {
			$card = $this->db->prepare("UPDATE CARDS
				SET 
					fronttext = :newfronttext, frontimageurl = :newfrontimageurl, backtext = :newbacktext, backimageurl = :newbackimageurl
				WHERE	
					user = :user AND setname = :setname AND chapter = :chapter AND
					fronttext = :fronttext AND backtext = :backtext AND
					frontimageurl = :frontimageurl AND backimageurl = :backimageurl
				LIMIT 1;");
			$card->bindParam(':user', $set['user']);
			$card->bindParam(':setname', $set['name']);
			$card->bindParam(':chapter', $set['chapter']);
			$card->bindParam(':fronttext', $fronttext);
			$card->bindParam(':backtext', $backtext);
			$card->bindParam(':frontimageurl', $frontimageurl);
			$card->bindParam(':backimageurl', $backimageurl);
			$card->bindParam(':newfronttext', $newfronttext);
			$card->bindParam(':newbacktext', $newbacktext);
			$card->bindParam(':newfrontimageurl', $newfrontimageurl);
			$card->bindParam(':newbackimageurl', $newbackimageurl);
			$card->execute();
			if($card->rowCount() > 0)
				return SUCCESS;
		}
		return FAILURE;
		
	}
	
	public function copySet($id) {
		if($this->user->islogged()) {
			$set = $this->db->prepare("SELECT * FROM SETS WHERE id = :id");
			$set->bindParam(':id', $id);
			$set->execute();
			if($set->rowCount() > 0) {
				$set = $set->fetch(PDO::FETCH_ASSOC);
				$username = $this->user->getUsername();
				
				$checkIfAlreadyExists = $this->db->prepare("SELECT COUNT(*) as anzahl FROM SETS WHERE LOWER(name) = LOWER(:name) AND chapter = :chapter AND user = :user");
				$checkIfAlreadyExists->bindParam(':user', $username);
				$checkIfAlreadyExists->bindParam(':name', $set['name']);
				$checkIfAlreadyExists->bindParam(':chapter', $set['chapter']);
				$checkIfAlreadyExists->execute();
				$checkIfAlreadyExists = $checkIfAlreadyExists->fetch(PDO::FETCH_ASSOC);
				if(!$this->user->check($set['user']) && $checkIfAlreadyExists['anzahl'] == 0) {
					$id = $this->getNewId();
					
					$copySet = $this->db->prepare("INSERT INTO SETS (id, name, chapter, user, descreption)
						VALUES (:id, :name, :chapter, :user, :descreption);");
					$copySet->bindParam(':id', $id);
					$copySet->bindParam(':name', $set['name']);
					$copySet->bindParam(':chapter', $set['chapter']);
					$copySet->bindParam(':user', $username);
					$copySet->bindParam(':descreption', $set['descreption']);
					$copySet->execute();
					
					// (`user`, `setname`, `chapter`, `fronttext`, `backtext`, `frontimageurl`, `backimageurl`, `timetolearn`)
					$copyCards = $this->db->prepare("INSERT INTO `CARDS` (user, setname, chapter, fronttext, backtext, frontimageurl, backimageurl, timetolearn)
														SELECT :user, :setname, :chapter, fronttext, backtext, frontimageurl, backimageurl, CURRENT_TIMESTAMP
															FROM CARDS WHERE user = :olduser AND setname = :setname AND chapter = :chapter");
					$copyCards->bindParam(':user', $username);
					$copyCards->bindParam(':olduser', $set['user']);
					$copyCards->bindParam(':setname', $set['name']);
					$copyCards->bindParam(':chapter', $set['chapter']);
					$copyCards->execute();
					
					return SUCCESS;
				}
			}
		}
		return FAILURE;
	}
	
	public function qrCodeGetSetAndCards($id, $user) {
		$json = array();
		$set = $this->db->prepare("SELECT *, (SELECT COUNT(*) FROM CARDS WHERE CARDS.user = SETS.user AND CARDS.setname = SETS.name AND  CARDS.chapter = SETS.chapter) as cards 
							FROM SETS WHERE id = :id LIMIT 1");
		$set->bindParam(':id', $id);
		$set->execute();
		if($set->rowCount() > 0) {
			$set = $set->fetch(PDO::FETCH_ASSOC);
			$cards = $this->getCards($id);
			$json['setname'] = $set['name'];
			$json['chapter'] = $set['chapter'];
			$json['description'] = $set['description'] ?? '';
			$json['created_on'] = strtotime('now').'';
			if($user == $set['user'] or true)
				$json['created_on'] = strtotime($set['created_on']).'';
			$json['cards'] = $set['cards'];
			$i = 0;
			foreach ($cards as $card) {
			    $json[$i.'fronttext'] = $card['fronttext'];
			    $json[$i.'frontimageurl'] = $card['frontimageurl'];
			    $json[$i.'backtext'] = $card['backtext'];
			    $json[$i.'backimageurl'] = $card['backimageurl'];
			    $json[$i.'timetolearn'] = strtotime($card['timetolearn']).'';
			    $i++;
			}
			
		}
		return $json;
	}
	
	public function getNextCardToLearn($id) {
		$result = FAILURE;
		$sth = $this->db->prepare("SELECT * FROM CARDS
			JOIN SETS ON CARDS.user = SETS.user AND CARDS.setname = SETS.name AND CARDS.chapter = SETS.chapter
			WHERE SETS.id = :id AND CARDS.timetolearn <= CURRENT_TIMESTAMP ORDER BY RAND() LIMIT 1");
		$sth->bindParam(':id', $id);
		
		if($sth->execute()) {
		    $row = $sth->fetch(PDO::FETCH_ASSOC);
        		$result = $row;
		}
		return $result;
	}
	
	public function deleteAllCards($id) {
		$result = FAILURE;
		$sth = $this->db->prepare("DELETE c
			FROM CARDS c
			INNER JOIN SETS s
			  ON s.name = c.setname AND s.chapter = c.chapter AND s.user = c.user
			WHERE s.id = :id;");
		$sth->bindParam(':id', $id);
		
		if($sth->execute())
        	$result = SUCCESS;
		return $result;
	}
	
	public function deleteSet($id) {
		$result = FAILURE;
		$this->deleteAllCards($id);
		$sth = $this->db->prepare("DELETE FROM SETS WHERE id = :id LIMIT 1;");
		$sth->bindParam(':id', $id);
		
		if($sth->execute())
        	$result = SUCCESS;
		return $result;
	}
}
























