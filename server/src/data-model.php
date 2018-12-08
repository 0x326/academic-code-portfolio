<?php

const tokenByteLength = 16;

class AuthenticationException extends Exception
{

}

class DatabaseException extends Exception
{

}

/**
 * @param string $token
 * @param string $username
 * @throws DatabaseException
 */
function authorizeToken($token, $username)
{
    global $database;

    $query = $database->prepare('
        insert into tokens(user, token)
        values (?, ?);');
    $query->bind_param('ss', $username, $token);
    $query->execute();
    $query->get_result();

    if ($database->errno) {
        throw new DatabaseException();
    }
}

/**
 * @param string $token
 * @return string
 * @throws AuthenticationException
 * @throws DatabaseException
 */
function validateToken($token)
{
    global $database;

    $query = $database->prepare('
        select user as username
        from tokens
        where token = ?;');
    $query->bind_param('s', $token);
    $query->execute();
    $query = $query->get_result();
    if ($query) {
        // Assume there is only one username for a given token (although database schema does not guarantee this)
        $authenticatedUsername = $query->fetch_assoc()['username'] ?? null;
        if ($authenticatedUsername === null) {
            throw new AuthenticationException();
        }
        return $authenticatedUsername;
    } else {
        throw new DatabaseException();
    }
}

/**
 * @param string $username
 * @param string $password
 * @return string
 * @throws DatabaseException
 * @throws AuthenticationException
 * @throws Exception
 */
function getToken($username, $password)
{
    $authenticatedUsername = null;

    global $database;
    $query = $database->prepare('
        select user as username, password as passwordHash
        from users
        where user = ?;');
    $query->bind_param('s', $username);
    $query->execute();
    $query = $query->get_result();
    if ($query) {
        foreach ($query->fetch_all(MYSQLI_ASSOC) as $rowNumber => $row) {
            if (password_verify($password, $row['passwordHash']) === true) {
                $authenticatedUsername = $row['username'];
                break;
            }
        }

        if ($authenticatedUsername !== null) {
            // Generate token
            $token = bin2hex(random_bytes(tokenByteLength));

            authorizeToken($token, $authenticatedUsername);
            return $token;
        } else {
            throw new AuthenticationException();
        }

    } else {
        throw new DatabaseException();
    }
}

/**
 * @return array
 * @throws DatabaseException
 */
function getItems()
{
    global $database;
    $query = $database->query('
        select pk, item
        from diaryItems;');
    if ($query) {
        return $query->fetch_all(MYSQLI_ASSOC);
    } else {
        throw new DatabaseException();
    }
}

/**
 * @param string $token
 * @return array
 * @throws AuthenticationException
 * @throws DatabaseException
 */
function getConsumedItems($token)
{
    $authenticatedUsername = validateToken($token);
    global $database;

    // Not including diary.pk because professor's implementation doesn't
    $query = $database->prepare('
        select item, diary.timestamp
        from diary, diaryItems, users
        where diary.userFK = users.pk
          and diary.itemFK = diaryItems.pk
          and users.user = ?
        order by diary.timestamp desc;');
    $query->bind_param('s', $authenticatedUsername);
    $query->execute();
    $query = $query->get_result();

    if ($query) {
        return $query->fetch_all(MYSQLI_ASSOC);
    } else {
        throw new DatabaseException();
    }
}

/**
 * @param string $token
 * @return array
 * @throws AuthenticationException
 * @throws DatabaseException
 */
function computeItemSummary($token)
{
    $authenticatedUsername = validateToken($token);
    global $database;

    // Not including diary.pk because professor's implementation doesn't
    $query = $database->prepare('
        select item, count(*) as count
        from diary, diaryItems, users
        where diary.userFK = users.pk
          and diary.itemFK = diaryItems.pk
          and users.user = ?
        group by item
        order by diary.timestamp desc;');
    $query->bind_param('s', $authenticatedUsername);
    $query->execute();
    $query = $query->get_result();

    if ($query) {
        return $query->fetch_all(MYSQLI_ASSOC);
    } else {
        throw new DatabaseException();
    }
}

/**
 * @param string $token
 * @param string $itemKey
 * @return void
 * @throws AuthenticationException
 * @throws DatabaseException
 */
function updateItem($token, $itemKey)
{
    $authenticatedUsername = validateToken($token);
    global $database;

    $query = $database->prepare('
        insert into diary(userFK, itemFK)
        select users.pk, diaryItems.pk 
        from diaryItems, users
        where diaryItems.pk = ? 
          and users.user = ?;');
    $query->bind_param('ss', $itemKey, $authenticatedUsername);
    $query->execute();
    $query = $query->get_result();

    if (!$query) {
        throw new DatabaseException();
    }
}
