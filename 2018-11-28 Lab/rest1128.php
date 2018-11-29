<?php

$httpBody = file_get_contents('php://input');

require_once('database-credentials.php');
$database = connectToDatabase('cse383');

/**
 * @param string $json
 * @param string $mediaType
 * @return mixed
 */
function decodeJson($json, $mediaType)
{
    if ($mediaType !== 'application/json') {
        http_response_code(406);
        return null;
    }

    $obj = json_decode($json);
    if ($obj == null) {
        http_response_code(400);
        return null;
    }
    return $obj;
}

/**
 * @param mysqli_result $mysqliResult
 * @return array
 */
function encodeSqlResult($mysqliResult)
{
    // TODO: Fix
    var_dump($mysqliResult->fetch_all(MYSQLI_ASSOC));
    return array();
}

/**
 * @return void
 */
function getKeys()
{
    global $database;
    $query = $database->query('select keyName from KeyValue');
    if ($query) {
        encodeSqlResult($query);
    } else {
        // TODO: Send error
    }
}

/**
 * @param string $httpBody
 * @return void
 */
function getValue($httpBody)
{
    $key = $httpBody;

    global $database;
    $query = $database->prepare('select value from KeyValue where keyName = ?');
    $query->bind_param('s', $key);
    $query->execute();
    $query = $query->get_result();
    if ($query) {
        encodeSqlResult($query);
    } else {
        // TODO: Send error
    }
}

/**
 * @param string $httpBody
 * @return void
 */
function addKeyValue($httpBody)
{
//    $key, $value
}

/**
 * @param string $path
 * @param string $method
 * @param string $httpBody
 * @return void
 */
function processRestApi($path, $method, $httpBody)
{
    $pathParts = explode('/', $path);
    $pathDepth = count($pathParts);

    $method = strtoupper($method);

    if ($pathDepth >= 1) {
        switch ($pathParts[0]) {
            // /v1
            case 'v1':
                if ($pathDepth >= 2) {
                    switch ($pathParts[1]) {
                        // /v1/keys
                        case 'keys':
                            if ($pathDepth === 2) {
                                // TODO: Add part 1 and 3
                                switch ($method) {
                                    case 'GET':
                                        getKeys();
                                        break;

                                    case 'POST':
                                        addKeyValue();
                                        break;

                                    default:
                                        // TODO: Send error
                                        break;
                                }
                            } else if ($pathDepth === 3) {
                                getValue($pathParts[2]);
                            } else {
                                http_response_code(404);
                            }
                            break;

                        default:
                            http_response_code(404);
                            break;
                    }
                }
                break;

            default:
                http_response_code(501);
                break;
        }
    }

}

//isset($_SERVER['CONTENT_TYPE']);

processRestApi($_SERVER['PATH_INFO'], $_SERVER['REQUEST_METHOD'], $httpBody);
