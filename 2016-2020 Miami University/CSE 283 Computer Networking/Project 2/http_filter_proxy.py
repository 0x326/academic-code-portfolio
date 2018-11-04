# Name: John Meyer
# Date: 2017/11/22
# Assignment: Project 2

"""
A simple socket-based HTTP proxy server.
"""

import argparse
import logging
import socket
from collections import namedtuple
from logging import Logger

from urllib3.util.url import parse_url

HTTPRequest = namedtuple('HTTPRequest', 'method, url, version, headers, payload')
HTTPResponse = namedtuple('HTTPResponse', 'version, status_code, headers, payload')
ServerAddress = namedtuple('ServerAddress', 'host, port')


class Proxy:
    """
    Represents an HTTP proxy.

    Call ``.run()`` to start.
    """

    def __init__(self, port: int, logger: Logger, forbidden_words=set(), timeout: int=0.5):
        """
        Create a new HTTP proxy

        :param port: The port on which to run the proxy
        :param logger: The Logger this proxy is to use (see ``logging`` module)
        :param forbidden_words: A set of words that constitute what to censor
        :param timeout: The period of time to wait for a message from a given socket
        """
        self.port = port
        self.logger = logger
        self.forbidden_words = {word.lower() for word in forbidden_words}
        self.timeout = timeout

    @staticmethod
    def parse_http_message(http_message: bytes, http_response=False) -> HTTPRequest or HTTPResponse:
        """
        Parses a given HTTP message.

        :param http_message: The HTTP message
        :param http_response: Whether this message is an HTTP response
        :return: A named tuple with of the request/response.
        """
        http_header, http_payload = http_message.split(b'\r\n\r\n', maxsplit=1)

        http_headers: [bytes] = http_header.split(b'\r\n')
        first_header, *other_headers = http_headers  # type: bytes

        headers = dict()
        for header in other_headers:  # type: bytes
            header_name, header_value = header.split(b': ')
            header_name = header_name.lower()
            headers[header_name] = header_value

        if not http_response:
            # HTTP Request
            request_method, request_url, http_version = first_header.split()  # type: bytes

            request_method = request_method.upper()  # HTTP methods are case-insensitive

            # Parse url
            request_url = request_url.decode()
            request_url = parse_url(request_url)

            # Parse version
            _, http_version, = http_version.split(b'HTTP/', maxsplit=1)
            http_version = tuple(map(lambda num: int(num), http_version.split(b'.')))

            return HTTPRequest(method=request_method, url=request_url, version=http_version,
                               headers=headers,
                               payload=http_payload)
        else:
            # HTTP Response
            http_version, status_code = first_header.split(maxsplit=1)  # type: bytes

            # Parse version
            _, http_version, = http_version.split(b'HTTP/', maxsplit=1)
            http_version = tuple(map(lambda num: int(num), http_version.split(b'.')))

            return HTTPResponse(version=http_version, status_code=status_code,
                                headers=headers,
                                payload=http_payload)

    def run(self) -> None:
        """
        Starts the HTTP proxy.

        :return: ``None``
        """
        socket_family, _, _, _, socket_address_info = socket.getaddrinfo('localhost', self.port)[0]

        # Setup listening socket
        proxy_socket = socket.socket(family=socket_family)
        self.logger.debug('Trying to bind to {socket_address}'.format(socket_address=repr(socket_address_info)))
        proxy_socket.bind(socket_address_info)
        proxy_socket.listen()

        print('Listening on port {port}'.format(port=self.port))
        while True:
            client_socket, client_socket_address = proxy_socket.accept()
            self.logger.info('Got new connection with {socket_address}'.format(socket_address=client_socket_address))
            with client_socket:
                client_socket.settimeout(self.timeout)
                try:
                    # Get request
                    try:
                        self.logger.debug('Receiving client HTTP request')
                        http_request_message: bytes = client_socket.recv(4096)
                        if not http_request_message:
                            continue  # http_request_message is empty
                    except socket.timeout:
                        self.logger.error('Failed to receive client request due to timeout')
                        continue

                    # Parse client_request
                    try:
                        client_request: HTTPRequest = self.parse_http_message(http_request_message)
                        self.logger.info('Got message: %s', repr(client_request))
                    except ValueError as error:
                        self.logger.error('Unable to parse client request: %s', repr(http_request_message))
                        self.logger.exception(error)
                        continue

                    # Check URL for censored words
                    if any(map(lambda bad_word: bad_word in str(client_request.url).lower(), self.forbidden_words)):
                        # Censor content
                        self.logger.info('Censoring content based on URL')
                        response = b'HTTP/1.1 302 Found\r\n' \
                                   b'Location: http://ceclnx01.eas.miamioh.edu/~gomezlin/error.html\r\n' \
                                   b'\r\n' \
                                   b'\r\n'
                        self.logger.debug('Sending HTTP 302 (URL-caused)')
                        client_socket.send(response)
                        continue

                    # PAC file support
                    if not client_request.url.host:
                        # This is a request for us
                        self.logger.info('Sending a PAC file')
                        client_socket.send(b'HTTP/1.1 200 OK\r\n'
                                           b'\r\n'
                                           b'\r\n'
                                           b'function FindProxyForURL(url, host) {\n'
                                           b'  if (url.startswith("http://") {\n'
                                           b'    return "PROXY localhost:' + str(self.port).encode() + b'; DIRECT";\n'
                                           b'  } else {\n'
                                           b'    return "DIRECT";\n'
                                           b'  }\n'
                                           b'}\n')
                        continue

                    # Fetch resource from Web
                    server_address = ServerAddress(client_request.url.host, client_request.url.port or 80)
                    self.logger.debug('Connecting to %s', repr(server_address))
                    server_socket = socket.create_connection(server_address)
                    with server_socket:
                        # Compose top HTTP header
                        http_version = b'.'.join(map(lambda num: str(num).encode(), client_request.version))
                        first_request_header = b'%s %s %s\r\n' % (
                            client_request.method,
                            client_request.url.request_uri.encode(),
                            b'HTTP/' + http_version)
                        # Filter out proxy-related headers
                        headers: dict = client_request.headers
                        headers_to_delete = set()
                        for key in headers.keys():  # type: bytes
                            if key.startswith(b'proxy'):
                                headers_to_delete.add(key)
                        for header in headers_to_delete:
                            del headers[header]

                        # Compose HTTP request
                        http_headers = first_request_header + b'\r\n'.join(b'%s: %s' % (header, value)
                                                                           for header, value in headers.items())
                        http_payload = client_request.payload

                        # Send HTTP request
                        http_request = b'\r\n\r\n'.join((http_headers, http_payload))
                        self.logger.info('Sending request to server')
                        self.logger.debug('Request header: %s', first_request_header)
                        server_socket.send(http_request)

                        # Wait for response
                        server_socket.settimeout(self.timeout)
                        try:
                            try:
                                self.logger.debug('Receiving server response')
                                response_chunk: bytes = server_socket.recv(4096)
                                parsed_response_chunk: HTTPResponse = self.parse_http_message(response_chunk,
                                                                                              http_response=True)
                            except socket.timeout:
                                self.logger.error('Unable to receive server response due to timeout')
                                self.logger.debug('Sending HTTP 504')
                                client_socket.send(b'HTTP/1.1 504 Gateway Timeout\r\n'
                                                   b'\r\n'
                                                   b'\r\n')
                                continue

                            except ValueError:
                                # HTTP Response was unable to be parsed
                                parsed_response_chunk = None
                                self.logger.warning('Unable to parse response')

                            # Filter content if the response is uncompressed text
                            if parsed_response_chunk is not None and \
                                    (b'content-encoding' not in parsed_response_chunk.headers or
                                             parsed_response_chunk.headers[b'content-encoding'] == b'identity') and \
                                    (b'content-type' not in parsed_response_chunk.headers or
                                             b'text' in parsed_response_chunk.headers[b'content-type']):
                                self.logger.debug('Checking response content')

                                # Wait for entire resource
                                full_response: bytes = b''
                                try:
                                    self.logger.debug('Receiving remaining server response')
                                    while response_chunk:
                                        full_response += response_chunk
                                        response_chunk: bytes = server_socket.recv(4096)
                                except socket.timeout:
                                    self.logger.debug('Receiving remaining server response timed-out. '
                                                      'Assuming response is complete')
                                    pass
                                self.logger.debug('Done receiving full server response')

                                parsed_response: HTTPResponse = self.parse_http_message(full_response,
                                                                                        http_response=True)
                                self.logger.debug('Response: %s', repr(parsed_response))

                                # Check content for censored words
                                self.logger.debug('Checking response content')
                                if any(map(lambda bad_word: bad_word.encode() in parsed_response.payload.lower(),
                                           self.forbidden_words)):
                                    # Censor content
                                    self.logger.debug('Response content is BAD')
                                    self.logger.info('Censoring content based on content')
                                    response = b'HTTP/1.1 302 Found\r\n' \
                                               b'Location: http://ceclnx01.eas.miamioh.edu/~gomezlin/error2.html\r\n' \
                                               b'\r\n' \
                                               b'\r\n'
                                    self.logger.debug('Sending HTTP 302 (Content-caused)')
                                    client_socket.send(response)
                                else:
                                    self.logger.debug('Response content is OK')
                                    # Forward response
                                    self.logger.debug('Forwarding full response to client')
                                    client_socket.send(full_response)
                            else:
                                self.logger.debug('Ignoring response content')
                                # Forward response
                                try:
                                    self.logger.debug('Forwarding response chunks to client')
                                    while response_chunk:
                                        client_socket.send(response_chunk)
                                        response_chunk: bytes = server_socket.recv(4096)
                                except socket.timeout:
                                    self.logger.debug('Response chunk from server timed-out. '
                                                      'Assuming there are no more chunks')
                                self.logger.debug('Done forwarding response chunks')

                        except ConnectionAbortedError:
                            self.logger.error('Server connection aborted')
                        except ConnectionResetError:
                            self.logger.error('Server connection reset')

                        self.logger.debug('Closing server connection')

                except ConnectionAbortedError:
                    self.logger.error('Client connection aborted')
                except ConnectionResetError:
                    self.logger.error('Client connection reset')

                except Exception as error:
                    # If error is not caught before this,
                    # send an HTTP 500 and log the error
                    self.logger.exception(error)
                    client_socket.send(b'HTTP/1.1 500 Internal Server Error\r\n'
                                       b'\r\n'
                                       b'\r\n')


if __name__ == '__main__':
    logging.basicConfig(level=logging.INFO)

    # Setup argparse
    parser = argparse.ArgumentParser(description='A simple socket-based HTTP server')
    parser.add_argument('--port', '-p', type=int, default=4000,
                        help='the port on which to run the proxy')
    parser.add_argument('--block-list', '-b', type=argparse.FileType('r'), default='block_list.txt',
                        help='list of words to block (each word must be on its own line)')
    args = parser.parse_args()

    # Read blocked words from file
    blocked_words = set()
    with args.block_list as block_list:
        for word in block_list:  # type: str
            word = word.strip()
            if word:
                blocked_words.add(word)

    app = Proxy(logger=logging.getLogger('proxy'),
                forbidden_words=blocked_words,
                port=args.port)
    app.run()
