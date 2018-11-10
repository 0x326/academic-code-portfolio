# Name: John Meyer
# Date: 2017/10/16
# Assignment: Project 1

"""
A simple socket-based HTTP server.
"""

import argparse
import logging
import re
import socket
import webbrowser
from pathlib import Path

if __name__ == '__main__':
    logging.basicConfig(level=logging.DEBUG)
    logger = logging.getLogger('server')

    # Setup argparse
    parser = argparse.ArgumentParser(description='A simple socket-based HTTP server')
    parser.add_argument('--port', '-p', type=int, default=4000,
                        help='the port on which to run the server')
    parser.add_argument('dir', type=str, nargs='?', default='./',
                        help='the directory out of which this server is to serve files')
    args = parser.parse_args()
    public_directory = Path(args.dir)
    port = args.port

    socket_family, _, _, _, socket_address_info = socket.getaddrinfo('localhost', port)[0]

    server_socket = socket.socket(family=socket_family)
    logger.debug('Trying to bind to {socket_address}'.format(socket_address=repr(socket_address_info)))
    server_socket.bind(socket_address_info)
    server_socket.listen()

    valid_url_regex = re.compile('[\w/]*')
    logger.info('Listening on port {port}'.format(port=port))
    webbrowser.open('http://localhost:{port}'.format(port=port))
    while True:
        connection_socket, client_socket_address = server_socket.accept()
        logger.info('Got new connection with {socket_address}'.format(socket_address=client_socket_address))
        with connection_socket:
            http_message: bytes = connection_socket.recv(4096)
            http_response_header: bytes
            http_response_payload: bytes = b''

            # Parse request
            try:
                http_header, http_body = http_message.split(b'\r\n\r\n', maxsplit=1)

                http_headers: [bytes] = http_header.split(b'\r\n')
                first_header, *_ = http_headers

                request_method, request_url, http_version = first_header.split()
                request_method = request_method.upper()

                request_url = request_url.decode()
                request_url = request_url[1:] or 'index.html'  # Remove first slash

                assert request_method == b'GET' and valid_url_regex.match(request_url), 'Not a valid GET request'

                # Serve file
                try:
                    with open(public_directory / request_url, 'r') as file:
                        file_contents = '\n'.join(file)
                    http_response_header = b'HTTP/1.1 200 OK'
                    http_response_payload = file_contents.encode()

                except FileNotFoundError:
                    logger.debug('Could not find {path}. Serving 404 instead'.format(path=request_url))
                    http_response_header = b'HTTP/1.1 404 Not Found'
                    try:
                        with open(public_directory / '404.html', 'r') as file:
                            file_contents = '\n'.join(file)
                        http_response_payload = file_contents.encode()
                    except FileNotFoundError:
                        pass

            except (ValueError, AssertionError):
                http_response_header = b'HTTP/1.1 400 Bad Request'
            except PermissionError as error:
                http_response_header = b'HTTP/1.1 500 Internal Server Error'
                logger.exception(error)

            logger.info('Sending {http_header}'.format(http_header=http_response_header))
            http_response = b'\r\n\r\n'.join((http_response_header, http_response_payload))
            connection_socket.send(http_response)
            logger.debug('Response sent')
