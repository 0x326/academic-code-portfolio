<?php

$httpBody = file_get_contents('php://input');

require_once('database-credentials.php');
$database = connectToDatabase('cse383');
$database->set_charset('utf8');

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
            // /v1
            case 'v1':
                if ($pathDepth >= 2) {
                    switch ($pathParts[1]) {
                        case 'user':
                            switch ($method) {
                                case 'POST':
                                    getToken($httpBody, $mediaType);
                                    break;

                                default:
                                    http_response_code(405);
                                    break;
                            }
                            break;

                        case 'items':
                            if ($pathDepth == 2) {
                                switch ($method) {
                                    case 'GET':
                                        getItems();
                                        break;

                                    case 'POST':
                                        updateItem($httpBody, $mediaType);
                                        break;

                                    default:
                                        http_response_code(405);
                                        break;
                                }
                            } else if ($pathDepth == 3) {
                                switch ($method) {
                                    case 'GET':
                                        getConsumedItems($pathParts[2]);
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
                            if ($pathDepth == 3) {
                                switch ($method) {
                                    case 'GET':
                                        computeItemSummary($pathParts[2]);
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
