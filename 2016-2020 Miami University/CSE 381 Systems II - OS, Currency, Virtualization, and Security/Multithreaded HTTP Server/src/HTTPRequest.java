import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest implements Runnable {
    private Socket socket;
    private String transferEncoding;

    HTTPRequest(Socket socket) {
        this(socket, false);
    }

    HTTPRequest(Socket socket, boolean useChunkedTransferEncoding) {
        this.socket = socket;
        this.transferEncoding = useChunkedTransferEncoding ? "chunked" : "identity";
    }

    private static void assertTrue(boolean expression) {
        if (!expression) {
            throw new AssertionError();
        }
    }

    private static String inferMediaType(Path filename) {
        return inferMediaType(filename.toString());
    }

    private static String inferMediaType(String filename) {
        String[] filenameParts = filename.split("\\.");
        String extension = filenameParts[filenameParts.length - 1];

        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";

            case "png":
                return "image/png";

            case "gif":
                return "image/gif";

            case "html":
            case "htm":
                return "text/html";

            case "js":
                return "application/javascript";

            case "css":
                return "text/css";

            case "json":
                return "application/json";

            case "pdf":
                return "application/pdf";

            case "ogg":
                return "audio/ogg";

            default:
                return "text/plain";
        }
    }

    private static void sendFile(DataOutputStream output, Path filePath, String transferEncoding) throws IOException {
        if (transferEncoding.equalsIgnoreCase("identity")) {
            Files.copy(filePath, output);
        } else if (transferEncoding.equalsIgnoreCase("chunked")) {
            FileInputStream fileInputStream = new FileInputStream(filePath.toFile());
            byte[] buffer = new byte[100 * 1024];

            int bufferAmountUsed = fileInputStream.read(buffer);
            while (bufferAmountUsed != -1) {
                // TODO: Fix issue
                output.writeBytes(String.format("%d\r\n", bufferAmountUsed));
                output.write(buffer, 0, bufferAmountUsed);

                bufferAmountUsed = fileInputStream.read(buffer);
            }
        } else {
            throw new IllegalArgumentException(String.format("'%s' is not a valid transfer encoding", transferEncoding));
        }
    }

    @Override
    public void run() {
        boolean startedHTTPResponse = false;
        BufferedReader input = null;
        DataOutputStream output = null;
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String httpRequestLine = input.readLine();
            System.out.printf("> %s\n", httpRequestLine); // Debugging
            String[] httpRequestLineParts = httpRequestLine.split(" ");

            final int REQUEST_METHOD = 0;
            final int REQUEST_URL = 1;
            final int HTTP_VERSION = 2;

            assertTrue(httpRequestLineParts.length == 3);

            String[] httpVersion = httpRequestLineParts[HTTP_VERSION].split("/");
            assertTrue(httpVersion.length == 2);
            assertTrue(httpVersion[0].equalsIgnoreCase("HTTP"));

            if (!(httpVersion[1].equals("1.0") || httpVersion[1].equals("1.1"))) {
                System.out.println("Sending HTTP 505 HTTP Version Not Supported");
                output.writeBytes("HTTP/1.1 505 HTTP Version Not Supported\r\n");
                output.writeBytes("Transfer-Encoding: identity\r\n");
                output.writeBytes("Connection: close\r\n");
                output.writeBytes("\r\n");
                startedHTTPResponse = true;
                return;
            }

            // Remove first slash in URL
            httpRequestLineParts[REQUEST_URL] = httpRequestLineParts[REQUEST_URL].substring(1);
            if (httpRequestLineParts[REQUEST_URL].equals("")) {
                httpRequestLineParts[REQUEST_URL] = "index.html";
            }

            if (!httpRequestLineParts[REQUEST_METHOD].equalsIgnoreCase("GET")) {
                System.out.println("Sending HTTP 501 Not Implemented");
                output.writeBytes(String.format("HTTP/%s 501 Not Implemented\r\n", httpVersion[1]));
                output.writeBytes("Transfer-Encoding: identity\r\n");
                output.writeBytes("Connection: close\r\n");
                output.writeBytes("\r\n");
                startedHTTPResponse = true;
                return;
            }

            Map<String, String> httpHeaders = new HashMap<>();
            String httpHeaderLine = input.readLine();
            while (httpHeaderLine != null && !httpHeaderLine.equals("")) {
                System.out.printf("> %s\n", httpHeaderLine); // Debugging
                String[] httpHeaderLineParts = httpHeaderLine.split(": ", 2);

                String headerName = httpHeaderLineParts[0].toLowerCase();
                String headerValue = httpHeaderLineParts[1];

                httpHeaders.put(headerName, headerValue);

                httpHeaderLine = input.readLine();
            }

            if (httpVersion[1].equals("1.1")) {
                // Require host header
                assertTrue(httpHeaders.containsKey("host"));
            }

            assertTrue(!httpRequestLineParts[REQUEST_URL].matches("[\\w/]*"));

            Path filePath = FileSystems.getDefault().getPath(httpRequestLineParts[REQUEST_URL]);
            if (filePath.toFile().exists()) {
                System.out.println("Sending HTTP 200 OK");
                output.writeBytes(String.format("HTTP/%s 200 OK\r\n", httpVersion[1]));
                output.writeBytes(String.format("Transfer-Encoding: %s\r\n", transferEncoding));
                output.writeBytes(String.format("X-Client-Header-Count: %d\r\n", httpHeaders.size()));
                output.writeBytes(String.format("Content-Type: %s\r\n", inferMediaType(filePath)));
                output.writeBytes("Connection: close\r\n");
                output.writeBytes("\r\n");
                startedHTTPResponse = true;

                sendFile(output, filePath, transferEncoding);
            } else {
                System.out.println("Sending HTTP 404 Not Found");
                output.writeBytes(String.format("HTTP/%s 404 Not Found\r\n", httpVersion[1]));
                output.writeBytes("Transfer-Encoding: identity\r\n");
                output.writeBytes(String.format("X-Client-Header-Count: %d\r\n", httpHeaders.size()));
                output.writeBytes("Connection: close\r\n");
                output.writeBytes("\r\n");
                startedHTTPResponse = true;
            }
        } catch (IOException e) {
            try {
                assertTrue(!startedHTTPResponse);
                assertTrue(output != null);
                System.out.println("Sending HTTP 500 Internal Server Error");
                output.writeBytes("HTTP/1.1 500 Internal Server Error\r\n");
                output.writeBytes("Transfer-Encoding: identity\r\n");
                output.writeBytes("Connection: close\r\n");
                output.writeBytes("\r\n");
            } catch (IOException | AssertionError e2) {
                e.printStackTrace();
                e2.printStackTrace();
            }
        } catch (AssertionError e) {
            try {
                assertTrue(!startedHTTPResponse);
                assertTrue(output != null);
                System.out.println("Sending HTTP 400 Bad Request");
                output.writeBytes("HTTP/1.1 400 Bad Request\r\n");
                output.writeBytes("Transfer-Encoding: identity\r\n");
                output.writeBytes("Connection: close\r\n");
                output.writeBytes("\r\n");
            } catch (IOException | AssertionError e2) {
                e.printStackTrace();
                e2.printStackTrace();
            }
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                // Do nothing
            }

            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                // Do nothing
            }

            try {
                socket.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }
}
