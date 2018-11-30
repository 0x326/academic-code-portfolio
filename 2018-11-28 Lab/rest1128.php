<?php

$httpBody = file_get_contents('php://input');

require_once('database-credentials.php');
$database = connectToDatabase('cse383');
$database->set_charset('utf8');

/**
 * @return void
 */
function getKeys()
{
    global $database;
    $query = $database->query('select keyName from KeyValue;');
    if ($query) {
        $keys = array_map(function ($elem) {
            return $elem['keyName'];
        }, $query->fetch_all(MYSQLI_ASSOC));

        http_response_code(200);
        header('Content-Type: application/json');
        echo json_encode(array(
            'status' => 'OK',
            'keys' => $keys,
        ));
    } else {
        http_response_code(500);
        header('Content-Type: application/json');
        echo json_encode(array(
            'status' => 'FAIL',
            'keys' => array(),
        ));
    }
}

/**
 * @param string key
 * @return void
 */
function getValue($key)
{
    $key = urldecode($key);

    global $database;
    $query = $database->prepare('select KeyValue.value from KeyValue where keyName = ?;');
    $query->bind_param('s', $key);
    $query->execute();
    $query = $query->get_result();
    if ($query) {
        $value = $query->fetch_assoc()['value'];

        if ($value !== null) {
            http_response_code(200);
            header('Content-Type: application/json');
            echo json_encode(array(
                'status' => 'OK',
                'value' => $value,
            ));
        } else {
            http_response_code(404);
            header('Content-Type: application/json');
            echo json_encode(array(
                'status' => 'FAIL',
                'value' => '',
            ));
        }
    } else {
        http_response_code(500);
        header('Content-Type: application/json');
        echo json_encode(array(
            'status' => 'FAIL',
            'value' => '',
        ));
    }
}

/**
 * @param string $httpBody
 * @param string $mediaType
 * @return void
 */
function addKeyValue($httpBody, $mediaType)
{
    $mediaType = strtolower($mediaType);
    if ($mediaType !== 'application/json') {
        http_response_code(415);
    } else {
        $entry = json_decode($httpBody);

        try {
            if ($entry === null) {
                throw new InvalidArgumentException();
            }

            if (!isset($entry['keyName']) || !isset($entry['value'])) {
                throw new InvalidArgumentException();
            }

            $key = $entry['keyName'];
            $value = $entry['value'];

            if (gettype($key) !== 'string' || gettype($value) !== 'string') {
                throw new InvalidArgumentException();
            }

            global $database;
            $query = $database->prepare('insert into KeyValue (keyName, value) values (?, ?);');
            $query->bind_param('ss', $key, $value);
            $query->execute();
            $query = $query->get_result();

            if ($query) {
                http_response_code(200);
                header('Content-Type: application/json');
                echo json_encode(array(
                    'status' => 'OK',
                ));
            } else {
                http_response_code(500);
                header('Content-Type: application/json');
                echo json_encode(array(
                    'status' => 'FAIL',
                ));
            }
        } catch (InvalidArgumentException $error) {
            http_response_code(400);
            header('Content-Type: application/json');
            echo json_encode(array(
                'status' => 'FAIL',
            ));
        }
    }
}

/**
 * @param string $path
 * @param string $method
 * @param string $httpBody
 * @param string $mediaType
 * @return void
 */
function processRestApi($path, $method, $httpBody, $mediaType)
{
    $path = trim($path, '/');
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
                                switch ($method) {
                                    case 'GET':
                                        getKeys();
                                        break;

                                    case 'POST':
                                        addKeyValue($httpBody, $mediaType);
                                        break;

                                    default:
                                        http_response_code(405);
                                        break;
                                }
                            } else if ($pathDepth === 3) {
                                switch ($method) {
                                    case 'GET':
                                        getValue($pathParts[2]);
                                        break;

                                    default:
                                        http_response_code(405);
                                        break;
                                }
                            } else {
                                http_response_code(404);
                            }
                            break;

                        default:
                            http_response_code(404);
                            break;
                    }
                } else {
                    http_response_code(404);
                }
                break;

            case '':
                http_response_code(404);
                break;

            default:
                http_response_code(501);
                break;
        }
    }

}

if ($database->connect_errno) {
    http_response_code(500);
} else {
    processRestApi($_SERVER['PATH_INFO'] ?? '', $_SERVER['REQUEST_METHOD'], $httpBody, $_SERVER['CONTENT_TYPE'] ?? '');
}
