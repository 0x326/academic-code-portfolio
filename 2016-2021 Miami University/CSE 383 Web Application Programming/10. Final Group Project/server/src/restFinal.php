<?php

$httpBody = file_get_contents('php://input');

require_once('database-credentials.php');
$database = connectToDatabase('cse383');
$database->set_charset('utf8');

require_once('data-model.php');

class MalformedRequestObjectException extends Exception
{

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
    $mediaType = strtolower($mediaType);

    if ($pathDepth >= 1) {
        switch ($pathParts[0]) {
            case 'v1':
                processRestApiV1(array_slice($pathParts,1), $method, $httpBody, $mediaType);
                break;

            case '':
                http_response_code(404);
                break;

            default:
                // http_response_code(501);
                processRestApiV1($pathParts, $method, $httpBody, $mediaType);
                break;
        }
    }
}

/**
 * @param array $pathParts
 * @param string $method
 * @param string $httpBody
 * @param string $mediaType
 */
function processRestApiV1($pathParts, $method, $httpBody, $mediaType)
{
    $pathDepth = count($pathParts);

    switch ($mediaType) {
        case 'application/json':
        default:
            // Default to json encoding
            $requestObject = json_decode($httpBody);
            break;
    }

    // Object to send back to client
    $responseObject = null;

    if ($pathDepth >= 1) {
        switch ($pathParts[0]) {
            case 'user':
                switch ($method) {
                    case 'POST':
                        try {
                            $username = (string) $requestObject->user;
                            $password = (string) $requestObject->password;

                            if ($username === '' || $password === '') {
                                throw new MalformedRequestObjectException();
                            }

                            http_response_code(200);
                            $token = getToken($username, $password);
                            $responseObject = [
                                'status' => 'OK',
                                'msg' => '',
                                'token' => $token,
                            ];
                        } catch (AuthenticationException $e) {
                            // Probably should be HTTP 401, but it requires a WWW-Authenticate header
                            http_response_code(403);
                            $responseObject = [
                                'status' => 'AUTH_FAIL',
                                'msg' => 'Invalid credentials',
                                'items' => [],
                            ];
                        } catch (DatabaseException $e) {
                            http_response_code(500);
                            $responseObject = [
                                'status' => 'FAIL',
                                'msg' => 'Database error',
                                'items' => [],
                            ];
                        } catch (MalformedRequestObjectException $e) {
                            http_response_code(415);
                            $responseObject = [
                                'status' => 'FAIL',
                                'msg' => 'Bad request parameters',
                                'items' => [],
                            ];
                        } catch (Exception $e) {
                            http_response_code(500);
                            $responseObject = [
                                'status' => 'FAIL',
                                'msg' => '',
                                'items' => [],
                            ];
                        }
                        break;

                    default:
                        http_response_code(405);
                        break;
                }
                break;

            case 'items':
                if ($pathDepth == 1) {
                    switch ($method) {
                        case 'GET':
                            try {
                                http_response_code(200);
                                $items = getItems();
                                $responseObject = [
                                    'status' => 'OK',
                                    'msg' => '',
                                    'items' => $items,
                                ];
                            } catch (DatabaseException $e) {
                                http_response_code(500);
                                $responseObject = [
                                    'status' => 'FAIL',
                                    'msg' => 'Database error',
                                    'items' => [],
                                ];
                            }
                            break;

                        case 'POST':
                            try {
                                $token = (string) $requestObject->token;
                                $itemKey = (string) $requestObject->itemFK;

                                if ($token === '' || $itemKey === '') {
                                    throw new MalformedRequestObjectException();
                                }

                                http_response_code(200);
                                updateItem($token, $itemKey);
                                $responseObject = [
                                    'status' => 'OK',
                                    'msg' => '',
                                ];
                            } catch (AuthenticationException $e) {
                                // Probably should be HTTP 401, but it requires a WWW-Authenticate header
                                http_response_code(403);
                                $responseObject = [
                                    'status' => 'AUTH_FAIL',
                                    'msg' => 'Invalid token',
                                ];
                            } catch (DatabaseException $e) {
                                http_response_code(500);
                                $responseObject = [
                                    'status' => 'FAIL',
                                    'msg' => 'Database error',
                                ];
                            } catch (MalformedRequestObjectException $e) {
                                http_response_code(415);
                                $responseObject = [
                                    'status' => 'FAIL',
                                    'msg' => 'Bad request parameters',
                                ];
                            }
                            break;

                        default:
                            http_response_code(405);
                            break;
                    }
                } else if ($pathDepth == 2) {
                    switch ($method) {
                        case 'GET':
                            $token = urldecode($pathParts[1]);
                            try {
                                http_response_code(200);
                                $items = getConsumedItems($token);
                                $responseObject = [
                                    'status' => 'OK',
                                    'msg' => '',
                                    'items' => $items,
                                ];
                            } catch (AuthenticationException $e) {
                                // Probably should be HTTP 401, but it requires a WWW-Authenticate header
                                http_response_code(403);
                                $responseObject = [
                                    'status' => 'AUTH_FAIL',
                                    'msg' => 'Invalid token',
                                    'items' => [],
                                ];
                            } catch (DatabaseException $e) {
                                http_response_code(500);
                                $responseObject = [
                                    'status' => 'FAIL',
                                    'msg' => 'Database error',
                                    'items' => [],
                                ];
                            }
                            break;

                        default:
                            http_response_code(405);
                            break;
                    }
                } else {
                    http_response_code(404);
                }
                break;

            case 'itemsSummary':
                if ($pathDepth == 2) {
                    switch ($method) {
                        case 'GET':
                            $token = urldecode($pathParts[1]);
                            try {
                                http_response_code(200);
                                $itemsSummary = computeItemSummary($token);
                                $responseObject = [
                                    'status' => 'OK',
                                    'msg' => '',
                                    'items' => $itemsSummary,
                                ];
                            } catch (AuthenticationException $e) {
                                // Probably should be HTTP 401, but it requires a WWW-Authenticate header
                                http_response_code(403);
                                $responseObject = [
                                    'status' => 'AUTH_FAIL',
                                    'msg' => 'Invalid token',
                                    'items' => [],
                                ];
                            } catch (DatabaseException $e) {
                                http_response_code(500);
                                $responseObject = [
                                    'status' => 'FAIL',
                                    'msg' => 'Database error',
                                    'items' => [],
                                ];
                            }
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

    if ($responseObject !== null) {
        header('Content-Type: application/json');
        echo json_encode($responseObject);
    }
}

if ($database->connect_errno) {
    http_response_code(500);
} else {
    processRestApi($_SERVER['PATH_INFO'] ?? '', $_SERVER['REQUEST_METHOD'], $httpBody, $_SERVER['CONTENT_TYPE'] ?? '');
}